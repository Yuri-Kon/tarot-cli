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

本项目的牌阵模块围绕三个核心概念设计：**模板 (`SpreadPattern`) → 牌阵 (`Spread`) → 抽牌结果 (`SpreadResult`)**。

- **`Spread`**：抽牌逻辑只依赖这个接口
  - `String getName()`：牌阵名称（展示用）
  - `List<CardPosition> getPositions()`：每一张牌对应的位置定义，顺序即为抽牌顺序
  - `int getCardCount()`：默认返回 `getPositions().size()`，决定需要抽几张牌
- **`CardPosition`**：单个位置的语义
  - 例如「过去 / 现在 / 未来」「你 / 对方 / 关系 / 建议」中的每一个位置
- **`SpreadResult`**：把 `Spread` 与抽到的牌列表绑定在一起
  - 构造时校验抽到的牌数必须等于 `spread.getCardCount()`
  - `toString()` 会按 `Spread.getPositions()` 的顺序输出「位置标签 + 抽到的牌」

### 模板接口 `SpreadPattern`

为了支持任意 N 张牌的解读模板，引入了统一的模板接口：

```java
public interface SpreadPattern {
    String getDisplayName();          // 模板展示名称，例如「过去 / 现在 / 未来」
    List<CardPosition> getPositions();// 该模板下的牌位定义列表
}
```

任何张数的模板（3 张、4 张、5 张、7 张等），只要实现了 `SpreadPattern` 接口，就可以在 CLI 中通过统一的逻辑列出并选择。

当前内置的模板：

- `ThreeCardPattern implements SpreadPattern`：三张牌常见模板集合
- `FourCardPattern implements SpreadPattern`：四张牌常见模板集合

每个枚举常量都定义了：

- 展示名称 `displayName`（给用户看的中文标题）
- 一组 `CardPosition`，用于描述每个位置的含义

### 通用牌阵实现 `PatternBasedSpread`

`PatternBasedSpread` 是一个通用的 `Spread` 实现，用来把任意 `SpreadPattern` 包装成可抽牌的牌阵：

```java
public class PatternBasedSpread implements Spread {
    private final String namePrefix;   // 如「三张牌」「四张牌」
    private final SpreadPattern pattern;

    public PatternBasedSpread(String namePrefix, SpreadPattern pattern) { ... }

    @Override
    public String getName() { ... }        // 例如「三张牌：过去 / 现在 / 未来」
    @Override
    public List<CardPosition> getPositions() { ... } // 直接返回 pattern.getPositions()
}
```

牌阵的实际张数由 `pattern.getPositions().size()` 决定，因此可以自然支持任意 N 张牌的结构，而无需为 3 张、4 张、5 张分别写不同的 `Spread` 实现类。

### CLI 中的通用模板选择逻辑

在命令行应用 `TarotCliApp` 中，三张牌和四张牌的二级菜单都是通过一个通用方法来实现的：

- 三张牌菜单：`chooseThreeCardSpread(scanner)`  
  - 内部调用 `choosePatternSpread(scanner, ThreeCardPattern.values(), "三张牌")`
- 四张牌菜单：`chooseFourCardSpread(scanner)`  
  - 内部调用 `choosePatternSpread(scanner, FourCardPattern.values(), "四张牌")`

核心逻辑（概念化描述）：

1. 接收一组实现了 `SpreadPattern` 的模板数组（例如 `ThreeCardPattern.values()`）。
2. 在 CLI 中依次打印每个模板的 `getDisplayName()` 供用户选择。
3. 支持输入编号、默认值、边界检查和「返回上级菜单」。
4. 根据用户选择的模板，构造 `new PatternBasedSpread(cardCountLabel, pattern)` 并返回。

这样一来，只要实现了一个新的 `SpreadPattern` 枚举，并在 CLI 中多加一个菜单项，即可无缝扩展到更多张数的牌阵，而无需改动抽牌服务或结果展示逻辑。

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
