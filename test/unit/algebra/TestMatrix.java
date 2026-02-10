package algebra;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;


public class TestMatrix {

    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;

    /**
     * The name for test matrices.
     */
    private static final String TEST_MATRIX_NAME = "testMatrix";

    /**
     * The name for invalid matrices.
     */
    private static final String INVALID_MATRIX_NAME = "invalidMatrix";

    /**
     * Helper method to create and populate a matrix from a 2D array.
     * @param name the name of the matrix
     * @param values the values to populate the matrix with
     * @return the populated matrix
     */
    private Matrix createMatrixFromArray(String name, double[][] values) {
        final Matrix matrix = new Matrix(name, values.length, values[0].length);
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                matrix.set(i, j, values[i][j]);
            }
        }
        return matrix;
    }

    /**
     * Helper method to verify matrix values against expected values.
     * @param expected the expected values
     * @param actual the actual matrix
     */
    private void assertMatrixEquals(double[][] expected, Matrix actual) {
        assertEquals("Matrix row count mismatch",
                expected.length, actual.getNRows());
        assertEquals("Matrix column count mismatch",
                expected[0].length, actual.getNCols());
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals("Value mismatch at [" + i + "][" + j + "]",
                        expected[i][j], actual.get(i, j), EPSILON);
            }
        }
    }

    /**
     * Test the creation of a Matrix.
     */
    @Test
    public void testMatrixCreation() {
        final int numRows = 3;
        final int numCols = 3;
        final int numCols2 = 2;
        final Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        assertNotNull(m);
        assertEquals(TEST_MATRIX_NAME, m.getName());
        assertEquals(numRows, m.getNRows());
        assertEquals(numCols, m.getNCols());

        final Matrix m2 = new Matrix("rectMatrix", numRows, numCols2);
        assertNotNull(m2);
        assertEquals("rectMatrix", m2.getName());
        assertEquals(numRows, m2.getNRows());
        assertEquals(numCols2, m2.getNCols());
    }

    /**
     * Test the creation of an invalid Matrix with 0 row.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatrixCreation() throws IllegalArgumentException {
        final int numCols = 3;
        new Matrix(INVALID_MATRIX_NAME, 0, numCols);
    }

    /**
     * Test the creation of an invalid Matrix with negative rows.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleNegativeSizesMatrixCreation1()
            throws IllegalArgumentException {
        final int negativeRows = -5;
        final int positiveCols = 4;
        new Matrix(INVALID_MATRIX_NAME, negativeRows, positiveCols);
    }

    /**
     * Test the creation of an invalid Matrix with negative cols.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleNegativeSizesMatrixCreation2()
            throws IllegalArgumentException {
        final int numCols = 2;
        final int negativeCols = -2;
        new Matrix(TEST_MATRIX_NAME, numCols, negativeCols);
    }

    /**
     * Test the creation of an invalid Matrix with negative rows and cols.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNegativeSizesMatrixCreation() throws IllegalArgumentException {
        final int negativeRows = -5;
        final int negativeCols = -4;
        new Matrix(INVALID_MATRIX_NAME, negativeRows, negativeCols);
    }

    /**
     * Test the set and get methods of a Matrix.
     * @throws IllegalArgumentException
     */
    @Test
    public void testSetAndGets() throws IllegalArgumentException {
        final int numRows = 2;
        final int numCols = 3;
        final Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                m.set(i, j, value);
                assertEquals(value, m.get(i, j), EPSILON);
                value += 1.0;
            }
        }
    }

    /**
     * Test the set method with an invalid indices.
     * @throws IndexOutOfBoundsException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidValue() {
        final Matrix m = new Matrix(TEST_MATRIX_NAME, 2, 2);
        m.set(2, 2, .0);
    }

    /**
     * Test the get method with an invalid indices.
     * @throws IndexOutOfBoundsException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidValue() {
        final Matrix m = new Matrix(TEST_MATRIX_NAME, 2, 2);
        m.get(2, 2);
    }

    /**
     * Test the transpose of a square matrix.
     */
    @Test
    public void testTransposeSquareMatrix() {
        final int numRows = 4;
        final int numCols = 4;
        final Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                m.set(i, j, value++);
            }
        }
        final Matrix transposed = m.transpose();
        value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                assertEquals(value++, transposed.get(j, i), EPSILON);
            }
        }
    }

    /**
     * Test the transpose of a rectangular matrix.
     */
    @Test
    public void testTransposeRectangularMatrix() {
        final int numRows = 3;
        final int numCols = 4;
        final Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
            m.set(i, j, value++);
            }
        }
        final Matrix transposed = m.transpose();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
            assertEquals(m.get(i, j), transposed.get(j, i), EPSILON);
            }
        }
    }

    /**
     * Test the multiplication of two square matrices.
     * Verifies that multiply() returns a new matrix and doesn't modify the originals.
     */
    @Test
    public void testMultiplySquareMatrices() {
        final double[][] m1Values = {
            {1.0, 2.0},
            {3.0, 4.0}
        };
        final double[][] m2Values = {
            {2.0, 0.0},
            {1.0, 2.0}
        };
        final double[][] expectedValues = {
            {4.0, 4.0},
            {10.0, 8.0}
        };

        final Matrix m1 = createMatrixFromArray("m1", m1Values);
        final Matrix m2 = createMatrixFromArray("m2", m2Values);
        final Matrix result = m1.multiply(m2);

        assertMatrixEquals(expectedValues, result);

        // Verify immutability - original matrices unchanged
        assertMatrixEquals(m1Values, m1);
        assertMatrixEquals(m2Values, m2);
    }

    /**
     * Test the multiplication of two rectangular matrices.
     */
    @Test
    public void testMultiplyRectangularMatrices() {
        final double[][] m1Values = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        final double[][] m2Values = {
            {7.0, 8.0},
            {9.0, 10.0},
            {11.0, 12.0}
        };
        final double[][] expectedValues = {
            {58.0, 64.0},
            {139.0, 154.0}
        };

        final Matrix m1 = createMatrixFromArray("m1", m1Values);
        final Matrix m2 = createMatrixFromArray("m2", m2Values);
        final Matrix result = m1.multiply(m2);

        assertMatrixEquals(expectedValues, result);
    }

    /**
     * Test the multiplication of a matrices with wrong sizes.
     * @throws SizeMismatchException
     */
    @Test(expected = SizeMismatchException.class)
    public void testMultiplyInvalidMatrices() {
        final Matrix m1 = new Matrix("m1", 2, 2);
        final Matrix m2 = new Matrix("m2", 3, 3);
        m1.multiply(m2);
    }

    /**
     * Test the identity matrix.
     * @throws SizeMismatchException
     */
    @Test
    public void testIdentityMatrix() {
        final int identitySize = 3;
        final Matrix m = Matrix.createIdentity("identity", identitySize);
        for (int i = 0; i < identitySize; i++) {
            for (int j = 0; j < identitySize; j++) {
                if (i == j) {
                    assertEquals(1.0, m.get(i, j), EPSILON);
                } else {
                    assertEquals(0.0, m.get(i, j), EPSILON);
                }
            }
        }
    }

    /**
     * Test setting the column values.
     */
    @Test
    public void testSetColValid() {
        final int sizeRow = 5;
        final int sizeCol = 3;
        final int colToSet = 1;
        final Matrix matrix = Matrix.createRandom(TEST_MATRIX_NAME, sizeRow, sizeCol);
        final Vector vector = new Vector(sizeRow);
        // Set vector values
        for (int i = 0; i < sizeRow; i++) {
            vector.set(i, i + 1.);
        }

        matrix.setCol(colToSet, vector);

        for (int i = 0; i < sizeRow; i++) {
            assertEquals(i + 1., matrix.get(i, colToSet), EPSILON);
        }
    }

    /**
     * Test setting the column values with an invalid vector size.
     * @throws IllegalArgumentException
     */
    @Test
    public void testSetColInvalidVectorSize() {
        final int sizeMat = 3;
        final Matrix matrix = new Matrix(sizeMat, sizeMat);
        final Vector vector = new Vector(2); // Invalid size

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            matrix.setCol(1, vector));

        assertEquals("Vector size does not match matrix size", exception.getMessage());
    }

    /**
     * Test setting the column values with column.
     */
    @Test
    public void testGetColValidIndex() {
        final int numRows = 3;
        final int numCols = 3;
        final Matrix matrix = new Matrix(numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix.set(i, j, value++);
            }
        }

        for (int j = 0; j < numCols; j++) {
            final Vector col = matrix.getCol(j);
            for (int i = 0; i < numRows; i++) {
                assertEquals((i * numCols) + (j + 1), col.get(i), EPSILON);
            }
        }
    }

    /**
     * Test getting the column values with an invalid index.
     */
    @Test
    public void testGetColInvalidIndexTooLarge() {
        final int squareMat = 3;
        final int invalidColNegative = -4;
        Matrix matrix = new Matrix(squareMat, squareMat);
        assertThrows(IllegalArgumentException.class, () ->
            matrix.getCol(squareMat));
        assertThrows(IllegalArgumentException.class, () ->
            matrix.getCol(invalidColNegative));
    }

    /**
     * Test setting the column values with an invalid index.
     */
    @Test
    public void testSetColInvalidColumnIndex()  {
        Matrix matrix;
        final int numRows = 3;
        final int numCols = 3;
        final int invalidColNegative = -1;
        final int invalidColPositive = 3;
        matrix = new Matrix(numRows, numCols);

        Vector vector = new Vector(numRows);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setCol(invalidColNegative, vector);
        });

        assertEquals("Invalid column index", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setCol(invalidColPositive, vector);
        });

        assertEquals("Invalid column index", exception.getMessage());
    }

    /**
     * Test setting the row values.
     */
    @Test
    public void testSetRowValid() {
        final int numRows = 3;
        final int numCols = 5;
        final int rowToSet = 2;
        final Matrix matrix = Matrix.createRandom("test", numRows, numCols);
        final Vector vector = new Vector(numCols);
        // set vector values
        for (int i = 0; i < numCols; i++) {
            vector.set(i, i + 1.);
        }

        matrix.setRow(rowToSet, vector);

        for (int i = 0; i < numCols; i++) {
            assertEquals(i + 1., matrix.get(rowToSet, i), EPSILON);
        }
    }

    /**
     * Test setting the row values with an invalid vector size.
     */
    @Test
    public void testSetRowInvalidSize() {
        final int numRows = 3;
        final int numCols = 3;
        final Matrix matrix = new Matrix(numRows, numCols);
        final Vector vector = new Vector(2); // Invalid size
        vector.set(0, 1.0);
        vector.set(1, 2.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(1, vector);
        });

        assertEquals("Vector size does not match matrix size", exception.getMessage());
    }

    /**
     * Test setting the row values with an invalid index.
     */
    @Test
    public void testSetRowInvalidIndex() {
        final int numRows = 3;
        final int numCols = 3;
        final int invalidRowNegative = -1;
        final int invalidRowPositive = 3;
        final Matrix matrix = new Matrix(numRows, numCols);
        final Vector vector = new Vector(numCols);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(invalidRowNegative, vector);
        });

        assertEquals("Invalid row index", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(invalidRowPositive, vector);
        });

        assertEquals("Invalid row index", exception.getMessage());
    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowValidIndex() {
        final int numRows = 3;
        final int numCols = 4;
        final Matrix matrix = new Matrix(numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix.set(i, j, value++);
            }
        }

        for (int i = 0; i < numRows; i++) {
            Vector row = matrix.getRow(i);
            assertEquals(numCols, row.size());
            for (int j = 0; j < numCols; j++) {
                assertEquals((double) (i * numCols) + (j + 1), row.get(j), EPSILON);
            }
        }
    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowInvalidIndexNegative() {
        final int numRows = 3;
        final int numCols = 3;
        final Matrix matrix = new Matrix(numRows, numCols);
        assertThrows(IllegalArgumentException.class, () -> matrix.getRow(-1));

    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowInvalidIndexTooLarge() {
        final int numRows = 3;
        final int numCols = 3;
        final int invalidRowNegative = -3;
        final int invalidRowPositive = 3;
        final Matrix matrix = new Matrix(numRows, numCols);
        assertThrows(IllegalArgumentException.class, ()
            -> matrix.getRow(invalidRowPositive));
        assertThrows(IllegalArgumentException.class, ()
            -> matrix.getRow(invalidRowNegative));
    }

    /**
     * Test the addition of two squares Matrices.
     * Verifies that add() returns a new matrix and doesn't modify the originals.
     */
    @Test
    public void testAddSquaredMatrix() {
        final double[][] m1Values = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        final double[][] m2Values = {
            {9.0, 8.0, 7.0},
            {6.0, 5.0, 4.0},
            {3.0, 2.0, 1.0}
        };
        final double[][] expectedValues = {
            {10.0, 10.0, 10.0},
            {10.0, 10.0, 10.0},
            {10.0, 10.0, 10.0}
        };

        final Matrix m1 = createMatrixFromArray("m1", m1Values);
        final Matrix m2 = createMatrixFromArray("m2", m2Values);
        final Matrix res = m1.add(m2);

        assertMatrixEquals(expectedValues, res);

        // Verify immutability - original matrices unchanged
        assertMatrixEquals(m1Values, m1);
        assertMatrixEquals(m2Values, m2);
    }

    /**
     * Test the addition of two rectangular Matrices.
     */
    @Test
    public void testAddRectangleMatrix() {
        final double[][] m1Values = {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        };
        final double[][] m2Values = {
            {2.0, 3.0},
            {4.0, 5.0},
            {6.0, 7.0}
        };
        final double[][] expectedValues = {
            {3.0, 5.0},
            {7.0, 9.0},
            {11.0, 13.0}
        };

        final Matrix m1 = createMatrixFromArray("m1", m1Values);
        final Matrix m2 = createMatrixFromArray("m2", m2Values);
        final Matrix res = m1.add(m2);

        assertMatrixEquals(expectedValues, res);
    }

    /**
     * Test the addition of two matrices with different sizes.
     * Tests all branches of the size mismatch condition.
     */
    @Test
    public void testAddInvalidSize() {
        // Test structure: {m1Rows, m1Cols, m2Rows, m2Cols}
        final int[][] testCases = {
            {2, 3, 2, 4},  // Same rows, different columns
            {2, 3, 3, 3},  // Different rows, same columns
            {2, 3, 3, 4},  // Different rows and columns
            {3, 2, 2, 3}   // Completely mismatched
        };

        for (int i = 0; i < testCases.length; i++) {
            final int[] testCase = testCases[i];
            final int rows1 = 0;
            final int cols1 = 1;
            final int rows2 = 2;
            final int cols2 = 3;
            final Matrix m1 = new Matrix("m1", testCase[rows1], testCase[cols1]);
            final Matrix m2 = new Matrix("m2", testCase[rows2], testCase[cols2]);

            assertThrows("Test case " + i + " should throw SizeMismatchException",
                        SizeMismatchException.class, () -> m1.add(m2));
        }
    }


    /**
     * Test the subtraction of two squares matrix.
     * Verifies that subtract() returns a new matrix and doesn't modify the originals.
     */
    @Test
    public void testSubtractSquaresMatrix() {
        final double[][] m1Values = {
            {10.0, 8.0, 6.0},
            {4.0, 2.0, 0.0},
            {9.0, 7.0, 5.0}
        };
        final double[][] m2Values = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        final double[][] expectedValues = {
            {9.0, 6.0, 3.0},
            {0.0, -3.0, -6.0},
            {2.0, -1.0, -4.0}
        };

        final Matrix m1 = createMatrixFromArray("m1", m1Values);
        final Matrix m2 = createMatrixFromArray("m2", m2Values);
        final Matrix res = m1.subtract(m2);

        assertMatrixEquals(expectedValues, res);

        // Verify immutability - original matrices unchanged
        assertMatrixEquals(m1Values, m1);
        assertMatrixEquals(m2Values, m2);
    }

    /**
     * Test the subtraction of two matrices with different sizes.
     * Tests all branches of the size mismatch condition.
     */
    @Test
    public void testSubtractInvalidSize() {
        // Test structure: {m1Rows, m1Cols, m2Rows, m2Cols}
        final int[][] testCases = {
            {2, 3, 2, 4},  // Same rows, different columns
            {2, 3, 3, 3},  // Different rows, same columns
            {2, 3, 3, 4},  // Different rows and columns
            {3, 2, 2, 3}   // Completely mismatched
        };

        for (int i = 0; i < testCases.length; i++) {
            final int[] testCase = testCases[i];
            final int rows1 = 0;
            final int cols1 = 1;
            final int rows2 = 2;
            final int cols2 = 3;
            final Matrix m1 = new Matrix("m1", testCase[rows1], testCase[cols1]);
            final Matrix m2 = new Matrix("m2", testCase[rows2], testCase[cols2]);

            assertThrows("Test case " + i + " should throw SizeMismatchException",
                        SizeMismatchException.class, () -> m1.subtract(m2));
        }
    }

    /**
     * Test Matrix scale() and verify immutability.
     */
    @Test
    public void testScale() {
        final double[][] matrixValues = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        final double scaleFactor = 2.5;
        final double[][] expectedValues = {
            {2.5, 5.0, 7.5},
            {10.0, 12.5, 15.0}
        };

        final Matrix m = createMatrixFromArray("test", matrixValues);
        final Matrix scaled = m.scale(scaleFactor);

        // Verify result is correct
        assertMatrixEquals(expectedValues, scaled);

        // Verify immutability - original matrix unchanged
        assertMatrixEquals(matrixValues, m);
    }

    /**
     * Test Matrix transpose() and verify immutability.
     */
    @Test
    public void testTransposeImmutability() {
        final double[][] matrixValues = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        final double[][] expectedTransposed = {
            {1.0, 4.0},
            {2.0, 5.0},
            {3.0, 6.0}
        };

        final Matrix m = createMatrixFromArray("test", matrixValues);
        final Matrix transposed = m.transpose();

        // Verify result is correct
        assertMatrixEquals(expectedTransposed, transposed);

        // Verify immutability - original matrix unchanged
        assertMatrixEquals(matrixValues, m);
    }

    /**
     * Test getSubMatrix with valid parameters.
     */
    @Test
    public void testGetSubMatrixValid() {
        final double[][] matrixValues = {
            {1.0, 2.0, 3.0, 4.0},
            {5.0, 6.0, 7.0, 8.0},
            {9.0, 10.0, 11.0, 12.0},
            {13.0, 14.0, 15.0, 16.0}
        };
        final Matrix matrix = createMatrixFromArray("original", matrixValues);

        // Test structure: {offsetRow, offsetCol, numRows, numCols, expected values}
        final Object[][] testCases = {
            {0, 0, 2, 2, new double[][]{{1.0, 2.0}, {5.0, 6.0}}},      // Top-left
            {2, 2, 2, 2, new double[][]{{11.0, 12.0}, {15.0, 16.0}}},  // Bottom-right
            {1, 1, 2, 2, new double[][]{{6.0, 7.0}, {10.0, 11.0}}},    // Center
            {0, 0, 4, 4, matrixValues},                                 // Entire matrix
            {1, 0, 1, 4, new double[][]{{5.0, 6.0, 7.0, 8.0}}}         // Single row
        };

        for (int i = 0; i < testCases.length; i++) {
            final Object[] testCase = testCases[i];
            final int offsetRow = (Integer) testCase[0];
            final int offsetCol = (Integer) testCase[1];
            final int numRows = (Integer) testCase[2];
            final int numCols = (Integer) testCase[3];
            final double[][] expected = (double[][]) testCase[4];

            final Matrix subMatrix = matrix.getSubMatrix(offsetRow, offsetCol,
                numRows, numCols);
            assertMatrixEquals(expected, subMatrix);
        }
    }

    /**
     * Test getSubMatrix with invalid parameters.
     * Tests all exception conditions: negative offsets, invalid sizes, and out of bounds.
     */
    @Test
    public void testGetSubMatrixInvalid() {
        final int matrixRows = 4;
        final int matrixCols = 4;
        final Matrix matrix = new Matrix("test", matrixRows, matrixCols);

        // Test structure: {offsetRow, offsetCol, numRows, numCols, description}
        final Object[][] testCases = {
            {-1, 0, 2, 2, "Negative offsetRow"},
            {0, -1, 2, 2, "Negative offsetCol"},
            {0, 0, 0, 2, "Zero numRows"},
            {0, 0, 2, 0, "Zero numCols"},
            {0, 0, -1, 2, "Negative numRows"},
            {0, 0, 2, -1, "Negative numCols"},
            {3, 0, 2, 2, "offsetRow + numRows exceeds bounds"},
            {0, 3, 2, 2, "offsetCol + numCols exceeds bounds"},
            {2, 2, 3, 3, "Both exceed bounds"},
            {4, 0, 1, 1, "offsetRow equals matrix rows"},
            {0, 4, 1, 1, "offsetCol equals matrix cols"}
        };

        for (int i = 0; i < testCases.length; i++) {
            final Object[] testCase = testCases[i];
            final int offsetRow = (Integer) testCase[0];
            final int offsetCol = (Integer) testCase[1];
            final int numRows = (Integer) testCase[2];
            final int numCols = (Integer) testCase[3];
            final String description = (String) testCase[4];

            Exception exception = assertThrows(
                description + " should throw IllegalArgumentException",
                IllegalArgumentException.class,
                () -> matrix.getSubMatrix(offsetRow, offsetCol, numRows, numCols)
            );

            assertEquals("Invalid submatrix", exception.getMessage());
        }
    }

    /**
     * Test getDimensionString returns NxM format.
     */
    @Test
    public void testGetDimensionStringFormat() {
        final Matrix m = new Matrix(3, 4);
        assertEquals("3x4", m.getDimensionString());
    }

    /**
     * Test getDimensionString with square matrix.
     */
    @Test
    public void testGetDimensionStringSquareMatrix() {
        final Matrix m = new Matrix(5, 5);
        assertEquals("5x5", m.getDimensionString());
    }

    /**
     * Test getDimensionString with 1x1 matrix.
     */
    @Test
    public void testGetDimensionStringSingleElement() {
        final Matrix m = new Matrix(1, 1);
        assertEquals("1x1", m.getDimensionString());
    }

    /**
     * Test getDimensionString with large matrix.
     */
    @Test
    public void testGetDimensionStringLargeMatrix() {
        final Matrix m = new Matrix(100, 200);
        assertEquals("100x200", m.getDimensionString());
    }
}
