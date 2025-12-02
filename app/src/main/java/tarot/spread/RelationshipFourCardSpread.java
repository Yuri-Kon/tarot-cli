package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 四张牌：你 / 对方 / 关系走向 / 建议
 */
public class RelationshipFourCardSpread implements Spread{
   
    @SuppressWarnings("null")
    private static final List<CardPosition> POSITIONS = Arrays.asList(
        new CardPosition("你", "你在这段关系中的状态、立场与感受"),
        new CardPosition("对方", "对方在这段关系中的状态或态度"),
        new CardPosition("关系走向", "在当前互动模式下，关系可能的发展方向"),
        new CardPosition("建议", "对你更有帮助的态度和行动模式")
    );

    @Override
    public String getName() {
        return "四张牌：你 / 对方 / 关系走向 / 建议";
    }

    @Override
    public List<CardPosition> getPositions() {
        return POSITIONS;
    }
}
