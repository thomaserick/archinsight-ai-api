package com.fiap.pj.infra.analise.gateways;

import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.infra.analise.persistense.AnaliseDiagramaEntity;
import com.fiap.pj.infra.analise.persistense.ArquivoEmbeddable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AnaliseDiagramaRepositoryMapper {

    public static AnaliseDiagramaEntity mapToTable(AnaliseDiagrama analiseDiagrama) {
        return new AnaliseDiagramaEntity(
                analiseDiagrama.getId(),
                new ArquivoEmbeddable(analiseDiagrama.getArquivo()),
                analiseDiagrama.getStatus(),
                analiseDiagrama.getDescricao(),
                analiseDiagrama.getDataCriacao(),
                analiseDiagrama.getDataAtualizacao(), analiseDiagrama.getMotivo()
        );
    }

    public static AnaliseDiagrama mapToDomain(AnaliseDiagramaEntity entity) {
        return new AnaliseDiagrama(
                entity.getId(),
                entity.getDescricao(),
                entity.getArquivo().toDomain(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao());

    }
}
