package com.fiap.pj.infra.analise.controller.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fiap.pj.core.analise.domain.StatusProcessamento;

import java.time.ZonedDateTime;

@JsonPropertyOrder({"id", "descricao", "status", "motivo"})
public interface AnaliseDiagramaResponse {

    String getId();

    String getDescricao();

    String getMotivo();

    ArquivoProjection getArquivo();

    StatusProcessamento getStatus();

    ZonedDateTime getDataCriacao();

    interface ArquivoProjection {
        String getNome();
        String getTipoConteudo();
    }
}
