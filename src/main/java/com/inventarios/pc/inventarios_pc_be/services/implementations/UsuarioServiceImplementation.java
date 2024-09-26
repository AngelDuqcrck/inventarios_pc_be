package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUsuarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;

@Service
public class UsuarioServiceImplementation implements IUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    UbicacionRepository ubicacionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema, asignándole su rol, tipo de
     * documento y ubicación.
     * Valida que existan el rol, tipo de documento y ubicación especificados, y
     * encripta la contraseña
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
        if(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())){
            throw new IllegalArgumentException("El correo "+ usuarioDTO.getCorreo()+" ya se encuentra registrado");
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
         //----------------------------------------------------------------
        usuario.setRolId(rol);
        usuario.setTipoDocumento(tipoDocumento);
        usuario.setUbicacionId(ubicacion);

        usuario.setContraseña(passwordEncoder.encode(usuarioDTO.getContraseña()));

        Usuario usuarioCreado = usuarioRepository.save(usuario);

        UsuarioDTO usuarioCreadoDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioCreado, usuarioCreadoDTO);

        return usuarioCreadoDTO;
    }

}
