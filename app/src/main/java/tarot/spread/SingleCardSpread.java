package tarot.spread;

import java.util.Collections;
import java.util.List;

/**
 * 单张牌牌型：用于今日指引、单一问题提示
 */
public class SingleCardSpread implements Spread{
   
    @SuppressWarnings("null")
    private static final List<CardPosition> POSITIONS = Collections.singletonList(
        new CardPosition("主题", "对本次问题或当下状态的整体提示")
    );

    @Override
    public String getName() {
        return "单张牌：主题指引";
    }

    @Override
    public List<CardPosition> getPositions() {
        return POSITIONS;
    }
}
