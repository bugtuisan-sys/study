package com.example.platform.aicodehelper.ai;

import org.springframework.stereotype.Component;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AiCodeHelper {
    @Resource
    private ChatModel qwenChatModel;
    private final String SystemPrompt = "你是一个有帮助的人工智能助手。";


    public String chat(String msg) {
        SystemMessage systemMessage = SystemMessage.from(SystemPrompt);
        UserMessage userMessage = UserMessage.from(msg);
        ChatResponse response = qwenChatModel.chat(systemMessage,userMessage);
        AiMessage aiMessage = response.aiMessage();
        log.info("aiMessage: {}", aiMessage);
        return aiMessage.text();
    }




    public String chat(UserMessage userMessage) {
        ChatResponse response = qwenChatModel.chat(userMessage);
        AiMessage aiMessage = response.aiMessage();
        log.info("aiMessage: {}", aiMessage);
        return aiMessage.text();
    }
}
