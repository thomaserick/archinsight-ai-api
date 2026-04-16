package com.fiap.pj.infra.analise.gateways;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.exception.AnaliseDiagramaExceptions;
import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.analise.persistense.AnaliseDiagramaRepositoryJpa;
import com.fiap.pj.infra.analise.persistense.specification.AnaliseDiagramaSpecification;
import com.fiap.pj.infra.sk.api.Slice;

import java.util.UUID;

public class AnaliseDiagramaRepositoryGatewayImpl implements AnaliseDiagramaGateway {

    private final AnaliseDiagramaRepositoryJpa repository;

    public AnaliseDiagramaRepositoryGatewayImpl(AnaliseDiagramaRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public AnaliseDiagrama salvar(AnaliseDiagrama analiseDiagrama) {
        var analiseDiagramaEntity = AnaliseDiagramaRepositoryMapper.mapToTable(analiseDiagrama);
        return AnaliseDiagramaRepositoryMapper.mapToDomain(repository.save(analiseDiagramaEntity));
    }

    @Override
    public Slice<AnaliseDiagramaResponse> listarAnaliseDiagrama(ListarAnaliseDiagramaRequest request) {
        var specification = new AnaliseDiagramaSpecification(request.getDescricao(), request.getStatus());
        return repository.findProjectedBy(specification.buildSpecification(), request.getPageable(), AnaliseDiagramaResponse.class);
    }

    @Override
    public AnaliseDiagrama buscarPorIdOrThrow(UUID id) {
        return repository.findById(id)
                .map(AnaliseDiagramaRepositoryMapper::mapToDomain)
                .orElseThrow(AnaliseDiagramaExceptions.AnaliseDiagramaNaoEncontradoException::new);
    }


}
