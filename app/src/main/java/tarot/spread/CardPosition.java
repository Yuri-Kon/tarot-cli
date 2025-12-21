package tarot.spread;

/**
 * 牌阵中的一个具体位置定义。
 * <p>
 * 每个 {@code CardPosition} 由两部分组成：
 * <ul>
 * <li>{@link #label 简短标签}：用于列表展示、结果输出（如「过去」「现在」「建议」）。</li>
 * <li>{@link #description 详细说明}：用于解释该位置在解牌时侧重的含义。</li>
 * </ul>
 *
 * <h2>规约</h2>
 * <ul>
 * <li>构造函数期望传入的 {@code label} 与 {@code description} 为非空业务值；
 * 当前实现不做运行时校验，由上层配置保证语义正确。</li>
 * <li>{@link #toString()} 仅返回 {@link #label}，便于在日志或简单输出中使用。</li>
 * </ul>
 */
public class CardPosition {

  private final String label;
  private final String description;

  /**
   * 创建一个新的牌位定义。
   *
   * @param label       简短标签，例如「过去」「现在」「未来」
   * @param description 该牌位在解读中的详细意义说明
   */
  public CardPosition(String label, String description) {
    this.label = label;
    this.description = description;
  }

  /**
   * 牌位标签，例如「过去」「现在」「未来」。
   *
   * @return 用于简单展示的短文本
   */
  public String getLabel() {
    return label;
  }

  /**
   * 牌位详细说明，用于辅助解读。
   *
   * @return 更长、更具体的含义描述
   */
  public String getDescription() {
    return description;
  }

  /**
   * 返回 {@link #label}，便于简洁打印。
   */
  @Override
  public String toString() {
    return label;
  }
}
