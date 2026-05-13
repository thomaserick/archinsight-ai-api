package com.fiap.pj.core.analise.app;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.usecase.BuscarAnaliseDiagramaUseCase;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.infra.analise.controller.response.BuscarAnaliseDiagramaResponse;

import java.util.UUID;

public class BuscarAnaliseDiagramaUseCaseImpl implements BuscarAnaliseDiagramaUseCase {

    private final AnaliseDiagramaGateway analiseDiagramaGateway;
    private final ArquivoStorageGateway arquivoStorageGateway;

    public BuscarAnaliseDiagramaUseCaseImpl(AnaliseDiagramaGateway analiseDiagramaGateway,
                                            ArquivoStorageGateway arquivoStorageGateway) {
        this.analiseDiagramaGateway = analiseDiagramaGateway;
        this.arquivoStorageGateway = arquivoStorageGateway;
    }

    @Override
    public BuscarAnaliseDiagramaResponse handle(UUID id) {
        var analise = analiseDiagramaGateway.buscarPorIdOrThrow(id);
        String urlAssinada = null;
        if (analise.getStatus().equals(StatusProcessamento.CONCLUIDO)) {
            urlAssinada = arquivoStorageGateway.gerarUrlAssinada(analise.getId());
        }

        return BuscarAnaliseDiagramaResponse.builder()
                .id(analise.getId().toString())
                .descricao(analise.getDescricao())
                .status(analise.getStatus())
                .motivo(analise.getMotivo())
                .nomeArquivo(analise.getArquivo().getNome())
                .tipoConteudo(analise.getArquivo().getTipoConteudo())
                .urlRelatorio(urlAssinada)
                .dataCriacao(analise.getDataCriacao())
                .dataAtualizacao(analise.getDataAtualizacao())
                .build();
    }
}

