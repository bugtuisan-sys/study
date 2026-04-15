package com.example.platform.aicodehelper.memory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.platform.aicodehelper.entity.ChatMemoryEntity;
import com.example.platform.aicodehelper.service.ChatMemoryService;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于数据库的聊天记忆存储实现
 * 实现ChatMemoryStore接口，让LangChain4j自动管理消息
 */
@Slf4j
public class DatabaseChatMemoryStore implements ChatMemoryStore {

    private final ChatMemoryService chatMemoryService;
    private final int maxMessages;

    public DatabaseChatMemoryStore(ChatMemoryService chatMemoryService, int maxMessages) {
        this.chatMemoryService = chatMemoryService;
        this.maxMessages = maxMessages;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String id = memoryId.toString();
        List<ChatMemoryEntity> entities = chatMemoryService.getMessagesByMemoryId(id);

        List<ChatMessage> messages = entities.stream()
                .map(this::convertToChatMessage)
                .filter(msg -> msg != null)
                .collect(Collectors.toList());

        log.debug("从数据库加载消息: memoryId={}, count={}", id, messages.size());
        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String id = memoryId.toString();

        if (messages == null || messages.isEmpty()) {
            return;
        }

        // 获取数据库中现有的消息
        List<ChatMemoryEntity> existingEntities = chatMemoryService.getMessagesByMemoryId(id);
        
        // 将现有消息转换为字符串集合，用于快速比较
        Set<String> existingContents = existingEntities.stream()
                .map(ChatMemoryEntity::getMessageContent)
                .collect(Collectors.toSet());

        // 找出新增的消息
        int newMessagesCount = 0;
        for (int i = 0; i < messages.size(); i++) {
            ChatMessage message = messages.get(i);
            String content = getMessageContent(message);
            
            // 如果这条消息的内容不在数据库中，说明是新消息
            if (!existingContents.contains(content)) {
                ChatMemoryEntity entity = convertToEntity(message, id, i + 1);
                chatMemoryService.saveMessage(entity);
                newMessagesCount++;
            }
        }

        // 如果超过最大消息数，清理旧消息
        if (messages.size() > maxMessages) {
            chatMemoryService.cleanOldMessages(id, maxMessages);
        }

        if (newMessagesCount > 0) {
            log.info("保存新消息: memoryId={}, 新增={}, 总数={}", 
                    id, newMessagesCount, messages.size());
        } else {
            log.debug("无新消息: memoryId={}, 总数={}", id, messages.size());
        }
    }

    /**
     * 获取消息内容
     */
    private String getMessageContent(ChatMessage message) {
        if (message instanceof UserMessage) {
            return ((UserMessage) message).singleText();
        } else if (message instanceof AiMessage) {
            return ((AiMessage) message).text();
        } else if (message instanceof SystemMessage) {
            return ((SystemMessage) message).text();
        }
        return "";
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String id = memoryId.toString();
        chatMemoryService.clearMessagesByMemoryId(id);
        log.info("删除消息: memoryId={}", id);
    }

    /**
     * 将数据库实体转换为ChatMessage
     */
    private ChatMessage convertToChatMessage(ChatMemoryEntity entity) {
        try {
            String messageType = entity.getMessageType();
            String content = entity.getMessageContent();

            return switch (messageType) {
                case "user" -> UserMessage.from(content);
                case "ai" -> AiMessage.from(content);
                case "system" -> SystemMessage.from(content);
                default -> null;
            };
        } catch (Exception e) {
            log.error("转换消息失败: id={}", entity.getId(), e);
            return null;
        }
    }

    /**
     * 将ChatMessage转换为数据库实体
     */
    private ChatMemoryEntity convertToEntity(ChatMessage message, String memoryId, int index) {
        ChatMemoryEntity entity = new ChatMemoryEntity();
        entity.setMemoryId(memoryId);
        entity.setMessageIndex(index);

        if (message instanceof UserMessage) {
            entity.setMessageType("user");
            entity.setMessageContent(((UserMessage) message).singleText());
        } else if (message instanceof AiMessage) {
            entity.setMessageType("ai");
            entity.setMessageContent(((AiMessage) message).text());
        } else if (message instanceof SystemMessage) {
            entity.setMessageType("system");
            entity.setMessageContent(((SystemMessage) message).text());
        }

        return entity;
    }
}
