package tarot.domain;

/** 抽牌结果中的一张牌，包括本体+是否逆位 */
public class DrawnCard {

  private final TarotCard card;
  private final boolean reversed;

  public DrawnCard(TarotCard card, boolean reversed) {
    this.card = card;
    this.reversed = reversed;
  }

  public TarotCard getCard() {
    return card;
  }

  public boolean isReversed() {
    return reversed;
  }

  @Override
  public String toString() {
    return card.getName() + (reversed ? "(逆位)" : "(正位)");
  }
}
