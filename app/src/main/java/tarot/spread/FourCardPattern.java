package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 四张牌模板集合。
 * <p>
 * 每个枚举常量代表一个常见的四张牌解读结构，通过 {@link #getDisplayName()} 和
 * 对应的 {@link CardPosition} 列表来表达具体含义。
 * <p>
 * 这些模板通常与 {@link PatternBasedSpread} 搭配，在 CLI 中让用户选择。
 */
public enum FourCardPattern implements SpreadPattern {
    @SuppressWarnings("null")
    YOU_OTHER_RELATIONSHIP_ADVICE(
        "你 / 对方 / 关系 / 建议",
        Arrays.asList(
            new CardPosition("你", "你在这段关系中的、状态与立场"),
            new CardPosition("对方", "对方的感受、态度或潜在倾向"),
            new CardPosition("关系", "当前关系的氛围、互动模式或核心议题"),
            new CardPosition("建议", "这段关系的未来方向、可采取的行动或态度")
        )
    ),

    @SuppressWarnings("null")
    SITUATION_OBSTACLE_HELP_ADVICE(
        "现状 / 阻碍 / 阻力 / 建议",
        Arrays.asList(
            new CardPosition("现状", "当前客观情况或你所处的位置"),
            new CardPosition("阻碍", "挑战、矛盾、问题源头或阻力"),
            new CardPosition("助力", "你可以利用的优势、资源或隐藏支持"),
            new CardPosition("建议", "最佳行动方向、态度或可采取的策略")
        )
    ),

    @SuppressWarnings("null")
    PROBLEM_CAUSE_SOLUTION_OUTCOME(
        "问题 / 成因 / 解决方案 / 结果",
        Arrays.asList(
            new CardPosition("问题", "表面的困境、核心议题或正在发生的事件"),
            new CardPosition("原因", "产生问题的深层动因"),
            new CardPosition("解决方案", "可采取的行动、应对方式或转化思路"),
            new CardPosition("结果", "若采取方案后可能出现的走向或发展")
        )
    ),

    @SuppressWarnings("null")
    INNER_OUTER_BLOCK_ADVICE(
        "内在 / 外在 / 阻碍 /建议",
        Arrays.asList(
            new CardPosition("内在", "你的内在状态、情绪、心理或潜意识"),
            new CardPosition("外在", "外在环境、实际状况、人际交互"),
            new CardPosition("阻碍", "造成内外不一致的阻碍或关键矛盾"),
            new CardPosition("建议", "如何协调内外、整合力量或前进方向")
        )
    ),

    @SuppressWarnings("null")
    TIME_LINE(
        "过去 / 现在 / 未来 / 建议",
        Arrays.asList(
            new CardPosition("过去", "影响当前情况的重要历史因素或模式"),
            new CardPosition("现在", "当前事件的核心、此刻的关键能量"),
            new CardPosition("未来", "在当前轨迹下最可能的发展趋势"),
            new CardPosition("建议", "应采取何种方式应对未来或改变轨迹")
        )
    ),

    @SuppressWarnings("null")
    FIRE_WATER_AIR_EARTH(
        "火 / 水 / 风 / 土",
        Arrays.asList(
            new CardPosition("火", "动力、意志、行动力、热情"),
            new CardPosition("水", "情感、感受、人际关系、直觉"),
            new CardPosition("风", "思维、逻辑、学习与沟通"),
            new CardPosition("土", "物质、工作、稳定与现实基础")
        )
    );

    private final String displayName;
    private final List<CardPosition> positions;

    FourCardPattern(String displayName, List<CardPosition> positions) {
        this.displayName = displayName;
        this.positions = positions;
    }

    /**
     * 返回模板的展示名称，例如「你 / 对方 / 关系 / 建议」。
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 返回固定的四个位置定义，顺序即为抽牌与解读顺序。
     */
    @Override
    public List<CardPosition> getPositions() {
        return positions;
    }
}
