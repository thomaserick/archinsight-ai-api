package com.fiap.pj.core.storage.exception;

public class StorageExceptions {
    private StorageExceptions() {
    }

    public static class ArquivoUploadException extends RuntimeException {
        public ArquivoUploadException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }
    }

}

