package com.github.kmu_wink.common.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.github.kmu_wink.common.property.OauthProperty;
import com.github.kmu_wink.common.security.jwt.TokenProvider;
import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;
    private final OauthProperty oauthProperty;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        OAuth2GoogleUser oAuth2GoogleUser = (OAuth2GoogleUser) authentication.getPrincipal();
        String socialId = oAuth2GoogleUser.getSocialId();

        boolean isNewUser = !userRepository.existsBySocialId(socialId);

        User user = userRepository.findBySocialId(socialId)
            .orElseGet(() -> userRepository.save(
                    User.builder()
                        .socialId(socialId)
                        .name(null)
                        .email(oAuth2GoogleUser.getEmail())
                        .club(null)
                        .build()
                )
            );

        String accessToken = tokenProvider.generateToken(user);
        getRedirectStrategy().sendRedirect(request, response, String.format("%s?token=%s&isNewUser=%s", oauthProperty.getCallback(), accessToken, isNewUser));
    }
}