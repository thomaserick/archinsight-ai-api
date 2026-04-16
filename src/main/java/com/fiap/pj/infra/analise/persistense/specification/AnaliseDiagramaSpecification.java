package com.fiap.pj.infra.analise.persistense.specification;

import com.fiap.pj.infra.analise.persistense.AnaliseDiagramaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.fiap.pj.infra.util.SpecificationUtils.likeTerm;
import static org.springframework.util.StringUtils.hasText;


public class AnaliseDiagramaSpecification {


    private static final String FIELD_DESCRICAO = "descricao";
    private static final String FIELD_ATIVO = "ativo";

    private final String nome;
    private final Boolean ativo;

    public AnaliseDiagramaSpecification(String nome, Boolean ativo) {
        this.nome = nome;
        this.ativo = ativo;
    }

    public Specification<AnaliseDiagramaEntity> buildSpecification() {
        Specification<AnaliseDiagramaEntity> specs = Specification.allOf();

        if (hasText(this.nome)) {
            specs = specs.and(queContenhaNomeCom(this.nome));
        }

        if (Objects.nonNull(ativo)) {
            specs = specs.and(queContenhaAtivoIgualA(this.ativo));
        }
        return specs;
    }

    private Specification<AnaliseDiagramaEntity> queContenhaNomeCom(String name) {
        return (root, criteriaQuery, builder) ->
                builder.like(builder.upper(root.get(FIELD_DESCRICAO)), likeTerm(name.trim().toUpperCase()));
    }

    private Specification<AnaliseDiagramaEntity> queContenhaAtivoIgualA(boolean active) {
        return (root, criteriaQuery, builder) ->
                builder.equal(root.get(FIELD_ATIVO), active);
    }
}
