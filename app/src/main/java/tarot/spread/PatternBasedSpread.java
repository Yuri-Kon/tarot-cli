package tarot.spread;

import java.util.List;

/**
 * 通用的「基于模板」牌阵实现。
 * <p>
 * 通过组合 {@link SpreadPattern}，可以支持任意张数的牌阵，
 * 而无需为 3 张、4 张、N 张分别写不同的 {@link Spread} 实现类。
 *
 * <h2>命名策略</h2>
 * <ul>
 *     <li>如果 {@code namePrefix} 为非空且非空白，则名称为
 *     「{@code namePrefix} + '：' + pattern.getDisplayName()」。</li>
 *     <li>否则直接返回 {@code pattern.getDisplayName()}。</li>
 * </ul>
 *
 * <h2>规约</h2>
 * <ul>
 *     <li>{@link #getPositions()} 直接委托给 {@link SpreadPattern#getPositions()}。</li>
 *     <li>调用方不应修改返回的列表；实现类不保证其可变性。</li>
 * </ul>
 */
public class PatternBasedSpread implements Spread {

    private final String namePrefix;
    private final SpreadPattern pattern;

    /**
     * 使用给定前缀和模板创建一个牌阵。
     *
     * @param namePrefix 牌阵名称前缀，例如「三张牌」「四张牌」「五张牌」，可为 {@code null}
     * @param pattern    具体的位置模板，不应为 {@code null}
     */
    public PatternBasedSpread(String namePrefix, SpreadPattern pattern) {
        this.namePrefix = namePrefix;
        this.pattern = pattern;
    }

    @Override
    public String getName() {
        if (namePrefix == null || namePrefix.isBlank()) {
            return pattern.getDisplayName();
        }
        return namePrefix + "：" + pattern.getDisplayName();
    }

    /**
     * 返回模板中定义的牌位列表，顺序即为抽牌顺序。
     */
    @Override
    public List<CardPosition> getPositions() {
        return pattern.getPositions();
    }

    /**
     * 返回内部使用的模板对象，便于上层在需要时查看具体配置。
     */
    public SpreadPattern getPattern() {
        return pattern;
    }
}
