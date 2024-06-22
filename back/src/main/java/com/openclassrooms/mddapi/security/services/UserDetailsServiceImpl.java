package com.openclassrooms.mddapi.security.services;


import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.openclassrooms.mddapi.model.User;

/**
 * Implémentation du service UserDetailsService pour la sécurité de l'application.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    /**
     * Charge les détails de l'utilisateur par son email ou son nom d'utilisateur.
     * @param emailOrUsername l'email ou le nom d'utilisateur de l'utilisateur.
     * @return les détails de l'utilisateur.
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userService.getUserByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with this email or username: " + emailOrUsername));

        return UserDetailsImpl
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
