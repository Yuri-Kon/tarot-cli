package tarot.spread;

import java.util.Arrays;
import java.util.List;

/**
 * 经典的三张牌牌阵，过去/现在/未来
 */
public class ThreeCardSpread implements Spread {

    @SuppressWarnings("null")
    private static final List<CardPosition> POSITIONS = Arrays.asList(
        new CardPosition("过去", "影响当前问题的过去经历与背景"),
        new CardPosition("现在", "当前正在发生的情况与核心问题"),
        new CardPosition("未来", "在当前轨迹下可能的发展趋势")
    );

   @Override
   public String getName() {
    return "三张牌牌阵：过去 / 现在 / 未来";
   } 

   @Override
   public List<CardPosition> getPositions() {
    return POSITIONS;
   }
   
}
