package tarot.domain;

/**
 * 单张塔罗牌的本体定义
 * 不包含正/逆位信息
 */
public class TarotCard {
   
    private final String id; // 唯一标识
    private final String name; // 名称
    private final Arcana arcana; // 大/小阿尔卡纳
    private final Suit suit; // 花色，大阿尔卡纳用Suit.None
    private final int number; // 序号：0-21大阿尔卡纳，1-14小阿尔卡纳

    public TarotCard(String id, String name, Arcana arcana, Suit suit, int number) {
        this.id = id;
        this.name = name;
        this.arcana = arcana;
        this.suit = suit;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Arcana getArcana() {
        return arcana;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "TarotCard{" + 
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", arcana=" + arcana +
                ", suit=" + suit +
                ", number=" + number +
                '}';
    }
}
