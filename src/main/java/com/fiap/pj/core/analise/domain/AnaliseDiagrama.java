package com.fiap.pj.core.analise.domain;


import com.fiap.pj.core.analise.domain.vo.Arquivo;
import com.fiap.pj.core.util.DateTimeUtils;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Getter
public class AnaliseDiagrama {

    UUID id;
    Arquivo arquivo;
    StatusProcessamento status;
    ZonedDateTime dataCriacao;
    ZonedDateTime dataAtualizacao;

    public AnaliseDiagrama(Arquivo arquivo, StatusProcessamento status) {
        this(UUID.randomUUID(), arquivo, status, DateTimeUtils.getNow(), DateTimeUtils.getNow());
    }

    public AnaliseDiagrama(UUID id, Arquivo arquivo, StatusProcessamento status, ZonedDateTime dataCriacao,
                           ZonedDateTime dataAtualizacao) {
        this.id = requireNonNull(id);
        this.arquivo = requireNonNull(arquivo);
        this.status = requireNonNull(status);
        this.dataCriacao = requireNonNull(dataCriacao);
        this.dataAtualizacao = requireNonNull(dataAtualizacao);
    }
}
