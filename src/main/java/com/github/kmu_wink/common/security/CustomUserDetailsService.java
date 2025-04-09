package com.github.kmu_wink.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;
import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return new OAuth2GoogleUser(user);
    }
}
