package tarot.spread;

import java.util.Collections;
import java.util.List;

/**
 * 单张牌牌阵定义：用于「今日指引」或单一问题提示。
 * <p>
 * 特点：
 * <ul>
 * <li>永远只包含一个牌位：「主题」。</li>
 * <li>{@link #getCardCount()} 始终为 1。</li>
 * <li>主要用于快速抽取一张总体能量或主线信息。</li>
 * </ul>
 */
public class SingleCardSpread implements Spread {

  @SuppressWarnings("null")
  private static final List<CardPosition> POSITIONS = Collections.singletonList(
      new CardPosition("主题", "对本次问题或当下状态的整体提示"));

  /**
   * 牌阵名称，固定为「单张牌：主题指引」。
   */
  @Override
  public String getName() {
    return "单张牌：主题指引";
  }

  /**
   * 返回唯一的「主题」牌位。
   */
  @Override
  public List<CardPosition> getPositions() {
    return POSITIONS;
  }
}
