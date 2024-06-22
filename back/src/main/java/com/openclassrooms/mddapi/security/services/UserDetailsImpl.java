package com.openclassrooms.mddapi.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Implémentation de UserDetails pour la sécurité de l'application.
 */
@Builder
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    // Mot de passe de l'utilisateur, ignoré lors de la sérialisation JSON
    @JsonIgnore
    private String password;

    /**
     * Retourne les autorités accordées à l'utilisateur. Dans ce cas, une collection vide est retournée.
     * @return une collection d'autorités accordées à l'utilisateur.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }

    /**
     * Indique si le compte de l'utilisateur n'a pas expiré.
     * @return toujours vrai dans cette implémentation.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si l'utilisateur n'est pas verrouillé.
     * @return toujours vrai dans cette implémentation.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indique si les informations d'identification de l'utilisateur n'ont pas expiré.
     * @return toujours vrai dans cette implémentation.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si l'utilisateur est activé.
     * @return toujours vrai dans cette implémentation.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Compare cet objet UserDetailsImpl à un autre objet.
     * @param o l'objet à comparer.
     * @return vrai si les objets sont égaux, faux sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
