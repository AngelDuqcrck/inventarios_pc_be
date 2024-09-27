package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
