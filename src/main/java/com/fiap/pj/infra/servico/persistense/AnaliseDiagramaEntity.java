package com.fiap.pj.infra.servico.persistense;


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

    @Column(nullable = false)
    private ZonedDateTime dataCriacao;

    @Column(nullable = false)
    private ZonedDateTime dataAtualizacao;

    public AnaliseDiagramaEntity(UUID id, ArquivoEmbeddable arquivo, StatusProcessamento status,
                                 ZonedDateTime dataCriacao, ZonedDateTime dataAtualizacao) {
        this.id = requireNonNull(id);
        this.arquivo = requireNonNull(arquivo);
        this.status = requireNonNull(status);
        this.dataCriacao = requireNonNull(dataCriacao);
        this.dataAtualizacao = requireNonNull(dataAtualizacao);
    }

}
