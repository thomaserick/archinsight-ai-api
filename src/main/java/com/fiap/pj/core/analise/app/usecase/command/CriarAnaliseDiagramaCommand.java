package com.fiap.pj.core.analise.app.usecase.command;


import org.springframework.web.multipart.MultipartFile;

public record CriarAnaliseDiagramaCommand(MultipartFile arquivo) {
}
