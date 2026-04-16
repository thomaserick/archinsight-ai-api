package com.fiap.pj.core.analise.app;


import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaPublisherGateway;
import com.fiap.pj.core.analise.app.usecase.CriarAnaliseDiagramaUseCase;
import com.fiap.pj.core.analise.app.usecase.command.CriarAnaliseDiagramaCommand;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.event.AnaliseDiagramaSolicitadaEvent;
import com.fiap.pj.core.analise.domain.vo.Arquivo;
import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.core.storage.domain.UploadStorage;
import com.fiap.pj.core.storage.exception.StorageExceptions.ArquivoUploadException;


import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class CriarAnaliseDiagramaUseCaseImpl implements CriarAnaliseDiagramaUseCase {

    private final AnaliseDiagramaGateway analiseDiagramaGateway;
    private final ArquivoStorageGateway arquivoStorageGateway;
    private final AnaliseDiagramaPublisherGateway publisherGateway;

    public CriarAnaliseDiagramaUseCaseImpl(AnaliseDiagramaGateway analiseDiagramaGateway,
                                           ArquivoStorageGateway arquivoStorageGateway,
                                           AnaliseDiagramaPublisherGateway publisherGateway) {
        this.analiseDiagramaGateway = analiseDiagramaGateway;
        this.arquivoStorageGateway = arquivoStorageGateway;
        this.publisherGateway = publisherGateway;
    }

    @Override
    public AnaliseDiagrama handle(CriarAnaliseDiagramaCommand cmd) {
        var id = UUID.randomUUID();
        var nomeArquivo = Objects.requireNonNullElse(cmd.arquivo().getOriginalFilename(), cmd.arquivo().getName());
        var tipoConteudo = Objects.requireNonNullElse(cmd.arquivo().getContentType(), "application/octet-stream");

        try {
            var uploadStorage = new UploadStorage(id, nomeArquivo, tipoConteudo, cmd.arquivo().getInputStream(), cmd.arquivo().getSize());
            arquivoStorageGateway.upload(uploadStorage);
        } catch (IOException e) {
            throw new ArquivoUploadException("Erro ao realizar upload do arquivo para o S3", e);
        }

        var arquivo = new Arquivo(nomeArquivo, tipoConteudo);
        var analiseDiagrama = new AnaliseDiagrama(id, cmd.descricao(), arquivo, StatusProcessamento.EM_PROCESSAMENTO);
        analiseDiagramaGateway.salvar(analiseDiagrama);

        publisherGateway.dispatch(new AnaliseDiagramaSolicitadaEvent(analiseDiagrama.getId()));

        return analiseDiagrama;
    }
}
