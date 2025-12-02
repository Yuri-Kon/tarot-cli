package tarot.deck.factory;

import tarot.domain.Deck;

/**
 * 牌堆工厂接口
 * 用于创建一副标准塔罗牌牌堆，未来可以支持其他牌组
 */
public interface DeckFactory {
   
    /**
     * 创建一副标准的塔罗牌牌堆
     * @return
     */
    Deck createStandardDeck();
}
