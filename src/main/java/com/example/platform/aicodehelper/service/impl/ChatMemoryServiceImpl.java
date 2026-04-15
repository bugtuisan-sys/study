package com.example.platform.aicodehelper.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.platform.aicodehelper.entity.ChatMemoryEntity;
import com.example.platform.aicodehelper.mapper.ChatMemoryMapper;
import com.example.platform.aicodehelper.service.ChatMemoryService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天记忆服务实现类
 */
@Slf4j
@Service
public class ChatMemoryServiceImpl implements ChatMemoryService {

    @Resource
    private ChatMemoryMapper chatMemoryMapper;

    @Override
    public void saveMessage(ChatMemoryEntity chatMemory) {
        chatMemory.setCreateTime(LocalDateTime.now());
        chatMemory.setUpdateTime(LocalDateTime.now());
        chatMemory.setDeleted(0);
        chatMemoryMapper.insert(chatMemory);
        log.debug("保存聊天消息: memoryId={}, messageType={}, messageIndex={}",
                chatMemory.getMemoryId(), chatMemory.getMessageType(), chatMemory.getMessageIndex());
    }

    @Override
    public List<ChatMemoryEntity> getMessagesByMemoryId(String memoryId) {
        return chatMemoryMapper.selectByMemoryIdOrderByIndex(memoryId);
    }

    @Override
    @Transactional
    public void cleanOldMessages(String memoryId, int maxMessages) {
        if (maxMessages <= 0) {
            return;
        }

        // 获取当前消息数量
        int currentCount = getMessageCount(memoryId);

        // 如果超过限制，删除旧消息
        if (currentCount > maxMessages) {
            chatMemoryMapper.deleteOldMessages(memoryId, maxMessages);
            log.info("清理旧消息: memoryId={}, 保留最新{}条", memoryId, maxMessages);
        }
    }

    @Override
    public int getMessageCount(String memoryId) {
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMemoryEntity::getMemoryId, memoryId)
                .eq(ChatMemoryEntity::getDeleted, 0);
        return Math.toIntExact(chatMemoryMapper.selectCount(wrapper));
    }

    @Override
    @Transactional
    public void clearMessagesByMemoryId(String memoryId) {
        LambdaQueryWrapper<ChatMemoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMemoryEntity::getMemoryId, memoryId);
        chatMemoryMapper.delete(wrapper);
        log.info("清空消息: memoryId={}", memoryId);
    }
}
