package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.usecase.command.AtualizarStatusAnaliseCommand;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.vo.Arquivo;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusAnaliseUseCaseImplTest {

    @Mock
    private AnaliseDiagramaGateway analiseDiagramaGateway;

    @InjectMocks
    private AtualizarStatusAnaliseUseCaseImpl atualizarStatusAnaliseUseCase;

    @Captor
    private ArgumentCaptor<AnaliseDiagrama> analiseDiagramaCaptor;

    private AnaliseDiagrama criarAnalise(UUID id, StatusProcessamento status) {
        return new AnaliseDiagrama(id, "Descricao teste", new Arquivo("diagrama.png", "image/png"), status);
    }

    @Test
    @DisplayName("Deve atualizar status para CONCLUIDO com motivo null")
    void deveAtualizarStatusParaConcluido() {
        var id = UUID.randomUUID();
        var analise = criarAnalise(id, StatusProcessamento.EM_PROCESSAMENTO);
        var cmd = new AtualizarStatusAnaliseCommand(id, StatusProcessamento.CONCLUIDO);

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        atualizarStatusAnaliseUseCase.handle(cmd);

        verify(analiseDiagramaGateway).buscarPorIdOrThrow(id);
        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());

        var salva = analiseDiagramaCaptor.getValue();
        assertEquals(StatusProcessamento.CONCLUIDO, salva.getStatus());
        assertNull(salva.getMotivo());
    }

    @Test
    @DisplayName("Deve atualizar status para FALHA com motivo")
    void deveAtualizarStatusParaFalhaComMotivo() {
        var id = UUID.randomUUID();
        var analise = criarAnalise(id, StatusProcessamento.EM_PROCESSAMENTO);
        var motivo = "Erro ao processar diagrama";
        var cmd = new AtualizarStatusAnaliseCommand(id, StatusProcessamento.FALHA, motivo);

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        atualizarStatusAnaliseUseCase.handle(cmd);

        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());

        var salva = analiseDiagramaCaptor.getValue();
        assertEquals(StatusProcessamento.FALHA, salva.getStatus());
        assertEquals(motivo, salva.getMotivo());
    }

    @Test
    @DisplayName("Deve buscar analise pelo id do command")
    void deveBuscarAnalisePeloId() {
        var id = UUID.randomUUID();
        var analise = criarAnalise(id, StatusProcessamento.EM_PROCESSAMENTO);
        var cmd = new AtualizarStatusAnaliseCommand(id, StatusProcessamento.CONCLUIDO);

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        atualizarStatusAnaliseUseCase.handle(cmd);

        verify(analiseDiagramaGateway).buscarPorIdOrThrow(id);
    }

    @Test
    @DisplayName("Deve propagar exceção quando analise não for encontrada")
    void devePropagarExcecaoQuandoNaoEncontrada() {
        var id = UUID.randomUUID();
        var cmd = new AtualizarStatusAnaliseCommand(id, StatusProcessamento.FALHA, "motivo");

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id))
                .thenThrow(new RuntimeException("Analise Diagrama não encontrado."));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> atualizarStatusAnaliseUseCase.handle(cmd));

        verify(analiseDiagramaGateway, org.mockito.Mockito.never()).salvar(any());
    }

    @Test
    @DisplayName("Deve salvar a mesma instância retornada pelo gateway")
    void deveSalvarMesmaInstancia() {
        var id = UUID.randomUUID();
        var analise = criarAnalise(id, StatusProcessamento.EM_PROCESSAMENTO);
        var cmd = new AtualizarStatusAnaliseCommand(id, StatusProcessamento.CONCLUIDO);

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        atualizarStatusAnaliseUseCase.handle(cmd);

        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());
        assertEquals(id, analiseDiagramaCaptor.getValue().getId());
    }
}
