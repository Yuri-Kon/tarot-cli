package tarot.app;

import java.util.Random;

import tarot.deck.factory.DeckFactory;
import tarot.deck.factory.StandardTarotDeckFactory;
import tarot.draw.strategy.DrawStrategy;
import tarot.draw.strategy.StandardDrawStrategy;
import tarot.repository.CardRepository;
import tarot.repository.JsonCardRepository;
import tarot.service.TarotDrawService;
import tarot.spread.SingleCardSpread;
import tarot.spread.SpreadResult;
import tarot.spread.ThreeCardSpread;
import tarot.spread.ThreePlusOneAdviceSpread;

/**
 * 简单的命令行演示
 * 初始化服务->抽3张牌->打印结果
 */
public class ConsoleDemo {
    public static void main(String[] args) {

        CardRepository repository = new JsonCardRepository();
        DeckFactory factory = new StandardTarotDeckFactory(repository);
        DrawStrategy strategy = new StandardDrawStrategy();
        Random random = new Random();

        TarotDrawService service = new TarotDrawService(factory, strategy, random);


        // 单张牌
        System.out.println("=== 单张牌 ===");
        SingleCardSpread spread = new SingleCardSpread();
        SpreadResult singleResult = service.draw(spread, true);
        System.out.println(singleResult);
        System.out.println();

        // 三张牌：过去+未来+现在
        System.out.println("=== 三张牌：过去+未来+现在 ===");
        ThreeCardSpread threeSpreed = new ThreeCardSpread();
        SpreadResult threeResult = service.draw(threeSpreed, true);
        System.out.println(threeResult);
        System.out.println();

        // 三张+一张：过去+未来+现在+建议
        System.out.println("=== 三张+一张：过去+未来+现在+建议 ===");
        ThreePlusOneAdviceSpread threePlusOneSpread = new ThreePlusOneAdviceSpread();
        SpreadResult threePlusOneResult = service.draw(threePlusOneSpread, true);
        System.out.println(threePlusOneResult);
        System.out.println();
    }
}
