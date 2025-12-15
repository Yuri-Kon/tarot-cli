package tarot.spread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class SpreadRecommenderTest {

    @Test
    void shouldRecommendRelationshipSpreadsWhenQuestionMentionsPartner() {
        List<SpreadRecommender.SpreadSuggestion> suggestions = SpreadRecommender.recommend("我和她的关系走向？");

        assertFalse(suggestions.isEmpty());
        assertEquals("四张牌：你 / 对方 / 关系走向 / 建议", suggestions.get(0).spread().getName());
        assertTrue(suggestions.stream().anyMatch(s -> s.spread().getName().contains("关系")));
    }

    @Test
    void shouldFallbackToDefaultWhenNoKeywords() {
        List<SpreadRecommender.SpreadSuggestion> suggestions = SpreadRecommender.recommend("");

        assertEquals(2, suggestions.size());
        assertEquals("单张牌：主题指引", suggestions.get(0).spread().getName());
    }

    @Test
    void shouldHandleDecisionOrWorkQuestions() {
        List<SpreadRecommender.SpreadSuggestion> suggestions = SpreadRecommender.recommend("工作遇到瓶颈怎么办");

        assertFalse(suggestions.isEmpty());
        assertEquals("四张牌：问题 / 成因 / 解决方案 / 结果", suggestions.get(0).spread().getName());
        assertTrue(suggestions.stream().anyMatch(s -> s.spread().getName().contains("阻碍")));
    }
}
