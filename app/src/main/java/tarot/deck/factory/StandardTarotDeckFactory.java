package tarot.deck.factory;

import java.util.List;
import tarot.domain.Deck;
import tarot.domain.TarotCard;
import tarot.repository.CardRepository;

/** 使用CardRepository提供的数据构建一副标准的塔罗牌牌堆 */
public class StandardTarotDeckFactory implements DeckFactory {

  private final CardRepository cardRepository;

  public StandardTarotDeckFactory(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public Deck createStandardDeck() {
    List<TarotCard> cards = cardRepository.findAll();
    return new Deck(cards);
  }
}
