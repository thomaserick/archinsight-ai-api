package com.fiap.pj.infra.analise.persistense.specification;

import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.infra.analise.persistense.AnaliseDiagramaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.fiap.pj.infra.util.SpecificationUtils.likeTerm;
import static org.springframework.util.StringUtils.hasText;


public class AnaliseDiagramaSpecification {


    private static final String FIELD_DESCRICAO = "descricao";
    private static final String FIELD_STATUS = "status";

    private final String descricao;
    private final StatusProcessamento status;

    public AnaliseDiagramaSpecification(String descricao, StatusProcessamento status) {
        this.descricao = descricao;
        this.status = status;
    }

    public Specification<AnaliseDiagramaEntity> buildSpecification() {
        Specification<AnaliseDiagramaEntity> specs = Specification.allOf();

        if (hasText(this.descricao)) {
            specs = specs.and(queContenhaNomeCom(this.descricao));
        }

        if (Objects.nonNull(status)) {
            specs = specs.and(queContenhaStatusIgualA(this.status));
        }
        return specs;
    }

    private Specification<AnaliseDiagramaEntity> queContenhaNomeCom(String descricao) {
        return (root, criteriaQuery, builder) ->
                builder.like(builder.upper(root.get(FIELD_DESCRICAO)), likeTerm(descricao.trim().toUpperCase()));
    }

    private Specification<AnaliseDiagramaEntity> queContenhaStatusIgualA(StatusProcessamento status) {
        return (root, criteriaQuery, builder) ->
                builder.equal(root.get(FIELD_STATUS), status.name());
    }
}
