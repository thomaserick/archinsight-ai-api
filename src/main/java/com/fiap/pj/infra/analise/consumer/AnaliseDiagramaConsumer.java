package com.fiap.pj.infra.analise.consumer;

import com.fiap.pj.core.analise.app.usecase.AtualizarStatusAnaliseUseCase;
import com.fiap.pj.core.analise.app.usecase.command.AtualizarStatusAnaliseCommand;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaConcluidaEvent;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaFalhouEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AnaliseDiagramaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AnaliseDiagramaConsumer.class);

    private final AtualizarStatusAnaliseUseCase atualizarStatusAnaliseUseCase;

    public AnaliseDiagramaConsumer(AtualizarStatusAnaliseUseCase atualizarStatusAnaliseUseCase) {
        this.atualizarStatusAnaliseUseCase = atualizarStatusAnaliseUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${broker.queue.analise.falhou}")
    public void consumir(AnaliseDiagramaFalhouEvent event) {
        log.info("Evento recebido na fila 'Erro' análise id '{}'", event.id());
        atualizarStatusAnaliseUseCase.handle(new AtualizarStatusAnaliseCommand(event.id(), StatusProcessamento.FALHA, event.motivo()));
    }

    @Transactional
    @RabbitListener(queues = "${broker.queue.analise.concluida}")
    public void consumir(AnaliseDiagramaConcluidaEvent event) {
        log.info("Evento recebido na fila 'Concluida' análise id '{}'", event.id());
        atualizarStatusAnaliseUseCase.handle(new AtualizarStatusAnaliseCommand(event.id(), StatusProcessamento.CONCLUIDO));
    }
}

