package com.fiap.pj.infra.analise.consumer;

import com.fiap.pj.core.analise.app.usecase.AtualizarStatusAnaliseUseCase;
import com.fiap.pj.core.analise.app.usecase.command.AtualizarStatusAnaliseCommand;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaConcluidaEvent;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaFalhouEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnaliseDiagramaConsumerTest {

    @Mock
    private AtualizarStatusAnaliseUseCase atualizarStatusAnaliseUseCase;

    @InjectMocks
    private AnaliseDiagramaConsumer consumer;

    @Captor
    private ArgumentCaptor<AtualizarStatusAnaliseCommand> commandCaptor;

    @Nested
    @DisplayName("Evento AnaliseDiagramaFalhouEvent")
    class EventoFalhou {

        @Test
        @DisplayName("Deve chamar use case com status FALHA e motivo")
        void deveChamarUseCaseComStatusFalhaEMotivo() {
            var id = UUID.randomUUID();
            var motivo = "Erro ao processar diagrama";
            var event = new AnaliseDiagramaFalhouEvent(id, motivo);

            consumer.consumir(event);

            verify(atualizarStatusAnaliseUseCase).handle(commandCaptor.capture());
            var cmd = commandCaptor.getValue();
            assertEquals(id, cmd.id());
            assertEquals(StatusProcessamento.FALHA, cmd.status());
            assertEquals(motivo, cmd.motivo());
        }

        @Test
        @DisplayName("Deve chamar use case com motivo null quando motivo for null")
        void deveChamarUseCaseComMotivoNull() {
            var id = UUID.randomUUID();
            var event = new AnaliseDiagramaFalhouEvent(id, null);

            consumer.consumir(event);

            verify(atualizarStatusAnaliseUseCase).handle(commandCaptor.capture());
            var cmd = commandCaptor.getValue();
            assertEquals(id, cmd.id());
            assertEquals(StatusProcessamento.FALHA, cmd.status());
            assertNull(cmd.motivo());
        }
    }

    @Nested
    @DisplayName("Evento AnaliseDiagramaConcluidaEvent")
    class EventoConcluida {

        @Test
        @DisplayName("Deve chamar use case com status CONCLUIDO e motivo null")
        void deveChamarUseCaseComStatusConcluido() {
            var id = UUID.randomUUID();
            var event = new AnaliseDiagramaConcluidaEvent(id);

            consumer.consumir(event);

            verify(atualizarStatusAnaliseUseCase).handle(commandCaptor.capture());
            var cmd = commandCaptor.getValue();
            assertEquals(id, cmd.id());
            assertEquals(StatusProcessamento.CONCLUIDO, cmd.status());
            assertNull(cmd.motivo());
        }
    }
}

