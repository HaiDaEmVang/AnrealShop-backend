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
        @Index(name = "idx_history_login_loginAt", columnList = "loginAt")
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
    @Column(nullable = false)
    private LocalDateTime loginAt;

    @Column(nullable = false, length = 45, name = "ip_address")
    private String ipAddress;

    @Column(length = 255, name = "user_agent")
    private String userAgent;

    @Column(length = 100)
    private String location;
}
