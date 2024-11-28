package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.ArrayList;
import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inventarios.pc.inventarios_pc_be.controllers.NotificationController;
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
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcta";

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

    @Autowired
    private NotificationController notificationController;

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
            throws DuplicateEntityException,LocationNotFoundException, RolNotFoundException, DocumentNotFoundException, EmailExistException,
            SelectNotAllowedException {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailExistException(String.format(IS_ALREADY_USE, "EL CORREO").toUpperCase());
        }
        Usuario usuario = new Usuario();
        if(usuarioRepository.existsByCorreoIgnoreCase(usuarioDTO.getCorreo())){
            throw new DuplicateEntityException("Ya existe un usuario con el correo " + usuarioDTO.getCorreo());
        }

        if(usuarioRepository.existsByCedulaIgnoreCase(usuarioDTO.getCedula())){
            throw new DuplicateEntityException("Ya existe un usuario con el número de documento " + usuarioDTO.getCedula());
        }
        
        BeanUtils.copyProperties(usuarioDTO, usuario);
        Rol rol = rolRepository.findById(usuarioDTO.getRol()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacion()).orElse(null);

        if (rol == null)
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());

        if (tipoDocumento == null)
            throw new DocumentNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE DOCUMENTO").toUpperCase());

        if (ubicacion == null)
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA UBICACION").toUpperCase());

        if (ubicacion.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA UBICACION " + ubicacion.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                    .toUpperCase());
        }

        if(ubicacion.getEstaOcupada() == true){
            throw new  SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA UBICACION " + ubicacion.getNombre() + " PORQUE SE ENCUENTRA OCUPADA")
                    .toUpperCase());
        }
        
        if (rol.getDeleteFlag() == true)

        {
            throw new

            SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL ROL " + rol.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADO")
                    .toUpperCase());
        }

        usuario.setRolId(rol);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setUbicacionId(ubicacion);
        usuario.setDeleteFlag(false);
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        
        Usuario usuarioCreado = usuarioRepository.save(usuario);
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(usuarioCreado.getId());
        usuarioResponse.setRol(usuarioCreado.getRolId().getNombre());
        usuarioResponse.setTipoDocumento(usuarioCreado.getTipoDocumento().getNombre());
        usuarioResponse.setSede(usuarioCreado.getUbicacionId().getArea().getSede().getNombre());
        usuarioResponse.setArea(usuarioCreado.getUbicacionId().getArea().getNombre());
        usuarioResponse.setUbicacion(usuarioCreado.getUbicacionId().getNombre());
        usuarioResponse.setPrimerNombre(usuarioCreado.getPrimerNombre());
        usuarioResponse.setSegundoNombre(usuarioCreado.getSegundoNombre());
        usuarioResponse.setPrimerApellido(usuarioCreado.getPrimerApellido());
        usuarioResponse.setSegundoApellido(usuarioCreado.getSegundoApellido());
        usuarioResponse.setCorreo(usuarioCreado.getCorreo());
        notificationController.sendNotification("USUARIO", usuarioCreado.getId(), usuarioResponse);
        Integer rolId = ubicacion.getArea().getRol().getId();
        if(rolId == 2){
        ubicacion.setEstaOcupada(true);
        ubicacionRepository.save(ubicacion);
        }
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
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, " EL CORREO").toUpperCase());
        }

        String tokenRecuperacion = jwtGenerador.generarTokenRecuperacion(correo);
         // Definir la ruta de acceso a la página de cambio de contraseña
        String urlRecuperacion = "http://192.168.8.2:83/cambiar-contrasena?token="+tokenRecuperacion;                                                                                               // la
                                                                                                        

        emailService.sendEmail(correo, "Solicitud de Cambio de Contraseña",
                "Para cambiar la contraseña y completar la solicitud, por favor ingrese al siguiente enlace:",
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
            throw new TokenNotValidException(String.format(IS_NOT_VALID, "EL TOKEN").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreo(email).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EL CORREO").toUpperCase());
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
            throw new TokenNotValidException(String.format(IS_NOT_VALID, "EL TOKEN").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreo(email).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EL CORREO").toUpperCase());
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
            UpdateNotAllowedException, SelectNotAllowedException, DuplicateEntityException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String
                    .format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE USUARIO PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }

        if(usuario.getId()==1){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL USUARIO CON ID 1").toUpperCase());
        }
        if(usuarioRepository.existsByCedulaIgnoreCaseAndIdNot(usuarioDTO.getCedula(), id)){
            throw new DuplicateEntityException("Ya existe un usuario con el número de documento " + usuarioDTO.getCedula());
        }
        if(usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(usuarioDTO.getCorreo(), id)){
            throw new DuplicateEntityException("Ya existe un usuario con el correo " + usuarioDTO.getCorreo());
        }

        Ubicacion ubicacionActual = usuario.getUbicacionId();

        Rol rol = rolRepository.findById(usuarioDTO.getRol()).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
        }
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacion()).orElse(null);
        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA UBICACION").toUpperCase());
        }
        if(ubicacionActual.getId() != ubicacion.getId()){
        
            if(ubicacion.getEstaOcupada() == true){
                throw new  SelectNotAllowedException(String
                        .format(IS_NOT_ALLOWED,
                                "SELECCIONAR LA UBICACION " + ubicacion.getNombre() + " PORQUE SE ENCUENTRA OCUPADA")
                        .toUpperCase());
            }
        }

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);

        if (tipoDocumento == null) {
            throw new DocumentNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE DOCUMENTO").toUpperCase());
        }

        if (ubicacion.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA UBICACION " + ubicacion.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                    .toUpperCase());
        }

        if (rol.getDeleteFlag() == true)

        {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL ROL " + rol.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                    .toUpperCase());
        }
        Ubicacion antiguaUbicacion = usuario.getUbicacionId();
        usuario.setRolId(rol);
        usuario.setUbicacionId(ubicacion);
        usuario.setTipoDocumento(tipoDocumento);
        BeanUtils.copyProperties(usuarioDTO, usuario);
       

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        notificationController.sendNotification("USERNAME", usuarioActualizado.getId(), usuario);
        Integer rolId = ubicacion.getArea().getRol().getId();
        Integer rolId2 = antiguaUbicacion.getArea().getRol().getId();
        if(rolId == 2 ){
            ubicacion.setEstaOcupada(true);
            ubicacionRepository.save(ubicacion);
            antiguaUbicacion.setEstaOcupada(false);
            ubicacionRepository.save(antiguaUbicacion);
        } else if (usuario.getRolId().getId() == 1 && antiguaUbicacion.getEstaOcupada() == true){
            antiguaUbicacion.setEstaOcupada(false);
            ubicacionRepository.save(antiguaUbicacion);
        } else if (rolId2 == 2){
            antiguaUbicacion.setEstaOcupada(false);
            ubicacionRepository.save(antiguaUbicacion);
        }
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
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String
                    .format(IS_NOT_ALLOWED, "DESACTIVAR ESTE USUARIO PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }

        if(usuario.getId()==1){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DESACTIVAR EL USUARIO CON ID 1").toUpperCase());
        }

        usuario.setDeleteFlag(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void activarUsuario(Integer id) throws UserNotFoundException, ActivateNotAllowedException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String
                    .format(IS_NOT_ALLOWED, "ACTIVAR ESTE USUARIO PORQUE YA SE ENCUENTRA ACTIVADO").toUpperCase());
        }

        usuario.setDeleteFlag(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponse listarUsuarioById(Integer id) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
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

    @Override
    public UsuarioResponse userByEmail(String correo) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
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

    @Override
    public List<UsuarioResponse> listarUsuarioByUbic(Integer ubicacionId) throws LocationNotFoundException {
        List<UsuarioResponse> usuariosResponse = new ArrayList<>();
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId).orElse(null);
        List<Usuario> usuario = usuarioRepository.findByUbicacionId(ubicacion);

        for (Usuario u : usuario) {
            UsuarioResponse usuarioResponse = new UsuarioResponse();
            BeanUtils.copyProperties(u, usuarioResponse);
            usuarioResponse.setDelete_flag(u.getDeleteFlag());
            usuarioResponse.setSede(u.getUbicacionId().getArea().getSede().getNombre());
            usuarioResponse.setUbicacion(u.getUbicacionId().getNombre());
            usuarioResponse.setRol(u.getRolId().getNombre());
            usuarioResponse.setTipoDocumento(u.getTipoDocumento().getAbreviatura());
            usuarioResponse.setArea(u.getUbicacionId().getArea().getNombre());
            usuariosResponse.add(usuarioResponse);
        }
        return usuariosResponse;
    }
}
