# Tarot CLI

基于Java的跨平台命令行塔罗牌抽牌程序

## 介绍

Tarot CLI是一个使用Java + Gradle 构建的跨平台塔罗牌抽牌工具。  
它内置了标准78张Rider-Waite塔罗牌牌库(JSON)，提供常见的塔罗牌牌阵，并支持逆位抽牌。

这是本项目的首个公开版本v0.1.0

---

## 快速开始

### 下载发布包

前往Release(发布页)：  

下载 `tarot-cli.zip`并解压

### 运行要求

- Java 21
- 无需安装或其他依赖

### 运行程序

macOS/Linux:

```bash
cd tarot-cli
./bin/tarot-cli
```

Windows:  

```powershell
.\bin\tarot-cli.bat
```

---

## 支持的牌阵

1. 单张牌：主题指引  
   用于快速提示，今日运势等
2. 三张牌(过去/现在/未来)
   最经典的塔罗牌结构，适用于绝大多是问题
3. 三张 + 一张(过去/现在/未来 + 建议)
   前三张构成时间线，第四张给出建议
4. 四张牌(你/对方/关系走向/建议)
   适用于关系问题

---

## 项目结构

项目采用Gradle多模块结构，目前仅`app`子模块包括核心逻辑

```bash
app/src
├── main
│   ├── java
│   │   └── tarot
│   │       ├── app
│   │       ├── deck
│   │       ├── domain
│   │       ├── draw
│   │       ├── repository
│   │       ├── service
│   │       └── spread
│   └── resources
│       └── tarot
│           └── cards.json
└── test
    ├── java
    └── resources
```

---

## 构建与开发

### 安装依赖

```bash
./gradlew build
```

### 运行开发版

```bash
./gradlew :app:run
```

### 打包发布版本

```bash
./gradlew :app:distZip
```

### 输出目录

```bash
app/build/distributions/
```

---

## 技术栈

- Java21
- Gradle Kotlin DSL
- Gson(解析塔罗牌JSON数据)
- 模块化结构，方便扩展
  
---

## 未来计划

v0.2

- 引入更多抽牌策略
- 引入更多牌阵(五张牌、凯尔特十字、马蹄牌阵)
- 增强CLI显示效果

---

## 许可证

```text
MIT License - 允许自由使用、修改、分发，作者不承担责任
```
