package com.fiap.pj.infra.analise.controller;

import com.fiap.pj.core.analise.app.usecase.CriarAnaliseDiagramaUseCase;
import com.fiap.pj.core.analise.app.usecase.ListarAnaliseDiagramaUseCase;
import com.fiap.pj.core.analise.app.usecase.command.CriarAnaliseDiagramaCommand;
import com.fiap.pj.infra.analise.controller.openapi.AnaliseDiagramaControllerOpenApi;
import com.fiap.pj.infra.analise.controller.request.ListarAnaliseDiagramaRequest;
import com.fiap.pj.infra.analise.controller.response.AnaliseDiagramaResponse;
import com.fiap.pj.infra.sk.api.Slice;
import com.fiap.pj.infra.sk.web.ResponseEntityUtils;
import com.fiap.pj.infra.sk.web.ResponseEntityUtils.ResponseId;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = AnaliseDiagramaController.PATH)
@AllArgsConstructor
public class AnaliseDiagramaController implements AnaliseDiagramaControllerOpenApi {

    public static final String PATH = "v1/analises-diagrama";

    private final CriarAnaliseDiagramaUseCase criarAnaliseDiagramaUseCase;
    private final ListarAnaliseDiagramaUseCase listarAnaliseDiagramaUseCase;


    @Override
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseId> criarAnaliseDiagrama(@Parameter(description = "Arquivo para analise diagrama", content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
                                                           @RequestPart(value = "file") MultipartFile file) {
        var service = criarAnaliseDiagramaUseCase.handle(new CriarAnaliseDiagramaCommand(file));
        return ResponseEntityUtils.create(getClass(), service.getId());
    }


    @Override
    @GetMapping
    public Slice<AnaliseDiagramaResponse> listarAnalisesDiagram(@ParameterObject ListarAnaliseDiagramaRequest filterRequest, @ParameterObject Pageable pageable) {
        filterRequest.setPageable(pageable);
        return listarAnaliseDiagramaUseCase.handle(filterRequest);
    }


}
