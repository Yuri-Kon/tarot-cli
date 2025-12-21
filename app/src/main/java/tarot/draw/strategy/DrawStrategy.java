package tarot.draw.strategy;

import java.util.List;
import java.util.Random;
import tarot.domain.Deck;
import tarot.domain.DrawnCard;

/**
 * 抽牌策略接口
 * 不同实现可以用不同抽取方式
 */
public interface DrawStrategy {

  /**
   * 从给定牌堆中抽取制定数量的牌
   * 
   * @param deck           当前的牌堆
   * @param count          需要抽取的数量
   * @param enableReversed 是否启用逆位
   * @param random         随机数生成器
   * @return 抽到的牌列表(包含正/逆位信息)
   */
  List<DrawnCard> draw(Deck deck, int count, boolean enableReversed, Random random);
}
