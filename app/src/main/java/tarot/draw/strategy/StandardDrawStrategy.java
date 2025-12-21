package tarot.draw.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tarot.domain.Deck;
import tarot.domain.DrawnCard;
import tarot.domain.TarotCard;

/**
 * 标准塔罗牌抽牌策略：
 * 1. 使用给定的Random对当前牌堆进行洗牌
 * 2. 依次从牌堆顶抽取count张牌
 * 3. 每张牌根据enableReversed决定是否可能为逆位
 */
public class StandardDrawStrategy implements DrawStrategy {

  @Override
  public List<DrawnCard> draw(Deck deck, int count, boolean enableReversed, Random random) {
    if (deck.size() < count) {
      throw new IllegalArgumentException("牌堆剩余牌不足，无法抽取 " + count + " 张牌");
    }

    /**
     * 洗牌
     */
    deck.shuffle(random);

    List<DrawnCard> result = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      TarotCard card = deck.drawTop();
      boolean reversed = enableReversed && random.nextBoolean();
      result.add(new DrawnCard(card, reversed));
    }

    return result;
  }
}
