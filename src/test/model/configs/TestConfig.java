package model.configs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Seg;

public class TestConfig {

    @Test
    public void test() {
        PieceConfigs pc = new PieceConfigs();
        ScoreConfig sc = new ScoreConfig();

        assertNotNull(pc);
        assertEquals(PieceConfigs.class, pc.getClass());

        assertNotNull(sc);
        assertEquals(ScoreConfig.class, sc.getClass());
    }

    // REQUIRES: expected and actual have the same length
    // EFFECTS: performs assertEquals on x and y positions of both Seg arrays
    public static void assertConfigEquals(Seg[] expected, Seg[] actual) {
        for (int i = 0; i < expected.length; i++) {
            Seg expectedSeg = expected[i];
            Seg actualSeg = actual[i];

            assertEquals(expectedSeg.getX(), actualSeg.getX());
            assertEquals(expectedSeg.getY(), actualSeg.getY());
        }
    }
}
