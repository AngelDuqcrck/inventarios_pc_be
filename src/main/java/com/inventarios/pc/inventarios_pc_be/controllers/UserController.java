package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarUsuarioRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuarioResponse;
//Controlador donde se encuentran todos los endpoints relacionados con los usuarios
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

        @Autowired
        private IUsuarioService usuarioServiceImplementation;

        @Autowired
        private JwtGenerador jwtGenerador;

        /**
         * Cambia la contraseña de un usuario autenticado.
         * 
         * @param cambiarPasswordRequest Objeto que contiene la contraseña actual y la
         *                               nueva contraseña.
         * @return Respuesta HTTP con un mensaje indicando el éxito o error de la
         *         operación.
         * @throws EmailNotFoundException     Si no se encuentra un usuario asociado al
         *                                    correo.
         * @throws TokenNotValidException     Si el token de autenticación no es válido.
         * @throws PasswordNotEqualsException Si la contraseña actual no coincide o las
         *                                    nuevas contraseñas no son iguales.
         */
        @PreAuthorize("isAuthenticated()")
        @PostMapping("/cambiar-password")
        public ResponseEntity<HttpResponse> cambiarPassword(
                        @Valid @RequestBody CambiarPasswordRequest cambiarPasswordRequest)
                        throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException {
                usuarioServiceImplementation.cambiarContraseña(cambiarPasswordRequest);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Contraseña cambiada exitosamente"),
                                HttpStatus.OK);
        }

        /**
         * Lista todos los usuarios del sistema.
         * Este método está restringido solo para los usuarios con el rol "ADMIN".
         *
         * @return ResponseEntity que contiene una lista de objetos UsuariosResponse y
         *         el estado HTTP OK.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        // public ResponseEntity<List<UsuariosResponse>> listarUsuarios() {
        // List<UsuariosResponse> usuariosResponses =
        // usuarioServiceImplementation.listarUsuarios();
        // return new ResponseEntity<>(usuariosResponses, HttpStatus.OK);
        // }
        public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
                return ResponseEntity.ok(
                                usuarioServiceImplementation.listarUsuarios().stream().map(usuario -> {
                                        UsuarioResponse usuarioResponse = new UsuarioResponse();
                                        BeanUtils.copyProperties(usuario, usuarioResponse);
                                        usuarioResponse.setUbicacion(usuario.getUbicacionId().getNombre());

                                        usuarioResponse.setTipoDocumento(usuario.getTipoDocumento().getNombre());
                                        usuarioResponse.setRol(usuario.getRolId().getNombre());
                                        usuarioResponse.setDelete_flag(usuario.getDeleteFlag());
                                        return usuarioResponse;
                                }).collect(Collectors.toList()));
        }

        /**
         * Actualiza la información de un usuario específico.
         * Solo los usuarios con el rol "ADMIN" pueden acceder a este servicio.
         *
         * @param usuarioId                El ID del usuario que se va a actualizar.
         * @param actualizarUsuarioRequest Objeto que contiene la nueva información del
         *                                 usuario.
         * @return ResponseEntity con un mensaje de éxito y el estado HTTP OK.
         * @throws RolNotFoundException      Si el rol especificado no existe.
         * @throws LocationNotFoundException Si la ubicación especificada no existe.
         * @throws DocumentNotFoundException Si el tipo de documento especificado no
         *                                   existe.
         * @throws UserNotFoundException     Si el usuario no existe en la base de
         *                                   datos.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @PutMapping("/actualizar/{usuarioId}")
        public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Integer usuarioId,
                        @RequestBody ActualizarUsuarioRequest actualizarUsuarioRequest)
                        throws SelectNotAllowedException, UpdateNotAllowedException, RolNotFoundException,
                        LocationNotFoundException, DocumentNotFoundException, UserNotFoundException {
                UsuarioDTO usuarioDTO = usuarioServiceImplementation.actualizarUsuario(usuarioId, actualizarUsuarioRequest);
                return new ResponseEntity<>( usuarioDTO, HttpStatus.OK);
        }

        /**
         * Elimina un usuario marcándolo como eliminado.
         * Solo los usuarios con el rol "ADMIN" pueden acceder a este servicio.
         *
         * @param usuarioId El ID del usuario que se va a eliminar.
         * @return ResponseEntity con un mensaje de éxito y el estado HTTP OK.
         * @throws UserNotFoundException     Si el usuario no existe.
         * @throws DeleteNotAllowedException Si el usuario ya está marcado como
         *                                   eliminado.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{usuarioId}")
        public ResponseEntity<HttpResponse> eliminarUsuario(@PathVariable Integer usuarioId)
                        throws UserNotFoundException, DeleteNotAllowedException {
                usuarioServiceImplementation.eliminarUsuario(usuarioId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Usuario eliminado con exito"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/activar/{usuarioId}")
        public ResponseEntity<HttpResponse> activarUsuario(@PathVariable Integer usuarioId)
                        throws UserNotFoundException, ActivateNotAllowedException {
                usuarioServiceImplementation.activarUsuario(usuarioId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Usuario activado con exito"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/{id}")
        public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Integer id) throws UserNotFoundException {
                UsuarioResponse usuarioResponse = usuarioServiceImplementation.listarUsuarioById(id);
                return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/ubicacion")
        public ResponseEntity<List<UsuarioResponse>> getUsuarioByUbicacion(@RequestParam Integer ubicId) throws LocationNotFoundException {
                List<UsuarioResponse> usuariosResponse = usuarioServiceImplementation.listarUsuarioByUbic(ubicId);
                return new ResponseEntity<>(usuariosResponse, HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/validar")
        public ResponseEntity<Boolean> validarToken(@RequestHeader("Authorization") String token) {
                // Elimina el prefijo "Bearer " del token si está presente
                if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                }

                boolean isValid = jwtGenerador.validarToken(token);
                return ResponseEntity.ok(isValid);
        }
}
