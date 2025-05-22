package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestScore {
    Score score1;
    Score score2;
    Score score3;

    @BeforeEach
    public void runBefore() {
        score1 = new Score("Jefferson", 540);
        score2 = new Score("Tommy", 600000);
        score3 = new Score("Hacker", 5000000);
    }

    @Test
    public void testScoreConstructor() {
        assertEquals("Jefferson", score1.getName());
        assertEquals(540, score1.getScore());

        assertEquals("Tommy", score2.getName());
        assertEquals(600000, score2.getScore());

        assertEquals("Hacker", score3.getName());
        assertEquals(5000000, score3.getScore());
    }
}
