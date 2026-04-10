package com.fiap.pj.infra.storage.gateways;


import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.core.storage.domain.UploadStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.UUID;

public class S3ArquivoStorageGatewayImpl implements ArquivoStorageGateway {

    private static final Logger log = LoggerFactory.getLogger(S3ArquivoStorageGatewayImpl.class);
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3ArquivoStorageGatewayImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void upload(UploadStorage storage) {
        var key = buildKey(storage.id());
        var putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(storage.tipoConteudo())
                .metadata(Map.of("filename", storage.nomeArquivo()))
                .build();
        s3Client.putObject(putRequest, RequestBody.fromInputStream(storage.conteudo(), storage.tamanho()));
        log.info("Arquivo '{}' enviado para S3 bucket '{}' com chave '{}'", storage.nomeArquivo(), bucketName, key);
    }

    private String buildKey(UUID id) {
        return "diagramas/" + id;
    }
}
