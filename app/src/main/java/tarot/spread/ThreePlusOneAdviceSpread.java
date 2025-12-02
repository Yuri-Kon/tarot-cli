package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 三张 + 一张牌
 * 过去 / 现在 / 未来 /建议
 * 
 * 常见用法：
 * - 前三张给出时间线上的故事
 * - 第四张给出综合性的行动建议或指引
 */
public class ThreePlusOneAdviceSpread implements Spread{
   
    @SuppressWarnings("null")
    private static final List<CardPosition> POSITIONS = Arrays.asList(
        new CardPosition("过去", "影响当前问题的过去经历与模式"),
        new CardPosition("现在", "当前状态与你正在面对的核心问题"),
        new CardPosition("未来", "在当前轨迹下较可能的发展趋势"),
        new CardPosition("建议", "综合考虑上述三张牌后的行动建议或态度指引")   
    );


    @Override
    public String getName() {
        return " 三张 + 一张：过去 / 现在 / 未来 / 建议";
    }

    @Override
    public List<CardPosition> getPositions() {
        return POSITIONS;
    }
}
