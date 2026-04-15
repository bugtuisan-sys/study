package com.example.platform.aicodehelper.controller;

import com.example.platform.aicodehelper.ai.AiCodeHelper;
import com.example.platform.aicodehelper.service.AIHelperService;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContext;
import reactor.core.publisher.Flux;
import tools.jackson.databind.json.JsonMapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/ai")
public class AiHelperController {
    @Resource
    private AiCodeHelper aiCodeHelper;
    @Resource
    private AIHelperService aiHelperService;

    @RequestMapping(value = "/chat/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithStream(String memoryId, String msg) {
        Flux<String> stringFlux = aiHelperService.chatStream(memoryId, msg);
        return stringFlux;
    }
    @RequestMapping("/chat")
    public Result<String> chat(String memoryId, String msg) {
        Result<String> chat = aiHelperService.chat(memoryId, msg);
        log.info("chat: {}", chat.tokenUsage());
        return chat;
    }

}
