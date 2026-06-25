# 智膳坊-AI 个性化药膳推荐与健康管理平台

智膳坊是一个药食同源方向的 Web 端展示与服务平台，提供药膳精选、汤方专区、茶方秘典 / 中药奶茶、养生资讯、收藏管理，以及基于健康档案的 Mock AI 个性化药膳推荐。

项目已完成前后端基础联调：前端优先请求 Spring Boot 接口，后端不可用时保留前端 Mock 兜底，避免页面空白。

## 技术栈

前端：

- Vue 3
- Vite
- TypeScript
- Vue Router
- Pinia
- Axios
- Element Plus
- 普通 CSS

后端：

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- MySQL
- Lombok

## 目录结构

```text
.
├── assets/                 # 参考 UI 与前端裁切展示图
├── backend/                # Spring Boot 后端
├── docs/                   # 项目资料文档
├── frontend/               # Vue 3 前端
└── README.md
```

## 前端启动

```powershell
cd frontend
npm install
npm run dev
```

默认地址：`http://localhost:5173`

如果 5173 被占用：

```powershell
npm run dev -- --host 127.0.0.1 --port 5174
```

前端接口代理配置在 `frontend/vite.config.ts`，默认将 `/api` 转发到 `http://localhost:18080`。

## 后端启动

后端默认启用 MySQL profile，端口为 `18080`。

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

如需只用内存 Mock profile 启动：

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mock"
```

后端配置文件：

- `backend/src/main/resources/application.properties`
- `backend/src/main/resources/application-mysql.properties`
- `backend/src/main/resources/application-mock.properties`

## MySQL 建库 SQL

默认连接信息：

- 数据库：`zhishanfang_ai`
- 用户名：`root`
- 密码：`123456`
- 端口：`3306`

建库 SQL：

```sql
CREATE DATABASE IF NOT EXISTS zhishanfang_ai
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

JPA 配置为 `spring.jpa.hibernate.ddl-auto=update`，首次启动会自动建表。`DataInitializer` 会写入测试用户、药膳、汤方、茶饮、资讯和收藏数据。

如需修改密码，请编辑：

```text
backend/src/main/resources/application-mysql.properties
```

## 测试账号

登录页为模拟验证码登录：

- 手机号：`13800000000`
- 验证码：任意 4 位或 6 位数字
- 游客登录：可直接点击游客登录

当前收藏、健康档案和 AI 推荐默认绑定演示用户 `app.mock-user-id=1`。

## 页面路由

| 页面 | 路由 |
| --- | --- |
| 首页 | `/` |
| 登录注册页 | `/login` |
| 药膳精选页 | `/recipes` |
| 汤方专区页 | `/soups` |
| 茶方秘典 / 中药奶茶页 | `/teas` |
| AI 药膳入口页 | `/ai` |
| AI 健康档案填写页 | `/ai/profile` |
| AI 推荐结果页 | `/ai/result?id={resultId}` |
| 养生资讯页 | `/articles` |
| 收藏页 | `/favorites` |

## 接口说明

认证：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 手机号模拟登录 |
| POST | `/api/auth/register` | 手机号模拟注册 |
| POST | `/api/auth/guest` | 游客登录 |

首页：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/home` | 首页推荐、当季推荐、资讯、收藏概览 |

药膳 / 汤方 / 茶方：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/recipes?type=&category=` | 按类型和分类查询 |
| GET | `/api/recipes/{id}` | 详情 |

资讯：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/articles` | 资讯列表 |
| GET | `/api/articles/{id}` | 资讯详情 |

收藏：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/favorites` | 收藏列表 |
| POST | `/api/favorites` | 新增收藏，参数：`targetType`、`targetId` |
| DELETE | `/api/favorites/{id}` | 取消收藏 |

AI 药膳：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/ai/profile` | 保存健康档案 |
| POST | `/api/ai/recommend` | 生成 Mock AI 药膳推荐 |
| GET | `/api/ai/result/{id}` | 获取推荐结果详情 |

## Mock AI 规则

后端保留 `AiService` 接口，当前实现为 `MockAiService`，后续可替换 DeepSeek 或其他大模型。

当前规则：

- `气虚质 / 疲劳乏力 / 补气养血`：推荐「当归黄芪乌鸡汤」
- `湿热质 / 容易上火 / 祛湿`：推荐「茯苓山药粥」
- `失眠多梦 / 安神 / 改善睡眠`：推荐「莲子百合银耳羹」
- `减脂塑形`：推荐「冬瓜薏米汤」

推荐结果包含推荐理由、食材配方、制作方法、注意事项、忌口建议、适合体质、相关推荐和可搭配茶饮。

## 演示流程

1. 启动 MySQL。
2. 启动后端：`cd backend && .\mvnw.cmd spring-boot:run`
3. 启动前端：`cd frontend && npm run dev`
4. 打开前端地址。
5. 登录：手机号 `13800000000`，验证码任意。
6. 进入首页，点击 `AI 药膳`。
7. 进入健康档案问卷，保留默认气虚/疲劳/补气选项或自行填写。
8. 点击生成方案，跳转到 AI 推荐结果页。
9. 点击收藏方案。
10. 进入收藏页，查看 AI 药膳方案收藏。

## 常见问题

1. 后端启动失败，提示无法连接 MySQL：
   检查 MySQL 是否启动、端口是否为 `3306`、账号密码是否与 `application-mysql.properties` 一致。

2. 前端页面能打开但数据走 Mock：
   检查后端是否在 `http://localhost:18080` 运行，Vite 代理只在开发服务器下生效。

3. 端口被占用：
   前端可使用 `npm run dev -- --port 5174`；后端可在 `application.properties` 修改 `server.port`。

4. 想重新生成测试数据：
   清空 MySQL 中相关表，重启后端，`DataInitializer` 会在空表时重新播种。

5. 浏览器中文正常但 PowerShell 接口输出乱码：
   这是 PowerShell 控制台编码显示问题。接口实际返回 UTF-8 JSON，浏览器和 Axios 可正常解析。
