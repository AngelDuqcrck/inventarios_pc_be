package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.DocumentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;

public interface IUsuarioService {
    
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) throws LocationNotFoundException, RolNotFoundException, DocumentNotFoundException;
    public void restablecerpassword(String token, String nuevaPassword, String nuevaPassword2)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException;
    
    public void cambiarContraseña(CambiarPasswordRequest cambiarPasswordRequest)
            throws TokenNotValidException, EmailNotFoundException, PasswordNotEqualsException;

    public void enviarTokenRecuperacion(String correo) throws EmailNotFoundException;

    public List<UsuariosResponse> listarUsuarios();

}
