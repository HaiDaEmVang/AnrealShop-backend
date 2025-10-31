package com.haiemdavang.AnrealShop.modal.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user"})
@EqualsAndHashCode(of = "id")
@Table(name = "history_login", indexes = {
        @Index(name = "idx_history_login_username", columnList = "user_id"),
        @Index(name = "idx_history_login_loginAt", columnList = "loginAt"),
})
public class HistoryLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(nullable = false, name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(nullable = false, length = 45, name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(length = 100)
    private String location;

    @Column(length = 100, unique = true)
    private String device;


}
