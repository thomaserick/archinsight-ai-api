package com.fiap.pj.infra.servico.persistense;

import com.fiap.pj.core.analise.domain.vo.Arquivo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArquivoEmbeddable {

    @Column(name = "nome_arquivo", nullable = false)
    private String nome;

    @Column(name = "tipo_conteudo", nullable = false)
    private String tipoConteudo;

    public ArquivoEmbeddable(String nome, String tipoConteudo) {
        this.nome = requireNonNull(nome);
        this.tipoConteudo = requireNonNull(tipoConteudo);
    }

    public ArquivoEmbeddable(Arquivo arquivo) {
        this(arquivo.getNome(), arquivo.getTipoConteudo());
    }

    public Arquivo toDomain() {
        return new Arquivo(nome, tipoConteudo);
    }
}

