package com.fiap.pj.infra.analise.persistense;


import com.fiap.pj.core.analise.domain.StatusProcessamento;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "analises_diagrama")
@NoArgsConstructor
@Getter
public class AnaliseDiagramaEntity {

    @Id
    private UUID id;

    @Embedded
    private ArquivoEmbeddable arquivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProcessamento status;

    private String descricao;

    private String motivo;

    @Column(nullable = false)
    private ZonedDateTime dataCriacao;

    @Column(nullable = false)
    private ZonedDateTime dataAtualizacao;

    public AnaliseDiagramaEntity(UUID id, ArquivoEmbeddable arquivo, StatusProcessamento status, String descricao,
                                 ZonedDateTime dataCriacao, ZonedDateTime dataAtualizacao, String motivo) {
        this.id = requireNonNull(id);
        this.arquivo = requireNonNull(arquivo);
        this.status = requireNonNull(status);
        this.descricao = descricao;
        this.dataCriacao = requireNonNull(dataCriacao);
        this.dataAtualizacao = requireNonNull(dataAtualizacao);
        this.motivo = motivo;
    }

}
