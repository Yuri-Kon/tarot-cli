package tarot.spread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据用户问题推荐合适的牌阵。
 * <p>
 * 当前实现基于牌阵描述文本的轻量级近似匹配，保持纯离线且可扩展：
 * <ul>
 * <li>每个牌阵根据名称与牌位描述生成“语义画像”；</li>
 * <li>将用户问题与画像做字符 n-gram 相似度对比；</li>
 * <li>按相似度排序并返回前若干个结果；</li>
 * <li>若输入为空或相似度过低，则返回默认推荐。</li>
 * </ul>
 * 如需接入更复杂的意图解析（例如模型服务），可在 {@link #recommend(String)} 内替换或追加策略。
 */
public final class SpreadRecommender {

  private static final int MAX_SUGGESTIONS = 3;
  private static final double MIN_SCORE = 0.05;

  private SpreadRecommender() {
  }

  private static final List<SpreadSuggestion> DEFAULT_SUGGESTIONS = List.of(
      suggestion(new SingleCardSpread(), "适合开放式问题的整体提示"),
      suggestion(new PatternBasedSpread("三张牌", ThreeCardPattern.PAST_PRESENT_FUTURE), "基础时间线解读，帮助观察趋势"));

  private static final Pattern TERM_PATTERN = Pattern.compile("(?i)[a-z0-9]+|\\p{IsHan}+");

  private static final Map<String, String> CANONICAL_TERMS = Map.ofEntries(
      Map.entry("感情", "关系"),
      Map.entry("恋爱", "关系"),
      Map.entry("爱情", "关系"),
      Map.entry("婚姻", "关系"),
      Map.entry("伴侣", "关系"),
      Map.entry("对象", "关系"),
      Map.entry("情侣", "关系"),
      Map.entry("暧昧", "关系"),
      Map.entry("合作", "关系"),
      Map.entry("搭档", "关系"),
      Map.entry("走向", "未来"),
      Map.entry("趋势", "未来"),
      Map.entry("发展", "未来"),
      Map.entry("前景", "未来"),
      Map.entry("接下来", "未来"),
      Map.entry("之后", "未来"),
      Map.entry("怎么办", "建议"),
      Map.entry("如何", "建议"),
      Map.entry("怎么", "建议"),
      Map.entry("要不要", "建议"),
      Map.entry("是否", "建议"),
      Map.entry("方案", "建议"),
      Map.entry("方向", "建议"),
      Map.entry("选择", "建议"),
      Map.entry("决定", "建议"),
      Map.entry("困境", "阻碍"),
      Map.entry("困难", "阻碍"),
      Map.entry("瓶颈", "阻碍"),
      Map.entry("压力", "阻碍"),
      Map.entry("挑战", "阻碍"),
      Map.entry("障碍", "阻碍"),
      Map.entry("问题", "阻碍"),
      Map.entry("阻力", "阻碍"),
      Map.entry("成因", "原因"),
      Map.entry("结局", "结果"));

  private static final List<SpreadProfile> PROFILES = buildProfiles();

  /**
   * 基于用户输入问题返回推荐牌阵列表。
   *
   * @param question 用户输入的问题，允许为空或空白
   * @return 非空列表，包含按优先级排序的推荐结果
   */
  public static List<SpreadSuggestion> recommend(String question) {
    String normalized = normalize(question);
    if (normalized.isBlank()) {
      return Collections.unmodifiableList(DEFAULT_SUGGESTIONS);
    }

    TokenVector queryVector = TokenVector.fromText(normalized);
    if (queryVector.isEmpty()) {
      return Collections.unmodifiableList(DEFAULT_SUGGESTIONS);
    }

    List<ScoredSuggestion> scored = new ArrayList<>();
    for (SpreadProfile profile : PROFILES) {
      double score = queryVector.cosineSimilarity(profile.vector());
      if (score >= MIN_SCORE) {
        scored.add(new ScoredSuggestion(profile, score));
      }
    }

    if (scored.isEmpty()) {
      return Collections.unmodifiableList(DEFAULT_SUGGESTIONS);
    }

    scored.sort(Comparator.comparingDouble(ScoredSuggestion::score).reversed()
        .thenComparing(scoredSuggestion -> scoredSuggestion.profile().spread().getCardCount())
        .thenComparing(scoredSuggestion -> scoredSuggestion.profile().spread().getName()));

    List<SpreadSuggestion> results = new ArrayList<>();
    for (ScoredSuggestion scoredSuggestion : scored) {
      addSuggestion(results, scoredSuggestion.profile());
      if (results.size() >= MAX_SUGGESTIONS) {
        break;
      }
    }

    return Collections.unmodifiableList(results);
  }

  private static SpreadSuggestion suggestion(Spread spread, String reason) {
    return new SpreadSuggestion(spread, reason);
  }

  private static List<SpreadProfile> buildProfiles() {
    List<SpreadProfile> profiles = new ArrayList<>();
    profiles.add(buildProfile(new SingleCardSpread()));
    profiles.add(buildProfile(new RelationshipFourCardSpread()));
    for (ThreeCardPattern pattern : ThreeCardPattern.values()) {
      profiles.add(buildProfile(new PatternBasedSpread("三张牌", pattern)));
    }
    for (FourCardPattern pattern : FourCardPattern.values()) {
      profiles.add(buildProfile(new PatternBasedSpread("四张牌", pattern)));
    }
    return List.copyOf(profiles);
  }

  private static SpreadProfile buildProfile(Spread spread) {
    String profileText = buildProfileText(spread);
    TokenVector vector = TokenVector.fromText(profileText);
    String reason = buildReason(spread);
    return new SpreadProfile(spread, vector, reason);
  }

  private static String buildProfileText(Spread spread) {
    StringBuilder builder = new StringBuilder(spread.getName());
    for (CardPosition position : spread.getPositions()) {
      builder.append(' ').append(position.getLabel());
      String description = position.getDescription();
      if (description != null && !description.isBlank()) {
        builder.append(' ').append(description);
      }
    }
    return builder.toString();
  }

  private static String buildReason(Spread spread) {
    List<CardPosition> positions = spread.getPositions();
    if (positions.isEmpty()) {
      return "与你的问题描述相近";
    }
    StringBuilder builder = new StringBuilder("牌位侧重：");
    for (int i = 0; i < positions.size(); i++) {
      if (i > 0) {
        builder.append(" / ");
      }
      builder.append(positions.get(i).getLabel());
    }
    return builder.toString();
  }

  private static void addSuggestion(List<SpreadSuggestion> target, SpreadProfile profile) {
    boolean exists = target.stream()
        .anyMatch(it -> it.spread().getName().equals(profile.spread().getName()));
    if (!exists) {
      target.add(new SpreadSuggestion(profile.spread(), profile.reason()));
    }
  }

  private static String normalize(String text) {
    return text == null ? "" : text.toLowerCase(Locale.ROOT);
  }

  private static String normalizeToken(String token) {
    String canonical = CANONICAL_TERMS.get(token);
    return canonical == null ? token : canonical;
  }

  private static void addNormalizedToken(Map<String, Integer> counts, String token) {
    String normalized = normalizeToken(token);
    increment(counts, normalized);
    if (!normalized.equals(token)) {
      increment(counts, token);
    }
  }

  private static void increment(Map<String, Integer> counts, String token) {
    counts.merge(token, 1, Integer::sum);
  }

  private static void addTokens(Map<String, Integer> counts, String term) {
    if (term.isBlank()) {
      return;
    }
    if (isAsciiTerm(term)) {
      addNormalizedToken(counts, term);
      return;
    }
    int length = term.length();
    if (length == 1) {
      addNormalizedToken(counts, term);
      return;
    }
    for (int size = 2; size <= 3; size++) {
      if (length < size) {
        continue;
      }
      for (int i = 0; i <= length - size; i++) {
        addNormalizedToken(counts, term.substring(i, i + size));
      }
    }
  }

  private static boolean isAsciiTerm(String term) {
    for (int i = 0; i < term.length(); i++) {
      if (term.charAt(i) > 0x7f) {
        return false;
      }
    }
    return true;
  }

  private static final class TokenVector {

    private final Map<String, Integer> counts;
    private final double magnitude;

    private TokenVector(Map<String, Integer> counts) {
      this.counts = counts;
      this.magnitude = Math.sqrt(counts.values().stream()
          .mapToDouble(value -> value * value)
          .sum());
    }

    private boolean isEmpty() {
      return counts.isEmpty();
    }

    private double cosineSimilarity(TokenVector other) {
      if (magnitude == 0 || other.magnitude == 0) {
        return 0;
      }
      Map<String, Integer> smaller = counts.size() <= other.counts.size() ? counts : other.counts;
      Map<String, Integer> larger = smaller == counts ? other.counts : counts;
      double dot = 0;
      for (Map.Entry<String, Integer> entry : smaller.entrySet()) {
        Integer otherValue = larger.get(entry.getKey());
        if (otherValue != null) {
          dot += entry.getValue() * otherValue;
        }
      }
      return dot / (magnitude * other.magnitude);
    }

    private static TokenVector fromText(String text) {
      if (text == null || text.isBlank()) {
        return new TokenVector(Collections.emptyMap());
      }
      Map<String, Integer> counts = new HashMap<>();
      Matcher matcher = TERM_PATTERN.matcher(text);
      while (matcher.find()) {
        String term = matcher.group().toLowerCase(Locale.ROOT);
        addTokens(counts, term);
      }
      return new TokenVector(counts);
    }
  }

  private record SpreadProfile(Spread spread, TokenVector vector, String reason) {
  }

  private record ScoredSuggestion(SpreadProfile profile, double score) {
  }

  /**
   * 推荐结果，包含具体牌阵与推荐理由。
   */
  public record SpreadSuggestion(Spread spread, String reason) {
  }
}
