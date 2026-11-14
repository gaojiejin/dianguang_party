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
| `tests/` | 学习上报、重试及账号隔离验证 |

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

## 用户端调整与接口兼容

- 简洁红白界面，标题搜索、分页、刷新及图片失败提示。
- 按身份显示课程，支持搜索、完成状态筛选与继续学习。
- 图文章节字号、阅读位置及视频位置恢复，按账号隔离缓存。
- 上报失败保留重试队列，以服务端成功记录确认完成状态。
- 新增 `GET /learning/course-progress?user_id=...` 查询课程统计。
- `/classLearnInfo/addLearnDuration` 支持可选 `report_id`，通过 `learning_report` 表事务去重；旧客户端不传时保留原行为。

## 验证与限制

已有本地验证：后端打包、Web 生产构建、小程序编译；四类账号登录、学习上报重复请求及并发重试去重、服务重启去重、离线队列与账号隔离。微信开发者工具已检查登录、首页文章及党员课程入口。完整多机型验收和所有业务回归仍在进行，当前不是正式发布验收版本。

```powershell
node tests/learning_queue.cjs
python tests/learning_integration.py
```

集成脚本需要正在运行的后端和本地数据库，会创建并清理独立测试用户；脚本中的 MySQL 可执行路径需按机器修改。`learning_restart.py prepare` / `verify` 用于人工重启后端前后的去重检查，不要在生产库执行。

历史 OSS 资源未迁移，旧链接对应文件不包含在本仓库。短信登录需要单独配置服务；外链需要满足微信业务域名要求。云密钥通过环境变量配置，不随仓库分发。当前版本用于本地开发，尚未进行生产部署安全审计。

## Git 历史

远端初始提交包含当前三端代码、图片、可重建数据库的 SQL 和开发说明。早期本地提交存在硬编码云密钥，因此远端采用清理后的独立初始提交；本地原始 `main` / `v1.0.0` 继续保留，旧历史不推送。
