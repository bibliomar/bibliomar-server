package bibliomar.bibliomarserver.model.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
public class JwtTokenResponse {
    @JsonProperty("token_type")
    private final String tokenType = "Bearer";
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_at")
    private Instant expiresAt;

    public static JwtTokenResponse build(DecodedJWT decodedJWT){
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setAccessToken(decodedJWT.getToken());
        jwtTokenResponse.setExpiresAt(decodedJWT.getExpiresAtAsInstant());

        return jwtTokenResponse;
    }

}
