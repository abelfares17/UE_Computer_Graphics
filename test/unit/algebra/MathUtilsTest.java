package algebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import renderer.algebra.MathUtils;



/**
 * Unit tests for the MathUtils class.
 */
public class MathUtilsTest {

    /** Minimum integer value for testing. */
    private static final int MIN_INT = 1;
    /** Maximum integer value for testing. */
    private static final int MAX_INT = 10;
    /** Mid-range integer value for testing. */
    private static final int MID_INT = 5;
    /** Integer value below the minimum for testing. */
    private static final int BELOW_MIN_INT = 0;
    /** Integer value above the maximum for testing. */
    private static final int ABOVE_MAX_INT = 11;

    /** Minimum double value for testing. */
    private static final double MIN_DOUBLE = 1.0;
    /** Maximum double value for testing. */
    private static final double MAX_DOUBLE = 10.0;
    /** Mid-range double value for testing. */
    private static final double MID_DOUBLE = 5.0;
    /** Double value below the minimum for testing. */
    private static final double BELOW_MIN_DOUBLE = 0.0;
    /** Double value above the maximum for testing. */
    private static final double ABOVE_MAX_DOUBLE = 11.0;

    /** Epsilon for comparing double values. */
    private static final double EPSILON = 0.001;

    /**
     * Test clamping an integer value within the range.
     */
    @Test
    public void testClampIntWithinRange() {
        int result = MathUtils.clamp(MID_INT, MIN_INT, MAX_INT);
        assertEquals(MID_INT, result);
    }

    /**
     * Test clamping an integer value below the minimum.
     */
    @Test
    public void testClampIntBelowMin() {
        int result = MathUtils.clamp(BELOW_MIN_INT, MIN_INT, MAX_INT);
        assertEquals(MIN_INT, result);
    }

    /**
     * Test clamping an integer value above the maximum.
     */
    @Test
    public void testClampIntAboveMax() {
        int result = MathUtils.clamp(ABOVE_MAX_INT, MIN_INT, MAX_INT);
        assertEquals(MAX_INT, result);
    }

    /**
     * Test clamping a double value within the range.
     */
    @Test
    public void testClampDoubleWithinRange() {
        double result = MathUtils.clamp(MID_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertEquals(MID_DOUBLE, result, EPSILON);
    }

    /**
     * Test clamping a double value below the minimum.
     */
    @Test
    public void testClampDoubleBelowMin() {
        double result = MathUtils.clamp(BELOW_MIN_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertEquals(MIN_DOUBLE, result, EPSILON);
    }

    /**
     * Test clamping a double value above the maximum.
     */
    @Test
    public void testClampDoubleAboveMax() {
        double result = MathUtils.clamp(ABOVE_MAX_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertEquals(MAX_DOUBLE, result, EPSILON);
    }

    /**
     * Test robustness of the clamp method with wrong min/max values.
     */
    @Test
    public void testClampWrongMinMax() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.clamp(MID_INT, MAX_INT, MIN_INT));
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.clamp(MID_DOUBLE, MAX_DOUBLE, MIN_DOUBLE));
    }

    /**
     * Test clamping a double value with zero interval.
     */
    @Test
    public void testClampZeroInterval() {
        double result = MathUtils.clamp(ABOVE_MAX_DOUBLE, MAX_DOUBLE, MAX_DOUBLE);
        assertEquals(MAX_DOUBLE, result, EPSILON);
    }

    /**
     * Test checking if an integer value is within the range.
     */
    @Test
    public void testIsInRangeIntWithinRange() {
        boolean result = MathUtils.isInRange(MID_INT, MIN_INT, MAX_INT);
        assertTrue(result);
    }

    /**
     * Test checking if an integer value is below the minimum.
     */
    @Test
    public void testIsInRangeIntBelowMin() {
        boolean result = MathUtils.isInRange(BELOW_MIN_INT, MIN_INT, MAX_INT);
        assertFalse(result);
    }

    /**
     * Test checking if an integer value is above the maximum.
     */
    @Test
    public void testIsInRangeIntAboveMax() {
        boolean result = MathUtils.isInRange(ABOVE_MAX_INT, MIN_INT, MAX_INT);
        assertFalse(result);
    }

    /**
     * Test checking if a double value is within the range.
     */
    @Test
    public void testIsInRangeDoubleWithinRange() {
        boolean result = MathUtils.isInRange(MID_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertTrue(result);
    }

    /**
     * Test checking if a double value is below the minimum.
     */
    @Test
    public void testIsInRangeDoubleBelowMin() {
        boolean result = MathUtils.isInRange(BELOW_MIN_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertFalse(result);
    }

    /**
     * Test checking if a double value is above the maximum.
     */
    @Test
    public void testIsInRangeDoubleAboveMax() {
        boolean result = MathUtils.isInRange(ABOVE_MAX_DOUBLE, MIN_DOUBLE, MAX_DOUBLE);
        assertFalse(result);
    }

    /**
     * Test checking if mixing types throws an exception.
     */
    @Test
    public void testIsInRangeMixedTypes() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.isInRange(MID_INT, MIN_DOUBLE, MAX_DOUBLE));
    }

    /**
     * Test robustness of the isInRange method with wrong min/max values.
     */
    @Test
    public void testIsInRangeWrongMinMax() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.isInRange(MID_INT, MAX_INT, MIN_INT));
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.isInRange(MID_DOUBLE, MAX_DOUBLE, MIN_DOUBLE));
    }
}
