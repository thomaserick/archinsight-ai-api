package com.fiap.pj.core.usuario.domain;

import com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory.E_MAIL;
import static com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory.LAST_NAME;
import static com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory.NAME;
import static com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory.PASSWORD;
import static com.fiap.pj.core.usuario.util.factrory.UsuarioTestFactory.USER_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioTest {


    @Test
    @DisplayName("Deve criar com sucesso uma instância de usuario.")
    void deveCriarUsuario() {

        var usuario = UsuarioTestFactory.umUsuario();

        assertEquals(NAME, usuario.getNome());
        assertEquals(LAST_NAME, usuario.getSobreNome());
        assertEquals(E_MAIL, usuario.getEmail());
        assertEquals(PASSWORD, usuario.getSenha());
        assertTrue(usuario.getPerfis().contains(USER_ROLE));
        assertTrue(usuario.isAtivo());
    }

    @Nested
    class FalhaNaCriacao {

        @Test
        void deveFalharComNomeInvalido() {
            assertThrows(NullPointerException.class,
                    () -> new Usuario(UsuarioTestFactory.ID,
                            null,
                            null,
                            null, null, true));
        }

        @Test
        void deveFalharComEmailInvalido() {
            assertThrows(NullPointerException.class,
                    () -> new Usuario(UsuarioTestFactory.ID,
                            NAME,
                            LAST_NAME,
                            null, null, true));
        }

        @Test
        void deveFalharComSenhaInvalida() {
            assertThrows(NullPointerException.class,
                    () -> new Usuario(UsuarioTestFactory.ID,
                            NAME,
                            LAST_NAME,
                            E_MAIL, null, true));
        }

    }

}
