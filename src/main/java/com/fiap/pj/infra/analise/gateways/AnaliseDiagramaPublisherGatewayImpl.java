package com.fiap.pj.infra.analise.gateways;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaPublisherGateway;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaSolicitadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class AnaliseDiagramaPublisherGatewayImpl implements AnaliseDiagramaPublisherGateway {

    private static final Logger log = LoggerFactory.getLogger(AnaliseDiagramaPublisherGatewayImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final String routingKey;

    public AnaliseDiagramaPublisherGatewayImpl(RabbitTemplate rabbitTemplate, String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.routingKey = routingKey;
    }

    @Override
    public void dispatch(AnaliseDiagramaSolicitadaEvent event) {
        rabbitTemplate.convertAndSend(routingKey, event);
        log.info("Mensagem publicada na fila '{}' com id '{}'", routingKey, event.id());
    }
}

