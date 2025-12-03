package tarot.spread;

import java.util.List;

/**
 * 牌阵位置模板：
 * - 展示给用户看的名称
 * - 每个位置的含义列表
 *
 * 不同张数的牌阵模板（例如三张牌、四张牌等）都可以实现这个接口，
 * 以便通过统一的方式在 CLI 中列出和选择。
 */
public interface SpreadPattern {

    /**
     * 模板展示名称，例如「过去 / 现在 / 未来」
     */
    String getDisplayName();

    /**
     * 该模板下的牌位定义列表
     */
    List<CardPosition> getPositions();
}

