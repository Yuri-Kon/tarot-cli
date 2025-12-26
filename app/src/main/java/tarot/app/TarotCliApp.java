package tarot.app;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import tarot.deck.factory.DeckFactory;
import tarot.deck.factory.StandardTarotDeckFactory;
import tarot.draw.strategy.DrawStrategy;
import tarot.draw.strategy.StandardDrawStrategy;
import tarot.repository.CardRepository;
import tarot.repository.JsonCardRepository;
import tarot.service.TarotDrawService;
import tarot.spread.FourCardPattern;
import tarot.spread.PatternBasedSpread;
import tarot.spread.RelationshipFourCardSpread;
import tarot.spread.SingleCardSpread;
import tarot.spread.Spread;
import tarot.spread.SpreadPattern;
import tarot.spread.SpreadRecommender;
import tarot.spread.SpreadRecommender.SpreadSuggestion;
import tarot.spread.ThreeCardPattern;

/** 简单命令行交互的塔罗牌程序 功能： - 让用户选择牌阵类型 - 选择是否启动逆位 - 显示抽牌结果 */
public class TarotCliApp {

  public static void main(String[] args) {

    CardRepository repository = new JsonCardRepository();
    DeckFactory deckFactory = new StandardTarotDeckFactory(repository);
    DrawStrategy strategy = new StandardDrawStrategy();
    Random random = new Random();
    TarotDrawService service = new TarotDrawService(deckFactory, strategy, random);

    // 命令行交互
    try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
      System.out.println("欢迎使用塔罗牌抽牌程序");
      System.out.println("当前使用标准78张塔罗牌 & 标准抽牌策略");
      System.out.println();

      boolean running = true;

      while (running) {
        String question = promptUserQuestion(scanner);
        if (question == null) {
          break;
        }

        SpreadSelection selection = chooseSpreadWithRecommendations(scanner, question);
        if (selection.exitRequested()) {
          running = false;
          System.out.println("已退出程序，再见");
          continue;
        }

        Spread spread = selection.spread();
        if (spread == null) {
          System.out.println("未能确定牌阵，请重新选择。");
          System.out.println();
          continue;
        }

        // 询问是否启用逆位
        boolean enableReversed = askReversedEnabled(scanner);

        // 每次占卜前重置牌堆
        service.resetDeck();

        // 执行抽牌
        System.out.println("正在抽牌，请稍候...");
        var result = service.draw(spread, enableReversed);

        // 输出结果
        System.out.println("=====================");
        System.out.println(result);
        System.out.println("=====================");
        System.out.println();

        // 是否继续
        System.out.println("是否继续抽牌? (y/n 默认y): ");

        String again = scanner.nextLine().trim();
        if (again.equalsIgnoreCase("n") || again.equalsIgnoreCase("no")) {
          running = false;
          System.out.println("好的，祝你今天愉快");
        } else {
          System.out.println();
        }
      }
    }
  }

  private static void printMenu() {
    System.out.println("请选择牌阵类型：");
    System.out.println(" 1. 单张牌：主题指引");
    System.out.println(" 2. 三张牌(选择解读方式)");
    System.out.println(" 3. 四张牌(选择解读方式)");
    System.out.println(" 4. 四张牌：你 / 对方 / 关系走向 / 建议");
    System.out.println(" 0. 退出程序");
    System.out.println();
  }

  /**
   * 通用的「从一组模板中选择牌阵」逻辑。 只要模板实现了 SpreadPattern，即可使用本方法； 因此可以支持任意 N 张牌的解读模板。
   *
   * @param scanner 输入流
   * @param patterns 模板数组（如 ThreeCardPattern.values()）
   * @param cardCountLabel 展示用的张数说明，例如「三张牌」「四张牌」
   * @return 根据选择创建出的牌阵，返回 null 表示返回上级菜单
   */
  private static Spread choosePatternSpread(
      Scanner scanner, SpreadPattern[] patterns, String cardCountLabel) {
    System.out.println();
    System.out.println("请选择" + cardCountLabel + "的解读方式：");

    for (int i = 0; i < patterns.length; i++) {
      System.out.printf(" %d. %s%n", i + 1, patterns[i].getDisplayName());
    }

    System.out.println(" 0. 返回上级菜单");
    System.out.println("请输入编号，默认1：");

    if (!scanner.hasNextLine()) {
      System.out.println("检测到输入结束，返回上一级菜单");
      return null;
    }

    String input = scanner.nextLine().trim();

    if (input.equals("0")) {
      return null;
    }

    int index = 0;
    if (!input.isEmpty()) {
      try {
        int choice = Integer.parseInt(input);
        if (choice >= 1 && choice <= patterns.length) {
          index = choice - 1;
        } else {
          System.out.println("输入超出范围，默认选择第一个");
        }
      } catch (NumberFormatException e) {
        System.out.println("输入不是数字，默认选择第一个");
      }
    }

    SpreadPattern pattern = patterns[index];
    System.out.println("你选择了" + cardCountLabel + "解读方式：" + pattern.getDisplayName());
    // 使用通用的 PatternBasedSpread，而不是写死三张 / 四张牌
    return new PatternBasedSpread(cardCountLabel, pattern);
  }

  /** 选择三张牌的解读模板（基于通用逻辑封装） */
  private static Spread chooseThreeCardSpread(Scanner scanner) {
    return choosePatternSpread(scanner, ThreeCardPattern.values(), "三张牌");
  }

  /** 选择四张牌的解读模板（基于通用逻辑封装） */
  private static Spread chooseFourCardSpread(Scanner scanner) {
    return choosePatternSpread(scanner, FourCardPattern.values(), "四张牌");
  }

  private static SpreadSelection chooseSpreadWithRecommendations(Scanner scanner, String question) {
    List<SpreadSuggestion> suggestions = SpreadRecommender.recommend(question);
    if (!suggestions.isEmpty()) {
      Spread spread = chooseFromSuggestions(scanner, suggestions);
      if (spread != null) {
        return new SpreadSelection(spread, false);
      }
    }
    return chooseSpreadFromMenu(scanner);
  }

  private static SpreadSelection chooseSpreadFromMenu(Scanner scanner) {
    while (true) {
      printMenu();

      System.out.println("请输入选项编号：");
      if (!scanner.hasNextLine()) {
        System.out.println("未检测到输入，程序结束。");
        return new SpreadSelection(null, true);
      }
      String choice = scanner.nextLine().trim();

      Spread spread;
      switch (choice) {
        case "1":
          spread = new SingleCardSpread();
          break;
        case "2":
          spread = chooseThreeCardSpread(scanner);
          if (spread == null) {
            System.out.println();
            continue;
          }
          break;
        case "3":
          spread = chooseFourCardSpread(scanner);
          if (spread == null) {
            System.out.println();
            continue;
          }
          break;
        case "4":
          spread = new RelationshipFourCardSpread();
          break;
        case "0":
          return new SpreadSelection(null, true);
        default:
          System.out.println("无效选项，请重新输入。");
          System.out.println();
          continue;
      }
      return new SpreadSelection(spread, false);
    }
  }

  private static Spread chooseFromSuggestions(Scanner scanner, List<SpreadSuggestion> suggestions) {
    System.out.println("根据你的问题，推荐以下牌阵：");
    for (int i = 0; i < suggestions.size(); i++) {
      SpreadSuggestion suggestion = suggestions.get(i);
      System.out.printf(
          " %d. %s —— %s%n", i + 1, suggestion.spread().getName(), suggestion.reason());
    }
    System.out.println(" 0. 都不合适，我想自己选择");
    System.out.println("请输入编号，默认1：");

    if (!scanner.hasNextLine()) {
      System.out.println("未检测到输入，默认选择第一个推荐。");
      return suggestions.get(0).spread();
    }
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) {
      return suggestions.get(0).spread();
    }
    if (input.equals("0")) {
      System.out.println("好的，你可以自行选择牌阵。");
      System.out.println();
      return null;
    }
    try {
      int index = Integer.parseInt(input);
      if (index >= 1 && index <= suggestions.size()) {
        return suggestions.get(index - 1).spread();
      }
    } catch (NumberFormatException ignored) {
      // ignore and fall back to default
    }
    System.out.println("输入无效，默认使用第一个推荐。");
    return suggestions.get(0).spread();
  }

  private static boolean askReversedEnabled(Scanner scanner) {
    System.out.println("是否启用逆位? (y/n 默认y): ");
    if (!scanner.hasNextLine()) {
      // 如果没有更多输入，保持默认设置（这里选择不启用逆位）
      System.out.println("未检测到输入，保持默认：不启用逆位。");
      return false;
    }
    String line = scanner.nextLine().trim();

    if (line.isEmpty()) {
      return true;
    }

    if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
      return false;
    }
    return true;
  }

  private static String promptUserQuestion(Scanner scanner) {
    System.out.println("在选择牌阵前，请简要描述你想问的问题（例如「近期工作机会如何？」、「我和TA的关系走向？」）。");
    System.out.println("请输入你的问题，直接回车则使用常规流程：");

    if (!scanner.hasNextLine()) {
      System.out.println("未检测到输入，程序结束。");
      return null;
    }

    String question = scanner.nextLine().trim();
    if (question.isEmpty()) {
      System.out.println("好的，将按常规流程选择牌阵。");
    } else {
      System.out.println("收到，你的问题是：「" + question + "」。");
    }
    System.out.println();
    return question;
  }

  private record SpreadSelection(Spread spread, boolean exitRequested) {}
}
