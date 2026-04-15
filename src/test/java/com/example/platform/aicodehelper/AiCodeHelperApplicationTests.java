package com.example.platform.aicodehelper;

import com.alibaba.dashscope.tokenizers.QwenTokenizer;
import com.example.platform.aicodehelper.service.AIHelperService;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.platform.aicodehelper.ai.AiCodeHelper;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;

@Slf4j
@SpringBootTest
class AiCodeHelperApplicationTests {
    @Resource
    private AiCodeHelper aiCodeHelper;
    @Resource
    private AIHelperService aiHelperService;

    //@Test
    void testchat() {
        aiCodeHelper.chat("你好我是程序员王大锤");
    }

    //@Test
    void chat() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述一个图片"),
                ImageContent.from("https://www.dogetu.com/data/sogou/eb5c5388d87f975d3ee796aac1cf4058.jpg"));
        String chat = aiCodeHelper.chat(userMessage);
    }
    //@Test
    void testchat2() {
        Result<String> chat = aiHelperService.chatWithRag("2", "你好我是程序员王大锤我想学习ai应用开发,有哪些学习资源");
        log.info("源文档-{}", chat.sources());
        log.info("返回内容-{}", chat.content());

    }
    @Test
    void testchat3() {

        QwenTokenizer qwenTokenizer = new QwenTokenizer();
        UserMessage userMessage = new UserMessage("描述一个图片");


    }
}
