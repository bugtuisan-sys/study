package com.example.platform.aicodehelper.factory;

import com.example.platform.aicodehelper.rag.RagConfig;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.platform.aicodehelper.memory.DatabaseChatMemoryStore;
import com.example.platform.aicodehelper.service.AIHelperService;
import com.example.platform.aicodehelper.service.ChatMemoryService;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;

@Configuration
public class AiServiceFactory {
    @Resource
    private ChatModel qwenChatModel;
    @Resource
    private ChatMemoryService chatMemoryService;
    @Resource
    private ContentRetriever contentRetriever;
    @Resource
    private StreamingChatModel qwenStreamingChatModel;

    //通过反射和动态代理生成AIHelperService类对象
    @Bean
    public AIHelperService aiHelperService(){
        // 创建数据库存储
        DatabaseChatMemoryStore store = new DatabaseChatMemoryStore(chatMemoryService, 10);

        // 使用MessageWindowChatMemory配合自定义Store
        AIHelperService aiHelperServiceBean = AiServices.builder(AIHelperService.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel) //流式输出
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(10)
                        .chatMemoryStore(store)
                        .build()) //会话记忆存储
                .contentRetriever(contentRetriever) //内容检索
                .build();
        return aiHelperServiceBean;
    }
}
