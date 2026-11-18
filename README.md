# 电光党建

包含 uni-app 微信小程序、Web 管理端和 Spring Boot 后端的本地开发工作区。小程序面向党员、积极分子，提供党建阅读、课程学习、学习进度及个人信息管理；管理端提供内容、章节、轮播图、支部和成员管理。

## 项目结构

| 目录 | 用途 |
| --- | --- |
| `dianguang-party-miniprogram/` | uni-app / Vue 2 小程序，保留移动管理入口 |
| `dianguang-party-admin/` | Vue 2 / Element UI Web 管理端 |
| `dianguang-party-backend/` | Java 8 / Spring Boot 2.6.13 / MyBatis Plus 后端 |
| `database/` | 空库结构、迁移、演示账号、文章及课程 SQL |
| `assets/banners/` | 五张轮播原图，已纳入版本管理 |
| `scripts/` | 本地资源准备脚本 |
| `tests/` | 首页搜索分页、学习上报和账号隔离验证，不参与业务运行 |

两端共用 `http://127.0.0.1:8080` 后端，后端连接本地 MySQL 和 Redis。上传及导出默认写入后端 `.local/uploads`，通过 `/uploads/` 访问，默认无需 OSS。

## 环境

- JDK 8、Maven 3、MySQL 8、Redis。
- Node.js 18 与 npm（当前本地构建环境）。
- Windows PowerShell、HBuilderX（uni-app 编译与 Dart Sass 插件）、微信开发者工具。
- Python 3：运行集成验证时需要。

依赖目录、构建产物、日志、私人上传文件不提交。原始数据库快照含历史用户信息，仅保留本机；仓库提供无历史个人数据的结构和演示种子。

## 数据库与图片初始化

在 MySQL 客户端执行，替换为实际克隆位置。结构脚本仅用于**全新空库**，已有库先备份，只执行所需迁移。

```sql
CREATE DATABASE dianguang_party CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE dianguang_party;
SOURCE D:/电光党建/database/schema.sql;
SOURCE D:/电光党建/database/learning-report.sql;
SOURCE D:/电光党建/database/local-admin.sql;
SOURCE D:/电光党建/database/demo-accounts.sql;
SOURCE D:/电光党建/database/demo-content.sql;
SOURCE D:/电光党建/database/demo-courses.sql;
```

种子脚本按存在性检查避免重复插入演示数据。`SOURCE` 是 MySQL 客户端命令。仓库根目录运行图片准备脚本：

```powershell
./scripts/prepare-assets.ps1
```

将五张原图复制到后端 `.local/uploads/`，供示例文章和课程通过 `http://127.0.0.1:8080/uploads/banner-N.png` 使用。

## 启动后端

先启动 MySQL（3306）及 Redis（6379）。

```powershell
cd dianguang-party-backend
# 可选，覆盖默认本地 root / root
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = 'root'
./start-local.ps1
```

默认启用 `local` profile，端口 8080，配置见 `src/main/resources/application-local.yml`。Maven 使用项目内 settings，避免机器全局配置影响。也可打包启动：

```powershell
mvn -gs scripts/maven-settings.xml -s scripts/maven-settings.xml -DskipTests package
java -jar target/dianguang-party-backend-0.0.1-SNAPSHOT.jar
```

启动目录应为后端目录，以保证上传路径正确。历史 Java 包名 `com.example.spb` 保留。

## 启动 Web 管理端

```powershell
cd dianguang-party-admin
npm ci
npm run serve
# 生产构建：npm run build
```

访问 <http://localhost:8090>，默认请求本地 8080 接口。

## 运行微信小程序

先在管理端目录执行 `npm ci`（编译兼容配置使用其 Babel 插件），再执行：

```powershell
cd dianguang-party-miniprogram
npm ci
./build-local.ps1 -Platform mp-weixin
```

当前构建脚本、`babel.config.js`、`vue.config.js` 和 `start-local.ps1` 使用 HBuilderX 路径 `D:/HBuilderX/HBuilderX`，换机器时修改这些路径。

微信开发者工具导入 `dianguang-party-miniprogram/unpackage/dist/build/mp-weixin`。本地模拟器调试关闭合法域名校验，使用自己的 AppID 或可用的测试模式。H5 辅助预览：

```powershell
./start-local.ps1
# H5 生产构建：./build-local.ps1 -Platform h5
```

真机无法通过 `127.0.0.1` 访问电脑：将小程序 `api/index.js`、后端 `server.url` 和数据库内资源地址改为电脑局域网 IP，确保手机同网且端口可访问。正式发布需要 HTTPS 和微信合法域名。

## 演示账号

| 身份 | 账号 | 密码 |
| --- | --- | --- |
| 超级管理员 | `admin` | `admin` |
| 支部管理员 | `branchadmin` | `Demo123!` |
| 党员 | `member` | `Demo123!` |
| 积极分子 | `activist` | `Demo123!` |

仅用于本地演示。部署前修改密码，设置独立的 `JWT_SECRET` 并检查权限与密码存储。示例内容不代表实际活动或正式培训。

## 当前小程序版本

小程序已恢复最初代码的页面风格、配色和图标，主要将依赖屏幕高度的尺寸改为按屏宽缩放的 `rpx`。此前的整套红金主题已撤回，不作为当前界面。

党员和积极分子共用的首页在原有风格上做了局部优化：

- 保留时政要闻、支部动态、微课堂展示三个栏目。
- 轮播图采用约 2.4∶1 的展示区域，图片完整显示，不拉伸。
- 每个栏目支持标题搜索，分别保留搜索词；切换栏目和搜索时回到第一页。
- 后端分页，每页最多 5 条，只展示审核通过的内容，按发布时间、文章 ID 倒序排列。
- 微课堂同时包含原有“链接展示”类型；补充加载中、空结果和失败重试提示。
- 列表仅微调标题行高、间距和缩略图比例，不增加额外装饰或业务入口。

## 接口兼容

- 新增 `GET /article/passed-page?type=支部动态&keyword=党建&page=1`，返回 `records`、`total`、`page`、`pageSize`，每页固定 5 条。支持三个首页栏目，搜索参数最长 100 个字符，页码至少为 1。
- 保留旧文章列表接口，Web 管理端及其他页面无需迁移。
- 后端保留 `GET /learning/course-progress?user_id=...` 及学习上报可选 `report_id` 的兼容扩展。
- `learning_report` 表用于新上报协议的事务去重；当前恢复的原版学习页面仍使用原有上报方式，不具备此前重构版的继续学习、字号选择和离线队列等新增交互。

## 验证与限制

当前首页改动已通过后端编译打包、小程序编译、三个栏目的标题查询及分页接口测试，以及栏目切换、搜索词隔离和过期响应保护测试。Web 管理端生产构建在此前验证通过。本次未完成全部页面和全部真机型号回归，当前用于本地开发。

```powershell
node tests/home_feed.cjs
python tests/article_feed.py
```

`home_feed.cjs` 在本地模拟首页请求；`article_feed.py` 对运行中的本地后端做只读验证，需要先启动后端并初始化数据。

`learning_queue.cjs`、`learning_integration.py`、`learning_restart.py` 为保留的学习协议兼容测试，不代表当前原版页面已启用新版交互。集成脚本会创建并清理独立测试用户，MySQL 可执行路径需按机器修改；重启测试按 `prepare`、人工重启后端、`verify` 顺序执行，不要在生产库运行。

## 目录维护

根目录 `scripts` 用于复制演示图片，`tests` 用于开发验证，建议保留。后端 `scripts/maven-settings.xml` 被启动脚本直接引用，不应删除。

`node_modules`、`target`、`dist`、`unpackage` 是可重新生成的依赖或构建产物，不上传远端。日志已清空整理，运行中的服务会继续生成日志。`.local/uploads` 是实际资源目录，不能作为日志目录整体清理。根目录的临时修改脚本及含历史数据的原始 SQL 备份不上传。

历史 OSS 资源未迁移，旧链接对应文件不包含在本仓库。短信登录需要单独配置服务；外链需要满足微信业务域名要求。云密钥通过环境变量配置，不随仓库分发。当前版本用于本地开发，尚未进行生产部署安全审计。

## Git 历史

远端初始提交包含当前三端代码、图片、可重建数据库的 SQL 和开发说明。早期本地提交存在硬编码云密钥，因此远端采用清理后的独立初始提交；本地原始 `main` / `v1.0.0` 继续保留，旧历史不推送。
