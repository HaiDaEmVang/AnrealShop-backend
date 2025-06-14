package com.haiemdavang.AnrealShop.security.userDetails;

import com.haiemdavang.AnrealShop.modal.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailSecu implements UserDetails {
    private String id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> grantedAuthorities;


    public static UserDetails createUserDetails(User u){
        return UserDetailSecu.builder().email(u.getEmail()).password(u.getPassword())
                .id(u.getId()).grantedAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + u.getRole().getName()))).build();
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

}
