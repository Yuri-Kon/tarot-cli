# Tarot CLI

基于Java的跨平台命令行塔罗牌抽牌程序

## 介绍

Tarot CLI是一个使用Java + Gradle 构建的跨平台塔罗牌抽牌工具。  
它内置了标准78张Rider-Waite塔罗牌牌库(JSON)，提供常见的塔罗牌牌阵，支持逆位抽牌，并可根据用户问题推荐合适的牌阵。

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

在选择牌阵前，CLI 会先提示输入问题，并基于轻量级语义近似匹配给出推荐（可跳过，直接进入常规选择）。

1. 单张牌：主题指引  
   用于快速提示，今日运势等
2. 三张牌(选择解读模板)
   - 通过三张牌模板枚举 `ThreeCardPattern` 选择，例如：
     - 过去 / 现在 / 未来
     - 现状 / 困境 / 建议
     - 你 / 关系 / 对方
3. 四张牌(选择解读模板)
   - 通过四张牌模板枚举 `FourCardPattern` 选择，例如：
     - 你 / 对方 / 关系 / 建议
     - 现状 / 阻碍 / 助力 / 建议
     - 问题 / 成因 / 解决方案 / 结果
4. 四张牌：你 / 对方 / 关系走向 / 建议（固定关系牌阵）

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

## 扩展牌阵与解读模板的设计

项目的牌阵扩展都集中在 `app/src/main/java/tarot/spread`，由以下几个层次组成：

- **牌阵接口 `Spread`**：抽牌逻辑只依赖它，定义牌阵名称与牌位顺序。
- **牌位 `CardPosition`**：描述每张牌所处的位置语义。
- **模板 `SpreadPattern`**：用枚举描述“解读模板”，输出一组 `CardPosition`。
- **通用实现 `PatternBasedSpread`**：把任意模板包装成可抽牌阵。
- **固定牌阵**：例如 `SingleCardSpread`、`RelationshipFourCardSpread`。
- **抽牌结果 `SpreadResult`**：绑定 `Spread` 与抽到的牌，确保牌数一致并输出结果。

### 现有实现对应关系

- 三张/四张牌模板分别在 `tarot/spread/ThreeCardPattern.java`、`tarot/spread/FourCardPattern.java`
- 通用牌阵实现为 `tarot/spread/PatternBasedSpread.java`
- CLI 入口 `tarot/app/TarotCliApp.java` 通过 `choosePatternSpread(...)` 复用模板选择逻辑

### 新增牌阵或模板的最小改动点

1. **新增模板**：新增一个枚举实现 `SpreadPattern`，定义展示名与 `CardPosition` 列表。
2. **挂载到 CLI**：在 `TarotCliApp` 中新增菜单项，并通过 `choosePatternSpread(...)` 生成 `PatternBasedSpread`。
3. **推荐支持（可选）**：若希望进入问题推荐流程，把新模板加入 `SpreadRecommender` 的画像构建逻辑。

以上结构保证了“模板 → 牌阵 → 抽牌结果”的分层清晰，扩展时不需要改动抽牌服务与结果展示逻辑。

---

## 问题驱动的牌阵推荐

CLI 会在常规选择牌阵前先询问你的问题，并提供最多 3 个推荐牌阵，按相似度排序。当前推荐逻辑纯离线实现，思路如下：

- 为每个牌阵生成“语义画像”：由牌阵名称与牌位标签/描述构成
- 对用户问题与画像做字符 n-gram 相似度比较
- 支持常见中文关键词归一（如“感情/恋爱/爱情 → 关系”，“趋势/走向 → 未来”）
- 相似度过低或输入为空时，退回到默认推荐

推荐实现位于 `app/src/main/java/tarot/spread/SpreadRecommender.java`，如需接入更复杂的意图解析，可在该类中替换或追加策略。

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
