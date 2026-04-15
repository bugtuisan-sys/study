-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_helper DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_helper;

-- 聊天记忆表
CREATE TABLE IF NOT EXISTS chat_memory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    memory_id VARCHAR(128) NOT NULL COMMENT '记忆ID（会话ID）',
    message_type VARCHAR(20) NOT NULL COMMENT '消息类型：user-用户消息，ai-AI回复，system-系统消息',
    message_content TEXT NOT NULL COMMENT '消息内容',
    message_index INT NOT NULL COMMENT '消息顺序索引',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_memory_id (memory_id),
    INDEX idx_message_index (message_index),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天记忆表';
