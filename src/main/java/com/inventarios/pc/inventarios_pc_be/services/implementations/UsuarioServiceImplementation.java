package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.ArrayList;
import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarUsuarioRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuarioResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;

@Service
public class UsuarioServiceImplementation implements IUsuarioService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerador jwtGenerador;

    /**
     * Registra un nuevo usuario en el sistema, asignándole su rol, tipo de
     * documento y ubicación.
     * Valida que existan el rol, tipo de documento y ubicación especificados, y
     * encripta la password
     * antes de almacenar el usuario en la base de datos.
     *
     * @param usuarioDTO Un objeto {@link UsuarioDTO} que contiene la información
     *                   del usuario a registrar,
     *                   incluyendo su rol, tipo de documento, y ubicación.
     * @return Un objeto {@link UsuarioDTO} que contiene la información del usuario
     *         recién creado.
     * @throws IllegalArgumentException Si no se encuentra el rol, el tipo de
     *                                  documento o la ubicación
     *                                  especificados en el {@link UsuarioDTO}.
     */
    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO)
            throws LocationNotFoundException, RolNotFoundException, DocumentNotFoundException, EmailExistException,
            SelectNotAllowedException {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailExistException(String.format(IS_ALREADY_USE, "CORREO").toUpperCase());
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        Rol rol = rolRepository.findById(usuarioDTO.getRol()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacion()).orElse(null);

        if (rol == null)
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());

        if (tipoDocumento == null)
            throw new DocumentNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DOCUMENTO").toUpperCase());

        if (ubicacion == null)
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        if (ubicacion.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
        }

        if (rol.getDeleteFlag() == true)

        {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ROL").toUpperCase());
        }

        usuario.setRolId(rol);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setUbicacionId(ubicacion);
        usuario.setDeleteFlag(false);
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        Usuario usuarioCreado = usuarioRepository.save(usuario);

        UsuarioDTO usuarioCreadoDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioCreado, usuarioCreadoDTO);

        return usuarioCreadoDTO;
    }

    /**
     * Método que obtiene todos los usuarios desde la base de datos y los convierte
     * a una lista de objetos UsuariosResponse.
     * 
     * @return Lista de objetos UsuariosResponse que contiene la información de los
     *         usuarios.
     */
    @Override
    public List<Usuario> listarUsuarios() {
        // List<Usuario> usuarios = usuarioRepository.findAll();

        // List<UsuariosResponse> usuariosResponses = new ArrayList<>();

        // for (Usuario usuario : usuarios) {
        // UsuariosResponse usuarioResponse = new UsuariosResponse();
        // usuarioResponse.setId(usuario.getId());
        // usuarioResponse.setNombreCompleto(usuario.getPrimerNombre() + " " +
        // usuario.getSegundoNombre() + " "
        // + usuario.getPrimerApellido() + " " + usuario.getSegundoApellido());
        // usuarioResponse.setRol(usuario.getRolId().getNombre());
        // usuarioResponse.setUbicacion(usuario.getUbicacionId().getNombre());
        // usuarioResponse.setCorreo(usuario.getCorreo());

        // usuariosResponses.add(usuarioResponse);
        // }

        return (List<Usuario>) usuarioRepository.findAll();
    }

    /**
     * Genera un token de recuperación de contraseña y envía un correo al usuario
     * con un enlace
     * para restablecer la contraseña.
     *
     * @param correo El correo electrónico del usuario que solicita la recuperación.
     * @throws EmailNotFoundException Si no se encuentra un usuario asociado al
     *                                correo proporcionado.
     */
    @Async
    @Override
    public void enviarTokenRecuperacion(String correo) throws EmailNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "CORREO").toUpperCase());
        }

        String tokenRecuperacion = jwtGenerador.generarTokenRecuperacion(correo);
        String urlRecuperacion = "http://localhost:4200/cambiar-contrasena?token=" + tokenRecuperacion; // Definir
                                                                                                        // la
                                                                                                        // password

        emailService.sendEmail(correo, "Solicitud de Cambio de Contraseña",
                "Para restablecer su contraseña, presione el siguiente botón",
                urlRecuperacion);
    }

    /**
     * Restablece la contraseña de un usuario utilizando un token de recuperación.
     * 
     * @param token          El token de recuperación generado para el usuario.
     * @param nuevaPassword  La nueva contraseña que el usuario desea establecer.
     * @param nuevaPassword2 La confirmación de la nueva contraseña.
     * @throws TokenNotValidException     Si el token proporcionado no es válido.
     * @throws EmailNotFoundException     Si no se encuentra un usuario asociado al
     *                                    correo en el token.
     * @throws PasswordNotEqualsException Si las contraseñas proporcionadas no
     *                                    coinciden.
     */
    @Override
    public void restablecerpassword(String token, String nuevaPassword, String nuevaPassword2)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException {
        String email = jwtGenerador.obtenerCorreoDeJWT(token);

        if (email == null) {
            throw new TokenNotValidException(String.format(IS_NOT_VALID, "TOKEN").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreo(email).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "CORREO").toUpperCase());
        }

        if (!nuevaPassword.equals(nuevaPassword2)) {
            throw new PasswordNotEqualsException(String.format(ARE_NOT_EQUALS, "LAS NUEVAS CONTRASEÑAS").toUpperCase());
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    /**
     * Cambia la contraseña actual de un usuario autenticado.
     * 
     * @param cambiarPasswordRequest Objeto que contiene la contraseña actual, la
     *                               nueva contraseña y la confirmación de la nueva
     *                               contraseña.
     * @throws TokenNotValidException     Si el token de autenticación no es válido.
     * @throws EmailNotFoundException     Si no se encuentra un usuario asociado al
     *                                    token.
     * @throws PasswordNotEqualsException Si la contraseña actual es incorrecta o
     *                                    las nuevas contraseñas no coinciden.
     */
    @Override
    public void cambiarContraseña(CambiarPasswordRequest cambiarPasswordRequest)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException {
        String email = jwtGenerador.obtenerCorreoDeJWT(cambiarPasswordRequest.getToken());

        if (email == null) {
            throw new TokenNotValidException(String.format(IS_NOT_VALID, "TOKEN").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreo(email).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "CORREO").toUpperCase());
        }

        if (!passwordEncoder.matches(cambiarPasswordRequest.getActualPassword(), usuario.getPassword())) {
            throw new PasswordNotEqualsException(String.format(IS_NOT_CORRECT, "LA ACTUAL CONTRASEÑA").toUpperCase());
        }

        if (!cambiarPasswordRequest.getNuevaPassword().equals(cambiarPasswordRequest.getNuevaPassword2())) {
            throw new PasswordNotEqualsException(String.format(ARE_NOT_EQUALS, "LAS NUEVAS CONTRASEÑAS").toUpperCase());

        }

        usuario.setPassword(passwordEncoder.encode(cambiarPasswordRequest.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }

    /**
     * Método que actualiza la información de un usuario existente.
     * 
     * @param id         El ID del usuario que se va a actualizar.
     * @param usuarioDTO Objeto ActualizarUsuarioRequest que contiene la nueva
     *                   información del usuario.
     * @return UsuarioDTO con la información del usuario actualizado.
     * @throws UserNotFoundException     Si el usuario no se encuentra en la base de
     *                                   datos.
     * @throws RolNotFoundException      Si el rol especificado no se encuentra en
     *                                   la base de datos.
     * @throws LocationNotFoundException Si la ubicación especificada no se
     *                                   encuentra en la base de datos.
     * @throws DocumentNotFoundException Si el tipo de documento especificado no se
     *                                   encuentra en la base de datos.
     */
    @Override
    public UsuarioDTO actualizarUsuario(Integer id, ActualizarUsuarioRequest usuarioDTO)
            throws UserNotFoundException, RolNotFoundException, LocationNotFoundException, DocumentNotFoundException,
            UpdateNotAllowedException, SelectNotAllowedException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE USUARIO").toUpperCase());
        }

        Rol rol = rolRepository.findById(usuarioDTO.getRol()).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());
        }
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacion()).orElse(null);
        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
        }

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);

        if (tipoDocumento == null) {
            throw new DocumentNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DOCUMENTO").toUpperCase());
        }

        if (ubicacion.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
        }

        if (rol.getDeleteFlag() == true)

        {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ROL").toUpperCase());
        }

        usuario.setRolId(rol);
        usuario.setUbicacionId(ubicacion);
        usuario.setTipoDocumento(tipoDocumento);
        BeanUtils.copyProperties(usuarioDTO, usuario);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        UsuarioDTO usuarioActualizadoDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioActualizado, usuarioActualizadoDTO);
        return usuarioActualizadoDTO;

    }

    /**
     * Método que marca un usuario como eliminado en la base de datos.
     * 
     * @param id El ID del usuario a eliminar.
     * @throws UserNotFoundException     Si el usuario no se encuentra en la base de
     *                                   datos.
     * @throws DeleteNotAllowedException Si el usuario ya ha sido eliminado
     *                                   previamente.
     */
    @Override
    public void eliminarUsuario(Integer id) throws UserNotFoundException, DeleteNotAllowedException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE USUARIO").toUpperCase());
        }

        usuario.setDeleteFlag(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Integer id) throws UserNotFoundException, ActivateNotAllowedException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR ESTE USUARIO").toUpperCase());
        }

        usuario.setDeleteFlag(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponse listarUsuarioById(Integer id) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        BeanUtils.copyProperties(usuario, usuarioResponse);
        usuarioResponse.setRol(usuario.getRolId().getNombre());
        usuarioResponse.setTipoDocumento(usuario.getTipoDocumento().getNombre());
        usuarioResponse.setSede(usuario.getUbicacionId().getArea().getSede().getNombre());
        usuarioResponse.setArea(usuario.getUbicacionId().getArea().getNombre());
        usuarioResponse.setUbicacion(usuario.getUbicacionId().getNombre());
        return usuarioResponse;
    }
}
