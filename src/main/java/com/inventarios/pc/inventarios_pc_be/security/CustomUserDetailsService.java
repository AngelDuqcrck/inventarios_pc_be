package com.inventarios.pc.inventarios_pc_be.security;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para traer todos los datos del usuario a traves de su correo
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username).orElseThrow(
                () -> new UsernameNotFoundException("El usuario con el correo " + username + " no fue encontrado"));


        // Nos traemos la lista de autoridades a traves de la lista de roles
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRolId().getNombre()));

        // Creamos un CustomUserDetails que extiende User para incluir información adicional
        return new CustomUserDetails(
            usuario.getCorreo(),
            usuario.getPassword(),
            true,
            true,
            true,
            true,
            authorities,
            usuario.getPrimerNombre(),
            usuario.getPrimerApellido()
        );
    }

    // Nueva clase CustomUserDetails
    public class CustomUserDetails extends User {
        private final String primerNombre;
        private final String primerApellido;

        public CustomUserDetails(String username, String password, boolean enabled,
                            boolean accountNonExpired, boolean credentialsNonExpired,
                            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                            String primerNombre, String primerApellido) {
            super(username, password, enabled, accountNonExpired, 
                credentialsNonExpired, accountNonLocked, authorities);
            this.primerNombre = primerNombre;
            this.primerApellido = primerApellido;
        }

        public String getPrimerNombre() {
            return primerNombre;
        }

        public String getPrimerApellido() {
            return primerApellido;
        }
    }

}
