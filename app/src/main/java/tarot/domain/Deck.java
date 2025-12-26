package tarot.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** 牌堆：代表当前可用的塔罗牌集合 */
public class Deck {

  private final List<TarotCard> cards;

  public Deck(List<TarotCard> cards) {
    this.cards = new ArrayList<>(cards);
  }

  /**
   * 返回当前牌的只读列表视图
   *
   * @return
   */
  public List<TarotCard> getCards() {
    return Collections.unmodifiableList(cards);
  }

  /**
   * 当前剩余牌数
   *
   * @return
   */
  public int size() {
    return cards.size();
  }

  /**
   * 使用给定的Random对牌堆进行洗牌
   *
   * @param random
   */
  public void shuffle(Random random) {
    Collections.shuffle(cards, random);
  }

  /**
   * 从牌堆顶移除一张牌并返回
   *
   * @return
   */
  public TarotCard drawTop() {

    if (cards.isEmpty()) {
      throw new IllegalStateException("牌堆已空，无法再抽牌");
    }
    return cards.remove(0);
  }

  /**
   * 是否为空
   *
   * @return
   */
  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
