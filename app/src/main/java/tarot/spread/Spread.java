package tarot.spread;

import java.util.List;

/**
 * 表示一次塔罗牌占卜中所使用的牌阵（Spread）。
 *
 * <p>
 * 一个牌阵通常由以下要素构成：
 *
 * <ul>
 * <li>一个便于展示与记录的<strong>名称</strong>；
 * <li>一组有固定语义的<strong>牌位</strong>（如「过去 / 现在 / 未来」等），顺序即为抽牌顺序；
 * <li>与牌位数量相对应的<strong>需抽取牌数</strong>。
 * </ul>
 *
 * 实现类只需要定义名称与牌位列表，默认的 {@link #getCardCount()} 会直接使用牌位数量。
 *
 * <p>
 * <strong>线程安全性</strong>：接口本身不对线程安全做任何保证，具体实现可选择不可变实现以便在多线程环境重用。
 *
 * @author Tarot
 * @since 1.0
 */
public interface Spread {

  /**
   * 返回该牌阵的名称。
   *
   * <p>
   * 该名称通常用于：
   *
   * <ul>
   * <li>命令行或 UI 展示（例如「三张牌——过去 / 现在 / 未来」）；
   * <li>日志或历史记录中的标识；
   * <li>对用户说明当前使用的是哪一种牌阵。
   * </ul>
   *
   * @return 非空的牌阵名称字符串，用于展示和标识牌阵
   */
  String getName();

  /**
   * 返回该牌阵中定义的所有牌位。
   *
   * <p>
   * 列表中元素的位置（index）即为实际抽牌顺序，例如：
   *
   * <pre>
   * index = 0 → 第一张抽出的牌（例如「过去」）
   * index = 1 → 第二张抽出的牌（例如「现在」）
   * index = 2 → 第三张抽出的牌（例如「未来」）
   * </pre>
   *
   * 调用方在执行抽牌逻辑时应按照该顺序逐一为每个 {@link CardPosition} 分配牌面。
   *
   * <p>
   * <strong>约定</strong>：
   *
   * <ul>
   * <li>实现类应返回一个按顺序排好、长度固定的列表；
   * <li>推荐返回不可变列表，避免调用方在运行时修改其内容；
   * <li>当列表为空时，表示该牌阵不需要抽牌（一般不推荐）。
   * </ul>
   *
   * @return 有序的牌位列表，顺序即为抽牌顺序；通常不应为 {@code null}
   */
  List<CardPosition> getPositions();

  /**
   * 返回该牌阵需要抽取的牌数。
   *
   * <p>
   * 默认实现直接使用 {@link #getPositions()} 的列表长度， 即「每个牌位正好对应一张要抽取的牌」。
   * 如需支持「多个牌位使用同一张牌」或「隐藏牌」等高级牌阵形式，
   * 实现类可以重写该方法以返回自定义的抽牌数量。
   *
   * @return 本次占卜需要抽取的牌数，默认等于牌位数量
   */
  default int getCardCount() {
    return getPositions().size();
  }
}
