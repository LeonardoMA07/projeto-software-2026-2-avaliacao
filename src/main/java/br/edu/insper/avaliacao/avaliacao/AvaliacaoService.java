package br.edu.insper.avaliacao.avaliacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<Avaliacao> listar() {
        return avaliacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataAvaliacao"));
    }

    public Avaliacao cadastrar(Avaliacao avaliacao) {
        avaliacao.setId(null);
        if (avaliacao.getDataAvaliacao() == null) {
            avaliacao.setDataAvaliacao(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }
        return avaliacaoRepository.save(avaliacao);
    }
}
