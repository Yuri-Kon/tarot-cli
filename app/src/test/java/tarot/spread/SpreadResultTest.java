package tarot.spread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import tarot.domain.Arcana;
import tarot.domain.DrawnCard;
import tarot.domain.Suit;
import tarot.domain.TarotCard;

class SpreadResultTest {

    @Test
    void shouldCreateSpreadResultWhenCardCountMatchesSpread() {
        Spread spread = new SingleCardSpread();
        DrawnCard drawn = new DrawnCard(sampleCard("fool"), false);
        Instant now = Instant.now();

        SpreadResult result = new SpreadResult(spread, List.of(drawn), now);

        assertEquals(spread, result.getSpread());
        assertEquals(List.of(drawn), result.getDrawnCards());
        String output = result.toString();
        assertTrue(output.contains("主题"));
        assertTrue(output.contains("愚者"));
    }

    @Test
    void shouldThrowWhenCardCountDoesNotMatchSpread() {
        Spread spread = new SingleCardSpread();
        DrawnCard drawn = new DrawnCard(sampleCard("magician"), false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new SpreadResult(spread, List.of(drawn, drawn), Instant.EPOCH));

        assertTrue(ex.getMessage().contains("牌阵需要"));
    }

    @Test
    void drawnCardsShouldBeUnmodifiable() {
        Spread spread = new SingleCardSpread();
        DrawnCard drawn = new DrawnCard(sampleCard("star"), false);
        SpreadResult result = new SpreadResult(spread, List.of(drawn), Instant.EPOCH);

        assertThrows(UnsupportedOperationException.class, () -> result.getDrawnCards().add(drawn));
    }

    private TarotCard sampleCard(String id) {
        return new TarotCard(id, "愚者", Arcana.MAJOR, Suit.NONE, 0);
    }
}
