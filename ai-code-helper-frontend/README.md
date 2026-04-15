# AI 编程小助手 - 前端项目

## 快速开始

### 1. 进入项目目录

```bash
cd ai-code-helper
```

### 2. 安装依赖（已完成）

依赖已经安装完毕，无需再次执行。

### 3. 启动开发服务器

```bash
npm run dev
```

### 4. 访问应用

在浏览器中打开终端显示的 URL（通常是 `http://localhost:5173`）

## 重要说明

**使用前需要启动后端服务！**

确保 SpringBoot 后端服务运行在 `http://localhost:8098`

后端接口：
- `GET /api/ai/chat/stream?memoryId={id}&msg={message}`
- 返回 SSE (Server-Sent Events) 流式响应

## 项目特点

- Vue 3 + TypeScript
- 单页面聊天应用
- 自动生成会话 ID
- SSE 实时流式对话
- 美观的渐变紫色主题
- 响应式设计

详细文档请查看：[PROJECT_GUIDE.md](ai-code-helper/PROJECT_GUIDE.md)
