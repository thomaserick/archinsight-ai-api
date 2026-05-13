package com.fiap.pj.infra.storage.gateways;


import com.fiap.pj.core.storage.app.gateways.ArquivoStorageGateway;
import com.fiap.pj.core.storage.domain.UploadStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public class S3ArquivoStorageGatewayImpl implements ArquivoStorageGateway {

    private static final Logger log = LoggerFactory.getLogger(S3ArquivoStorageGatewayImpl.class);
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3ArquivoStorageGatewayImpl(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
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

    @Override
    public String gerarUrlAssinada(UUID key) {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(buildKeyRelatorio(key))
                .build();

        var presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        var presignedUrl = s3Presigner.presignGetObject(presignRequest);
        return presignedUrl.url().toString();
    }

    private String buildKey(UUID id) {
        return "diagramas/" + id;
    }

    private String buildKeyRelatorio(UUID id) {
        return "relatorios/" + id;

    }
}
