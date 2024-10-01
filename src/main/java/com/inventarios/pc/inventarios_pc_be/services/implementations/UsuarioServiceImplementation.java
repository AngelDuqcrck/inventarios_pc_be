package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.DocumentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;

@Service
public class UsuarioServiceImplementation implements IUsuarioService {

    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    public static final String IS_NOT_VALID = "The %s is not valid";
    public static final String ARE_NOT_EQUALS = "The %s are not equals";
    public static final String IS_NOT_CORRECT = "The %s is not correct";

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
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) throws LocationNotFoundException, RolNotFoundException, DocumentNotFoundException {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new IllegalArgumentException("El correo " + usuarioDTO.getCorreo() + " ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);

        Rol rol = rolRepository.findById(usuarioDTO.getRolId()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacionId()).orElse(null);

        if (rol == null)
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());

        if (tipoDocumento == null)
            throw new DocumentNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        if (ubicacion == null)
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
        usuario.setRolId(rol);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setUbicacionId(ubicacion);

        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        Usuario usuarioCreado = usuarioRepository.save(usuario);

        UsuarioDTO usuarioCreadoDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioCreado, usuarioCreadoDTO);

        return usuarioCreadoDTO;
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
    @Override
    public void enviarTokenRecuperacion(String correo) throws EmailNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuario == null) {
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EMAIL").toUpperCase());
        }

        String tokenRecuperacion = jwtGenerador.generarTokenRecuperacion(correo);
        String urlRecuperacion = "https://inventarios-pc/restablecer-password?token=" + tokenRecuperacion; // Definir
                                                                                                           // la
                                                                                                           // password

        emailService.sendEmail(correo, "Solicitud de Cambio de Contraseña",
                "Presione el siguiente botón que lo redigirá a la página web para restablecer su contraseña",
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
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EMAIL").toUpperCase());
        }

        if (!nuevaPassword.equals(nuevaPassword2)) {
            throw new PasswordNotEqualsException(String.format(ARE_NOT_EQUALS, "NEW PASSWORDS").toUpperCase());
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
            throw new EmailNotFoundException(String.format(IS_NOT_FOUND, "EMAIL").toUpperCase());
        }

        if (!passwordEncoder.matches(cambiarPasswordRequest.getActualPassword(), usuario.getPassword())) {
            throw new PasswordNotEqualsException(String.format(IS_NOT_CORRECT, "CURRENT PASSWORD").toUpperCase());
        }

        if (!cambiarPasswordRequest.getNuevaPassword().equals(cambiarPasswordRequest.getNuevaPassword2())) {
            throw new PasswordNotEqualsException(String.format(ARE_NOT_EQUALS, "NEW PASSWORDS").toUpperCase());

        }

        usuario.setPassword(passwordEncoder.encode(cambiarPasswordRequest.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }
}
