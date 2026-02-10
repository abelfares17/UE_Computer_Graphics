/**
 * Basic linear algebra methods.
 * @author: cdehais
 */

package renderer.algebra;

/**
 * The Vector class represents a mathematical vector and provides methods to
 * manipulate it.
 */
public class Vector extends ArrayBase implements Cloneable {

    /**
     * The default name of the vector.
     */
    public static final String DEFAULT_NAME = "v";

    /**
     * The dimension of Z.
     */
    private static final int DIM_Z = 3;

    /**
     * Creates a random vector of the specified size.
     * Each element is filled with a random value between 0.0 (inclusive) and
     * 1.0 (exclusive).
     *
     * @param name  the name of the vector
     * @param nRows the size of the vector (must be strictly positive)
     * @return a new vector of size {@code nRows} filled with random values
     * @throws IllegalArgumentException if the vector dimension is invalid
     */
    public static Vector createRandom(final String name, final int nRows)
            throws IllegalArgumentException {
        final Vector v = new Vector(name, nRows);
        v.fillRandom();
        return v;
    }

    /**
     * Creates a named vector of the given size.
     *
     * @param vName  the name of the vector
     * @param length the size of the vector
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final String vName, final int length) {
        super(vName, length);
    }

    /**
     * Creates a vector of the given size.
     *
     * @param size the size of the vector
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final int size) {
        this(DEFAULT_NAME, size);
    }

    /**
     * Creates a named vector from the given values.
     *
     * @param name   the name of the vector
     * @param values the values to initialize the vector with
     */
    public Vector(final String name, final double... values) {
        this(name, values.length);
        copyValues(values);
    }

    /**
     * Creates a vector from an array of values.
     *
     * @param initValues the array of values
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final double... initValues) {
        this(DEFAULT_NAME, initValues);
    }

    /**
     * Creates a vector by copying another vector.
     *
     * @param v the vector to copy
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final Vector v) {
        this(v.size());
        copyValues(v.getValues());
    }

    /**
     * Computes the Euclidean norm (L2 norm) of the vector.
     *
     * @return the Euclidean norm of the vector (always non-negative)
     */
    public final double norm() {
        double r = 0.0;
        for (int i = 0; i < size(); i++) {
            r += this.get(i) * this.get(i);
        }
        return Math.sqrt(r);
    }

    /**
     * Returns a normalized vector.
     * This method does NOT modify the current vector.
     * If the norm is 0, returns a new zero vector of the same size.
     *
     * @return a new Vector that is the normalized version of this vector
     */
    public Vector normalize() {
        final double norm = norm();
        if (norm > 0) {
            return scale(1. / norm);
        } else {
            return new Vector(this.size());
        }
    }

    /**
     * Returns the homogeneous representation of the Vector as a point.
     * This method does NOT modify the current vector.
     *
     * @return a new Vector with the same elements plus an additional 1.0 at the end
     */
    public Vector homogeneousPoint() {
        return homogeneous(1.0);
    }

    /**
     * Returns the homogeneous representation of the Vector as a direction.
     * This method does NOT modify the current vector.
     *
     * @return a new Vector with the same elements plus an additional 0.0 at the end
     */
    public Vector homogeneousVector() {
        return homogeneous(0.0);
    }

    /**
     * Helper method to create a homogeneous representation with a specified w coordinate.
     * This method does NOT modify the current vector.
     *
     * @param w the value to set as the last coordinate (1.0 for point, 0.0 for direction)
     * @return a new Vector with the same elements plus the specified w value at the end
     */
    private Vector homogeneous(double w) {
        double[] h = new double[size() + 1];
        System.arraycopy(getValues(), 0, h, 0, size());
        h[size()] = w;
        return new Vector(h);
    }

    /**
     * Computes the dot product (scalar product) between this vector and another vector.
     * Both vectors must have the same size.
     *
     * @param v the vector to compute the dot product with
     * @return the dot product (a scalar value)
     * @throws SizeMismatchException if the two vectors are not the same size
     */
    public double dot(final Vector v) {
        validateSameSize(v);
        double result = 0.0;
        for (int i = 0; i < this.size(); i++) {
            result += this.get(i) * v.get(i);
        }
        return result;
    }

    /**
     * Returns a string representation of the vector in MATLAB-compatible format.
     * The format is: "name = [v0, v1, ..., vn]';"
     *
     * @return a MATLAB-compatible string representation of the vector
     */
    public String toString() {
        StringBuilder str = new StringBuilder(getName() + " = [");

        for (int i = 0; i < size() - 1; i++) {
            str.append(get(i) + ", ");
        }

        str.append(get(size() - 1) + "]';");
        return str.toString();
    }

    /**
     * Sets the i-th coordinate to the given value.
     * This method MODIFIES the current vector.
     *
     * @param i     the index of the coordinate to set
     * @param value the value to set the coordinate to
     */
    public void set(int i, double value) {
        setValue(i, value);
    }

    /**
     * Sets all elements of the vector to 0.
     * This method MODIFIES the current vector.
     *
     * @return this vector (modified) filled with zeros
     */
    public Vector zeros() {
        setAll(.0);
        return this;
    }

    /**
     * Sets all elements of the vector to 1.
     * This method MODIFIES the current vector.
     *
     * @return this vector (modified) filled with ones
     */
    public Vector ones() {
        setAll(1.0);
        return this;
    }

    /**
     * Gets the i-th coordinate of the Vector.
     *
     * @param i the index of the coordinate to get
     * @return the value of the i-th coordinate
     */
    public double get(int i) {
        return getValue(i);
    }

    /**
     * Clamps the values of the Vector between the given minimum and maximum.
     * This method does NOT modify the current vector.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a copy of this vector with clamped values
     */
    public Vector clamp(final double min, final double max) {
        Vector copy = this.clone();
        for (int i = 0; i < size(); i++) {
            final var value = MathUtils.clamp(copy.getValue(i), min, max);
            copy.setValue(i, value);
        }
        return copy;
    }

    /**
     * Vector/Vector subtraction : this - v.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to subtract
     * @return a new Vector containing the result of this - v
     * @throws SizeMismatchException if the vector sizes do not match for subtraction
     */
    public Vector subtract(Vector v) {
        Vector result = new Vector(this.size());
        subtractValues(v.getValues(), result.getValues());
        return result;
    }

    /**
     * Vector scaling : f * this.
     * This method does NOT modify the current vector.
     *
     * @param f the scalar to multiply by
     * @return a new Vector containing the result of f * this
     */
    public Vector scale(double f) {
        Vector result = new Vector(this.size());
        scaleValues(f, result.getValues());
        return result;
    }

    /**
     * Vector/Vector addition : this + v.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to add
     * @return a new Vector containing the result of this + v
     * @throws SizeMismatchException if the vector sizes do not match for addition
     */
    public Vector add(Vector v) {
        Vector result = new Vector(this.size());
        addValues(v.getValues(), result.getValues());
        return result;
    }

    /**
     * Returns the cross product : this × v.
     * This operation is only possible in dimension 3.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to compute cross product with
     * @return a new Vector containing the result of this × v
     * @throws IllegalArgumentException if either vector is not 3-dimensional
     */
    public Vector cross(Vector v) {
        validateCrossProductSize();
        v.validateCrossProductSize();
        final Vector res = new Vector(3);
        res.set(0, this.get(1) * v.get(2) - this.get(2) * v.get(1));
        res.set(1, this.get(2) * v.get(0) - this.get(0) * v.get(2));
        res.set(2, this.get(0) * v.get(1) - this.get(1) * v.get(0));
        return res;
    }

    /**
     * Set the values of the vector.
     * This method MODIFIES the current vector.
     *
     * @param values the values (can be a double[] or doubles values separated by a comma)
     * @throws RuntimeException if the number of values does not match the vector size
     */
    public void set(double... values) {
        if (values.length != this.size()) {
            throw new IllegalArgumentException("the number of values given has not "
                    + "the same number as the size of the vector.");
        }
        for (int i = 0; i < values.length; i++) {
            this.setValue(i, values[i]);
        }
    }

    /**
     * Gets the X component (the first element) of the vector.
     *
     * @return the value at index 0
     * @throws IllegalArgumentException if the vector size is less than 1
     */
    public double getX() {
        validateMinimumSize(1, "X");
        return getValue(0);
    }

    /**
     * Gets the Y component (the second element) of the vector.
     *
     * @return the value at index 1
     * @throws IllegalArgumentException if the vector size is less than 2
     */
    public double getY() {
        validateMinimumSize(2, "Y");
        return getValue(1);
    }

    /**
     * Gets the Z component (the third element) of the vector.
     *
     * @return the value at index 2
     * @throws IllegalArgumentException if the vector size is less than 3
     */
    public double getZ() {
        validateMinimumSize(DIM_Z, "Z");
        return getValue(2);
    }

    /**
     * Converts this vector to a column matrix.
     * This method does NOT modify the current vector.
     *
     * @return a new Matrix representing this vector as a column matrix
     */
    public Matrix toMatrix() {
        Matrix m = new Matrix(size(), 1);
        m.copyValues(getValues());
        return m;
    }

    /**
     * Creates and returns a copy of this vector.
     *
     * @return a new Vector that is a copy of this vector
     */
    @Override
    protected Vector clone() {
        return new Vector(this);
    }

    /**
     * Validates that the vector has at least the specified minimum size.
     *
     * @param minSize the minimum required size
     * @param compName the name of the component being accessed (for error message)
     * @throws IllegalArgumentException if the vector size is less than minSize
     */
    private void validateMinimumSize(final int minSize, final String compName) {
        if (size() < minSize) {
            throw new IllegalArgumentException(
                "There is no " + compName + " component, the size has to be at least "
                + minSize + " but here the size is " + size() + ".");
        }
    }

    /**
     * Validates that two vectors have the same size.
     *
     * @param other the other vector to compare with
     * @throws SizeMismatchException if the vectors have different sizes
     */
    private void validateSameSize(final Vector other) {
        if (this.size() != other.size()) {
            throw new SizeMismatchException(this, other);
        }
    }

    /**
     * Validates that this vector has size 3 (required for cross product).
     *
     * @throws IllegalArgumentException if the vector is not 3-dimensional
     */
    private void validateCrossProductSize() {
        if (size() != DIM_Z) {
            throw new IllegalArgumentException(
                "Cross product requires 3-dimensional vectors, but vector size is "
                + size() + ".");
        }
    }

    /**
     * Return a subvector from start with numberOfComponent components.
     * This method does NOT modify the current vector.
     *
     * @param start             the start index
     * @param numberOfComponent the number of components
     * @return a new Vector containing the extracted subvector
     */
    public Vector getSubVector(final int start, final int numberOfComponent) {
        Vector subVector = new Vector(numberOfComponent);
        subVector.copyValues(getValues(), start, numberOfComponent);
        return subVector;
    }

}
