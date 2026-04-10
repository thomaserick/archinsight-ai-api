package com.fiap.pj.core.analise.app.gateways;

import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.infra.servico.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.servico.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;

public interface AnaliseDiagramaGateway {

    AnaliseDiagrama salvar(AnaliseDiagrama domain);

    Slice<AnaliseDiagramaResponse> listarServico(ListarAnaliseDiagramaRequest request);


}
