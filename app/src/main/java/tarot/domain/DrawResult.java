package tarot.domain;

import java.util.List;
import java.util.Collections;
import java.time.Instant;
/**
 * 一次抽牌的结果
 */
public class DrawResult {
   private final List<DrawnCard> drawnCards;
   private final Instant timestamp;

   public DrawResult(List<DrawnCard> drawnCards, Instant timestamp) {
    this.drawnCards = drawnCards;
    this.timestamp = timestamp;
   }

   public List<DrawnCard> getDrawnCards() {
    return Collections.unmodifiableList(drawnCards);
   }

   public Instant getTimeStamp() {
    return timestamp;
   }

   @Override
   public String toString() {
    StringBuilder sb = new StringBuilder("抽牌时间：")
        .append(timestamp)
        .append(System.lineSeparator())
        .append("抽到的牌：")
        .append(System.lineSeparator());
    for (DrawnCard dc : drawnCards) {
        sb.append(" - ").append(dc.toString()).append(System.lineSeparator());
    }
    return sb.toString();
   }
}
