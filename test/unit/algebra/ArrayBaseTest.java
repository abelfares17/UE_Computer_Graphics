package algebra;

import renderer.algebra.ArrayBase;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ArrayBase class.
 * Since ArrayBase is abstract, we use a concrete TestArrayBase implementation.
 */
public class ArrayBaseTest {

    // Constants
    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;

    /**
     * The name of the test array.
     */
    private static final String ARRAY_NAME = "testArray";

    /**
     * The default test size.
     */
    private static final int DEFAULT_SIZE = 5;

    /**
     * The small test size.
     */
    private static final int SMALL_SIZE = 1;

    /**
     * The large test size.
     */
    private static final int LARGE_SIZE = 100;

    /**
     * The zero size for testing invalid constructor.
     */
    private static final int ZERO_SIZE = 0;

    /**
     * The negative size for testing invalid constructor.
     */
    private static final int NEGATIVE_SIZE = -1;

    /**
     * Scaling factor for tests.
     */
    private static final double SCALE_FACTOR = 2.5;

    /**
     * Zero scaling factor.
     */
    private static final double ZERO_SCALE = 0.0;

    /**
     * Negative scaling factor.
     */
    private static final double NEGATIVE_SCALE = -3.0;

    /**
     * Test value for array initialization.
     */
    private static final double TEST_VALUE = 1.5;

    /**
     * Another test value for array initialization.
     */
    private static final double TEST_VALUE_2 = 2.5;

    /**
     * Test array base object.
     */
    private TestArrayBase arrayBase;

    /**
     * Concrete implementation of ArrayBase for testing purposes.
     * Uses Vector as the underlying implementation.
     */
    private static class TestArrayBase extends ArrayBase {

        /**
         * Constructor with name and size.
         *
         * @param name the name
         * @param size the size
         * @throws IllegalArgumentException if size is not positive
         */
        TestArrayBase(final String name, final int size)
                throws IllegalArgumentException {
            super(name, size);
        }

        /**
         * Constructor with size only.
         *
         * @param size the size
         * @throws IllegalArgumentException if size is not positive
         */
        TestArrayBase(final int size)
                throws IllegalArgumentException {
            super(size);
        }

        /**
         * Public wrapper for scaleValues to enable testing.
         *
         * @param factor the scaling factor
         * @param dest the destination array
         */
        void testScaleValues(final double factor, final double[] dest) {
            scaleValues(factor, dest);
        }

        /**
         * Public wrapper for addValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testAddValues(final double[] other, final double[] dest) {
            addValues(other, dest);
        }

        /**
         * Public wrapper for subtractValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testSubtractValues(final double[] other, final double[] dest) {
            subtractValues(other, dest);
        }

        /**
         * Public wrapper for copyValues to enable testing.
         *
         * @param source the source array
         */
        void testCopyValues(final double[] source) {
            copyValues(source);
        }

        /**
         * Public wrapper for copyValues with start and length to enable testing.
         *
         * @param source the source array
         * @param start the starting index in the source array
         * @param length the number of elements to copy
         */
        void testCopyValuesWithStartAndLength(final double[] source,
                final int start, final int length) {
            copyValues(source, start, length);
        }

        /**
         * Public wrapper for multiplyValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testMultiplyValues(final double[] other, final double[] dest) {
            multiplyValues(other, dest);
        }

        /**
         * Public wrapper for sumValues to enable testing.
         *
         * @return the sum of all elements
         */
        double testSumValues() {
            return sumValues();
        }

        /**
         * Gets the internal values array for testing.
         *
         * @return a copy of the values array
         */
        double[] getValuesCopy() {
            final double[] copy = new double[size()];
            System.arraycopy(getValues(), 0, copy, 0, size());
            return copy;
        }

        /**
         * Public wrapper for getValues to enable testing.
         *
         * @return the internal values array reference
         */
        double[] testGetValues() {
            return getValues();
        }

        /**
         * Public wrapper for setValue to enable testing.
         *
         * @param index the index
         * @param value the value
         */
        void testSetValue(final int index, final double value) {
            setValue(index, value);
        }

        /**
         * Public wrapper for getValue to enable testing.
         *
         * @param index the index
         * @return the value at the specified index
         */
        double testGetValue(final int index) {
            return getValue(index);
        }

        /**
         * Public wrapper for setAll to enable testing.
         *
         * @param value the value to set
         */
        void testSetAll(final double value) {
            setAll(value);
        }

        /**
         * Public wrapper for fillRandom to enable testing.
         */
        void testFillRandom() {
            fillRandom();
        }
    }

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        arrayBase = new TestArrayBase(ARRAY_NAME, DEFAULT_SIZE);
    }

    /**
     * Test constructor with name and size.
     */
    @Test
    public void testConstructorWithName() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, DEFAULT_SIZE);
        assertNotNull(arr);
        assertEquals(ARRAY_NAME, arr.getName());
        assertEquals(DEFAULT_SIZE, arr.size());
    }

    /**
     * Test constructor with size only.
     */
    @Test
    public void testConstructorWithoutName() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        assertNotNull(arr);
        assertEquals("", arr.getName());
        assertEquals(DEFAULT_SIZE, arr.size());
    }

    /**
     * Test constructor with minimum valid size.
     */
    @Test
    public void testConstructorWithMinimumSize() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, SMALL_SIZE);
        assertNotNull(arr);
        assertEquals(SMALL_SIZE, arr.size());
    }

    /**
     * Test constructor with large size.
     */
    @Test
    public void testConstructorWithLargeSize() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, LARGE_SIZE);
        assertNotNull(arr);
        assertEquals(LARGE_SIZE, arr.size());
    }

    /**
     * Test constructor with zero size should throw exception.
     */
    @Test
    public void testConstructorWithZeroSize() {
        try {
            new TestArrayBase(ARRAY_NAME, ZERO_SIZE);
            fail("Should throw IllegalArgumentException for zero size");
        } catch (IllegalArgumentException e) {
            assertEquals("Size must be strictly positive", e.getMessage());
        }
    }

    /**
     * Test constructor with negative size should throw exception.
     */
    @Test
    public void testConstructorWithNegativeSize() {
        try {
            new TestArrayBase(ARRAY_NAME, NEGATIVE_SIZE);
            fail("Should throw IllegalArgumentException for negative size");
        } catch (IllegalArgumentException e) {
            assertEquals("Size must be strictly positive", e.getMessage());
        }
    }

    /**
     * Test size method returns correct value.
     */
    @Test
    public void testSize() {
        assertEquals(DEFAULT_SIZE, arrayBase.size());
    }

    /**
     * Test getName returns the correct name.
     */
    @Test
    public void testGetName() {
        assertEquals(ARRAY_NAME, arrayBase.getName());
    }

    /**
     * Test setName updates the name correctly.
     */
    @Test
    public void testSetName() {
        final String newName = "newName";
        arrayBase.setName(newName);
        assertEquals(newName, arrayBase.getName());
    }

    /**
     * Test setName with empty string.
     */
    @Test
    public void testSetNameEmpty() {
        arrayBase.setName("");
        assertEquals("", arrayBase.getName());
    }

    /**
     * Test setName with null.
     */
    @Test
    public void testSetNameNull() {
        arrayBase.setName(null);
        assertEquals(null, arrayBase.getName());
    }

    /**
     * Test scaleValues with positive factor.
     */
    @Test
    public void testScaleValuesPositiveFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        // Initialize array with known values
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
        }

        arr.testScaleValues(SCALE_FACTOR, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * SCALE_FACTOR, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues with zero factor.
     */
    @Test
    public void testScaleValuesZeroFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(ZERO_SCALE, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues with negative factor.
     */
    @Test
    public void testScaleValuesNegativeFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(NEGATIVE_SCALE, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 * NEGATIVE_SCALE, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues does not modify source array.
     */
    @Test
    public void testScaleValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] original = arr.getValuesCopy();
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(SCALE_FACTOR, dest);

        assertArrayEquals(original, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test scaleValues with mismatched destination size.
     */
    @Test
    public void testScaleValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testScaleValues(SCALE_FACTOR, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("Destination array size must match source size",
                        e.getMessage());
        }
    }

    /**
     * Test scaleValues with smaller destination size.
     */
    @Test
    public void testScaleValuesSmallerDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int smallerSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[smallerSize];

        try {
            arr.testScaleValues(SCALE_FACTOR, dest);
            fail("Should throw IllegalArgumentException for smaller dest size");
        } catch (IllegalArgumentException e) {
            assertEquals("Destination array size must match source size",
                        e.getMessage());
        }
    }

    /**
     * Test addValues with two arrays.
     */
    @Test
    public void testAddValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE_2;
        }

        arr.testAddValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 + TEST_VALUE_2, dest[i], EPSILON);
        }
    }

    /**
     * Test addValues does not modify source arrays.
     */
    @Test
    public void testAddValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE;
            otherCopy[i] = TEST_VALUE;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testAddValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test addValues with mismatched other array size.
     */
    @Test
    public void testAddValuesWrongOtherSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with mismatched destination size.
     */
    @Test
    public void testAddValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with all arrays wrong size.
     */
    @Test
    public void testAddValuesAllWrongSizes() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with zero values.
     */
    @Test
    public void testAddValuesWithZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testAddValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues with two arrays.
     */
    @Test
    public void testSubtractValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE;
        }

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 - TEST_VALUE, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues does not modify source arrays.
     */
    @Test
    public void testSubtractValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE_2;
            otherCopy[i] = TEST_VALUE_2;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testSubtractValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test subtractValues with mismatched other array size.
     */
    @Test
    public void testSubtractValuesWrongOtherSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with mismatched destination size.
     */
    @Test
    public void testSubtractValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with all arrays wrong size.
     */
    @Test
    public void testSubtractValuesAllWrongSizes() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with zero values.
     */
    @Test
    public void testSubtractValuesWithZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues resulting in negative values.
     */
    @Test
    public void testSubtractValuesNegativeResult() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double largeValue = 10.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = largeValue;
        }

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 - largeValue, dest[i], EPSILON);
        }
    }

    /**
     * Test that size is immutable after construction.
     */
    @Test
    public void testSizeIsImmutable() {
        final int size1 = arrayBase.size();
        final int size2 = arrayBase.size();
        assertEquals(size1, size2);
    }

    /**
     * Test operations on single element array.
     */
    @Test
    public void testSingleElementArray() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] other = new double[SMALL_SIZE];
        final double[] dest = new double[SMALL_SIZE];

        other[0] = TEST_VALUE;
        arr.testAddValues(other, dest);

        assertEquals(TEST_VALUE, dest[0], EPSILON);
    }

    /**
     * Test operations preserve precision.
     */
    @Test
    public void testPrecisionPreservation() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];
        final double preciseValue = 1.234567890123456;

        arr.testScaleValues(preciseValue, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 * preciseValue, dest[i], EPSILON);
        }
    }

    /**
     * Test copyValues copies all elements correctly.
     */
    @Test
    public void testCopyValuesValidSourceCopiesAllElements() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues does not modify source array.
     */
    @Test
    public void testCopyValuesValidSourceDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double[] sourceCopy = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE_2;
            sourceCopy[i] = i + TEST_VALUE_2;
        }

        arr.testCopyValues(source);

        assertArrayEquals(sourceCopy, source, EPSILON);
    }

    /**
     * Test copyValues with source size larger than destination.
     */
    @Test
    public void testCopyValuesLargerSourceSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int largerSize = DEFAULT_SIZE + 1;
        final double[] source = new double[largerSize];

        try {
            arr.testCopyValues(source);
            fail("Should throw IllegalArgumentException for larger source size");
        } catch (IllegalArgumentException e) {
            assertEquals("Source array size must match destination size",
                        e.getMessage());
        }
    }

    /**
     * Test copyValues with source size smaller than destination.
     */
    @Test
    public void testCopyValuesSmallerSourceSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int smallerSize = DEFAULT_SIZE - 1;
        final double[] source = new double[smallerSize];

        try {
            arr.testCopyValues(source);
            fail("Should throw IllegalArgumentException for smaller source size");
        } catch (IllegalArgumentException e) {
            assertEquals("Source array size must match destination size",
                        e.getMessage());
        }
    }

    /**
     * Test copyValues with zero values.
     */
    @Test
    public void testCopyValuesZeroValuesCopiesZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, result[i], EPSILON);
        }
    }

    /**
     * Test copyValues with negative values.
     */
    @Test
    public void testCopyValuesNegativeValuesCopiesNegatives() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double negativeValue = -5.5;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = negativeValue - i;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues with mixed positive and negative values.
     */
    @Test
    public void testCopyValuesMixedValuesCopiesAllValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = (i % 2 == 0) ? i + TEST_VALUE : -(i + TEST_VALUE);
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues replaces existing values.
     */
    @Test
    public void testCopyValuesExistingValuesReplacesWithNewValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] initialSource = new double[DEFAULT_SIZE];
        final double[] newSource = new double[DEFAULT_SIZE];
        final double initialValue = 1.0;
        final double newValue = 10.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            initialSource[i] = initialValue;
            newSource[i] = newValue;
        }

        arr.testCopyValues(initialSource);
        arr.testCopyValues(newSource);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(newSource, result, EPSILON);
    }

    /**
     * Test copyValues with single element array.
     */
    @Test
    public void testCopyValuesSingleElementCopiesSingleValue() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] source = new double[SMALL_SIZE];
        source[0] = TEST_VALUE;

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertEquals(TEST_VALUE, result[0], EPSILON);
    }

    /**
     * Test copyValues with large array.
     */
    @Test
    public void testCopyValuesLargeArrayCopiesAllElements() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        final double[] source = new double[LARGE_SIZE];

        for (int i = 0; i < LARGE_SIZE; i++) {
            source[i] = i * TEST_VALUE;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues preserves precision.
     */
    @Test
    public void testCopyValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double preciseValue = 1.234567890123456;
        final double increment = 0.1;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = preciseValue + i * increment;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test multiplyValues with positive values.
     */
    @Test
    public void testMultiplyValuesPositiveValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
            other[i] = SCALE_FACTOR;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * SCALE_FACTOR, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues does not modify source arrays.
     */
    @Test
    public void testMultiplyValuesValidArraysDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, TEST_VALUE);
            other[i] = TEST_VALUE_2;
            otherCopy[i] = TEST_VALUE_2;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testMultiplyValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test multiplyValues with zero values.
     */
    @Test
    public void testMultiplyValuesZeroValuesProducesZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, TEST_VALUE);
            other[i] = ZERO_SCALE;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with negative values.
     */
    @Test
    public void testMultiplyValuesNegativeValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double negativeValue = -2.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
            other[i] = negativeValue;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * negativeValue, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with mixed positive and negative values.
     */
    @Test
    public void testMultiplyValuesMixedValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
            other[i] = (i % 2 == 0) ? SCALE_FACTOR : NEGATIVE_SCALE;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double expected = (i + 1.0)
                * ((i % 2 == 0) ? SCALE_FACTOR : NEGATIVE_SCALE);
            assertEquals(expected, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with identity (ones).
     */
    @Test
    public void testMultiplyValuesIdentityMultiplicationPreservesValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double identityValue = 1.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + TEST_VALUE);
            other[i] = identityValue;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(i + TEST_VALUE, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with mismatched other array size.
     */
    @Test
    public void testMultiplyValuesWrongOtherSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with mismatched destination size.
     */
    @Test
    public void testMultiplyValuesWrongDestSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with all arrays wrong size.
     */
    @Test
    public void testMultiplyValuesAllWrongSizesThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with single element array.
     */
    @Test
    public void testMultiplyValuesSingleElementMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] other = new double[SMALL_SIZE];
        final double[] dest = new double[SMALL_SIZE];

        arr.testSetValue(0, TEST_VALUE);
        other[0] = SCALE_FACTOR;

        arr.testMultiplyValues(other, dest);

        assertEquals(TEST_VALUE * SCALE_FACTOR, dest[0], EPSILON);
    }

    /**
     * Test multiplyValues with large array.
     */
    @Test
    public void testMultiplyValuesLargeArrayMultipliesAllElements() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        final double[] other = new double[LARGE_SIZE];
        final double[] dest = new double[LARGE_SIZE];
        final double multiplier = 3.0;

        for (int i = 0; i < LARGE_SIZE; i++) {
            arr.testSetValue(i, i * TEST_VALUE);
            other[i] = multiplier;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < LARGE_SIZE; i++) {
            assertEquals(i * TEST_VALUE * multiplier, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues preserves precision.
     */
    @Test
    public void testMultiplyValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double preciseValue1 = 1.234567890123456;
        final double preciseValue2 = 2.345678901234567;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, preciseValue1);
            other[i] = preciseValue2;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(preciseValue1 * preciseValue2, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with varying values in both arrays.
     */
    @Test
    public void testMultiplyValuesVaryingValuesMultipliesElementWise() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, (i + 1) * TEST_VALUE);
            other[i] = (i + 2) * TEST_VALUE_2;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double expected = (i + 1) * TEST_VALUE
                * (i + 2) * TEST_VALUE_2;
            assertEquals(expected, dest[i], EPSILON);
        }
    }

    /**
     * Test sumValues with positive values.
     */
    @Test
    public void testSumValuesPositiveValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = i + TEST_VALUE;
            arr.testSetValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with zero values.
     */
    @Test
    public void testSumValuesZeroValuesReturnsZero() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        final double result = arr.testSumValues();

        assertEquals(0.0, result, EPSILON);
    }

    /**
     * Test sumValues with negative values.
     */
    @Test
    public void testSumValuesNegativeValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;
        final double negativeValue = -3.5;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = negativeValue - i;
            arr.testSetValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with mixed positive and negative values.
     */
    @Test
    public void testSumValuesMixedValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = (i % 2 == 0) ? (i + TEST_VALUE) : -(i + TEST_VALUE);
            arr.testSetValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues does not modify the array.
     */
    @Test
    public void testSumValuesDoesNotModifyArray() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + TEST_VALUE);
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testSumValues();

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test sumValues with single element.
     */
    @Test
    public void testSumValuesSingleElementReturnsValue() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        arr.testSetValue(0, TEST_VALUE);

        final double result = arr.testSumValues();

        assertEquals(TEST_VALUE, result, EPSILON);
    }

    /**
     * Test sumValues with large array.
     */
    @Test
    public void testSumValuesLargeArrayReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < LARGE_SIZE; i++) {
            final double value = i * TEST_VALUE;
            arr.testSetValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues preserves precision.
     */
    @Test
    public void testSumValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double preciseValue = 1.234567890123456;
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, preciseValue);
            expectedSum += preciseValue;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with values that sum to zero.
     */
    @Test
    public void testSumValuesOppositeValuesReturnZero() {
        final int evenSize = 4;
        final TestArrayBase arr = new TestArrayBase(evenSize);

        for (int i = 0; i < evenSize / 2; i++) {
            arr.testSetValue(i, TEST_VALUE);
        }
        for (int i = evenSize / 2; i < evenSize; i++) {
            arr.testSetValue(i, -TEST_VALUE);
        }

        final double result = arr.testSumValues();

        assertEquals(0.0, result, EPSILON);
    }

    /**
     * Test sumValues with varying values.
     */
    @Test
    public void testSumValuesVaryingValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = (i + 1) * (i + 2) * 0.5;
            arr.testSetValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test setValue sets value at specific index.
     */
    @Test
    public void testSetValueSetsCorrectValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int index = 2;

        arr.testSetValue(index, TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        assertEquals(TEST_VALUE, values[index], EPSILON);
    }

    /**
     * Test setValue at first index.
     */
    @Test
    public void testSetValueFirstIndex() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        arr.testSetValue(0, TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        assertEquals(TEST_VALUE, values[0], EPSILON);
    }

    /**
     * Test setValue at last index.
     */
    @Test
    public void testSetValueLastIndex() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int lastIndex = DEFAULT_SIZE - 1;

        arr.testSetValue(lastIndex, TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        assertEquals(TEST_VALUE, values[lastIndex], EPSILON);
    }

    /**
     * Test setValue with negative value.
     */
    @Test
    public void testSetValueNegativeValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double negativeValue = -5.5;

        arr.testSetValue(0, negativeValue);

        final double[] values = arr.getValuesCopy();
        assertEquals(negativeValue, values[0], EPSILON);
    }

    /**
     * Test setValue with zero.
     */
    @Test
    public void testSetValueZero() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        arr.testSetValue(0, TEST_VALUE);

        arr.testSetValue(0, 0.0);

        final double[] values = arr.getValuesCopy();
        assertEquals(0.0, values[0], EPSILON);
    }

    /**
     * Test setValue only modifies the specified index.
     */
    @Test
    public void testSetValueOnlyModifiesSpecifiedIndex() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int targetIndex = 2;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
        }

        arr.testSetValue(targetIndex, TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            if (i == targetIndex) {
                assertEquals(TEST_VALUE, values[i], EPSILON);
            } else {
                assertEquals(i + 1.0, values[i], EPSILON);
            }
        }
    }

    /**
     * Test getValue returns correct value.
     */
    @Test
    public void testGetValueReturnsCorrectValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int index = 2;
        arr.testSetValue(index, TEST_VALUE);

        final double result = arr.testGetValue(index);

        assertEquals(TEST_VALUE, result, EPSILON);
    }

    /**
     * Test getValue at first index.
     */
    @Test
    public void testGetValueFirstIndex() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        arr.testSetValue(0, TEST_VALUE);

        final double result = arr.testGetValue(0);

        assertEquals(TEST_VALUE, result, EPSILON);
    }

    /**
     * Test getValue at last index.
     */
    @Test
    public void testGetValueLastIndex() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int lastIndex = DEFAULT_SIZE - 1;
        arr.testSetValue(lastIndex, TEST_VALUE);

        final double result = arr.testGetValue(lastIndex);

        assertEquals(TEST_VALUE, result, EPSILON);
    }

    /**
     * Test getValue returns zero for uninitialized element.
     */
    @Test
    public void testGetValueUninitializedReturnsZero() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        final double result = arr.testGetValue(0);

        assertEquals(0.0, result, EPSILON);
    }

    /**
     * Test getValue with negative value.
     */
    @Test
    public void testGetValueNegativeValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double negativeValue = -3.7;
        arr.testSetValue(1, negativeValue);

        final double result = arr.testGetValue(1);

        assertEquals(negativeValue, result, EPSILON);
    }

    /**
     * Test getValue does not modify array.
     */
    @Test
    public void testGetValueDoesNotModifyArray() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + TEST_VALUE);
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testGetValue(2);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test setAll sets all elements to the same value.
     */
    @Test
    public void testSetAllSetsAllElements() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        arr.testSetAll(TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(TEST_VALUE, values[i], EPSILON);
        }
    }

    /**
     * Test setAll with zero.
     */
    @Test
    public void testSetAllZero() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + 1.0);
        }

        arr.testSetAll(0.0);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, values[i], EPSILON);
        }
    }

    /**
     * Test setAll with negative value.
     */
    @Test
    public void testSetAllNegativeValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double negativeValue = -2.5;

        arr.testSetAll(negativeValue);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(negativeValue, values[i], EPSILON);
        }
    }

    /**
     * Test setAll replaces existing values.
     */
    @Test
    public void testSetAllReplacesExistingValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i * TEST_VALUE);
        }

        arr.testSetAll(TEST_VALUE_2);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(TEST_VALUE_2, values[i], EPSILON);
        }
    }

    /**
     * Test setAll with single element array.
     */
    @Test
    public void testSetAllSingleElement() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);

        arr.testSetAll(TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        assertEquals(TEST_VALUE, values[0], EPSILON);
    }

    /**
     * Test setAll with large array.
     */
    @Test
    public void testSetAllLargeArray() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);

        arr.testSetAll(TEST_VALUE);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < LARGE_SIZE; i++) {
            assertEquals(TEST_VALUE, values[i], EPSILON);
        }
    }

    /**
     * Test setAll preserves precision.
     */
    @Test
    public void testSetAllPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double preciseValue = 1.234567890123456;

        arr.testSetAll(preciseValue);

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(preciseValue, values[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength copies from middle of source.
     */
    @Test
    public void testCopyValuesWithStartAndLengthFromMiddle() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int sourceSize = 10;
        final double[] source = new double[sourceSize];
        final int start = 2;
        final int length = DEFAULT_SIZE;

        for (int i = 0; i < sourceSize; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength copies from start of source.
     */
    @Test
    public void testCopyValuesWithStartAndLengthFromStart() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int start = 0;
        final int length = DEFAULT_SIZE;

        for (int i = 0; i < source.length; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength copies partial array.
     */
    @Test
    public void testCopyValuesWithStartAndLengthPartialCopy() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final int start = 1;
        final int length = 3;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength does not modify source.
     */
    @Test
    public void testCopyValuesWithStartAndLengthDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final double[] sourceCopy = new double[DEFAULT_SIZE + 2];
        final int start = 1;
        final int length = DEFAULT_SIZE;

        for (int i = 0; i < source.length; i++) {
            source[i] = i + TEST_VALUE_2;
            sourceCopy[i] = i + TEST_VALUE_2;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        assertArrayEquals(sourceCopy, source, EPSILON);
    }

    /**
     * Test copyValuesWithStartAndLength with negative start.
     */
    @Test
    public void testCopyValuesWithStartAndLengthNegativeStartThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int negativeStart = -1;
        final int length = DEFAULT_SIZE;

        try {
            arr.testCopyValuesWithStartAndLength(source, negativeStart, length);
            fail("Should throw IllegalArgumentException for negative start");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start or length for copy operation",
                        e.getMessage());
        }
    }

    /**
     * Test copyValuesWithStartAndLength with negative length.
     */
    @Test
    public void testCopyValuesWithStartAndLengthNegativeLengthThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int start = 0;
        final int negativeLength = -1;

        try {
            arr.testCopyValuesWithStartAndLength(source, start, negativeLength);
            fail("Should throw IllegalArgumentException for negative length");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start or length for copy operation",
                        e.getMessage());
        }
    }

    /**
     * Test copyValuesWithStartAndLength with length exceeding destination.
     */
    @Test
    public void testCopyValuesWithStartAndLengthLengthExceedsDestination() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE * 2];
        final int start = 0;
        final int tooLargeLength = DEFAULT_SIZE + 1;

        try {
            arr.testCopyValuesWithStartAndLength(source, start, tooLargeLength);
            fail("Should throw IllegalArgumentException for "
                    + "length exceeding destination");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start or length for copy operation",
                        e.getMessage());
        }
    }

    /**
     * Test copyValuesWithStartAndLength with start plus length exceeding source.
     */
    @Test
    public void testCopyValuesWithStartAndLengthExceedingSource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final int start = 2;
        final int length = DEFAULT_SIZE;

        try {
            arr.testCopyValuesWithStartAndLength(source, start, length);
            fail("Should throw IllegalArgumentException for exceeding source bounds");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start or length for copy operation",
                        e.getMessage());
        }
    }

    /**
     * Test copyValuesWithStartAndLength with start at end of source.
     */
    @Test
    public void testCopyValuesWithStartAndLengthStartAtSourceEnd() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final int start = DEFAULT_SIZE;
        final int length = 1;

        try {
            arr.testCopyValuesWithStartAndLength(source, start, length);
            fail("Should throw IllegalArgumentException for start at source end");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start or length for copy operation",
                        e.getMessage());
        }
    }

    /**
     * Test copyValuesWithStartAndLength with zero length.
     */
    @Test
    public void testCopyValuesWithStartAndLengthZeroLength() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final int start = 0;
        final int zeroLength = 0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, TEST_VALUE);
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testCopyValuesWithStartAndLength(source, start, zeroLength);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test copyValuesWithStartAndLength with single element.
     */
    @Test
    public void testCopyValuesWithStartAndLengthSingleElement() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final int start = 2;
        final int length = 1;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        assertEquals(source[start], result[0], EPSILON);
    }

    /**
     * Test copyValuesWithStartAndLength with large source array.
     */
    @Test
    public void testCopyValuesWithStartAndLengthLargeSource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[LARGE_SIZE];
        final int start = 50;
        final int length = DEFAULT_SIZE;

        for (int i = 0; i < LARGE_SIZE; i++) {
            source[i] = i * TEST_VALUE;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength with negative values.
     */
    @Test
    public void testCopyValuesWithStartAndLengthNegativeValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int start = 1;
        final int length = DEFAULT_SIZE;
        final double negativeValue = -7.5;

        for (int i = 0; i < source.length; i++) {
            source[i] = negativeValue - i;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength preserves precision.
     */
    @Test
    public void testCopyValuesWithStartAndLengthPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int start = 1;
        final int length = DEFAULT_SIZE;
        final double preciseValue = 1.234567890123456;
        final double increment = 0.1;

        for (int i = 0; i < source.length; i++) {
            source[i] = preciseValue + i * increment;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test copyValuesWithStartAndLength replaces existing values.
     */
    @Test
    public void testCopyValuesWithStartAndLengthReplacesExisting() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE + 2];
        final int start = 1;
        final int length = DEFAULT_SIZE;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i * TEST_VALUE);
        }

        for (int i = 0; i < source.length; i++) {
            source[i] = i + TEST_VALUE_2;
        }

        arr.testCopyValuesWithStartAndLength(source, start, length);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < length; i++) {
            assertEquals(source[start + i], result[i], EPSILON);
        }
    }

    /**
     * Test getValues returns the internal array reference.
     */
    @Test
    public void testGetValuesReturnsInternalArray() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values1 = arr.testGetValues();
        final double[] values2 = arr.testGetValues();

        // Verify it's the same reference
        assertEquals(values1, values2);
    }

    /**
     * Test getValues returns array with correct length.
     */
    @Test
    public void testGetValuesReturnsCorrectLength() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values = arr.testGetValues();

        assertEquals(DEFAULT_SIZE, values.length);
    }

    /**
     * Test modifications to getValues result affect internal state.
     */
    @Test
    public void testGetValuesModificationsAffectInternalState() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values = arr.testGetValues();

        // Modify the returned array
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            values[i] = i + TEST_VALUE;
        }

        // Verify internal state changed
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(i + TEST_VALUE, arr.testGetValue(i), EPSILON);
        }
    }

    /**
     * Test getValues returns array initialized to zeros.
     */
    @Test
    public void testGetValuesInitializedToZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values = arr.testGetValues();

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, values[i], EPSILON);
        }
    }

    /**
     * Test getValues after setValue reflects changes.
     */
    @Test
    public void testGetValuesReflectsSetValue() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i * TEST_VALUE);
        }

        final double[] values = arr.testGetValues();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(i * TEST_VALUE, values[i], EPSILON);
        }
    }

    /**
     * Test getValues after setAll reflects changes.
     */
    @Test
    public void testGetValuesReflectsSetAll() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        arr.testSetAll(TEST_VALUE);

        final double[] values = arr.testGetValues();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(TEST_VALUE, values[i], EPSILON);
        }
    }

    /**
     * Test getValues with single element array.
     */
    @Test
    public void testGetValuesSingleElement() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        arr.testSetValue(0, TEST_VALUE);

        final double[] values = arr.testGetValues();
        assertEquals(SMALL_SIZE, values.length);
        assertEquals(TEST_VALUE, values[0], EPSILON);
    }

    /**
     * Test getValues with large array.
     */
    @Test
    public void testGetValuesLargeArray() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        final double[] values = arr.testGetValues();

        assertEquals(LARGE_SIZE, values.length);
    }

    /**
     * Test getValues returns same reference after copyValues.
     */
    @Test
    public void testGetValuesSameReferenceAfterCopyValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values1 = arr.testGetValues();

        final double[] source = new double[DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE;
        }
        arr.testCopyValues(source);

        final double[] values2 = arr.testGetValues();
        assertEquals(values1, values2);
    }

    /**
     * Test getValues reference equality with multiple calls.
     */
    @Test
    public void testGetValuesReferenceEqualityMultipleCalls() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] ref1 = arr.testGetValues();
        final double[] ref2 = arr.testGetValues();
        final double[] ref3 = arr.testGetValues();

        assertEquals(ref1, ref2);
        assertEquals(ref2, ref3);
        assertEquals(ref1, ref3);
    }

    /**
     * Test direct array modification via getValues.
     */
    @Test
    public void testGetValuesDirectModification() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] values = arr.testGetValues();
        final int testIndex = 2;

        values[testIndex] = TEST_VALUE_2;

        assertEquals(TEST_VALUE_2, arr.testGetValue(testIndex), EPSILON);
    }

    /**
     * Test getValues after scaleValues operation.
     */
    @Test
    public void testGetValuesAfterScaleValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.testSetValue(i, i + TEST_VALUE);
        }

        arr.testScaleValues(SCALE_FACTOR, dest);
        final double[] values = arr.testGetValues();

        // Verify original values unchanged
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(i + TEST_VALUE, values[i], EPSILON);
        }
    }

    /**
     * Test getDimensionString returns size as string.
     */
    @Test
    public void testGetDimensionStringReturnsSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        assertEquals(String.valueOf(DEFAULT_SIZE), arr.getDimensionString());
    }

    /**
     * Test getDimensionString with small size.
     */
    @Test
    public void testGetDimensionStringSmallSize() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        assertEquals("1", arr.getDimensionString());
    }

    /**
     * Test getDimensionString with large size.
     */
    @Test
    public void testGetDimensionStringLargeSize() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        assertEquals("100", arr.getDimensionString());
    }

    /**
     * Test fillRandom populates all elements with values.
     */
    @Test
    public void testFillRandomPopulatesAllElements() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        arr.testFillRandom();

        final double[] values = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            // Random values should not be zero (with very high probability)
            // and should be in range [0.0, 1.0)
            assertTrue("Element " + i + " should be >= 0.0",
                      values[i] >= 0.0);
            assertTrue("Element " + i + " should be < 1.0",
                      values[i] < 1.0);
        }
    }

    /**
     * Test fillRandom with single element.
     */
    @Test
    public void testFillRandomSingleElement() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        arr.testFillRandom();

        final double[] values = arr.getValuesCopy();
        assertTrue(values[0] >= 0.0);
        assertTrue(values[0] < 1.0);
    }
}
