package com.haiemdavang.AnrealShop.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.modal.entity.OutboxMessage;
import com.haiemdavang.AnrealShop.modal.enums.OutboxMessageType;
import com.haiemdavang.AnrealShop.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxMessageService {
    private final ObjectMapper objectMapper;
    private final OutboxMessageRepository outboxMessageRepository;

    @Transactional
    public void saveMessage(Object dataRaw, OutboxMessageType type, String id, String objectName){
        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(dataRaw);
        }catch (Exception e){
            throw new AnrealShopException("INVALID_JSON_FORMAT");
        }

        OutboxMessage message = OutboxMessage.builder()
                .type(type)
                .aggregateId(id)
                .aggregateType(objectName)
                .payload(payload)
                .build();

        outboxMessageRepository.save(message);
    }
}
