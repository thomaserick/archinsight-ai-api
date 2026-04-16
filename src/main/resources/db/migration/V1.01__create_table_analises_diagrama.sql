CREATE TABLE analises_diagrama
(
    id               UUID                     NOT NULL,
    descricao        VARCHAR(255),
    nome_arquivo     VARCHAR(255)             NOT NULL,
    tipo_conteudo    VARCHAR(255)             NOT NULL,
    status           VARCHAR(50)              NOT NULL,
    motivo           VARCHAR(255),
    data_criacao     TIMESTAMP WITH TIME ZONE NOT NULL,
    data_atualizacao TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT analises_diagrama_pkey PRIMARY KEY (id)
);

