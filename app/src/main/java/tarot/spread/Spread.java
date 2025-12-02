package tarot.spread;

import java.util.List;

/**
 * 牌阵定义：
 * 名称
 * 需要抽取的牌数
 * 每个位置的标签
 */
public interface Spread {
    /**
     * 牌阵名称
     * @return
     */
    String getName();

    /**
     * 牌阵中的牌位信息，顺序即为抽牌顺序
     * @return
     */
    List<CardPosition> getPositions();

    /**
     * 此牌阵需要抽取的牌数，默认等于牌位数量
     * @return
     */
    default int getCardCount() {
        return getPositions().size();
    }


}
