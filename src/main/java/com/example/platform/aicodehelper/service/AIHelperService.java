package com.example.platform.aicodehelper.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;


public interface AIHelperService {
    @SystemMessage(fromResource = "code-helper-system-prompt.txt")
    Result<String> chat(@MemoryId String memoryId, @UserMessage String userMassage);

    @SystemMessage(fromResource = "code-helper-system-prompt.txt")
    Result<String> chatWithRag(@MemoryId String memoryId, @UserMessage String userMassage);

    @SystemMessage(fromResource = "code-helper-system-prompt.txt")
    Flux<String> chatStream(@MemoryId String memoryId, @UserMessage String userMassage);
}
