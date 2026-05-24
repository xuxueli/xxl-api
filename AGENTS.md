# 项目知识库

**生成时间:** 2026-05-24 11:59
**提交:** d846542
**分支:** master

## 概述

XXL-API — API管理平台（Mock、测试、文档）。Java 17、Spring Boot 4.0.1、MyBatis、MySQL、FreeMarker、AdminLTE 前端。单模块 Maven 项目（`xxl-api-admin`）。作者 xuxueli，GPLv3 协议。

## 目录结构

```
xxl-api/
├── pom.xml                   # 父 POM（单模块：xxl-api-admin）
├── xxl-api-admin/
│   ├── pom.xml               # Spring Boot jar 打包
│   ├── Dockerfile            # openjdk:21-jdk-slim
│   └── src/
│       ├── main/java/com/xxl/api/admin/
│       │   ├── XxlApiAdminApplication.java   # @SpringBootApplication 入口
│       │   ├── constant/                     # 3 个枚举/常量
│       │   ├── controller/base/              # 2 个基础控制器（首页、登录）
│       │   ├── controller/biz/               # 8 个业务控制器
│       │   ├── mapper/                       # 9 个 MyBatis Mapper 接口
│       │   ├── model/                        # 9 个 POJO + 1 个 DTO
│       │   ├── service/                      # 1 个接口 + 1 个实现
│       │   ├── util/                         # 3 个工具类
│       │   └── web/                          # SSO 认证 + 错误处理
│       ├── main/resources/
│       │   ├── application.properties        # 数据库、SSO、服务端配置
│       │   ├── i18n/                         # 中英文国际化消息
│       │   ├── mapper/                       # 9 个 MyBatis XML 映射
│       │   ├── templates/                    # 17 个 FreeMarker 视图
│       │   └── static/                       # AdminLTE、插件、业务 JS
│       └── test/java/                        # 1 个 Mock 测试（最小化）
└── .github/workflows/maven.yml               # CI：master 分支推送/PR 时构建
```

## 快速定位

| 任务 | 位置 | 说明 |
|------|------|------|
| 入口点 | `XxlApiAdminApplication.java` | 标准 `SpringApplication.run()` |
| REST 接口 | `controller/biz/` | 8 个控制器，`@RequestMapping("/{domain}")` |
| 数据模型 | `model/` | 简单 POJO，字段 + getter/setter |
| MyBatis 映射 | `mapper/` + `resources/mapper/` | 每个实体对应接口 + XML 配对 |
| 数据库配置 | `application.properties` | MySQL + HikariCP 连接池 |
| 认证 / SSO | `web/xxlsso/` | XXL-SSO 集成 |
| 错误处理 | `web/error/` | `WebExceptionResolver` + 自定义错误页面 |
| 视图模板 | `resources/templates/` | FreeMarker `.ftl`，每域一个目录 |
| 前端 JS | `resources/static/biz/` | 自定义前端 JS |
| 国际化 | `resources/i18n/` | `message_zh_CN.properties` + `message_en.properties` |
| CI | `.github/workflows/maven.yml` | JDK 17，`mvn -B package` |
| Docker | `Dockerfile` | Slim JDK 21，ENV 传参配置 |

## 编程约定

- **@Controller + @ResponseBody** — 不使用 `@RestController`。控制器方法返回 FTL 视图名（页面）或 `Response<>` 包装（AJAX）。
- **字段注入** — 统一使用 `@Resource` 注入私有字段，不使用构造器注入。
- **DAO 命名** — Mapper 字段命名为 `xxlApi{实体}Dao` 而非 `*Mapper`（例如 `xxlApiProjectDao`）。
- **响应包装** — 使用 `com.xxl.tool.response.Response`，通过 `ofSuccess()`/`ofFail()` 静态工厂方法构建。
- **分页接口** — 方法名 `pageList`，使用 `PageModel<>` + offset/pagesize 参数。
- **中文错误信息** — 校验/用户错误直接使用中文字符串（i18n 仅用于菜单标签）。
- **业务线权限检查** — 每个写操作控制器都必须调用 `hasBizPermission(request, response, bizId)`。
- **RequestMapping 按域划分** — 控制器映射到 `"/{domain}"`，CRUD 方法名统一为 `/add`、`/update`、`/delete`、`/pageList`。

## 禁止模式（本项目特有）

- 不得添加 xxl-tool / xxl-sso / Spring Boot Starter 之外的依赖。
- 不得改用构造器注入 — 项目统一使用 `@Resource` 字段注入。
- 不得改用 `@RestController` — 控制器同时提供页面和 JSON 响应。
- 不得移除控制器写操作的业务线权限检查。
- 不得使用 Lombok — 模型类显式编写 getter/setter。
- 不得跳过 AJAX 接口的 `Response<>` 包装。

## 命令

```bash
# 构建（默认跳过测试，已在父 POM 中配置）
mvn clean package

# 运行
java -jar xxl-api-admin/target/xxl-api-admin-*.jar

# Docker
docker build -t xxl-api-admin ./xxl-api-admin/
docker run -p 8080:8080 -e PARAMS="--server.port=8080" xxl-api-admin
```

## 注意事项

- 父 POM 设置了 `maven.test.skip=true` — 构建时默认不运行测试。
- 仅有一个测试文件 `XxlApiTest.java`（Mock 配置），CI 中不执行。
- 前端静态资源全部为 vendored 引入（AdminLTE bower_components、CodeMirror、zTree、CKEditor 等），无 npm/package.json。
- FTL 模板使用 `new_builtin_class_resolver=safer` 保障安全性。
- XXL-SSO 处理 `/auth/login` 认证路径，Token 存储在 cookie `xxl_api_login_token` 中。
- 应用上下文路径：`/xxl-api-admin`。

## 语言要求

- 本项目主要使用中文（代码注释、错误提示、配置说明均为中文）。
- 所有自动生成的内容（包括 AI 生成）必须使用中文。
- 禁止使用英文编写注释、文档或提示信息，除非处理国际化消息文件本身。

<!-- SPECKIT START -->
关于所用技术、项目结构、Shell 命令及其他重要信息的额外上下文，请阅读当前计划：`specs/001-project-structure-refactor/plan.md`
<!-- SPECKIT END -->
