package com.fiap.pj.core.analise.app;


import com.fiap.pj.core.analise.app.gateways.AnaliseDiagramaGateway;
import com.fiap.pj.core.analise.app.usecase.CriarAnaliseDiagramaUseCase;
import com.fiap.pj.core.analise.app.usecase.command.CriarAnaliseDiagramaCommand;
import com.fiap.pj.core.analise.domain.AnaliseDiagrama;
import com.fiap.pj.core.analise.domain.StatusProcessamento;
import com.fiap.pj.core.analise.domain.vo.Arquivo;

import java.util.Objects;

public class CriarAnaliseDiagramaUseCaseImpl implements CriarAnaliseDiagramaUseCase {

    private final AnaliseDiagramaGateway analiseDiagramaGateway;

    public CriarAnaliseDiagramaUseCaseImpl(AnaliseDiagramaGateway analiseDiagramaGateway) {
        this.analiseDiagramaGateway = analiseDiagramaGateway;
    }

    @Override
    /*
      Falta:
        - implementar upload para Amazon S3
        - validar se ja existe o arquivo

     */
    public AnaliseDiagrama handle(CriarAnaliseDiagramaCommand cmd) {
        var nomeArquivo = Objects.requireNonNullElse(cmd.arquivo().getOriginalFilename(), cmd.arquivo().getName());
        var tipoConteudo = Objects.requireNonNullElse(cmd.arquivo().getContentType(), "application/octet-stream");
        var arquivo = new Arquivo(nomeArquivo, tipoConteudo);
        var analiseDiagrama = new AnaliseDiagrama(arquivo, StatusProcessamento.EM_PROCESSAMENTO);
        return analiseDiagramaGateway.salvar(analiseDiagrama);
    }
}
