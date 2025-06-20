package com.haiemdavang.AnrealShop.security.userDetails;

import com.haiemdavang.AnrealShop.modal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailSecu implements UserDetails, OAuth2User {
    private String id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> grantedAuthorities;
    private Map<String, Object> attributes;

    public static UserDetailSecu createUserDetails(User u){
        return UserDetailSecu.builder().email(u.getEmail()).password(u.getPassword())
                .id(u.getId()).grantedAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + u.getRole().getName()))).build();
    }

    public static UserDetailSecu createUserDetailFormOAuth2(User user) {
        return UserDetailSecu.builder().email(user.getEmail()).password(user.getPassword())
                .id(user.getId()).grantedAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))).build();
    }

    public static UserDetailSecu createUserDetailFormOAuth2(User user, Map<String, Object> attributes) {
        UserDetailSecu userPrincipal = createUserDetailFormOAuth2(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
