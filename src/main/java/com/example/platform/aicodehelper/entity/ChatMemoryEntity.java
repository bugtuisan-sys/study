package com.example.platform.aicodehelper.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 聊天记忆实体类
 */
@Data
@TableName("chat_memory")
public class ChatMemoryEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 记忆ID（会话ID）
     */
    private String memoryId;

    /**
     * 消息类型：user-用户消息，ai-AI回复，system-系统消息
     */
    private String messageType;

    /**
     * 消息内容（JSON格式存储完整消息信息）
     */
    private String messageContent;

    /**
     * 消息顺序索引
     */
    private Integer messageIndex;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    private Integer deleted;
}
