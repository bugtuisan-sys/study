package com.example.platform.aicodehelper.service;

import java.util.List;

import com.example.platform.aicodehelper.entity.ChatMemoryEntity;

/**
 * 聊天记忆服务接口
 */
public interface ChatMemoryService {

    /**
     * 保存消息
     */
    void saveMessage(ChatMemoryEntity chatMemory);

    /**
     * 根据memoryId查询消息列表
     */
    List<ChatMemoryEntity> getMessagesByMemoryId(String memoryId);

    /**
     * 清理旧消息，保留最新的maxMessages条
     */
    void cleanOldMessages(String memoryId, int maxMessages);

    /**
     * 获取指定memoryId的消息数量
     */
    int getMessageCount(String memoryId);

    /**
     * 清空指定memoryId的所有消息
     */
    void clearMessagesByMemoryId(String memoryId);
}
