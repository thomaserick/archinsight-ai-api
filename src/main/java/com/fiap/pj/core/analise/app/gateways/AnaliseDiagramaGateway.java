package com.fiap.pj.core.analise.app.gateways;

import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;

import java.util.UUID;

public interface AnaliseDiagramaGateway {

    AnaliseDiagrama salvar(AnaliseDiagrama domain);

    Slice<AnaliseDiagramaResponse> listarAnaliseDiagrama(ListarAnaliseDiagramaRequest request);

    AnaliseDiagrama buscarPorIdOrThrow(UUID id);
}
