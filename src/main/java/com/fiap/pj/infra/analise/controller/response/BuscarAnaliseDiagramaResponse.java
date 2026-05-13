package com.fiap.pj.infra.analise.controller.response;

import com.fiap.pj.core.analise.domain.StatusProcessamento;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class BuscarAnaliseDiagramaResponse {

    private String id;
    private String descricao;
    private StatusProcessamento status;
    private String motivo;
    private String nomeArquivo;
    private String tipoConteudo;
    private String urlRelatorio;
    private ZonedDateTime dataCriacao;
    private ZonedDateTime dataAtualizacao;
}

