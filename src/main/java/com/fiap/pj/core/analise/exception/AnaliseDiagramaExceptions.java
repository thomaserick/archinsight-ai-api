package com.fiap.pj.core.analise.exception;

public class AnaliseDiagramaExceptions {
    private AnaliseDiagramaExceptions() {
    }

    public static class AnaliseDiagramaNaoEncontradoException extends RuntimeException {
        public AnaliseDiagramaNaoEncontradoException() {
            super("Analise Diagrama não encontrado.");
        }
    }

}

