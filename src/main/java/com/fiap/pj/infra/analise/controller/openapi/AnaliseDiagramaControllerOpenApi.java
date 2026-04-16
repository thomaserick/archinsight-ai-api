package com.fiap.pj.infra.analise.controller.openapi;

import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;
import com.fiap.pj.infra.sk.web.ResponseEntityUtils.ResponseId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public interface AnaliseDiagramaControllerOpenApi {

    @Operation(description = "Analisar Diagrama", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Analise Diagrama criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Analise Diagrama não pode ser processada.")})
    ResponseEntity<ResponseId> criarAnaliseDiagrama(@Parameter(description = "Arquivo comprovante", content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
                                                    @RequestPart(value = "file") MultipartFile file,
                                                    @RequestPart(value = "descricao") String descricao);


    @Operation(description = "Retorna uma lista de Analise Diagrama.", method = "GET")
    Slice<AnaliseDiagramaResponse> listarAnalisesDiagram(@ParameterObject ListarAnaliseDiagramaRequest filterRequest, @ParameterObject Pageable pageable);

}
