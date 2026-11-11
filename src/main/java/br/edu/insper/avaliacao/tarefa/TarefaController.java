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
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService TarefaService;

    // ADMIN e USER (regra em WebSecurityConfig)
    @GetMapping
    public List<Tarefa> listar() {
        return TarefaService.listar();
    }

    // Somente ADMIN (regra em WebSecurityConfig)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa cadastrar(@RequestBody @Valid Tarefa Tarefa) {
        return TarefaService.cadastrar(Tarefa);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { repository.deleteById(id); }
}
