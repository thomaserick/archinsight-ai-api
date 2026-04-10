package com.fiap.pj.infra.config;


import com.fiap.pj.core.analise.app.CriarAnaliseDiagramaUseCaseImpl;
import com.fiap.pj.core.analise.app.ListarAnaliseDiagramaUseCaseImpl;
import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaPublisherGateway;
import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.infra.servico.gateways.AnaliseDiagramaPublisherGatewayImpl;
import com.fiap.pj.infra.servico.gateways.AnaliseDiagramaRepositoryGatewayImpl;
import com.fiap.pj.infra.servico.persistense.AnaliseDiagramaRepositoryJpa;
import com.fiap.pj.infra.storage.gateways.S3ArquivoStorageGatewayImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AnaliseDiagramaConfig {


    @Bean
    CriarAnaliseDiagramaUseCaseImpl criarAnaliseDiagramaUseCase(AnaliseDiagramaGateway analiseDiagramaGateway,
                                                                ArquivoStorageGateway arquivoStorageGateway,
                                                                AnaliseDiagramaPublisherGateway publisherGateway) {
        return new CriarAnaliseDiagramaUseCaseImpl(analiseDiagramaGateway, arquivoStorageGateway, publisherGateway);
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

    @Bean
    S3ArquivoStorageGatewayImpl arquivoStorageGateway(S3Client s3Client
    ) {
        return new S3ArquivoStorageGatewayImpl(s3Client);
    }

    @Bean
    AnaliseDiagramaPublisherGatewayImpl analiseDiagramaPublisherGateway(
            RabbitTemplate rabbitTemplate,
            @Value("${broker.queue.analise.processar}") String routingKey
    ) {
        return new AnaliseDiagramaPublisherGatewayImpl(rabbitTemplate, routingKey);
    }
}
