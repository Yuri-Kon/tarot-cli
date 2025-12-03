package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 三张牌的标准解读模板集合
 * 这里是位置含义的配置
 */
public enum ThreeCardPattern implements SpreadPattern {

    @SuppressWarnings("null")
    PAST_PRESENT_FUTURE(
        "过去 / 现在 / 未来",
        Arrays.asList(
            new CardPosition("过去", "影响当前问题的过去经历与背景"),
            new CardPosition("现在", "当前正在发生的情况与核心问题"),
            new CardPosition("未来", "在当前轨迹下可能的发展趋势")
        )
    ),

    @SuppressWarnings("null")
    SITUATION_OBSTACLE_ADVICE(
        "现状 / 困境 /建议",
        Arrays.asList(
            new CardPosition("现状", "你当前所处的状态和客观情况"),
            new CardPosition("困境", "阻碍、矛盾、压力或需要看见的问题"),
            new CardPosition("建议", "对你更有帮助的态度、方向或行动建议")
        )
    ),

    @SuppressWarnings("null")
    YOU_RELATIONSHIP_OTHER(
        "你 / 关系 / 对方",
        Arrays.asList(
            new CardPosition("你", "你在这段关系中的状态、立场与感受"),
            new CardPosition("关系", "当前互动模式与关系氛围"),
            new CardPosition("对方", "对方当前的状态、立场或倾向")
        )
    );

    private final String displayName;
    private final List<CardPosition> positions;

    ThreeCardPattern(String displayName, List<CardPosition> positions) {
        this.displayName = displayName;
        this.positions = positions;
    }

    /**
     * 给用户展示用的名称
     * @return
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<CardPosition> getPositions() {
        return positions;
    }
}
