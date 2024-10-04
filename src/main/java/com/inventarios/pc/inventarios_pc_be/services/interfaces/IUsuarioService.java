package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DocumentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailExistException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarUsuarioRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuarioResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;

/**
 * Interfaz para el servicio de gestión de usuarios.
 */
public interface IUsuarioService {

        /**
         * Registra un nuevo usuario en el sistema.
         * 
         * @param usuarioDTO Objeto que contiene la información del usuario a registrar.
         * @return UsuarioDTO con la información del usuario registrado.
         * @throws LocationNotFoundException Si la ubicación asociada al usuario no se encuentra.
         * @throws RolNotFoundException Si el rol del usuario no se encuentra.
         * @throws DocumentNotFoundException Si el tipo de documento del usuario no se encuentra.
         */
        public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) 
            throws LocationNotFoundException, RolNotFoundException, DocumentNotFoundException, EmailExistException;
    
        /**
         * Restablece la contraseña de un usuario utilizando un token de recuperación.
         * 
         * @param token El token generado para la recuperación de la contraseña.
         * @param nuevaPassword La nueva contraseña a establecer.
         * @param nuevaPassword2 Confirmación de la nueva contraseña.
         * @throws TokenNotValidException Si el token de recuperación no es válido.
         * @throws EmailNotFoundException Si el correo asociado al token no se encuentra.
         * @throws PasswordNotEqualsException Si las contraseñas proporcionadas no coinciden.
         */
        public void restablecerpassword(String token, String nuevaPassword, String nuevaPassword2)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException;
    
        /**
         * Cambia la contraseña de un usuario autenticado.
         * 
         * @param cambiarPasswordRequest Objeto que contiene las contraseñas actual y nueva.
         * @throws TokenNotValidException Si el token no es válido.
         * @throws EmailNotFoundException Si el correo asociado al token no se encuentra.
         * @throws PasswordNotEqualsException Si las contraseñas no coinciden o la actual es incorrecta.
         */
        public void cambiarContraseña(CambiarPasswordRequest cambiarPasswordRequest)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException;
    
        /**
         * Envía un token de recuperación de contraseña al correo del usuario.
         * 
         * @param correo Correo electrónico del usuario que solicita la recuperación.
         * @throws EmailNotFoundException Si no se encuentra el correo en la base de datos.
         */
        public void enviarTokenRecuperacion(String correo) throws EmailNotFoundException;
    
        /**
         * Lista todos los usuarios en el sistema.
         * 
         * @return Lista de objetos UsuariosResponse con la información de todos los usuarios.
         */
        public List<Usuario> listarUsuarios();
    
        /**
         * Actualiza la información de un usuario existente.
         * 
         * @param id El ID del usuario a actualizar.
         * @param usuarioDTO Objeto que contiene la nueva información del usuario.
         * @return UsuarioDTO con la información del usuario actualizado.
         * @throws UserNotFoundException Si el usuario no se encuentra en la base de datos.
         * @throws RolNotFoundException Si el rol del usuario no se encuentra.
         * @throws LocationNotFoundException Si la ubicación asociada al usuario no se encuentra.
         * @throws DocumentNotFoundException Si el tipo de documento del usuario no se encuentra.
         */
        public UsuarioDTO actualizarUsuario(Integer id, ActualizarUsuarioRequest usuarioDTO)
            throws UserNotFoundException, RolNotFoundException, LocationNotFoundException, DocumentNotFoundException;
    
        /**
         * Elimina (marca como eliminada) un usuario en el sistema.
         * 
         * @param id El ID del usuario a eliminar.
         * @throws UserNotFoundException Si el usuario no se encuentra en la base de datos.
         * @throws DeleteNotAllowedException Si el usuario no puede ser eliminado.
         */
        public void eliminarUsuario(Integer id) 
            throws UserNotFoundException, DeleteNotAllowedException;
    
        /**
         * Obtiene la información de un usuario por su ID.
         * 
         * @param id El ID del usuario a buscar.
         * @return UsuarioResponse con la información del usuario encontrado.
         * @throws UserNotFoundException Si el usuario no se encuentra en la base de datos.
         */
        public UsuarioResponse listarUsuarioById(Integer id) throws UserNotFoundException;
    }
    