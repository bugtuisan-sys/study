# AI 编程小助手 - 前端项目

这是一个基于 Vue3 的 AI 编程小助手前端应用，帮助用户解答编程学习和求职面试相关的问题。

## 功能特性

- 聊天室风格的单页面应用
- 用户消息在右侧，AI 消息在左侧
- 自动生成唯一的会话 ID
- 通过 SSE（Server-Sent Events）实时流式显示对话内容
- 响应式设计，支持自动滚动到底部
- 打字指示器动画效果

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - 类型安全的 JavaScript 超集
- **Vite** - 下一代前端构建工具
- **Fetch API** - 用于 SSE 流式请求

## 项目结构

```
ai-code-helper/
├── src/
│   ├── api/
│   │   └── chat.ts          # API 服务和 SSE 流式处理
│   ├── components/
│   │   └── ChatContainer.vue # 聊天容器组件
│   ├── App.vue               # 主应用组件
│   └── main.ts               # 应用入口
├── index.html
├── package.json
└── vite.config.ts
```

## 安装和运行

### 前置要求

- Node.js >= 20.19.0 或 >= 22.12.0
- npm 或 yarn

### 安装依赖

```bash
npm install
```

### 开发模式运行

```bash
npm run dev
```

应用将在 `http://localhost:5173` 运行（端口可能根据可用性变化）

### 构建生产版本

```bash
npm run build
```

构建产物将输出到 `dist/` 目录

### 预览生产构建

```bash
npm run preview
```

## API 配置

后端接口地址配置在 `src/api/chat.ts` 文件中：

```typescript
const API_BASE_URL = 'http://localhost:8098/api'
```

如果后端服务运行在其他地址，请修改此常量。

## 后端接口

### 流式对话接口

- **URL**: `/api/ai/chat/stream`
- **方法**: GET
- **参数**:
  - `memoryId`: 会话 ID（字符串）
  - `msg`: 用户消息（字符串）
- **响应**: Server-Sent Events (SSE) 流

### SpringBoot 后端示例

```java
@RestController
@RequestMapping("/ai")
public class AiHelperController {
    @Resource
    private AIHelperService aiHelperService;

    @RequestMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithStream(String memoryId, String msg) {
        return aiHelperService.chatStream(memoryId, msg);
    }
}
```

## 使用说明

1. 启动后端服务（确保运行在 `http://localhost:8098`）
2. 启动前端开发服务器：`npm run dev`
3. 在浏览器中打开显示的 URL
4. 页面加载时会自动生成一个唯一的会话 ID
5. 在底部输入框中输入问题，按 Enter 发送
6. AI 回复会以流式方式实时显示

## 核心功能说明

### 会话管理

每个页面加载时会生成唯一的会话 ID，格式为：`chat_{timestamp}_{random}`

这个 ID 用于区分不同的会话，后端可以使用它来维护对话上下文。

### SSE 流式处理

使用 Fetch API 和 ReadableStream 来处理 SSE 流式响应：

1. 建立连接到后端 SSE 接口
2. 逐块读取响应数据
3. 解析 SSE 格式（`data:` 前缀）
4. 实时更新 UI 显示

### 消息显示

- 用户消息：右对齐，紫色背景
- AI 消息：左对齐，白色背景
- 时间戳：显示每条消息的发送时间
- 打字指示器：AI 思考时显示动画

## 注意事项

1. **CORS 配置**: 确保后端服务配置了正确的 CORS 头，允许前端域名访问
2. **后端服务**: 使用前需要启动后端 SpringBoot 服务
3. **浏览器兼容性**: 需要使用支持 Fetch API 和 ReadableStream 的现代浏览器

## 自定义样式

主要样式定义在 `ChatContainer.vue` 的 `<style scoped>` 部分，可以根据需要修改：

- 颜色主题：修改渐变色 `#667eea` 和 `#764ba2`
- 布局尺寸：调整 padding、margin 等
- 动画效果：修改 keyframes 动画

## 故障排除

### 无法连接到后端

- 检查后端服务是否运行在 `http://localhost:8098`
- 检查浏览器控制台的错误信息
- 确认 CORS 配置正确

### 构建失败

- 确保 Node.js 版本符合要求
- 删除 `node_modules` 并重新运行 `npm install`
- 检查 TypeScript 错误：`npm run type-check`

### 消息不显示

- 检查浏览器控制台的网络请求
- 确认后端返回的 SSE 格式正确
- 检查 `memoryId` 是否正确传递
