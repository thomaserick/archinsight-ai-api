package com.fiap.pj.infra.servico.gateways;

import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.infra.servico.persistense.AnaliseDiagramaEntity;
import com.fiap.pj.infra.servico.persistense.ArquivoEmbeddable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AnaliseDiagramaRepositoryMapper {

    public static AnaliseDiagramaEntity mapToTable(AnaliseDiagrama analiseDiagrama) {
        return new AnaliseDiagramaEntity(
                analiseDiagrama.getId(),
                new ArquivoEmbeddable(analiseDiagrama.getArquivo()),
                analiseDiagrama.getStatus(),
                analiseDiagrama.getDataCriacao(),
                analiseDiagrama.getDataAtualizacao()
        );
    }

    public static AnaliseDiagrama mapToDomain(AnaliseDiagramaEntity entity) {
        return new AnaliseDiagrama(
                entity.getId(),
                entity.getArquivo().toDomain(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }
}
