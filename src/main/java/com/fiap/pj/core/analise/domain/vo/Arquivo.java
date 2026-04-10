package com.fiap.pj.core.analise.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class Arquivo {

    private final String nome;
    private final String tipoConteudo;

    public Arquivo(String nome, String tipoConteudo) {
        this.nome = requireNonNull(nome);
        this.tipoConteudo = requireNonNull(tipoConteudo);
    }
}

