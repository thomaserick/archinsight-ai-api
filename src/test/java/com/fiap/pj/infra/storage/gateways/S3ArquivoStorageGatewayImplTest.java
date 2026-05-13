package com.fiap.pj.infra.storage.gateways;

import com.fiap.pj.core.storage.domain.UploadStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ArquivoStorageGatewayImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private S3ArquivoStorageGatewayImpl gateway;

    @BeforeEach
    void setUp() throws Exception {
        gateway = new S3ArquivoStorageGatewayImpl(s3Client, s3Presigner);
        Field bucketField = S3ArquivoStorageGatewayImpl.class.getDeclaredField("bucketName");
        bucketField.setAccessible(true);
        bucketField.set(gateway, "test-bucket");
    }

    @Nested
    @DisplayName("upload")
    class Upload {

        @Test
        @DisplayName("Deve fazer upload do arquivo no S3 com sucesso")
        void deveRealizarUploadComSucesso() {
            var id = UUID.randomUUID();
            var conteudo = new ByteArrayInputStream("conteudo".getBytes());
            var storage = new UploadStorage(id, "diagrama.png", "image/png", conteudo, 8L);

            gateway.upload(storage);

            var captor = ArgumentCaptor.forClass(PutObjectRequest.class);
            verify(s3Client).putObject(captor.capture(), any(RequestBody.class));

            var request = captor.getValue();
            assertThat(request.bucket()).isEqualTo("test-bucket");
            assertThat(request.key()).isEqualTo("diagramas/" + id);
            assertThat(request.contentType()).isEqualTo("image/png");
            assertThat(request.metadata()).containsEntry("filename", "diagrama.png");
        }
    }

    @Nested
    @DisplayName("gerarUrlAssinada")
    class GerarUrlAssinada {

        @Test
        @DisplayName("Deve gerar URL assinada para o relatorio no S3")
        void deveGerarUrlAssinadaComSucesso() throws Exception {
            var id = UUID.randomUUID();
            var expectedUrl = "https://test-bucket.s3.amazonaws.com/relatorios/" + id + "?signed=true";

            var presignedRequest = mock(PresignedGetObjectRequest.class);
            when(presignedRequest.url()).thenReturn(URI.create(expectedUrl).toURL());
            when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

            var result = gateway.gerarUrlAssinada(id);

            assertThat(result).isEqualTo(expectedUrl);
            verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
        }
    }
}

