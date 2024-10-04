package com.inventarios.pc.inventarios_pc_be.shared.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarPasswordRequest {
    public String token;
    
    public String actualPassword;

    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
     @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
             message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero y un caracter especial.")
    public String nuevaPassword;

    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
             message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero y un caracter especial.")
    public String nuevaPassword2;
    
}
