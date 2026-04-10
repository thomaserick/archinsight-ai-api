package com.fiap.pj.core.analise.app.usecase;

import com.fiap.pj.core.analise.app.usecase.command.CriarAnaliseDiagramaCommand;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;

public interface CriarAnaliseDiagramaUseCase {

    AnaliseDiagrama handle(CriarAnaliseDiagramaCommand cmd);
}
