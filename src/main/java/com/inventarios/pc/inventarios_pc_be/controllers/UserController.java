package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DocumentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarUsuarioRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
//Controlador donde se encuentran todos los endpoints relacionados con los usuarios
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUsuarioService usuarioServiceImplementation;

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
    public ResponseEntity<HttpResponse> cambiarPassword(@RequestBody CambiarPasswordRequest cambiarPasswordRequest)
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
    public ResponseEntity<List<UsuariosResponse>> listarUsuarios() {
        List<UsuariosResponse> usuariosResponses = usuarioServiceImplementation.listarUsuarios();
        return new ResponseEntity<>(usuariosResponses, HttpStatus.OK);
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
    public ResponseEntity<HttpResponse> actualizarUsuario(@PathVariable Integer usuarioId,
            @RequestBody ActualizarUsuarioRequest actualizarUsuarioRequest)
            throws RolNotFoundException, LocationNotFoundException, DocumentNotFoundException, UserNotFoundException {
        usuarioServiceImplementation.actualizarUsuario(usuarioId, actualizarUsuarioRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Usuario actializado con exito"),
                HttpStatus.OK);
    }

    /**
 * Elimina un usuario marcándolo como eliminado.
 * Solo los usuarios con el rol "ADMIN" pueden acceder a este servicio.
 *
 * @param usuarioId El ID del usuario que se va a eliminar.
 * @return ResponseEntity con un mensaje de éxito y el estado HTTP OK.
 * @throws UserNotFoundException Si el usuario no existe.
 * @throws DeleteNotAllowedException Si el usuario ya está marcado como eliminado.
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

}
