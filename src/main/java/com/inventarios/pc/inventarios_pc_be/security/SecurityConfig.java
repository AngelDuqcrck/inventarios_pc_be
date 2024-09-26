package com.inventarios.pc.inventarios_pc_be.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Esto indica que es una clase de seguridad al momento de arrancar la aplicacion
@EnableWebSecurity //Se activa la seguridad web de nuestra aplicacion y ademas sera una clase que contendra toda la configuracion refente a la seguridad
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final String[] PUBLIC_URL  = {"**"};

    //Este bean va a encargarse de verificar la información de los usuarios que se logearan en el sistema
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    //Este bean incorpora el filtro de seguridad de JWT del jwt authentication filter
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }

    //Este bean va a establecer una cadena de filtros de seguridad del sistema, aqui determinamos los permisos segun los roles de los usuarios para acceder a la aplicación
    @Bean
    SecurityFilterChain filterChain (HttpSecurity http) throws  Exception{
        http
            .csrf().disable()
            .exceptionHandling()//Permitimos el manejo de excepciones
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()//permite la gestion de sesiones
            .and()
            .authorizeHttpRequests()//Toda peticion debe ser autorizada
            .requestMatchers(PUBLIC_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
        
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
