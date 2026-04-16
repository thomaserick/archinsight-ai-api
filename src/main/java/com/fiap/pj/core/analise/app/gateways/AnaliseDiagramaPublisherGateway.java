package com.fiap.pj.core.analise.app.gateways;

import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaSolicitadaEvent;

public interface AnaliseDiagramaPublisherGateway {

    void dispatch(AnaliseDiagramaSolicitadaEvent event);
}

