package com.fiap.pj.core.analise.app.usecase;

import com.fiap.pj.infra.analise.controller.response.BuscarAnaliseDiagramaResponse;

import java.util.UUID;

public interface BuscarAnaliseDiagramaUseCase {

    BuscarAnaliseDiagramaResponse handle(UUID id);
}

