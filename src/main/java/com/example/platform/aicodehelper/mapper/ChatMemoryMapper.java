package com.example.platform.aicodehelper.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.platform.aicodehelper.entity.ChatMemoryEntity;

/**
 * 聊天记忆Mapper接口
 */
@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatMemoryEntity> {

    /**
     * 根据memoryId查询消息列表，按messageIndex排序
     */
    List<ChatMemoryEntity> selectByMemoryIdOrderByIndex(@Param("memoryId") String memoryId);

    /**
     * 删除指定memoryId的旧消息，保留最新的maxMessages条
     */
    void deleteOldMessages(@Param("memoryId") String memoryId, @Param("maxMessages") int maxMessages);
}
