package com.fiap.pj.infra.config;


import com.fiap.pj.core.analise.app.CriarAnaliseDiagramaUseCaseImpl;
import com.fiap.pj.core.analise.app.ListarAnaliseDiagramaUseCaseImpl;
import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.infra.servico.gateways.AnaliseDiagramaRepositoryGatewayImpl;
import com.fiap.pj.infra.servico.persistense.AnaliseDiagramaRepositoryJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnaliseDiagramaConfig {


    @Bean
    CriarAnaliseDiagramaUseCaseImpl criarAnaliseDiagramaUseCase(AnaliseDiagramaGateway analiseDiagramaGateway) {
        return new CriarAnaliseDiagramaUseCaseImpl(analiseDiagramaGateway);
    }

    @Bean
    ListarAnaliseDiagramaUseCaseImpl listarAnaliseDiagramaUseCase(AnaliseDiagramaGateway analiseDiagramaGateway
    ) {
        return new ListarAnaliseDiagramaUseCaseImpl(analiseDiagramaGateway);
    }

    @Bean
    AnaliseDiagramaRepositoryGatewayImpl analiseDiagramaRepositoryGateway(AnaliseDiagramaRepositoryJpa repository
    ) {
        return new AnaliseDiagramaRepositoryGatewayImpl(repository);
    }
}
