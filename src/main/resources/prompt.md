你是位专业的前端开发，请帮我根据下列信息来生成对应的前端项目代码。

## 需求

应用为《AI 编程小助手》，帮助用户解答编程学习和求职面试相关的问题，并给出建议。

- 只有一个页面，就是主页；
- 页面风格为聊天室（用户信息在右边，AI 信息在左边）；
- 下方是输入框，进入页面后自动生成一个聊天室 id，用于区分不同的会话；
- 通过 SSE 的方式调用 `chat` 接口，实时显示对话内容。

## 技术选型

1. Vue3 项目
2. Axios 请求库

## 后端接口信息

- 接口地址前缀：`http://localhost:8098/api`

## SpringBoot 后端接口代码

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
}