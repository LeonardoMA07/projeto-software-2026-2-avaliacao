package br.edu.insper.avaliacao.avaliacao;

import br.edu.insper.avaliacao.config.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvaliacaoController.class)
@Import(WebSecurityConfig.class)
class AvaliacaoControllerTest {

    private static final String AVALIACAO_VALIDA = """
            {"emailAvaliador":"admin@insper.edu.br","emailAvaliado":"aluno@insper.edu.br","comentarios":"Bom trabalho","nota":9}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvaliacaoService avaliacaoService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        // "admin", "user" e "semrole" são tokens falsos; o decoder mockado devolve o JWT com os papéis
        when(jwtDecoder.decode("admin")).thenReturn(token("admin", List.of("ADMIN")));
        when(jwtDecoder.decode("user")).thenReturn(token("user", List.of("USER")));
        when(jwtDecoder.decode("semrole")).thenReturn(token("semrole", List.of()));
        when(avaliacaoService.listar()).thenReturn(List.of());
        when(avaliacaoService.cadastrar(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    private Jwt token(String valor, List<String> roles) {
        return Jwt.withTokenValue(valor)
                .header("alg", "none")
                .subject(valor)
                .claim(WebSecurityConfig.ROLES_CLAIM, roles)
                .build();
    }

    @Test
    void semTokenRetorna401() throws Exception {
        mockMvc.perform(get("/avaliacoes")).andExpect(status().isUnauthorized());
    }

    @Test
    void userEAdminPodemListar() throws Exception {
        mockMvc.perform(get("/avaliacoes").header("Authorization", "Bearer user")).andExpect(status().isOk());
        mockMvc.perform(get("/avaliacoes").header("Authorization", "Bearer admin")).andExpect(status().isOk());
    }

    @Test
    void semPapelNaoPodeListar() throws Exception {
        mockMvc.perform(get("/avaliacoes").header("Authorization", "Bearer semrole")).andExpect(status().isForbidden());
    }

    @Test
    void userNaoPodeCadastrar() throws Exception {
        mockMvc.perform(post("/avaliacoes").header("Authorization", "Bearer user")
                        .contentType(MediaType.APPLICATION_JSON).content(AVALIACAO_VALIDA))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPodeCadastrar() throws Exception {
        mockMvc.perform(post("/avaliacoes").header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON).content(AVALIACAO_VALIDA))
                .andExpect(status().isCreated());
    }

    @Test
    void notaForaDe0a10Retorna400() throws Exception {
        String notaInvalida = AVALIACAO_VALIDA.replace("\"nota\":9", "\"nota\":11");
        mockMvc.perform(post("/avaliacoes").header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON).content(notaInvalida))
                .andExpect(status().isBadRequest());
    }
}
