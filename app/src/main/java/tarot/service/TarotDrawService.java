package tarot.service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import tarot.deck.factory.DeckFactory;
import tarot.domain.Deck;
import tarot.domain.DrawResult;
import tarot.domain.DrawnCard;
import tarot.draw.strategy.DrawStrategy;
import tarot.spread.Spread;
import tarot.spread.SpreadResult;

/**
 * 塔罗牌抽牌服务，对外提供简单的抽牌接口
 * 内部使用DeckFactory创建牌堆，使用DrawStrategy进行抽牌
 */
public class TarotDrawService {

  private final DeckFactory deckFactory;
  private final DrawStrategy drawStrategy;
  private final Random random;

  /**
   * 当前使用中的牌堆
   */
  private Deck currentDeck;

  public TarotDrawService(DeckFactory deckFactory, DrawStrategy drawStrategy, Random random) {
    this.deckFactory = deckFactory;
    this.drawStrategy = drawStrategy;
    this.random = random;
    this.currentDeck = deckFactory.createStandardDeck();
  }

  /**
   * 从当前牌堆中抽取指定数量的牌
   * 
   * @param count          抽牌数量
   * @param enableReversed 是否启用逆位
   * @return 抽牌结果
   */
  public DrawResult draw(int count, boolean enableReversed) {
    List<DrawnCard> drawn = drawStrategy.draw(currentDeck, count, enableReversed, random);
    return new DrawResult(drawn, Instant.now());
  }

  /**
   * 使用给定牌阵进行抽牌
   * 
   * @param spread         牌阵定义，决定需要的牌数与每个位置的含义
   * @param enableReversed 是否启用逆位
   * @return 带牌阵信息的抽牌结果
   */
  public SpreadResult draw(Spread spread, boolean enableReversed) {

    int count = spread.getCardCount();
    List<DrawnCard> drawn = drawStrategy.draw(currentDeck, count, enableReversed, random);
    return new SpreadResult(spread, drawn, Instant.now());
  }

  /**
   * 重置牌堆，重新洗一副标准塔罗牌
   */
  public void resetDeck() {
    this.currentDeck = deckFactory.createStandardDeck();
  }
}
