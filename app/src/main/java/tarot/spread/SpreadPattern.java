package tarot.spread;

import java.util.List;

/**
 * 牌阵位置模板定义。
 *
 * <p>
 * {@code SpreadPattern} 描述了一组「牌位含义的配置」，本身并不直接参与抽牌， 通常会被
 * {@link PatternBasedSpread} 等实现类组合使用：
 *
 * <ul>
 * <li>一个模板对应一个可展示的名称（如「过去 / 现在 / 未来」）。
 * <li>模板内部包含若干 {@link CardPosition}，依然遵循「顺序即为抽牌顺序」的约定。
 * </ul>
 *
 * <h2>典型用法</h2>
 *
 * <ul>
 * <li>通过枚举（如 {@code ThreeCardPattern}、{@code FourCardPattern}）列出常见模板。
 * <li>在 CLI 中使用 {@link #getDisplayName()} 展示给用户选择。
 * <li>被 {@link PatternBasedSpread} 组合成一个真正的 {@link Spread} 实例。
 * </ul>
 *
 * <h2>规约</h2>
 *
 * <ul>
 * <li>实现类应保证 {@link #getDisplayName()} 返回非空、适合展示的字符串。
 * <li>实现类应保证 {@link #getPositions()} 返回的列表非空且不包含 {@code null} 元素。
 * <li>列表顺序应与解读顺序一致，通常也与抽牌顺序一致。
 * <li>推荐返回不可变列表，调用方不应修改返回集合。
 * </ul>
 */
public interface SpreadPattern {

  /**
   * 模板展示名称，例如「过去 / 现在 / 未来」。
   *
   * @return 非空字符串，用于展示在 UI / CLI 菜单中
   */
  String getDisplayName();

  /**
   * 当前模板下的牌位定义列表。
   *
   * <p>
   * 索引与位置顺序一一对应，适合作为抽牌结果与位置含义的绑定依据。
   *
   * @return 非空列表，元素为非空 {@link CardPosition}
   */
  List<CardPosition> getPositions();
}
