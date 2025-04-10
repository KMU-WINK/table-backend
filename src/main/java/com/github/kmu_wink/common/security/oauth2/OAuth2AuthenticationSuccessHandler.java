package com.github.kmu_wink.common.security.oauth2;

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.github.kmu_wink.common.property.OauthCallbackProperty;
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
    private final OauthCallbackProperty oauthCallbackProperty;

    private final String KOOKMIN_EMAIL_REGEX = ".*@kookmin\\.ac\\.kr$";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        OAuth2GoogleUser oAuth2GoogleUser = (OAuth2GoogleUser) authentication.getPrincipal();
        String socialId = oAuth2GoogleUser.getSocialId();

        boolean isNewUser = !userRepository.existsBySocialId(socialId);

        User user = userRepository.findBySocialId(socialId)
            .orElseGet(() -> {

                String email = oAuth2GoogleUser.getEmail();

                if (!email.matches(KOOKMIN_EMAIL_REGEX)) return null;

                return userRepository.save(
                    User.builder()
                        .socialId(socialId)
                        .name(null)
                        .email(email)
                        .club(null)
                        .build()
                );
            });

        if (Objects.isNull(user)) {
            getRedirectStrategy().sendRedirect(request, response, String.format("%s?code=NOT_KOOKMIN_EMAIL", oauthCallbackProperty.getFailure()));
            return;
        }

        String accessToken = tokenProvider.generateToken(user);
        getRedirectStrategy().sendRedirect(request, response, String.format("%s?token=%s&isNewUser=%s", oauthCallbackProperty.getSuccess(), accessToken, isNewUser));
    }
}