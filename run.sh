#!/bin/bash

set -e  # 遇到错误立即退出
set -o pipefail  # 管道中任意命令失败时立即退出

JAR_PATH="app-runner/target/social-x.jar"
JAVA_OPTS="-Xmx1024m -Xms1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError"
ENV="local" # prod
# 检查 JAR 是否存在
if [ ! -f "$JAR_PATH" ]; then
    echo "JAR 文件未找到，正在执行打包..."
    if ./mvnw clean package -P$ENV -DskipTests; then
        echo "打包完成。"
    else
        echo "打包失败，请检查代码或环境配置。" >&2
        exit 1
    fi
fi

# 启动应用
echo "启动应用：$JAR_PATH"
exec nohup java $JAVA_OPTS -jar $JAR_PATH >output.log 2>&1 &