package com.unicap.idear.idear.models;

import com.unicap.idear.idear.models.enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageModel {
    private MessageType type;
    private String content;
    private String sender;
}
