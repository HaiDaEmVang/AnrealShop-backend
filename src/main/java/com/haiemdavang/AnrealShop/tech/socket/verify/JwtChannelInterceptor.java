package com.haiemdavang.AnrealShop.tech.socket.verify;

import com.haiemdavang.AnrealShop.security.userDetails.UserDetailSecu;
import com.haiemdavang.AnrealShop.security.userDetails.UserDetailSecuService;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final UserDetailSecuService userDetailSecuService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()) && accessor.getSessionAttributes() != null) {
            String email = (String) accessor.getSessionAttributes().get("user");
            if (email != null) {
                UserDetailSecu userDetails = (UserDetailSecu) userDetailSecuService.loadUserByUsername(email);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                accessor.setUser(auth);
            }
        }
        return message;
    }
}
