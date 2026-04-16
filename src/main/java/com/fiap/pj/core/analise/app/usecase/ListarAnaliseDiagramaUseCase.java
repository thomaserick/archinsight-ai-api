package com.fiap.pj.core.analise.app.usecase;

import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;

public interface ListarAnaliseDiagramaUseCase {

    Slice<AnaliseDiagramaResponse> handle(ListarAnaliseDiagramaRequest request);
}
