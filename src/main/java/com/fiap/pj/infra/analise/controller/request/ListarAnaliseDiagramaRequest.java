package com.fiap.pj.infra.analise.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class ListarAnaliseDiagramaRequest {


    private String descricao;
    @Setter
    @JsonIgnore
    private Pageable pageable;

    private StatusProcessamento status;


}
