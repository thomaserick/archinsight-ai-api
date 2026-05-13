package com.fiap.pj.core.storage.app.gateways;

import com.fiap.pj.core.storage.domain.UploadStorage;

import java.util.UUID;

public interface ArquivoStorageGateway {

    void upload(UploadStorage uploadStorage);

    String gerarUrlAssinada(UUID key);
}

