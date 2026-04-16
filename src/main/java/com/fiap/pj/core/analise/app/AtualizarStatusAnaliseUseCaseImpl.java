package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.usecase.AtualizarStatusAnaliseUseCase;
import com.fiap.pj.core.analise.app.usecase.command.AtualizarStatusAnaliseCommand;

public class AtualizarStatusAnaliseUseCaseImpl implements AtualizarStatusAnaliseUseCase {

    private final AnaliseDiagramaGateway analiseDiagramaGateway;

    public AtualizarStatusAnaliseUseCaseImpl(AnaliseDiagramaGateway analiseDiagramaGateway) {
        this.analiseDiagramaGateway = analiseDiagramaGateway;
    }

    @Override
    public void handle(AtualizarStatusAnaliseCommand cmd) {
        var analise = analiseDiagramaGateway.buscarPorIdOrThrow(cmd.id());
        analise.atualizarStatus(cmd.status(), cmd.motivo());
        analiseDiagramaGateway.salvar(analise);
    }
}

