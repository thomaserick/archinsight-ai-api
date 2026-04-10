package com.fiap.pj.core.analise.app;


import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.usecase.ListarAnaliseDiagramaUseCase;
import com.fiap.pj.infra.servico.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.servico.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;


public class ListarAnaliseDiagramaUseCaseImpl implements ListarAnaliseDiagramaUseCase {

    private final AnaliseDiagramaGateway analiseDiagramaGateway;

    public ListarAnaliseDiagramaUseCaseImpl(AnaliseDiagramaGateway analiseDiagramaGateway) {
        this.analiseDiagramaGateway = analiseDiagramaGateway;
    }

    @Override
    public Slice<AnaliseDiagramaResponse> handle(ListarAnaliseDiagramaRequest request) {
        return analiseDiagramaGateway.listarServico(request);
    }
}
