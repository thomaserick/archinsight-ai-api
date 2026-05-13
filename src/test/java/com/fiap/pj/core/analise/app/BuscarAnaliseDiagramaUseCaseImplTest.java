package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.vo.Arquivo;
import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarAnaliseDiagramaUseCaseImplTest {

    @Mock
    private AnaliseDiagramaGateway analiseDiagramaGateway;

    @Mock
    private ArquivoStorageGateway arquivoStorageGateway;

    @InjectMocks
    private BuscarAnaliseDiagramaUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar analise com URL do relatorio quando status CONCLUIDO")
    void deveRetornarComUrlQuandoConcluido() {
        var id = UUID.randomUUID();
        var analise = new AnaliseDiagrama(id, "descricao", new Arquivo("diagrama.png", "image/png"), StatusProcessamento.CONCLUIDO);
        var urlEsperada = "https://bucket.s3.amazonaws.com/relatorios/" + id;

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);
        when(arquivoStorageGateway.gerarUrlAssinada(id)).thenReturn(urlEsperada);

        var response = useCase.handle(id);

        assertThat(response.getId()).isEqualTo(id.toString());
        assertThat(response.getDescricao()).isEqualTo("descricao");
        assertThat(response.getStatus()).isEqualTo(StatusProcessamento.CONCLUIDO);
        assertThat(response.getNomeArquivo()).isEqualTo("diagrama.png");
        assertThat(response.getTipoConteudo()).isEqualTo("image/png");
        assertThat(response.getUrlRelatorio()).isEqualTo(urlEsperada);
        verify(arquivoStorageGateway).gerarUrlAssinada(id);
    }

    @Test
    @DisplayName("Deve retornar analise sem URL do relatorio quando status diferente de CONCLUIDO")
    void deveRetornarSemUrlQuandoNaoConcluido() {
        var id = UUID.randomUUID();
        var analise = new AnaliseDiagrama(id, "descricao", new Arquivo("diagrama.png", "image/png"), StatusProcessamento.EM_PROCESSAMENTO);

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        var response = useCase.handle(id);

        assertThat(response.getId()).isEqualTo(id.toString());
        assertThat(response.getStatus()).isEqualTo(StatusProcessamento.EM_PROCESSAMENTO);
        assertThat(response.getUrlRelatorio()).isNull();
        verify(arquivoStorageGateway, never()).gerarUrlAssinada(any());
    }

    @Test
    @DisplayName("Deve retornar analise com motivo quando status FALHA")
    void deveRetornarComMotivoQuandoFalha() {
        var id = UUID.randomUUID();
        var analise = new AnaliseDiagrama(id, "descricao", new Arquivo("diagrama.png", "image/png"), StatusProcessamento.FALHA);
        analise.atualizarStatus(StatusProcessamento.FALHA, "Erro ao processar");

        when(analiseDiagramaGateway.buscarPorIdOrThrow(id)).thenReturn(analise);

        var response = useCase.handle(id);

        assertThat(response.getStatus()).isEqualTo(StatusProcessamento.FALHA);
        assertThat(response.getMotivo()).isEqualTo("Erro ao processar");
        assertThat(response.getUrlRelatorio()).isNull();
        verify(arquivoStorageGateway, never()).gerarUrlAssinada(any());
    }
}

