package com.fiap.pj.core.analise.app.usecase.command;

import com.fiap.pj.core.analise.domain.StatusProcessamento;

import java.util.UUID;

public record AtualizarStatusAnaliseCommand(UUID id, StatusProcessamento status, String motivo) {
    public AtualizarStatusAnaliseCommand(UUID id, StatusProcessamento status) {
        this(id, status, null);
    }
}
