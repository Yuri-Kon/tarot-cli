package tarot.spread;

import java.util.Collections;
import java.util.List;
import java.time.Instant;
import tarot.domain.DrawnCard;

/**
 * 带牌阵信息的抽牌结果。
 * <p>
 * 该对象将一次占卜的关键信息聚合在一起：
 * <ul>
 *     <li>使用的 {@link Spread 牌阵结构}</li>
 *     <li>按牌位顺序排列的 {@link DrawnCard 抽到的牌}</li>
 *     <li>抽牌发生的时间戳 {@link #getTimestamp()}</li>
 * </ul>
 *
 * <h2>位置与牌的对应关系</h2>
 * <ul>
 *     <li>索引 {@code i} 处的 {@link #getDrawnCards()} 元素对应
 *     {@code spread.getPositions().get(i)} 所描述的牌位。</li>
 *     <li>构造函数会强制校验：{@code drawnCards.size() == spread.getCardCount()}，
 *     否则抛出 {@link IllegalArgumentException}。</li>
 * </ul>
 */
public class SpreadResult {

    private final Spread spread;
    private final List<DrawnCard> drawnCards;
    private final Instant timestamp;

    /**
     * 创建一次完整的牌阵抽牌结果。
     *
     * @param spread     使用的牌阵定义，不能为空
     * @param drawnCards 实际抽到的牌列表，顺序必须与 {@code spread.getPositions()} 对齐
     * @param timestamp  抽牌时间戳，通常由业务层在调用时传入
     * @throws IllegalArgumentException 当 {@code drawnCards.size() != spread.getCardCount()} 时抛出
     */
    public SpreadResult(Spread spread, List<DrawnCard> drawnCards, Instant timestamp) {
        if (drawnCards.size() != spread.getCardCount()) {
            throw new IllegalArgumentException("牌阵需要：" + spread.getCardCount() + "张牌, 但实际得到：" + drawnCards.size() + "张牌");
        }

        this.spread = spread;
        this.drawnCards = drawnCards;
        this.timestamp = timestamp;
    }

    /**
     * 返回本次结果所使用的牌阵结构。
     */
    public Spread getSpread() {
        return spread;
    }

    /**
     * 返回抽到的牌列表。
     * <p>
     * 列表为不可修改视图，索引与 {@code spread.getPositions()} 一一对应。
     *
     * @return 不可修改列表；企图修改将抛出 {@link java.lang.UnsupportedOperationException}
     */
    public List<DrawnCard> getDrawnCards() {
        return Collections.unmodifiableList(drawnCards);
    }

    /**
     * 抽牌的时间戳。
     *
     * @return 抽牌发生的时间
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * 以适合 CLI 输出的形式展示本次结果：
     * <ul>
     *     <li>牌阵名称</li>
     *     <li>抽牌时间</li>
     *     <li>每个位置的标签及对应的 {@link DrawnCard#toString()} 结果</li>
     * </ul>
     */
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
