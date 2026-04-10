package com.fiap.pj.core.analise.app.gateways;

import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaProcessadaEvent;

public interface AnaliseDiagramaPublisherGateway {

    void processar(AnaliseDiagramaProcessadaEvent event);
}

