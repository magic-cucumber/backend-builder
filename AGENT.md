# AGENT.md

本文面向在本仓库内进行开发/改造的自动化 Agent（以及人类贡献者），用于快速理解项目结构、关键约定与常见坑点。

## 1. 项目概览

- 本仓库是一个“更适合毕设/课设”的极简全栈脚手架：前端 Vue3 + Vite，后端 Spring Boot 3 + MyBatis-Plus。
- 项目说明与截图见：[README.md](README.md:1)。
- 数据库初始化脚本见：[db.sql](db.sql:1)。

## 2. 目录与技术栈

### 2.1 后端（Java / Spring Boot）

- Gradle 构建脚本：[backend-java/build.gradle.kts](backend-java/build.gradle.kts:1)
  - Spring Boot Web、MyBatis-Plus、FusionAuth JWT、Lombok 等依赖。
- 启动类：[java.BackendApplication](backend-java/src/main/java/top/kagg886/backend/BackendApplication.java:6)
- 主要模块：
  - 配置层：
    - [java.JacksonConfig](backend-java/src/main/java/top/kagg886/backend/config/JacksonConfig.java:21)
    - [java.MvcConfig](backend-java/src/main/java/top/kagg886/backend/config/MvcConfig.java:12)
    - [java.MyBatisPlusConfig](backend-java/src/main/java/top/kagg886/backend/config/MyBatisPlusConfig.java:9)
    - [java.UploadConfig](backend-java/src/main/java/top/kagg886/backend/config/UploadConfig.java:14)
  - 控制器层：
    - [java.AuthController](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:11)
    - [java.UserController](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:10)
    - [java.UploadController](backend-java/src/main/java/top/kagg886/backend/controller/UploadController.java:17)
  - 统一返回与鉴权拦截：
    - [java.ResponseWrapperAdvice](backend-java/src/main/java/top/kagg886/backend/interceptor/ResponseWrapperAdvice.java:17)
    - [java.RequireLogin](backend-java/src/main/java/top/kagg886/backend/interceptor/RequireLogin.java:10)
    - [java.RequireLoginInterceptor](backend-java/src/main/java/top/kagg886/backend/interceptor/RequireLoginInterceptor.java:17)
  - 领域与持久层：
    - 实体：[java.User](backend-java/src/main/java/top/kagg886/backend/entity/User.java:14)
    - Mapper：[java.UserMapper](backend-java/src/main/java/top/kagg886/backend/mapper/UserMapper.java:7)
    - Service：[java.UserService](backend-java/src/main/java/top/kagg886/backend/service/UserService.java:6)
    - ServiceImpl：[java.UserServiceImpl](backend-java/src/main/java/top/kagg886/backend/service/impl/UserServiceImpl.java:13)
  - JWT 工具类：[java.JWT.publish()](backend-java/src/main/java/top/kagg886/backend/util/JWT.java:25)、[java.JWT.verify()](backend-java/src/main/java/top/kagg886/backend/util/JWT.java:39)

### 2.2 前端（Vue3 / Vite / TS）

- 依赖与脚本：[fronted/package.json](fronted/package.json:1)
- Vite 开发代理（`/api` -> `http://localhost:8080`）：[fronted/vite.config.ts](fronted/vite.config.ts:6)
- 入口：
  - 应用挂载：[fronted/src/main.ts](fronted/src/main.ts:1)
  - Root 组件：[fronted/src/App.vue](fronted/src/App.vue:1)
- 路由：
  - 路由表：[fronted/src/route/index.ts](fronted/src/route/index.ts:1)
  - 路由守卫：[fronted/src/route/router.ts](fronted/src/route/router.ts:9)
- 网络层（Axios 实例 + 拦截器）：[fronted/src/util/axios.ts](fronted/src/util/axios.ts:1)
- 状态管理（Pinia Store）：[fronted/src/store/user.ts](fronted/src/store/user.ts:8)
- API 层：
  - DTO：[fronted/src/api/dto.ts](fronted/src/api/dto.ts:1)
  - 登录注册：[fronted/src/api/auth.ts](fronted/src/api/auth.ts:17)
  - 用户相关：[fronted/src/api/user.ts](fronted/src/api/user.ts:15)

## 3. 快速启动（本地开发）

### 3.1 数据库

1. 创建数据库并导入表结构/初始数据：[db.sql](db.sql:1)
2. 配置后端数据库连接（用户名/密码/地址）：[backend-java/src/main/resources/application.properties](backend-java/src/main/resources/application.properties:4)

### 3.2 后端启动

- 方式 1：IDEA 直接运行 [java.BackendApplication](backend-java/src/main/java/top/kagg886/backend/BackendApplication.java:6)
- 方式 2：Gradle（在 `backend-java/` 目录执行）
  - `./gradlew bootRun`

> 后端统一加了 `server.servlet.context-path=/api`，即所有 Controller 的 `@RequestMapping` 都会挂在 `/api` 下：[backend-java/src/main/resources/application.properties](backend-java/src/main/resources/application.properties:2)

### 3.3 前端启动

- 在前端目录安装依赖并启动开发服务器（建议 Node.js 20）：[README.md](README.md:79)
  - `pnpm install`
  - `pnpm run dev`

## 4. 后端核心约定与关键流程

### 4.1 统一返回结构（重要）

- Controller 层的返回值会被 [java.ResponseWrapperAdvice](backend-java/src/main/java/top/kagg886/backend/interceptor/ResponseWrapperAdvice.java:17) 包装为 [java.BaseResponse](backend-java/src/main/java/top/kagg886/backend/dto/BaseResponse.java:7)：
  - 成功：`{ code: 0, message: "ok", data: ... }`（见 [java.BaseResponse.ok()](backend-java/src/main/java/top/kagg886/backend/dto/BaseResponse.java:17)）
  - 失败：`{ code: xxx, message: ..., data: null }`（见 [java.BaseResponse.error()](backend-java/src/main/java/top/kagg886/backend/dto/BaseResponse.java:13)）
- 异常处理：
  - 若抛出 [java.BaseResponse.Wrapped](backend-java/src/main/java/top/kagg886/backend/dto/BaseResponse.java:21)，会按其 code/message 返回（见 [java.ResponseWrapperAdvice.handleException()](backend-java/src/main/java/top/kagg886/backend/interceptor/ResponseWrapperAdvice.java:46)）。
  - 其他异常会落到 code=-1（同上）。

> Agent 在新增/修改接口时，默认只需要返回“业务对象”，不需要手写包装；但要保证前端 Axios 拦截器期望 `BaseResponse` 格式（见 [ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:16)）。

### 4.2 鉴权：JWT + 拦截器 + 注解

- JWT：
  - 发布 token：[java.JWT.publish()](backend-java/src/main/java/top/kagg886/backend/util/JWT.java:25)
  - 校验 token：[java.JWT.verify()](backend-java/src/main/java/top/kagg886/backend/util/JWT.java:39)
- 拦截器：
  - 全局注册并排除 `/auth/**`：[java.MvcConfig.addInterceptors()](backend-java/src/main/java/top/kagg886/backend/config/MvcConfig.java:20)
  - 规则（见 [java.RequireLoginInterceptor.preHandle()](backend-java/src/main/java/top/kagg886/backend/interceptor/RequireLoginInterceptor.java:27)）：
    - 无注解、无 token：放行
    - 有注解、无 token：抛出 code=-2（前端会跳转登录）
    - 有 token：验证 token；失败抛 code=-2
    - 有注解且角色不匹配：抛 code=-1
- 注解：
  - [java.RequireLogin](backend-java/src/main/java/top/kagg886/backend/interceptor/RequireLogin.java:10) 可指定允许角色（默认 USER/ADMIN）。

### 4.3 用户模块（Auth / User）

- 登录：
  - 接口：[java.AuthController.login()](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:35)
  - 成功返回 token 字符串（最终会被统一包装成 `BaseResponse<String>`）
- 注册：
  - 接口：[java.AuthController.register()](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:50)
  - 返回用户对象（密码被置空）
- 个人信息：
  - 查询：[java.UserController.getProfile()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:16)
  - 更新：[java.UserController.updateProfile()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:22)
- 管理端用户 CRUD（ADMIN）：
  - 创建：[java.UserController.createUser()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:32)
  - 更新：[java.UserController.updateUser()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:38)
  - 删除：[java.UserController.deleteUser()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:45)
  - 查询单个：[java.UserController.getUser()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:51)

服务层逻辑：
- 登录校验：[java.UserServiceImpl.login()](backend-java/src/main/java/top/kagg886/backend/service/impl/UserServiceImpl.java:16)
- 注册逻辑（校验用户名唯一、填充 createdAt）：[java.UserServiceImpl.register()](backend-java/src/main/java/top/kagg886/backend/service/impl/UserServiceImpl.java:28)

### 4.4 上传模块

- 上传接口（PUT + multipart）：[java.UploadController.upload()](backend-java/src/main/java/top/kagg886/backend/controller/UploadController.java:25)
  - 返回可访问的 URL 字符串（`/api/upload/<uuid>.<ext>`）
- 静态资源映射：
  - 资源映射 `/upload/**` -> `file:<upload.root>/`：[java.UploadConfig.addResourceHandlers()](backend-java/src/main/java/top/kagg886/backend/config/UploadConfig.java:29)
  - 上传根目录配置项 `upload.root`：[backend-java/src/main/resources/application.properties](backend-java/src/main/resources/application.properties:9)

### 4.5 JSON 与时间序列化

- Long 以字符串输出（避免前端精度丢失）：[java.JacksonConfig.jacksonObjectMapper()](backend-java/src/main/java/top/kagg886/backend/config/JacksonConfig.java:31)
- `LocalDateTime` 序列化为 ISO 字符串：
  - Serializer：[java.LocalDateTimeAsStringSerializer](backend-java/src/main/java/top/kagg886/backend/util/LocalDateTimeAsStringSerializer.java:11)
  - Deserializer：[java.LocalDateTimeAsStringDeSerializer](backend-java/src/main/java/top/kagg886/backend/util/LocalDateTimeAsStringDeSerializer.java:11)

## 5. 前端核心约定与关键流程

### 5.1 Axios 约定（统一处理 BaseResponse）

- 请求拦截：从 `localStorage` 取 token 并写入 `Authorization` 头：[ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:8)
- 响应拦截：
  - code=-2：跳转登录：[ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:18)
  - code!=0：弹出错误并 reject：[ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:22)
  - code=0：直接返回 `data.data`（因此 API 层函数签名返回“业务对象”）

### 5.2 Pinia 用户态

- Store：保存 `user` 与 `token`（localStorage 持久化）：[ts.useUserStore()](fronted/src/store/user.ts:8)
- 登录：先拿 token，再拉 profile：[ts.useUserStore().login](fronted/src/store/user.ts:12)
- 退出：清空本地 user/token：[ts.useUserStore().logout](fronted/src/store/user.ts:20)

### 5.3 路由与页面

- 路由表：前台 `/`、后台 `/admin/*`、登录 `/login`、注册 `/register`：[fronted/src/route/index.ts](fronted/src/route/index.ts:3)
- 后台路由守卫：进入 `/admin` 必须有 token（注意：当前判断用的是 `== null`）：[ts.router.beforeEach()](fronted/src/route/router.ts:9)
- 登录页逻辑：[fronted/src/pages/login.vue](fronted/src/pages/login.vue:17)
- 注册页逻辑：[fronted/src/pages/register.vue](fronted/src/pages/register.vue:21)

## 6. API 对齐清单（Agent 修改接口时必看）

### 6.1 后端已实现的主要接口

- `POST /api/auth/login` -> token
  - 实现：[java.AuthController.login()](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:35)
  - 前端调用：[ts.login()](fronted/src/api/auth.ts:17)
- `POST /api/auth/register` -> User
  - 实现：[java.AuthController.register()](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:50)
  - 前端调用：[ts.register()](fronted/src/api/auth.ts:21)
- `GET /api/users/profile` -> User
  - 实现：[java.UserController.getProfile()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:16)
  - 前端调用：[ts.profile()](fronted/src/api/user.ts:15)
- `PUT /api/users/profile` -> User
  - 实现：[java.UserController.updateProfile()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:22)
  - 前端调用：[ts.updateProfile()](fronted/src/api/user.ts:19)
- `PUT /api/upload` (multipart) -> URL string
  - 实现：[java.UploadController.upload()](backend-java/src/main/java/top/kagg886/backend/controller/UploadController.java:25)

### 6.2 当前存在的“前后端不一致/缺失”

这些点不会阻止你阅读项目，但会影响你后续修功能/补齐接口时的决策：

1. 前端调用了 `GET /api/users`（分页列表），但后端没有对应接口
   - 前端：[ts.listUsers()](fronted/src/api/user.ts:39)
   - 后端 Controller 目前只有 profile 与按 id 查询/管理操作：[java.UserController](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:10)
2. 注册 DTO 字段不一致
   - 后端注册只接受 `username/email/phone/password`：[java.AuthController.RegisterInfo](backend-java/src/main/java/top/kagg886/backend/controller/AuthController.java:25)
   - 前端注册请求类型含 `name/contactInfo/address`：[ts.RegisterRequest](fronted/src/api/auth.ts:9)
3. 后台路由守卫对 token 的判断可能放过空字符串
   - 代码使用 `localStorage.getItem('token') == null`：[ts.router.beforeEach()](fronted/src/route/router.ts:9)
   - 但 store 默认把 token 写成 `''`：[ts.useUserStore()](fronted/src/store/user.ts:10)
4. 用户信息可能包含明文密码风险
   - 后端用户实体注释“明文”：[java.User.password](backend-java/src/main/java/top/kagg886/backend/entity/User.java:26)
   - 拦截器把 DB 用户对象放入 request attribute 后直接返回 profile：[java.UserController.getProfile()](backend-java/src/main/java/top/kagg886/backend/controller/UserController.java:16)
5. `upload.root` 默认值包含 `~`，Java 的 `File` 不会自动展开（可能创建相对路径 `~` 目录）
   - 配置项：[backend-java/src/main/resources/application.properties](backend-java/src/main/resources/application.properties:9)
   - 初始化逻辑：[java.UploadConfig.init()](backend-java/src/main/java/top/kagg886/backend/config/UploadConfig.java:20)

> Agent 如需“修复/补齐”，建议先补齐后端 `GET /users` 分页接口（可用 MyBatis-Plus 的 `Page<T>`，分页拦截器已启用：[java.MyBatisPlusConfig](backend-java/src/main/java/top/kagg886/backend/config/MyBatisPlusConfig.java:9)），再对齐前端 DTO 字段。

## 7. 对 Agent 的变更指导（避免踩坑）

1. 新增后端接口时：
   - 返回普通对象即可，由 [java.ResponseWrapperAdvice](backend-java/src/main/java/top/kagg886/backend/interceptor/ResponseWrapperAdvice.java:17) 包装。
   - 需要自定义错误 code 时，抛 [java.BaseResponse.Wrapped](backend-java/src/main/java/top/kagg886/backend/dto/BaseResponse.java:21)。
2. 修改鉴权逻辑时：
   - 保持前端对 code=-2 的语义（会跳转登录）：[ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:18)
   - 拦截器配置位置：[java.MvcConfig.addInterceptors()](backend-java/src/main/java/top/kagg886/backend/config/MvcConfig.java:20)
3. 改动前端 API 层时：
   - `axios` 响应拦截器会把 `BaseResponse<T>` 直接解包成 `T` 返回（见 [ts.fronted/src/util/axios.ts](fronted/src/util/axios.ts:16)），因此 API 函数签名应返回业务对象。
4. 数据库字段与实体需同步：
   - 表结构/初始数据：[db.sql](db.sql:1)
   - 实体映射：[java.User](backend-java/src/main/java/top/kagg886/backend/entity/User.java:14)

## 8. 常用检查点（提交前）

- 后端能启动，且 `/api/auth/login` 返回符合 `BaseResponse` 的 JSON（由 [java.ResponseWrapperAdvice.beforeBodyWrite()](backend-java/src/main/java/top/kagg886/backend/interceptor/ResponseWrapperAdvice.java:31) 保障）。
- 前端开发代理仍可用（`/api` 代理配置）：[fronted/vite.config.ts](fronted/vite.config.ts:8)
- 若改了接口路径/字段，同步更新：
  - [fronted/src/api/auth.ts](fronted/src/api/auth.ts:1)
  - [fronted/src/api/user.ts](fronted/src/api/user.ts:1)
  - 以及对应页面（例如 [fronted/src/pages/backend/user/index.vue](fronted/src/pages/backend/user/index.vue:1)）
