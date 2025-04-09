package com.github.kmu_wink.common.security.jwt;

import java.util.Date;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.kmu_wink.common.property.JwtProperty;
import com.github.kmu_wink.common.security.CustomUserDetailsService;
import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;
import com.github.kmu_wink.domain.user.schema.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperty jwtProperty;
    private final CustomUserDetailsService customUserDetailsService;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {

        algorithm = Algorithm.HMAC256(jwtProperty.getKey());
    }

    public String generateToken(User user) {

        JWTCreator.Builder builder = JWT.create()
            .withIssuedAt(new Date())
            .withClaim("id", user.getId())
            .withClaim("email", user.getEmail());

        if (Objects.nonNull(user.getClub())) {
            builder = builder.withClaim("club", user.getClub().toString());
        }

        return builder.sign(algorithm);
    }

    public String getEmailFromToken(String token) {

        return JWT.require(algorithm)
            .build()
            .verify(token)
            .getClaim("email")
            .asString();
    }

    public boolean validateToken(String token) {

        if (token == null) {

            return false;
        }

        JWT.require(algorithm).build().verify(token);

        return true;
    }

    public Authentication getAuthentication(String accessToken) {

        String email = getEmailFromToken(accessToken);
        OAuth2GoogleUser userDetails = (OAuth2GoogleUser) customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            accessToken,
            userDetails.getAuthorities()
        );
    }
}
