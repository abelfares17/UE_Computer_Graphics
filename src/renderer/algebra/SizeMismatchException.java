
package renderer.algebra;

/**
 * Exception class for incorrect dimensions in arithmetic operations on Matrix
 * and Vector.
 */
public class SizeMismatchException extends RuntimeException {

    /**
     * Constructor for SizeMismatchException.
     */
    public SizeMismatchException() {
        super();
    }

    /**
     * Constructor for SizeMismatchException with array-based objects.
     * @param a1 first array-based object
     * @param a2 second array-based object
     */
    public SizeMismatchException(ArrayBase a1, ArrayBase a2) {
        super(a1.getName() + "[" + a1.getDimensionString()
                + "] != " + a2.getName() + "[" + a2.getDimensionString() + "]");
    }

    /**
     * Constructor for SizeMismatchException when using vectors and matrices.
     * @param msg error message
     */
    public SizeMismatchException(String msg) {
        super(msg);
    }

}
