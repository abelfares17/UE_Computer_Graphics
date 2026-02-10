package renderer.algebra;

/**
 * Abstract base class for array-based mathematical objects.
 * Contains only operations that work on flat arrays without regard to structure.
 */
public abstract class ArrayBase {

    /**
     * The name of this mathematical object.
     */
    private String name;

    /**
     * The underlying data storage as a flat array.
     */
    private final double[] values;

    /**
     * Constructor for subclasses.
     *
     * @param name the name
     * @param size the total number of elements
     * @throws IllegalArgumentException if size is not positive
     */
    protected ArrayBase(final String name, final int size)
            throws IllegalArgumentException {
        if (size < 1) {
            throw new IllegalArgumentException(
                "Size must be strictly positive");
        }
        this.values = new double[size];
        this.name = name;
    }

    /**
     * Constructor for subclasses.
     *
     * @param size the total number of elements
     * @throws IllegalArgumentException if size is not positive
     */
    protected ArrayBase(final int size)
            throws IllegalArgumentException {
       this("", size);
    }

    /**
     * Gets the total number of elements.
     *
     * @return the number of elements
     */
    public final int size() {
        return values.length;
    }

    /**
     * Gets a string representation of the dimensions of this object.
     * Subclasses should override to provide meaningful dimension info.
     *
     * @return a string describing the dimensions
     */
    public String getDimensionString() {
        return String.valueOf(size());
    }

    /**
     * Fills the internal array with random values between 0.0 (inclusive)
     * and 1.0 (exclusive).
     * This is a pure array operation.
     */
    protected final void fillRandom() {
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.random();
        }
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the underlying values array.
     * @return the values array
     */
    protected final double[] getValues() {
        return values;
    }

    /**
     * Validates that all arrays have the same size for binary operations.
     *
     * @param other the other operand array
     * @param dest the destination array
     * @throws IllegalArgumentException if sizes don't match
     */
    private void validateBinaryOperationSizes(final double[] other, final double[] dest) {
        if (values.length != other.length || values.length != dest.length) {
            throw new IllegalArgumentException(
                "All arrays must have the same size");
        }
    }

    /**
     * Validates that destination array has the correct size.
     *
     * @param dest the destination array
     * @throws IllegalArgumentException if size doesn't match
     */
    private void validateDestinationSize(final double[] dest) {
        if (values.length != dest.length) {
            throw new IllegalArgumentException(
                "Destination array size must match source size");
        }
    }

    /**
     * Sets the value at a specific index.
     * This is a pure array operation.
     *
     * @param index the index
     * @param value the value to set
     */
    protected final void setValue(final int index, final double value) {
        values[index] = value;
    }

    /**
     * Gets the value at a specific index.
     * This is a pure array operation.
     *
     * @param index the index
     * @return the value at the specified index
     */
    protected final double getValue(final int index) {
        return values[index];
    }

    /**
     * Sets all elements to a specific value.
     * This is a pure array operation.
     *
     * @param value the value to set
     */
    protected final void setAll(final double value) {
        java.util.Arrays.fill(values, value);
    }

    /**
     * Copies values from a source array into the internal storage.
     * Copies from source[start] to source[start + length - 1] into values[0]
     * to values[length - 1].
     * This is a pure array operation.
     *
     * @param source the source array
     * @param start the starting index in the source array
     * @param length the number of elements to copy
     * @throws IllegalArgumentException if start or length are invalid or out of bounds
     */
    protected final void copyValues(final double[] source,
                                    final int start,
                                    final int length) {
        if (start < 0 || length < 0 || length > values.length) {
            throw new IllegalArgumentException(
                "Invalid start or length for copy operation");
        }
        if (start > source.length - length) {
            throw new IllegalArgumentException(
                "Invalid start or length for copy operation");
        }
        System.arraycopy(source, start, values, 0, length);
    }

    /**
     * Copies values from a source array into the internal storage.
     * This is a pure array operation.
     *
     * @param source the source array
     * @throws IllegalArgumentException if source size doesn't match
     */
    protected final void copyValues(final double[] source) {
        if (values.length != source.length) {
            throw new IllegalArgumentException(
                "Source array size must match destination size");
        }
        System.arraycopy(source, 0, values, 0, values.length);
    }

    /**
     * Scales all elements by a constant factor directly into a destination array.
     * This is a pure array operation.
     *
     * @param factor the scaling factor
     * @param dest the destination array
     * @throws IllegalArgumentException if destination size doesn't match
     */
    protected final void scaleValues(final double factor, final double[] dest) {
        validateDestinationSize(dest);
        for (int i = 0; i < values.length; i++) {
            dest[i] = values[i] * factor;
        }
    }

    /**
     * Element-wise addition of two arrays directly into a destination array.
     * This is a pure array operation.
     *
     * @param other the other array
     * @param dest the destination array
     * @throws IllegalArgumentException if sizes don't match
     */
    protected final void addValues(final double[] other, final double[] dest) {
        validateBinaryOperationSizes(other, dest);
        for (int i = 0; i < values.length; i++) {
            dest[i] = values[i] + other[i];
        }
    }

    /**
     * Element-wise subtraction of two arrays directly into a destination array.
     * This is a pure array operation.
     *
     * @param other the other array
     * @param dest the destination array
     * @throws IllegalArgumentException if sizes don't match
     */
    protected final void subtractValues(final double[] other, final double[] dest) {
        validateBinaryOperationSizes(other, dest);
        for (int i = 0; i < values.length; i++) {
            dest[i] = values[i] - other[i];
        }
    }

    /**
     * Element-wise multiplication of two arrays directly into a destination array.
     * This is a pure array operation.
     *
     * @param other the other array
     * @param dest the destination array
     * @throws IllegalArgumentException if sizes don't match
     */
    protected final void multiplyValues(final double[] other, final double[] dest) {
        validateBinaryOperationSizes(other, dest);
        for (int i = 0; i < values.length; i++) {
            dest[i] = values[i] * other[i];
        }
    }

    /**
     * Computes the sum of all elements.
     * This is a pure array operation.
     *
     * @return the sum of all elements
     */
    protected final double sumValues() {
        return java.util.stream.DoubleStream.of(values).sum();
    }
}
