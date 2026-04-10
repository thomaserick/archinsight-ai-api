package com.fiap.pj.core.analise.domain;

import com.fiap.pj.core.analise.domain.vo.Arquivo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnaliseDiagramaTest {

    private static final UUID ID = UUID.randomUUID();
    private static final Arquivo ARQUIVO = new Arquivo("diagrama.png", "image/png");
    private static final StatusProcessamento STATUS = StatusProcessamento.EM_PROCESSAMENTO;
    private static final ZonedDateTime DATA_CRIACAO = ZonedDateTime.of(2026, 4, 10, 12, 0, 0, 0, ZoneOffset.UTC);
    private static final ZonedDateTime DATA_ATUALIZACAO = ZonedDateTime.of(2026, 4, 10, 12, 30, 0, 0, ZoneOffset.UTC);

    @Nested
    @DisplayName("Construtor com datas automáticas")
    class ConstrutorSimples {

        @Test
        @DisplayName("Deve criar AnaliseDiagrama com datas geradas automaticamente")
        void deveCriarComDatasAutomaticas() {
            var analise = new AnaliseDiagrama(ID, ARQUIVO, STATUS);

            assertEquals(ID, analise.getId());
            assertEquals(ARQUIVO, analise.getArquivo());
            assertEquals(STATUS, analise.getStatus());
            assertNotNull(analise.getDataCriacao());
            assertNotNull(analise.getDataAtualizacao());
        }

        @Test
        @DisplayName("Datas geradas devem estar em UTC e sem nanos")
        void datasDeveEstarEmUtc() {
            var analise = new AnaliseDiagrama(ID, ARQUIVO, STATUS);

            assertEquals(ZoneOffset.UTC, analise.getDataCriacao().getZone());
            assertEquals(0, analise.getDataCriacao().getNano());
            assertEquals(ZoneOffset.UTC, analise.getDataAtualizacao().getZone());
            assertEquals(0, analise.getDataAtualizacao().getNano());
        }
    }

    @Nested
    @DisplayName("Construtor com datas explícitas")
    class ConstrutorCompleto {

        @Test
        @DisplayName("Deve criar AnaliseDiagrama com todas as propriedades informadas")
        void deveCriarComTodasPropriedades() {
            var analise = new AnaliseDiagrama(ID, ARQUIVO, STATUS, DATA_CRIACAO, DATA_ATUALIZACAO);

            assertEquals(ID, analise.getId());
            assertEquals(ARQUIVO, analise.getArquivo());
            assertEquals(STATUS, analise.getStatus());
            assertEquals(DATA_CRIACAO, analise.getDataCriacao());
            assertEquals(DATA_ATUALIZACAO, analise.getDataAtualizacao());
        }

        @Test
        @DisplayName("Deve aceitar todos os valores de StatusProcessamento")
        void deveAceitarTodosOsStatus() {
            for (StatusProcessamento status : StatusProcessamento.values()) {
                var analise = new AnaliseDiagrama(ID, ARQUIVO, status, DATA_CRIACAO, DATA_ATUALIZACAO);
                assertEquals(status, analise.getStatus());
            }
        }
    }

    @Nested
    @DisplayName("Validação de campos obrigatórios")
    class FalhaNaCriacao {

        @Test
        @DisplayName("Deve falhar quando id for null")
        void deveFalharComIdNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(null, ARQUIVO, STATUS, DATA_CRIACAO, DATA_ATUALIZACAO));
        }

        @Test
        @DisplayName("Deve falhar quando arquivo for null")
        void deveFalharComArquivoNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, null, STATUS, DATA_CRIACAO, DATA_ATUALIZACAO));
        }

        @Test
        @DisplayName("Deve falhar quando status for null")
        void deveFalharComStatusNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, ARQUIVO, null, DATA_CRIACAO, DATA_ATUALIZACAO));
        }

        @Test
        @DisplayName("Deve falhar quando dataCriacao for null")
        void deveFalharComDataCriacaoNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, ARQUIVO, STATUS, null, DATA_ATUALIZACAO));
        }

        @Test
        @DisplayName("Deve falhar quando dataAtualizacao for null")
        void deveFalharComDataAtualizacaoNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, ARQUIVO, STATUS, DATA_CRIACAO, null));
        }

        @Test
        @DisplayName("Deve falhar no construtor simples quando id for null")
        void deveFalharConstrutorSimplesComIdNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(null, ARQUIVO, STATUS));
        }

        @Test
        @DisplayName("Deve falhar no construtor simples quando arquivo for null")
        void deveFalharConstrutorSimplesComArquivoNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, null, STATUS));
        }

        @Test
        @DisplayName("Deve falhar no construtor simples quando status for null")
        void deveFalharConstrutorSimplesComStatusNull() {
            assertThrows(NullPointerException.class,
                    () -> new AnaliseDiagrama(ID, ARQUIVO, null));
        }
    }
}

