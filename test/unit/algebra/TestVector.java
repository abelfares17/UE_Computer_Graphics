package algebra;

import renderer.algebra.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class TestVector {

    // Constants
    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;
    /**
     * The name of the test vector.
     */
    private static final String VECTOR_NAME = "testVector";

    /**
     * Test the creation of a Vector.
     */
    @Test
    public void testVectorCreation() {
        final int vectorSize = 5;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        assertNotNull(v);
        assertEquals(VECTOR_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test
    public void testDefaultName() {
        final int vectorSize = 5;
        final Vector v = new Vector(vectorSize);
        assertEquals(Vector.DEFAULT_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSize() {
        new Vector("invalidVector", 0);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test
    public void testSetAndGetValues() {
        final int vectorSize = 5;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(i + 1.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the scaling of a Vector.
     * Verifies that scale() returns a new vector and doesn't modify the original.
     */
    @Test
    public void testScale() {
        final int vectorSize = 6;
        final double scale = 2.0;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }

        // Store original values to verify immutability
        final double[] originalValues = new double[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            originalValues[i] = v.get(i);
        }

        final Vector nv = v.scale(scale);

        // Verify result is correct
        for (int i = 0; i < vectorSize; i++) {
            assertEquals((i + 1.0) * scale, nv.get(i), EPSILON);
        }

        // Verify original vector is unchanged (immutability)
        assertFalse("scale() should return a new vector", v == nv);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Original vector should not be modified",
                        originalValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test the dot product of two Vectors.
     */
    @Test
    public void testDot() {
        final int vectorSize = 4;
        final double expected = 20.0;
        final Vector v1 = new Vector("v1", vectorSize);
        final Vector v2 = new Vector("v2", vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v1.set(i, i + 1.0);
            v2.set(i, (double) vectorSize - i);
        }

        assertEquals(expected, v1.dot(v2), EPSILON);

    }


    /**
     * Test the getter of the norm.
     */
    @Test
    public void testNorm() {
        final int vectorSize = 3;
        final Vector v = new Vector(vectorSize);

        // square root of the sum of integer squares from 1 to vectorSize

        final double expectedValue =
                Math.sqrt(vectorSize * (vectorSize + 1) * (2 * vectorSize + 1) / 6);

        double value = 1;
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, value++);
        }

        assertEquals(expectedValue, v.norm(), EPSILON);

    }

    /**
     * Test the normalization of a vector.
     * Verifies that normalize() returns a new vector and doesn't modify the original.
     */
    @Test
    public void testNormalization() {
        final int vectorSize = 23;
        final Vector v = new Vector(vectorSize);

        final double expectedValue = 1 / Math.sqrt(vectorSize);
        final double value = 1;

        for (int i = 0; i < vectorSize; i++) {
            v.set(i, value);
        }

        final Vector nv = v.normalize();

        // Verify normalize returns a new object
        assertFalse("v == nv should be false", v == nv);

        // Verify normalized result is correct
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("error for " + i + "-th component",
                                expectedValue, nv.get(i), EPSILON);
        }

        // Verify original vector is unchanged (immutability)
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Original vector component " + i + " should be unchanged",
                        value, v.get(i), EPSILON);
        }

    }

    /**
     * Test the set zeros on a vector.
     */
    @Test
    public void testZeros() {
        final int vectorSize = 6;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        final Vector nv = v.zeros();

        // v and nv are the same object.
        assertTrue(v == nv);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(0.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the set ones of a Vector.
     */
    @Test
    public void testOnes() {
        final int vectorSize = 6;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        final Vector nv = v.ones();

        // v and nv are the same object.
        assertTrue(v == nv);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(1.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test Vector addition and verify immutability.
     */
    @Test
    public void testAdd() {
        final double[] v1Values = {1.0, 2.0, 3.0, 4.0};
        final double[] v2Values = {5.0, 6.0, 7.0, 8.0};
        final double[] expectedValues = {6.0, 8.0, 10.0, 12.0};
        final int vectorSize = v1Values.length;

        final Vector v1 = new Vector("v1", v1Values);
        final Vector v2 = new Vector("v2", v2Values);

        final Vector result = v1.add(v2);

        // Verify result is correct
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Result component " + i + " should match",
                        expectedValues[i], result.get(i), EPSILON);
        }

        // Verify immutability - original vectors unchanged
        assertFalse("add() should return a new vector", v1 == result);
        assertFalse("add() should return a new vector", v2 == result);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("v1 should not be modified",
                        v1Values[i], v1.get(i), EPSILON);
            assertEquals("v2 should not be modified",
                        v2Values[i], v2.get(i), EPSILON);
        }
    }

    /**
     * Test Vector subtraction and verify immutability.
     */
    @Test
    public void testSubtract() {
        final double[] v1Values = {10.0, 8.0, 6.0, 4.0};
        final double[] v2Values = {1.0, 2.0, 3.0, 4.0};
        final double[] expectedValues = {9.0, 6.0, 3.0, 0.0};
        final int vectorSize = v1Values.length;

        final Vector v1 = new Vector("v1", v1Values);
        final Vector v2 = new Vector("v2", v2Values);

        final Vector result = v1.subtract(v2);

        // Verify result is correct
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Result component " + i + " should match",
                        expectedValues[i], result.get(i), EPSILON);
        }

        // Verify immutability - original vectors unchanged
        assertFalse("subtract() should return a new vector", v1 == result);
        assertFalse("subtract() should return a new vector", v2 == result);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("v1 should not be modified",
                        v1Values[i], v1.get(i), EPSILON);
            assertEquals("v2 should not be modified",
                        v2Values[i], v2.get(i), EPSILON);
        }
    }

    /**
     * Test the normalization of a vector with zero norm.
     * Verifies that normalize() returns a new vector even for zero vectors.
     */
    @Test
    public void testNormalizeZeroVector() {
        final int vectorSize = 5;
        final Vector v = new Vector(vectorSize);
        // All elements are 0 by default

        // Store original values
        final double[] originalValues = new double[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            originalValues[i] = v.get(i);
        }

        final Vector nv = v.normalize();

        // Verify normalize returns a new object (not the same reference)
        assertFalse("normalize() should return a new vector, not modify the original",
                   v == nv);

        // Verify normalized result is zero vector
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Component " + i + " should be 0",
                         0.0, nv.get(i), EPSILON);
        }

        // Verify original vector is unchanged (immutability)
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Original vector component " + i + " should be unchanged",
                        originalValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test homogeneous representation of vectors as points.
     */
    @Test
    public void testHomogeneousPoint() {
        // Test data: vector sizes to test
        final int[] testSizes = {2, 3, 4, 5};

        for (int size : testSizes) {
            final Vector v = new Vector(size);
            for (int i = 0; i < size; i++) {
                v.set(i, i + 1.0);
            }

            final Vector h = v.homogeneousPoint();

            // Check size increased by 1
            assertEquals("Homogeneous point size should be " + (size + 1),
                        size + 1, h.size());

            // Check original components preserved
            for (int i = 0; i < size; i++) {
                assertEquals("Component " + i + " should be preserved",
                            i + 1.0, h.get(i), EPSILON);
            }

            // Check last component is 1.0 (point)
            assertEquals("Last component should be 1.0 for point",
                        1.0, h.get(size), EPSILON);
        }
    }

    /**
     * Test homogeneous representation of vectors as directions.
     */
    @Test
    public void testHomogeneousVector() {
        // Test data: vector sizes to test
        final int[] testSizes = {2, 3, 4, 5};

        for (int size : testSizes) {
            final Vector v = new Vector(size);
            for (int i = 0; i < size; i++) {
                v.set(i, i + 1.0);
            }

            final Vector h = v.homogeneousVector();

            // Check size increased by 1
            assertEquals("Homogeneous direction size should be " + (size + 1),
                        size + 1, h.size());

            // Check original components preserved
            for (int i = 0; i < size; i++) {
                assertEquals("Component " + i + " should be preserved",
                            i + 1.0, h.get(i), EPSILON);
            }

            // Check last component is 0.0 (direction)
            assertEquals("Last component should be 0.0 for direction",
                        0.0, h.get(size), EPSILON);
        }
    }

    /**
     * Test that homogeneousPoint does not modify original vector.
     */
    @Test
    public void testHomogeneousPointImmutability() {
        final double[] values = {1.0, 2.0, 3.0};
        final Vector v = new Vector(values);

        // Store original values
        final double[] originalValues = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            originalValues[i] = v.get(i);
        }

        final Vector h = v.homogeneousPoint();

        // Verify homogeneousPoint returns a new object
        assertFalse("homogeneousPoint() should return a new vector", v == h);

        // Verify original vector is unchanged
        assertEquals("Original vector size should be unchanged",
                    values.length, v.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals("Original vector component " + i + " should be unchanged",
                        originalValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test that homogeneousVector does not modify original vector.
     */
    @Test
    public void testHomogeneousVectorImmutability() {
        final double[] values = {1.0, 2.0, 3.0};
        final Vector v = new Vector(values);

        // Store original values
        final double[] originalValues = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            originalValues[i] = v.get(i);
        }

        final Vector h = v.homogeneousVector();

        // Verify homogeneousVector returns a new object
        assertFalse("homogeneousVector() should return a new vector", v == h);

        // Verify original vector is unchanged
        assertEquals("Original vector size should be unchanged",
                    values.length, v.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals("Original vector component " + i + " should be unchanged",
                        originalValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test difference between homogeneousPoint and homogeneousVector.
     */
    @Test
    public void testHomogeneousPointVsHomogeneousVector() {
        final double[] values = {1.0, 2.0, 3.0};
        final Vector v = new Vector(values);

        final Vector point = v.homogeneousPoint();
        final Vector direction = v.homogeneousVector();

        // Both should have same size
        assertEquals("Point and direction should have same size",
                    point.size(), direction.size());

        // Both should preserve original components
        for (int i = 0; i < values.length; i++) {
            assertEquals("Point component " + i + " should match original",
                        values[i], point.get(i), EPSILON);
            assertEquals("Direction component " + i + " should match original",
                        values[i], direction.get(i), EPSILON);
        }

        // Last components should differ
        assertEquals("Point last component should be 1.0",
                    1.0, point.get(values.length), EPSILON);
        assertEquals("Direction last component should be 0.0",
                    0.0, direction.get(values.length), EPSILON);
    }

    /**
     * Test clamp method with various scenarios.
     * Verifies that clamp() returns a new vector and doesn't modify the original.
     */
    @Test
    public void testClamp() {
        // Test structure: input values, min, max, expected results
        final double[][] testCases = {
            {-5.0, -3.0, -1.0, 0.0, 1.0, 3.0, 5.0},  // input
            {-2.0, -2.0, -1.0, 0.0, 1.0, 2.0, 2.0}   // expected (clamped to [-2, 2])
        };

        final double min = -2.0;
        final double max = 2.0;
        final double[] inputValues = testCases[0];
        final double[] expectedValues = testCases[1];

        final Vector v = new Vector(inputValues);

        // Store original values to verify immutability
        final double[] originalValues = new double[inputValues.length];
        for (int i = 0; i < inputValues.length; i++) {
            originalValues[i] = v.get(i);
        }

        final Vector clamped = v.clamp(min, max);

        // Verify result is correct
        for (int i = 0; i < inputValues.length; i++) {
            assertEquals("Component " + i + " should be clamped",
                        expectedValues[i], clamped.get(i), EPSILON);
        }

        // Verify immutability - clamp returns new vector and doesn't modify original
        assertFalse("clamp() should return a new vector", v == clamped);
        for (int i = 0; i < inputValues.length; i++) {
            assertEquals("Original vector should not be modified",
                        originalValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test clamp with edge cases.
     * Verifies that clamp() returns a new vector and doesn't modify the original.
     */
    @Test
    public void testClampEdgeCases() {
        final int vectorSize = 4;
        final Vector v = new Vector(vectorSize);

        // Set all values to 0
        v.zeros();

        // Store original values
        final double[] originalValues = new double[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            originalValues[i] = v.get(i);
        }

        // Clamp with range that includes 0
        final Vector clamped = v.clamp(-1.0, 1.0);

        // Verify result is correct
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(0.0, clamped.get(i), EPSILON);
        }

        // Verify immutability
        assertFalse("clamp() should return a new vector", v == clamped);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Original vector should not be modified",
                        originalValues[i], v.get(i), EPSILON);
        }

        // Test with equal min and max
        final Vector v2 = new Vector(5.0, 10.0, 15.0);
        final double[] v2OriginalValues = {5.0, 10.0, 15.0};
        final double clampValue = 10.0;
        final Vector v2clamped = v2.clamp(clampValue, clampValue);

        // Verify result is correct
        for (int i = 0; i < v2.size(); i++) {
            assertEquals("All values should be clamped to 10.0",
                        clampValue, v2clamped.get(i), EPSILON);
        }

        // Verify immutability
        assertFalse("clamp() should return a new vector", v2 == v2clamped);
        for (int i = 0; i < v2.size(); i++) {
            assertEquals("Original vector should not be modified",
                        v2OriginalValues[i], v2.get(i), EPSILON);
        }
    }

    /**
     * Test clone method via copy constructor.
     * Since clone() is protected, we test the copy constructor which uses same logic.
     */
    @Test
    public void testClone() {
        final int vectorSize = 5;
        final double[] testValues = {1.5, 2.5, 3.5, 4.5, 5.5};

        final Vector original = new Vector(VECTOR_NAME, testValues);
        final Vector cloned = new Vector(original); // Using copy constructor

        // Check they are different objects
        assertFalse("Clone should be a different object", original == cloned);

        // Check same size
        assertEquals("Clone should have same size",
                    original.size(), cloned.size());

        // Check all values are the same
        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Component " + i + " should match",
                        original.get(i), cloned.get(i), EPSILON);
        }

        // Modify original and verify clone is unaffected
        final double newValue = 999.0;
        original.set(0, newValue);
        assertEquals("Clone should not be affected by changes to original",
                    testValues[0], cloned.get(0), EPSILON);
    }

    /**
     * Test getSubVector method.
     */
    @Test
    public void testGetSubVector() {
        final double[] values = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};
        final Vector v = new Vector("original", values);

        // Test structure: start index, number of components, expected values
        final Object[][] testCases = {
            {0, 3, new double[]{1.0, 2.0, 3.0}},      // First 3 elements
            {2, 3, new double[]{3.0, 4.0, 5.0}},      // Middle 3 elements
            {4, 3, new double[]{5.0, 6.0, 7.0}},      // Last 3 elements
            {0, 1, new double[]{1.0}},                 // Single element
            {0, 7, new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0}}  // Entire vector
        };

        for (int testIdx = 0; testIdx < testCases.length; testIdx++) {
            final Object[] testCase = testCases[testIdx];
            final int start = (Integer) testCase[0];
            final int numComponents = (Integer) testCase[1];
            final double[] expected = (double[]) testCase[2];

            final Vector subVector = v.getSubVector(start, numComponents);

            assertEquals("Test " + testIdx + ": SubVector size should be "
                + numComponents, numComponents, subVector.size());

            for (int i = 0; i < numComponents; i++) {
                assertEquals("Test " + testIdx + ": Component " + i + " should match",
                            expected[i], subVector.get(i), EPSILON);
            }
        }
    }

    /**
     * Test createRandom method.
     */
    @Test
    public void testCreateRandom() {
        final String randomVectorName = "randomVector";
        final int[] testSizes = {1, 5, 10, 20};

        for (int size : testSizes) {
            final Vector v = Vector.createRandom(randomVectorName, size);

            assertNotNull("Random vector should not be null", v);
            assertEquals("Random vector should have correct name",
                        randomVectorName, v.getName());
            assertEquals("Random vector should have correct size",
                        size, v.size());

            // Check all values are in range [0, 1)
            for (int i = 0; i < size; i++) {
                final double value = v.get(i);
                assertTrue("Value " + i + " should be >= 0",
                          value >= 0.0);
                assertTrue("Value " + i + " should be < 1",
                          value < 1.0);
            }
        }
    }

    /**
     * Test createRandom with invalid size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRandomInvalidSize() {
        Vector.createRandom("invalid", 0);
        fail("Expected IllegalArgumentException for zero size");
    }

    /**
     * Test getX method with valid size.
     */
    @Test
    public void testGetX() {
        final double expectedValue = 1.5;
        final double[] values = {expectedValue, 2.5, 3.5};
        final Vector v = new Vector(values);

        assertEquals("getX should return first component",
                    expectedValue, v.getX(), EPSILON);
    }

    /**
     * Test getY method with valid size.
     */
    @Test
    public void testGetY() {
        final double expectedValue = 2.5;
        final double[] values = {1.5, expectedValue, 3.5};
        final Vector v = new Vector(values);

        assertEquals("getY should return second component",
                    expectedValue, v.getY(), EPSILON);
    }

    /**
     * Test getY method throws exception for vector with size < 2.
     */
    @Test(expected = RuntimeException.class)
    public void testGetYInvalidSize() {
        final Vector v = new Vector(1);  // Size 1, no Y component
        v.getY();  // Should throw RuntimeException
        fail("Expected RuntimeException for vector with size < 2");
    }

    /**
     * Test getZ method with valid size.
     */
    @Test
    public void testGetZ() {
        final double expectedValue = 3.5;
        final double[] values = {1.5, 2.5, expectedValue, 4.5};
        final Vector v = new Vector(values);

        assertEquals("getZ should return third component",
                    expectedValue, v.getZ(), EPSILON);
    }

    /**
     * Test getZ method throws exception for vector with size < 3.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetZInvalidSizeTwo() {
        final Vector v = new Vector(2);  // Size 2, no Z component
        v.getZ();  // Should throw IllegalArgumentException
        fail("Expected IllegalArgumentException for vector with size < 3");
    }

    /**
     * Test getZ method throws exception for vector with size 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetZInvalidSizeOne() {
        final Vector v = new Vector(1);  // Size 1, no Z component
        v.getZ();  // Should throw IllegalArgumentException
        fail("Expected IllegalArgumentException for vector with size < 3");
    }

    /**
     * Test set(double...) method with valid input.
     */
    @Test
    public void testSetVariableArgs() {
        final int vectorSize = 4;
        final Vector v = new Vector(vectorSize);
        final double[] newValues = {10.0, 20.0, 30.0, 40.0};

        v.set(newValues);

        for (int i = 0; i < vectorSize; i++) {
            assertEquals("Component " + i + " should be updated",
                        newValues[i], v.get(i), EPSILON);
        }
    }

    /**
     * Test set(double...) method throws exception for size mismatch.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableArgsSizeMismatch() {
        final Vector v = new Vector(3);
        final double value = 3.0;
        v.set(value, value, value, value);  // 4 values for size 3 vector
        fail("Expected IllegalArgumentException for size mismatch");
    }

    /**
     * Test set(double...) method throws exception for too few values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableArgsTooFewValues() {
        final Vector v = new Vector(5);
        final double value = 3.0;
        v.set(value, value);  // 2 values for size 5 vector
        fail("Expected IllegalArgumentException for size mismatch");
    }

    /**
     * Test getXYZ methods on a 3D vector.
     */
    @Test
    public void testGetXYZOn3DVector() {
        final double x = 1.0;
        final double y = 2.0;
        final double z = 3.0;
        final Vector v = new Vector(x, y, z);

        assertEquals("X component should match", x, v.getX(), EPSILON);
        assertEquals("Y component should match", y, v.getY(), EPSILON);
        assertEquals("Z component should match", z, v.getZ(), EPSILON);
    }

    /**
     * Test getDimensionString returns size as string for Vector.
     */
    @Test
    public void testGetDimensionStringReturnsSize() {
        final Vector v = new Vector(5);
        assertEquals("5", v.getDimensionString());
    }

    /**
     * Test getDimensionString with different sizes.
     */
    @Test
    public void testGetDimensionStringVariousSizes() {
        final int[] sizes = {1, 3, 100};
        for (int size : sizes) {
            assertEquals(String.valueOf(size), new Vector(size).getDimensionString());
        }
    }

    /**
     * Test cross product with valid 3D vectors.
     */
    @Test
    public void testCrossProductValid() {
        final Vector v1 = new Vector("v1", 1.0, 0.0, 0.0);
        final Vector v2 = new Vector("v2", 0.0, 1.0, 0.0);

        final Vector result = v1.cross(v2);

        // i × j = k, so result should be (0, 0, 1)
        assertEquals(0.0, result.get(0), EPSILON);
        assertEquals(0.0, result.get(1), EPSILON);
        assertEquals(1.0, result.get(2), EPSILON);

        // Verify immutability
        assertFalse("cross() should return a new vector", v1 == result);
        assertFalse("cross() should return a new vector", v2 == result);
        assertEquals(1.0, v1.get(0), EPSILON);
        assertEquals(0.0, v1.get(1), EPSILON);
        assertEquals(0.0, v1.get(2), EPSILON);
    }

    /**
     * Test cross product with another example.
     */
    @Test
    public void testCrossProductExample() {
        final Vector v1 = new Vector("v1", 2.0, 3.0, 4.0);
        final Vector v2 = new Vector("v2", 5.0, 6.0, 7.0);

        final Vector result = v1.cross(v2);

        // Expected: (3*7 - 4*6, 4*5 - 2*7, 2*6 - 3*5) = (-3, 6, -3)
        final double[] expected = {-3.0, 6.0, -3.0};
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Component " + i + " should match expected",
                        expected[i], result.get(i), EPSILON);
        }
    }

    /**
     * Test cross product throws exception when this vector is not 3D.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCrossProductInvalidSizeThis() {
        final Vector v1 = new Vector("v1", 2);  // 2D vector
        final Vector v2 = new Vector("v2", 1.0, 2.0, 3.0);

        v1.cross(v2);
        fail("Expected IllegalArgumentException for cross product with non-3D vector");
    }

    /**
     * Test cross product throws exception when parameter vector is not 3D.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCrossProductInvalidSizeParameter() {
        final Vector v1 = new Vector("v1", 1.0, 2.0, 3.0);
        final Vector v2 = new Vector("v2", 4);  // 4D vector

        v1.cross(v2);
        fail("Expected IllegalArgumentException for cross product with non-3D vector");
    }

    /**
     * Test cross product throws exception when both vectors are not 3D.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCrossProductInvalidSizeBoth() {
        final Vector v1 = new Vector("v1", 2);  // 2D vector
        final Vector v2 = new Vector("v2", 4);  // 4D vector

        v1.cross(v2);
        fail("Expected IllegalArgumentException for cross product with non-3D vectors");
    }

    /**
     * Test cross product anti-commutativity: v1 × v2 = -(v2 × v1).
     */
    @Test
    public void testCrossProductAntiCommutativity() {
        final Vector v1 = new Vector("v1", 1.0, 2.0, 3.0);
        final Vector v2 = new Vector("v2", 4.0, 5.0, 6.0);

        final Vector result1 = v1.cross(v2);
        final Vector result2 = v2.cross(v1);

        // v1 × v2 should equal -(v2 × v1)
        assertEquals(-result2.get(0), result1.get(0), EPSILON);
        assertEquals(-result2.get(1), result1.get(1), EPSILON);
        assertEquals(-result2.get(2), result1.get(2), EPSILON);
    }

    /**
     * Test cross product of parallel vectors gives zero vector.
     */
    @Test
    public void testCrossProductParallelVectors() {
        final Vector v1 = new Vector("v1", 1.0, 2.0, 3.0);
        final Vector v2 = new Vector("v2", 2.0, 4.0, 6.0);  // v2 = 2 * v1

        final Vector result = v1.cross(v2);

        // Cross product of parallel vectors should be zero vector
        for (int i = 0; i < result.size(); i++) {
            assertEquals(0.0, result.get(i), EPSILON);
        }
    }
}
