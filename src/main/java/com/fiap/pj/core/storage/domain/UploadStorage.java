package com.fiap.pj.core.storage.domain;

import java.io.InputStream;
import java.util.UUID;

public record UploadStorage(
        UUID id, String nomeArquivo, String tipoConteudo, InputStream conteudo, long tamanho) {
}
