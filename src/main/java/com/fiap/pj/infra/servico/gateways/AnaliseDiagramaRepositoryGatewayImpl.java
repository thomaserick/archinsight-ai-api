package com.fiap.pj.infra.servico.gateways;

import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.infra.servico.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.servico.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.servico.persistense.AnaliseDiagramaRepositoryJpa;
import com.fiap.pj.infra.servico.persistense.specification.AnaliseDiagramaSpecification;
import com.fiap.pj.infra.sk.api.Slice;

public class AnaliseDiagramaRepositoryGatewayImpl implements AnaliseDiagramaGateway {

    private final AnaliseDiagramaRepositoryJpa repository;

    public AnaliseDiagramaRepositoryGatewayImpl(AnaliseDiagramaRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public AnaliseDiagrama salvar(AnaliseDiagrama analiseDiagrama) {
        var servicoEntity = AnaliseDiagramaRepositoryMapper.mapToTable(analiseDiagrama);
        return AnaliseDiagramaRepositoryMapper.mapToDomain(repository.save(servicoEntity));
    }

    @Override
    public Slice<AnaliseDiagramaResponse> listarServico(ListarAnaliseDiagramaRequest request) {
        var specification = new AnaliseDiagramaSpecification(request.getNome(), request.getAtivo());
        return repository.findProjectedBy(specification.buildSpecification(), request.getPageable(), AnaliseDiagramaResponse.class);
    }


}
