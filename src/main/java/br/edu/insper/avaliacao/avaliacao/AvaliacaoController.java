package br.edu.insper.avaliacao.avaliacao;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // ADMIN e USER (regra em WebSecurityConfig)
    @GetMapping
    public List<Avaliacao> listar() {
        return avaliacaoService.listar();
    }

    // Somente ADMIN (regra em WebSecurityConfig)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao cadastrar(@RequestBody @Valid Avaliacao avaliacao) {
        return avaliacaoService.cadastrar(avaliacao);
    }
}
