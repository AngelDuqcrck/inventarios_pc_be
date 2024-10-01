package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;

@Service
public class UsuarioServiceImplementation implements IUsuarioService {

    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    public static final String IS_NOT_VALID = "The %s is not valid";
    public static final String ARE_NOT_EQUALS = "The %s are not equals";

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
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new IllegalArgumentException("El correo " + usuarioDTO.getCorreo() + " ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);

        // Lineas por mejorar
        Rol rol = rolRepository.findById(usuarioDTO.getRolId()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);
        Ubicacion ubicacion = ubicacionRepository.findById(usuarioDTO.getUbicacionId()).orElse(null);

        if (rol == null)
            throw new IllegalArgumentException("Rol no encontrado");

        if (tipoDocumento == null)
            throw new IllegalArgumentException("Tipo de documento no encontrado");

        if (ubicacion == null)
            throw new IllegalArgumentException("Ubicacion no encontrada");
        // ----------------------------------------------------------------
        usuario.setRolId(rol);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setUbicacionId(ubicacion);

        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        Usuario usuarioCreado = usuarioRepository.save(usuario);

        UsuarioDTO usuarioCreadoDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioCreado, usuarioCreadoDTO);

        return usuarioCreadoDTO;
    }

    // Método para generar un token de recuperación y enviar el correo
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

    // Método para actualizar la password usando el token
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
            throw new PasswordNotEqualsException("Las contraseñas no coinciden");
        }
    
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }
}
