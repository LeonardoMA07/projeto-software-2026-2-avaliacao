package br.edu.insper.avaliacao.avaliacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository TarefaRepository;

    public List<Tarefa> listar() {
        return TarefaRepository.findAll());
    }

    public Tarefa cadastrar(Tarefa Tarefa) {
        Tarefa.setId(null);
        return TarefaRepository.save(Tarefa);
    }
}
