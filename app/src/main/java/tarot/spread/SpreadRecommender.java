package tarot.spread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 根据用户问题推荐合适的牌阵。
 * <p>
 * 当前实现基于关键字匹配的规则表，保持纯离线且可扩展：
 * <ul>
 *     <li>每条规则包含一组关键词与对应的推荐牌阵列表；</li>
 *     <li>匹配成功后按配置顺序追加推荐，避免名称重复；</li>
 *     <li>若无规则命中，则返回一组通用的默认推荐。</li>
 * </ul>
 * 如需接入更复杂的意图解析（例如模型服务），可在 {@link #recommend(String)} 内替换或追加策略。
 */
public final class SpreadRecommender {

    private SpreadRecommender() {
    }

    private static final List<SpreadSuggestion> DEFAULT_SUGGESTIONS = List.of(
        suggestion(new SingleCardSpread(), "适合开放式问题的整体提示"),
        suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.PAST_PRESENT_FUTURE), "基础时间线解读，帮助观察趋势")
    );

    @SuppressWarnings("null")
    private static final List<KeywordRule> KEYWORD_RULES = List.of(
        new KeywordRule(List.of("关系", "感情", "恋爱", "爱情", "暧昧", "对象", "伴侣", "婚姻", "情侣", "他", "她", "ta", "合作", "搭档"),
            List.of(
                suggestion(new RelationshipFourCardSpread(), "围绕你 / 对方 / 关系 / 建议的结构化解读"),
                suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.YOU_RELATIONSHIP_OTHER), "快速对照你 / 关系 / 对方的状态"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.YOU_OTHER_RELATIONSHIP_ADVICE), "关注双方立场与关系走向")
            )
        ),
        new KeywordRule(List.of("工作", "事业", "职业", "老板", "同事", "升职", "加薪", "面试", "offer", "项目", "团队", "合同"),
            List.of(
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.PROBLEM_CAUSE_SOLUTION_OUTCOME), "拆解问题成因、方案与可能结果"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.SITUATION_OBSTACLE_HELP_ADVICE), "看清现状、阻碍、助力与建议")
            )
        ),
        new KeywordRule(List.of("学业", "考试", "学习", "复习", "研究", "考研", "论文"),
            List.of(
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.SITUATION_OBSTACLE_HELP_ADVICE), "聚焦现状、阻碍、助力与建议的学习节奏"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.PROBLEM_CAUSE_SOLUTION_OUTCOME), "明确瓶颈、行动方案与可能结果")
            )
        ),
        new KeywordRule(List.of("财务", "金钱", "财富", "收入", "投资", "理财", "借贷", "账", "花销", "钱"),
            List.of(
                suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.PAST_PRESENT_FUTURE), "梳理财务状态的时间线"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.SITUATION_OBSTACLE_HELP_ADVICE), "识别影响财务的阻力与可用资源")
            )
        ),
        new KeywordRule(List.of("未来", "走向", "趋势", "发展", "前景", "接下来", "之后"),
            List.of(
                suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.PAST_PRESENT_FUTURE), "沿时间线梳理过去 / 现在 / 未来"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.TIME_LINE), "在时间线基础上增加建议位")
            )
        ),
        new KeywordRule(List.of("选择", "决定", "怎么办", "如何", "怎么做", "要不要", "是否", "方案", "方向", "建议"),
            List.of(
                suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.SITUATION_OBSTACLE_ADVICE), "当下 / 困境 / 建议的快速洞察"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.PROBLEM_CAUSE_SOLUTION_OUTCOME), "评估问题成因、可行方案及可能结果")
            )
        ),
        new KeywordRule(List.of("阻碍", "困难", "阻力", "挑战", "瓶颈", "问题", "困境", "压力", "卡住", "障碍"),
            List.of(
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.SITUATION_OBSTACLE_HELP_ADVICE), "定位阻碍、助力并给出建议"),
                suggestion(new PatternBasedSpread("四张牌", FourCardPattern.PROBLEM_CAUSE_SOLUTION_OUTCOME), "拆解问题、定位原因与解决路径")
            )
        )
    );

    /**
     * 基于用户输入问题返回推荐牌阵列表。
     *
     * @param question 用户输入的问题，允许为空或空白
     * @return 非空列表，包含按优先级排序的推荐结果
     */
    public static List<SpreadSuggestion> recommend(String question) {
        String normalized = normalize(question);
        List<SpreadSuggestion> results = new ArrayList<>();

        for (KeywordRule rule : KEYWORD_RULES) {
            if (rule.matches(normalized)) {
                addSuggestions(results, rule.suggestions());
            }
        }

        if (results.isEmpty()) {
            addSuggestions(results, DEFAULT_SUGGESTIONS);
        }

        return Collections.unmodifiableList(results);
    }

    private static void addSuggestions(List<SpreadSuggestion> target, List<SpreadSuggestion> candidates) {
        for (SpreadSuggestion candidate : candidates) {
            boolean exists = target.stream()
                .anyMatch(it -> it.spread().getName().equals(candidate.spread().getName()));
            if (!exists) {
                target.add(candidate);
            }
        }
    }

    private static SpreadSuggestion suggestion(Spread spread, String reason) {
        return new SpreadSuggestion(spread, reason);
    }

    private static String normalize(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT);
    }

    private record KeywordRule(List<String> keywords, List<SpreadSuggestion> suggestions) {

        boolean matches(String text) {
            if (text.isEmpty()) {
                return false;
            }
            for (String keyword : keywords) {
                if (text.contains(keyword.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 推荐结果，包含具体牌阵与推荐理由。
     */
    public record SpreadSuggestion(Spread spread, String reason) {
    }
}
