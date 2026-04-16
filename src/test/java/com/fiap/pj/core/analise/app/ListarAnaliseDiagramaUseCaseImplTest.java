package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAnaliseDiagramaUseCaseImplTest {

    @Mock
    private AnaliseDiagramaGateway analiseDiagramaGateway;

    @InjectMocks
    private ListarAnaliseDiagramaUseCaseImpl listarAnaliseDiagramaUseCase;

    @Test
    @DisplayName("Deve retornar lista de analises do gateway")
    void deveRetornarListaDeAnalises() {
        var request = new ListarAnaliseDiagramaRequest("desc", null, StatusProcessamento.EM_PROCESSAMENTO);
        request.setPageable(PageRequest.of(0, 10));

        var sliceEsperado = new Slice<AnaliseDiagramaResponse>(false, List.of());
        when(analiseDiagramaGateway.listarAnaliseDiagrama(request)).thenReturn(sliceEsperado);

        var resultado = listarAnaliseDiagramaUseCase.handle(request);

        assertSame(sliceEsperado, resultado);
        verify(analiseDiagramaGateway).listarAnaliseDiagrama(request);
    }

    @Test
    @DisplayName("Deve delegar ao gateway com o request recebido")
    void deveDelegarAoGateway() {
        var request = new ListarAnaliseDiagramaRequest(null, null, null);
        request.setPageable(PageRequest.of(0, 10));

        var sliceEsperado = new Slice<AnaliseDiagramaResponse>(true, List.of());
        when(analiseDiagramaGateway.listarAnaliseDiagrama(request)).thenReturn(sliceEsperado);

        var resultado = listarAnaliseDiagramaUseCase.handle(request);

        assertEquals(sliceEsperado.isHasNext(), resultado.isHasNext());
        assertEquals(sliceEsperado.getItems(), resultado.getItems());
        verify(analiseDiagramaGateway).listarAnaliseDiagrama(request);
    }

    @Test
    @DisplayName("Deve retornar slice com hasNext true quando houver mais páginas")
    void deveRetornarSliceComHasNext() {
        var request = new ListarAnaliseDiagramaRequest("teste", null, StatusProcessamento.CONCLUIDO);
        request.setPageable(PageRequest.of(0, 5));

        var sliceEsperado = new Slice<AnaliseDiagramaResponse>(true, List.of());
        when(analiseDiagramaGateway.listarAnaliseDiagrama(request)).thenReturn(sliceEsperado);

        var resultado = listarAnaliseDiagramaUseCase.handle(request);

        assertTrue(resultado.isHasNext());
    }
}

