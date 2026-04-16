package com.fiap.pj.core.analise.app.usecase;

import com.fiap.pj.core.analise.app.usecase.command.AtualizarStatusAnaliseCommand;

public interface AtualizarStatusAnaliseUseCase {

    void handle(AtualizarStatusAnaliseCommand cmd);
}

