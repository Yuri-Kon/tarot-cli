package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 固定结构的四张牌关系牌阵：
 * 「你 / 对方 / 关系走向 / 建议」。
 * <p>
 * 适用于聚焦两人关系（包括感情、人际、合作等）的整体状态与趋势分析。
 */
public class RelationshipFourCardSpread implements Spread{

    @SuppressWarnings("null")
    private static final List<CardPosition> POSITIONS = Arrays.asList(
        new CardPosition("你", "你在这段关系中的状态、立场与感受"),
        new CardPosition("对方", "对方在这段关系中的状态或态度"),
        new CardPosition("关系走向", "在当前互动模式下，关系可能的发展方向"),
        new CardPosition("建议", "对你更有帮助的态度和行动模式")
    );

    /**
     * 牌阵名称，固定为「四张牌：你 / 对方 / 关系走向 / 建议」。
     */
    @Override
    public String getName() {
        return "四张牌：你 / 对方 / 关系走向 / 建议";
    }

    /**
     * 返回固定的四个关系相关牌位，顺序即为解读顺序。
     */
    @Override
    public List<CardPosition> getPositions() {
        return POSITIONS;
    }
}
