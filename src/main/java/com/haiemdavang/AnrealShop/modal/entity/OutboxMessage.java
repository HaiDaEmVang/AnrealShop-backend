package com.haiemdavang.AnrealShop.modal.entity;

import com.haiemdavang.AnrealShop.modal.enums.OutboxMessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "outbox_messages")
public class OutboxMessage {
    @Id
    private UUID id;

    @Column(name = "aggregate_type")
    private String aggregateType;

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OutboxMessageType type;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "processed")
    @Builder.Default
    private Boolean processed = false;
}