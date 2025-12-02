package tarot.spread;

import java.util.Collections;
import java.util.List;
import java.time.Instant;
import tarot.domain.DrawnCard;

/**
 * 带牌阵信息的抽牌结果
 * 使用的Spread
 * 每个位置对应的一张牌
 */
public class SpreadResult {
   
    private final Spread spread;
    private final List<DrawnCard> drawnCards;
    private final Instant timestamp;

    public SpreadResult(Spread spread, List<DrawnCard> drawnCards, Instant timestamp) {
        if (drawnCards.size() != spread.getCardCount()) {
            throw new IllegalArgumentException("牌阵需要：" + spread.getCardCount() + "张牌, 但实际得到：" + drawnCards.size() + "张牌");
        }

        this.spread = spread;
        this.drawnCards = drawnCards;
        this.timestamp = timestamp;
    }


    public Spread getSpread() {
        return spread;
    }

    public List<DrawnCard> getDrawnCards() {
        return Collections.unmodifiableList(drawnCards);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("牌阵：")
        .append(spread.getName())
        .append(System.lineSeparator())
        .append("抽牌时间：")
        .append(System.lineSeparator())
        .append(timestamp)
        .append(System.lineSeparator());

        List<CardPosition> positions = spread.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            CardPosition position = positions.get(i);
            DrawnCard card = drawnCards.get(i);

            sb.append(" - ")
            .append(position.getLabel())
            .append(": ")
            .append(card.toString())
            .append(System.lineSeparator());
        }
        return sb.toString();
   }
}
