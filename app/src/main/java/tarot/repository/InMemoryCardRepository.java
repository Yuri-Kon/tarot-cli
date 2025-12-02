package tarot.repository;

import java.util.List;
import java.util.ArrayList;
import tarot.domain.TarotCard;
import tarot.domain.Arcana;
import tarot.domain.Suit;
public class InMemoryCardRepository implements CardRepository {

    @Override
    public List<TarotCard> findAll() {
        List<TarotCard> cards = new ArrayList<>();

        // 大阿尔卡纳示例
        cards.add(new TarotCard("MAJOR_00_FOOL", "愚者", Arcana.MAJOR, Suit.NONE, 0));
        cards.add(new TarotCard("MAJOR_01_MAGICIAN", "魔术师", Arcana.MAJOR, Suit.NONE, 1));
        cards.add(new TarotCard("MAJOR_02_HIGH_PRIESTESS", "女祭司", Arcana.MAJOR, Suit.NONE, 2));
        cards.add(new TarotCard("MAJOR_03_EMPRESS", "女皇", Arcana.MAJOR, Suit.NONE, 3));
        cards.add(new TarotCard("MAJOR_04_EMPEROR", "皇帝", Arcana.MAJOR, Suit.NONE, 4));
        cards.add(new TarotCard("MAJOR_05_HIPPIE", "教皇", Arcana.MAJOR, Suit.NONE, 5));
        cards.add(new TarotCard("MAJOR_06_LOVERS", "恋人", Arcana.MAJOR, Suit.NONE, 6));
        cards.add(new TarotCard("MAJOR_07_CHARIOT", "战车", Arcana.MAJOR, Suit.NONE, 7));
        cards.add(new TarotCard("MAJOR_08_STRENGTH", "力量", Arcana.MAJOR, Suit.NONE, 8));
        
        return cards;
    }
}
