package com.fiap.pj.core.analise.domain.event;

import java.util.UUID;

public record AnaliseDiagramaFalhouEvent(UUID id, String motivo) {
}

