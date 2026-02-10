/*
 * @author: cdehais
 */

package renderer.algebra;

/**
 * Matrix class.
 */
public class Matrix extends ArrayBase {

    /**
     * The number of rows.
     */
    private final int nRows;

    /**
     * The number of columns.
     */
    private final int nCols;

    /**
     * The default name of the matrix.
     */
    public static final String DEFAULT_NAME = "M";

    /**
     * Validates matrix dimensions and computes the total size.
     *
     * @param numRows the number of rows
     * @param numCols the number of columns
     * @return the total size (numRows * numCols)
     * @throws IllegalArgumentException if either dimension is not positive
     */
    private static int validateAndComputeSize(final int numRows, final int numCols)
            throws IllegalArgumentException {
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException(
                "Matrix dimensions must be positive: rows=" + numRows
                + ",cols=" + numCols);
        }
        return numRows * numCols;
    }

    /**
     * Creates a named Matrix of size nRows x nCols.
     * @param name the name of the matrix
     * @param numRows number of rows
     * @param numCols number of columns
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public Matrix(final String name, final int numRows, final int numCols)
            throws IllegalArgumentException {
        super(name, validateAndComputeSize(numRows, numCols));
        this.nRows = numRows;
        this.nCols = numCols;
    }

    /**
     * Creates a Matrix of size nRows x nCols.
     * @param nRows number of rows
     * @param nCols number of columns
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public Matrix(final int nRows, final int nCols) throws IllegalArgumentException {
        this(DEFAULT_NAME, nRows, nCols);
    }

    /**
     * Creates an identity matrix of the given size with name "I"+size (e.g., "I3"
     * for a 3x3 identity matrix).
     *
     * @param size the size of the identity matrix (number of rows and columns)
     * @return a new identity matrix of size {@code size x size}
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(final int size)
            throws IllegalArgumentException {
        String name = "I" + size;
        return createIdentity(name, size);
    }

    /**
     * Creates an identity matrix of the given size and name.
     *
     * @param name the name of the matrix
     * @param size the size of the identity matrix (number of rows and columns)
     * @return a new identity matrix of size {@code size x size} with the given name
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(final String name, final int size)
            throws IllegalArgumentException {
        Matrix id = new Matrix(name, size, size);

        for (int i = 0; i < size; i++) {
            id.set(i, i, 1.0);
        }
        return id;
    }

    /**
     * Creates a random matrix of size nRows x nCols.
     * Each element is filled with a random value between 0.0 (inclusive)
     * and 1.0 (exclusive).
     *
     * @param name  the name of the matrix
     * @param nRows the number of rows (must be strictly positive)
     * @param nCols the number of columns (must be strictly positive)
     * @return a new {@code nRows x nCols} matrix filled with random values
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static Matrix createRandom(final String name, final int nRows, final int nCols)
            throws IllegalArgumentException {
        Matrix m = new Matrix(name, nRows, nCols);
        m.fillRandom();
        return m;
    }

    /**
     * Extracts a submatrix of size {@code numRows x numCols} with top-left corner at
     * position {@code (offsetRow, offsetCol)}.
     * This method does NOT modify the current matrix.
     *
     * @param offsetRow the starting row index (0-based)
     * @param offsetCol the starting column index (0-based)
     * @param numRows the number of rows to extract (must be at least 1)
     * @param numCols the number of columns to extract (must be at least 1)
     * @return a new Matrix containing the extracted {@code numRows x numCols} submatrix
     * @throws IllegalArgumentException if the submatrix dimensions are invalid or extend
     * beyond the matrix bounds
     */
    public Matrix getSubMatrix(final int offsetRow, final int offsetCol,
            final int numRows, final int numCols)
            throws IllegalArgumentException {
        if ((offsetRow < 0) || (offsetCol < 0) || (numRows < 1) || (numCols < 1)
                || (offsetRow + numRows > this.nRows)
                || (offsetCol + numCols > this.nCols)) {
            throw new IllegalArgumentException("Invalid submatrix");
        }

        final Matrix sub = new Matrix(numRows, numCols);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                final var value = this.get(i + offsetRow, j + offsetCol);
                sub.set(i, j, value);
            }
        }

        return sub;
    }

    /**
     * Get the transposed matrix.
     * This method does NOT modify the current matrix.
     *
     * @return a new Matrix that is the transpose of this matrix
     */
    public final Matrix transpose() {
        final Matrix trans = new Matrix(this.nCols, this.nRows);
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                trans.set(c, r, this.get(r, c));
            }
        }
        return trans;
    }

    /**
     * Matrix/Matrix multiplication.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to multiply with
     * @return a new Matrix containing the result of this * m
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public final Matrix multiply(final Matrix m) throws SizeMismatchException {
        if (nCols != m.nRows) {
            throw new SizeMismatchException(this, m);
        }

        final Matrix res = new Matrix(this.nRows, m.nCols);

        for (int i = 0; i < res.nRows; i++) {
            for (int j = 0; j < res.nCols; j++) {
                for (int k = 0; k < this.nCols; k++) {
                    final double value = res.get(i, j) + this.get(i, k) * m.get(k, j);
                    res.set(i, j, value);
                }
            }
        }

        return res;
    }

    /**
     * Matrix/Vector multiplication.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param v the vector to multiply with
     * @return a new Vector containing the result of this * v
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public final Vector multiply(final Vector v) throws SizeMismatchException {
        final var other = v.toMatrix();
        return new Vector(multiply(other).getValues());
    }


    /**
     * Matrix/Matrix addition.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to add with
     * @return a new Matrix containing the result of this + m
     * @throws SizeMismatchException if the matrix sizes do not match for addition
     */
    public final Matrix add(final Matrix m) throws SizeMismatchException {
        validateSameDimensions(m);
        final Matrix res = new Matrix(this.getNRows(), m.getNCols());
        this.addValues(m.getValues(), res.getValues());
        return res;
    }

    /**
     * Matrix/Matrix subtraction : this - m.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to subtract
     * @return a new Matrix containing the result of this - m
     * @throws SizeMismatchException if the matrix sizes do not match for subtraction
     */
    public final Matrix subtract(final Matrix m) throws SizeMismatchException {
        validateSameDimensions(m);
        final Matrix res = new Matrix(this.getNRows(), m.getNCols());
        this.subtractValues(m.getValues(), res.getValues());
        return res;
    }

    /**
     * Sets the element on row i and column j to the given value.
     * This method MODIFIES the current matrix.
     *
     * @param i the row index
     * @param j the column index
     * @param value the value to set
     */
    public void set(final int i, final int j, final double value) {
        setValue(i * nCols + j, value);
    }

    /**
     * Sets the i-th column in the matrix to the given vector.
     * This method MODIFIES the current matrix.
     *
     * @param i the column index (0-based)
     * @param v the vector to set (its size must match the number of rows)
     * @throws IllegalArgumentException if the vector size does not match the number of
     * rows or if the column index is invalid
     */
    public void setCol(final int i, final Vector v) {
        validateVectorSizeForColumn(v);
        validateColumnIndex(i);
        for (int j = 0; j < getNRows(); j++) {
            set(j, i, v.get(j));
        }
    }

    /**
     * Get the elements of the i-th column of the matrix as a vector.
     * This method does NOT modify the current matrix.
     *
     * @param i the column index
     * @return a new Vector containing the elements of column i
     * @throws IllegalArgumentException if the column index is invalid
     */
    public final Vector getCol(final int i) {
        validateColumnIndex(i);
        final Vector v = new Vector(getNRows());
        for (int j = 0; j < getNRows(); j++) {
            final var val = get(j, i);
            v.set(j, val);
        }
        return v;
    }

    /**
     * Sets the i-th row in the matrix to the given vector.
     * This method MODIFIES the current matrix.
     *
     * @param i the row index (0-based)
     * @param v the vector to set (its size must match the number of columns)
     * @throws IllegalArgumentException if the vector size does not match the number of
     * columns or if the row index is invalid
     */
    public void setRow(final int i, final Vector v) {
        validateVectorSizeForRow(v);
        validateRowIndex(i);
        for (int j = 0; j < getNCols(); j++) {
            final double value = v.get(j);
            set(i, j, value);
        }
    }

    /**
     * Get a copy of the elements of the i-th row of the matrix as a vector.
     * This method does NOT modify the current matrix.
     *
     * @param i the row index
     * @return a new Vector containing the elements of row i
     * @throws IllegalArgumentException if the row index is invalid
     */
    public final Vector getRow(final int i) {
        validateRowIndex(i);
        final Vector v = new Vector(getNCols());
        for (int j = 0; j < getNCols(); j++) {
            final var val = get(i, j);
            v.set(j, val);
        }
        return v;
    }

    /**
     * Gets the element at the specified position.
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position {@code (i, j)}
     */
    public final double get(final int i, final int j) {
        return getValue(i * getNCols() + j);
    }

    /**
     * Returns a Matlab compatible representation of the Matrix.
     * @return the string representation of the matrix
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(getName() + " = [");

        int spacing = str.length();
        for (int i = 0; i < getNRows(); i++) {
            if (i > 0) {
                for (int j = 0; j < spacing; j++) {
                    str.append(" ");
                }
            }
            for (int j = 0; j < getNCols(); j++) {
                str.append(get(i, j) + " ");
            }
            str.append(";\n");
        }

        str.append("];");

        return str.toString();
    }

    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    public final int getNRows() {
        return nRows;
    }

    /**
     * Returns the number of columns.
     * @return the number of columns
     */
    public final int getNCols() {
        return nCols;
    }

    @Override
    public String getDimensionString() {
        return nRows + "x" + nCols;
    }

    /**
     * Multiplies the Matrix by the given constant.
     * This method does NOT modify the current matrix.
     *
     * @param f the constant to multiply the matrix by
     * @return a new Matrix containing the result of this * f
     */
    public Matrix scale(final double f) {
        final Matrix res = new Matrix(getNRows(), getNCols());
        scaleValues(f, res.getValues());
        return res;
    }

    /**
     * Checks if this matrix has the same size as another matrix.
     *
     * @param m the matrix to compare with
     * @return true if this matrix has the same number of rows and columns as m,
     * false otherwise
     */
    private boolean sameSize(final Matrix m) {
        return getNRows() == m.getNRows() && getNCols() == m.getNCols();
    }

    /**
     * Validates that the column index is within valid bounds.
     *
     * @param columnIndex the column index to validate
     * @throws IllegalArgumentException if the column index is invalid
     */
    private void validateColumnIndex(final int columnIndex) {
        if (columnIndex < 0 || columnIndex >= nCols) {
            throw new IllegalArgumentException("Invalid column index");
        }
    }

    /**
     * Validates that the row index is within valid bounds.
     *
     * @param rowIndex the row index to validate
     * @throws IllegalArgumentException if the row index is invalid
     */
    private void validateRowIndex(final int rowIndex) {
        if (rowIndex < 0 || rowIndex >= nRows) {
            throw new IllegalArgumentException("Invalid row index");
        }
    }

    /**
     * Validates that a vector has the correct size for a column operation.
     *
     * @param v the vector to validate
     * @throws IllegalArgumentException if the vector size does not match
     * the number of rows
     */
    private void validateVectorSizeForColumn(final Vector v) {
        if (v.size() != nRows) {
            throw new IllegalArgumentException("Vector size does not match matrix size");
        }
    }

    /**
     * Validates that a vector has the correct size for a row operation.
     *
     * @param v the vector to validate
     * @throws IllegalArgumentException if the vector size does not match
     * the number of columns
     */
    private void validateVectorSizeForRow(final Vector v) {
        if (v.size() != nCols) {
            throw new IllegalArgumentException("Vector size does not match matrix size");
        }
    }

    /**
     * Validates that two matrices have the same dimensions.
     *
     * @param other the other matrix to compare with
     * @throws SizeMismatchException if the matrices have different dimensions
     */
    private void validateSameDimensions(final Matrix other) {
        if (!sameSize(other)) {
            throw new SizeMismatchException(this, other);
        }
    }

}
