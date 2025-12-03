package tarot.spread;

import java.util.List;

/**
 * 通用的「基于模板」牌阵实现。
 *
 * 通过组合 {@link SpreadPattern}，可以支持任意张数的牌阵，
 * 而无需为 3 张、4 张、N 张分别写不同的 Spread 实现类。
 */
public class PatternBasedSpread implements Spread {

    private final String namePrefix;
    private final SpreadPattern pattern;

    /**
     * @param namePrefix 牌阵名称前缀，例如「三张牌」「四张牌」「五张牌」
     * @param pattern    具体的位置模板
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

    @Override
    public List<CardPosition> getPositions() {
        return pattern.getPositions();
    }

    public SpreadPattern getPattern() {
        return pattern;
    }
}

