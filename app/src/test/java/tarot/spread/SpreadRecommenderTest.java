package tarot.spread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class SpreadRecommenderTest {

    @Test
    void shouldRecommendRelationshipSpreadsWhenQuestionMentionsPartner() {
        List<SpreadRecommender.SpreadSuggestion> suggestions = SpreadRecommender.recommend("我和她的感情走向？");

        assertFalse(suggestions.isEmpty());
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
        assertTrue(suggestions.stream().anyMatch(s -> s.spread().getName().contains("阻碍")
                || s.spread().getName().contains("问题")));
    }
}
