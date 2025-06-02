# Social-X Backend

Social-X 是一个基于 Spring Boot 的社交平台后端服务，提供用户管理、内容管理、消息通知等核心功能。

## 项目架构

项目采用模块化设计，主要包含以下模块：

- `app-common`: 公共模块，包含工具类、常量、异常处理等
- `app-infra`: 基础设施模块，包含数据库访问、缓存、消息队列等
- `app-component`: 业务组件模块，包含各个业务功能的具体实现
- `app-runner`: 应用启动模块，包含主应用配置和启动类
- `app-test`: 测试模块，包含单元测试和集成测试

## 技术栈

- Java 17
- Spring Boot 2.7.18
- MyBatis
- Sa-Token (认证授权)
- Knife4j (API 文档)
- Hutool (工具库)
- MapStruct (对象映射)
- Easy-Captcha (验证码)
- 其他依赖详见 pom.xml

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

## 快速开始

> 同 https://github.com/yoyocraft/dev-template

### 1. 克隆项目

```bash
git clone https://github.com/yoyocraft/social-x.git
cd social-x
```

### 2. 配置环境

1. 创建数据库并导入初始化脚本
2. 在 `app-runner/src/main/resources/` 目录下创建配置文件：
   - 生产环境：`application-prod.yml`
   - 本地环境：`application-local.yml`
3. 配置数据库连接信息和 Redis 连接信息

### 3. 编译打包

生产环境：

```bash
./mvnw clean package -Pprod -Dmaven.test.skip=true
```

本地环境：

```bash
./mvnw clean package -Plocal -Dmaven.test.skip=true
```

### 4. 启动服务

方式一：使用脚本启动

```bash
./run.sh
```

方式二：直接运行 jar

```bash
java -jar app-runner/target/social-x.jar
```

### 5. 访问服务

- API 文档：http://localhost:8080/doc.html

## 开发指南

### 本地开发环境

1. 创建 `application-local.yml` 配置文件
2. 配置本地开发环境参数
3. 使用本地环境配置启动：

```bash
./mvnw clean package -Plocal -Dmaven.test.skip=true
```

### 运行测试

```bash
./mvnw test -Put
```

## 部署说明

1. 生产环境打包：

```bash
./mvnw clean package -Prelease -Dmaven.test.skip=true
```

2. 使用 Docker 部署：

```bash
docker build -t social-x .
docker run -d -p 8080:8080 social-x
```

## 项目结构

```
social-x/
├── app-common/        # 公共模块
├── app-infra/         # 基础设施模块
├── app-component/     # 业务组件模块
├── app-runner/        # 应用启动模块
├── app-test/          # 测试模块
├── pom.xml           # 项目依赖管理
├── run.sh            # 启动脚本
└── Dockerfile        # Docker 构建文件
```

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。
