package tarot.app;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import tarot.deck.factory.DeckFactory;
import tarot.deck.factory.StandardTarotDeckFactory;
import tarot.draw.strategy.DrawStrategy;
import tarot.draw.strategy.StandardDrawStrategy;
import tarot.repository.CardRepository;
import tarot.repository.JsonCardRepository;
import tarot.service.TarotDrawService;
import tarot.spread.RelationshipFourCardSpread;
import tarot.spread.SingleCardSpread;
import tarot.spread.Spread;
import tarot.spread.ThreeCardSpread;
import tarot.spread.ThreePlusOneAdviceSpread;

/**
 * 简单命令行交互的塔罗牌程序
 * 功能：
 *  - 让用户选择牌阵类型
 *  - 选择是否启动逆位
 *  - 显示抽牌结果
 */
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
                printMenu();

                System.out.println("请输入选项编号：");
                if (!scanner.hasNextLine()) {
                    System.out.println("未检测到输入，程序结束。");
                    break;
                }
                String choice = scanner.nextLine().trim();

                Spread spread;
                switch (choice) {
                    case "1":
                        spread = new SingleCardSpread();
                        break;
                    case "2":
                        spread = new ThreeCardSpread();
                        break;
                    case "3":
                        spread = new ThreePlusOneAdviceSpread();
                        break;
                    case "4":
                        spread = new RelationshipFourCardSpread();
                        break;
                    case "0":
                        running = false;
                        System.out.println("已退出程序，再见");
                        continue;
                    default:
                        System.out.println("无效选项，请重新输入。");
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
        System.out.println(" 2. 三张牌：过去 / 现在 / 未来");
        System.out.println(" 3. 三加一：过去 / 现在 / 未来 + 建议");
        System.out.println(" 4. 四张牌：你 / 对方 / 关系走向 / 建议");
        System.out.println(" 0. 退出程序");
        System.out.println();
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
}
