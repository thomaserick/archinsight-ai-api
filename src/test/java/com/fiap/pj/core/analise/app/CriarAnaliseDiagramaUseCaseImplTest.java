package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaPublisherGateway;
import com.fiap.pj.core.analise.app.usecase.command.CriarAnaliseDiagramaCommand;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.core.storage.domain.UploadStorage;
import com.fiap.pj.core.storage.exception.StorageExceptions.ArquivoUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarAnaliseDiagramaUseCaseImplTest {

    @Mock
    private AnaliseDiagramaGateway analiseDiagramaGateway;

    @Mock
    private ArquivoStorageGateway arquivoStorageGateway;

    @Mock
    private AnaliseDiagramaPublisherGateway publisherGateway;

    @InjectMocks
    private CriarAnaliseDiagramaUseCaseImpl criarAnaliseDiagramaUseCase;

    @Captor
    private ArgumentCaptor<UploadStorage> uploadStorageCaptor;

    @Captor
    private ArgumentCaptor<AnaliseDiagrama> analiseDiagramaCaptor;

    @Test
    void deveCriarAnaliseDiagramaComSucesso() {
        var multipartFile = new MockMultipartFile(
                "arquivo", "diagrama.png", "image/png", "conteudo-fake".getBytes()
        );
        var cmd = new CriarAnaliseDiagramaCommand(multipartFile);

        when(analiseDiagramaGateway.salvar(any(AnaliseDiagrama.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = criarAnaliseDiagramaUseCase.handle(cmd);

        verify(arquivoStorageGateway).upload(uploadStorageCaptor.capture());
        var uploadStorage = uploadStorageCaptor.getValue();
        assertNotNull(uploadStorage.id());
        assertEquals("diagrama.png", uploadStorage.nomeArquivo());
        assertEquals("image/png", uploadStorage.tipoConteudo());
        assertEquals(multipartFile.getSize(), uploadStorage.tamanho());

        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());
        var analiseSalva = analiseDiagramaCaptor.getValue();
        assertNotNull(analiseSalva.getId());
        assertEquals("diagrama.png", analiseSalva.getArquivo().getNome());
        assertEquals("image/png", analiseSalva.getArquivo().getTipoConteudo());
        assertEquals(StatusProcessamento.EM_PROCESSAMENTO, analiseSalva.getStatus());
        assertNotNull(analiseSalva.getDataCriacao());
        assertNotNull(analiseSalva.getDataAtualizacao());

        assertNotNull(resultado);
        assertEquals(analiseSalva.getId(), resultado.getId());

        verify(publisherGateway).dispatch(Mockito.any());
    }

    @Test
    void deveUsarNomeFallbackQuandoOriginalFilenameForNull() throws IOException {
        var multipartFile = mock(org.springframework.web.multipart.MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        when(multipartFile.getName()).thenReturn("meu-arquivo");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getInputStream()).thenReturn(new java.io.ByteArrayInputStream("conteudo".getBytes()));
        when(multipartFile.getSize()).thenReturn(8L);

        var cmd = new CriarAnaliseDiagramaCommand(multipartFile);

        when(analiseDiagramaGateway.salvar(any(AnaliseDiagrama.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = criarAnaliseDiagramaUseCase.handle(cmd);

        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());
        assertEquals("meu-arquivo", analiseDiagramaCaptor.getValue().getArquivo().getNome());
        assertNotNull(resultado);
    }

    @Test
    void deveUsarContentTypePadraoQuandoForNull() {
        var multipartFile = new MockMultipartFile(
                "arquivo", "diagrama.xml", null, "conteudo".getBytes()
        );
        var cmd = new CriarAnaliseDiagramaCommand(multipartFile);

        when(analiseDiagramaGateway.salvar(any(AnaliseDiagrama.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = criarAnaliseDiagramaUseCase.handle(cmd);

        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());
        assertEquals("application/octet-stream", analiseDiagramaCaptor.getValue().getArquivo().getTipoConteudo());
        assertNotNull(resultado);
    }

    @Test
    void deveLancarArquivoUploadExceptionQuandoIOExceptionOcorrer() throws IOException {
        var multipartFile = mock(org.springframework.web.multipart.MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("diagrama.png");
        when(multipartFile.getName()).thenReturn("arquivo");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenThrow(new IOException("Erro de leitura"));

        var cmd = new CriarAnaliseDiagramaCommand(multipartFile);

        var exception = assertThrows(ArquivoUploadException.class, () ->
                criarAnaliseDiagramaUseCase.handle(cmd)
        );

        assertEquals("Erro ao realizar upload do arquivo para o S3", exception.getMessage());
        assertInstanceOf(IOException.class, exception.getCause());

        verify(analiseDiagramaGateway, never()).salvar(any());
        verify(publisherGateway, never()).dispatch(any());
    }

    @Test
    void deveEnviarMesmoIdParaUploadEParaSalvar() {
        var multipartFile = new MockMultipartFile(
                "arquivo", "diagrama.png", "image/png", "conteudo".getBytes()
        );
        var cmd = new CriarAnaliseDiagramaCommand(multipartFile);

        when(analiseDiagramaGateway.salvar(any(AnaliseDiagrama.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        criarAnaliseDiagramaUseCase.handle(cmd);

        verify(arquivoStorageGateway).upload(uploadStorageCaptor.capture());
        verify(analiseDiagramaGateway).salvar(analiseDiagramaCaptor.capture());

        assertEquals(uploadStorageCaptor.getValue().id(), analiseDiagramaCaptor.getValue().getId(),
                "O ID enviado ao storage deve ser o mesmo salvo na AnaliseDiagrama");
    }
}

