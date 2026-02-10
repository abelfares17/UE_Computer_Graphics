package renderer.core.shader;

import renderer.algebra.Matrix;

/**
 * The DepthBuffer class implements a DepthBuffer and its pass test.
 */
public class DepthBuffer {
    /**
     * The buffer of depth values.
     */
    private Matrix buffer;

    /**
     * The width the buffer.
     */
    private int width;
    /**
     * The height the buffer.
     */
    private int height;

    /**
     * Constructs a DepthBuffer of size width x height.
     * The buffer is initially cleared.
     *
     * @param width  the width of the buffer
     * @param height the height of the buffer
     */
    public DepthBuffer(int width, int height) {
        buffer = new Matrix(height, width);
        this.width = width;
        this.height = height;
        clear();
    }

    /**
     * Clears the buffer to infinite depth for all fragments.
     */
    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buffer.set(i, j, Double.POSITIVE_INFINITY);
            }
        }

    }

    /**
     * Checks if the fragment coordinates are within the buffer bounds.
     *
     * @param f the fragment to check
     * @return true if coordinates are valid, false otherwise
     */
    private boolean isWithinBounds(Fragment f) {
        return f.getX() >= 0 && f.getX() < width
            && f.getY() >= 0 && f.getY() < height;
    }

    /**
     * Test if a fragment passes the DepthBuffer test, i.e. is the fragment the
     * closest at its position.
     *
     * @param f the fragment to test
     * @return true if the fragment passes the test, false otherwise
     */
    public boolean testFragment(Fragment f) {
        if (isWithinBounds(f)) {
            // TODO
            return false;
        } else {
            return false;
        }
    }

    /**
     * Writes the fragment depth to the buffer.
     *
     * @param f the fragment to write
     */
    public void writeFragment(Fragment f) {
        if (isWithinBounds(f)) {
            // TODO
        }
    }

    /**
     * Resize the buffer if it's needed.
     *
     * @param nWidth  the new width
     * @param nHeight the new height
     */
    public void resize(int nWidth, int nHeight) {
        if (width == nWidth && height == nHeight) {
            clear();
            return;
        }
        width = nWidth;
        height = nHeight;
        buffer = new Matrix(height, width);
        clear();
    }

}
