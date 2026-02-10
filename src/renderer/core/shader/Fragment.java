package renderer.core.shader;

import java.awt.Color;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;

/**
 * The Fragment class represents an attributed 'pixel' as generated
 * by a Rasterizer.
 * @author cdehais
 */
public class Fragment {

    /**
     * The pixel x-coordinate of the Fragment.
     */
    private int x;
    /**
     * The pixel y-coordinate of the Fragment.
     */
    private int y;
    /**
     * The number of attributes of the Fragment.
     */
    private int numAttributes;

    /** The number of attributes of the Fragment. */
    private static final int NUM_ATTRIBUTES = 9;

    /**
     * attributes placement:
     * 0: depth
     * 1-3: color
     * 4-6: normal
     * 7-8: (u,v) texture coordinates.
     */
    private double[] attributes;

    // Attribute indices
    /** the index for the depth. */
    public static final int DEPTH = 0;
    /** the red component of the color. */
    public static final int COLOR_R = 1;
    /** the green component of the color. */
    public static final int COLOR_G = 2;
    /** the blue component of the color. */
    public static final int COLOR_B = 3;
    /** the x component of the normal. */
    public static final int NORMAL_X = 4;
    /** the y component of the normal. */
    public static final int NORMAL_Y = 5;
    /** the z component of the normal. */
    public static final int NORMAL_Z = 6;
    /** the u texture coordinate. */
    public static final int TEXTURE_U = 7;
    /** the v texture coordinate. */
    public static final int TEXTURE_V = 8;

    /** maximum value for the color. */
    private static final double MAX_PIX_VAL = 255;

    /**
     * Creates a Fragment at pixel coordinates (x, y).
     * @param x the x pixel coordinate of the Fragment
     * @param y the y pixel coordinate of the Fragment
     */
    public Fragment(int x, int y) {
        this.x = x;
        this.y = y;
        numAttributes = NUM_ATTRIBUTES;
        attributes = new double[numAttributes];
    }

    /**
     * Creates a Fragment at pixel coordinates (x, y) with the given depth.
     * @return the number of attributes of the Fragment
     */
    public int getNumAttributes() {
        return numAttributes;
    }

    /**
     * Gets a scalar attribute at index.
     * @param index the index of the attribute to get
     * @return the value of the attribute at index
     */
    public double getAttribute(int index) {
        return attributes[index];
    }

    /**
     * Gets a vector attribute at the given starting location and with the given
     * dimension.
     * @param index the starting index of the attribute
     * @param dimension the dimension of the attribute
     * @return the attribute as a double array
     */
    public double[] getAttribute(int index, int dimension) {
        return java.util.Arrays.copyOfRange(attributes, index, index + dimension);
    }

    /**
     * Sets a scalar attribute at index.
     * @param index the index of the attribute to set
     * @param value the value to set the attribute to
     */
    public void setAttribute(int index, double value) {
        attributes[index] = value;
    }

    /**
     * Gets the x pixel coordinate of the Fragment.
     * @return the x pixel coordinate of the Fragment
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y pixel coordinate of the Fragment.
     * @return the y pixel coordinate of the Fragment
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the pixel coordinates (x, y) of the Fragment as a size 2 array.
     * @return the pixel coordinates (x, y) of the Fragment as a size 2 array
     */
    public int[] getPosition() {
        return new int[]{x, y};
    }

    /**
     * Sets the pixel coordinates (x, y) of the Fragment.
     * @param xc the x pixel coordinate of the Fragment
     * @param yc the y pixel coordinate of the Fragment
     */
    public void setPosition(int xc, int yc) {
        this.x = xc;
        this.y = yc;
    }

    /**
     * Gets the depth of the Fragment.
     * @return the depth of the Fragment
     */
    public double getDepth() {
        return attributes[DEPTH];
    }

    /**
     * Sets the depth of the Fragment.
     * @param z the depth of the Fragment
     */
    public void setDepth(double z) {
        attributes[DEPTH] = z;
    }

    /**
     * Gets the normal of the Fragment.
     * @return the normal of the Fragment
     */
    public Vector getNormal() {
        return new Vector(attributes[NORMAL_X],
                            attributes[NORMAL_Y],
                            attributes[NORMAL_Z]);
    }

    /**
     * Sets the normal of the Fragment.
     * @param normal the normal of the Fragment
     */
    public void setNormal(Vector normal) {
        attributes[NORMAL_X] = normal.get(0);
        attributes[NORMAL_Y] = normal.get(1);
        attributes[NORMAL_Z] = normal.get(2);
    }

    /**
     * Sets the normal of the Fragment.
     * @param nx the x component of the normal
     * @param ny the y component of the normal
     * @param nz the z component of the normal
     */
    public void setNormal(double nx, double ny, double nz) {
        attributes[NORMAL_X] = nx;
        attributes[NORMAL_Y] = ny;
        attributes[NORMAL_Z] = nz;
    }

    /**
     * Converts a color value from a double in [0, 1] to an integer in [0, 255].
     * @param c the color value to convert in [0, 1]
     * @return the value of the color in [0, 255]
     */
    public static int colorToInt(double c) {
        if  (!inRange(c, 0, 1)) {
            throw new IllegalArgumentException("Color value must be between 0 and 1");
        }
        return (int) Math.min(MAX_PIX_VAL, Math.max(MAX_PIX_VAL * c, 0));
    }

    /**
     * Converts a color value from an integer in [0, 255] to a double in [0, 1].
     * @param c the color value to convert in [0, 255]
     * @return the value of the color in [0, 1]
     */
    public static double colorToFloat(int c) {
        if (!inRange(c, 0, MAX_PIX_VAL)) {
            throw new IllegalArgumentException("Color value must be between 0 and 255");
        }
        return c / MAX_PIX_VAL;
    }

    /**
     * Gets the color of the Fragment.
     * @return the color of the Fragment
     */
    public Color getColor() {
        final int r = colorToInt(attributes[COLOR_R]);
        final int g = colorToInt(attributes[COLOR_G]);
        final int b = colorToInt(attributes[COLOR_B]);
        return new Color(r, g, b);
    }

    /**
     * Sets the color of the Fragment.
     * @param color the color of the Fragment
     */
    public void setColor(Color color) {
        attributes[COLOR_R] = colorToFloat(color.getRed());
        attributes[COLOR_G] = colorToFloat(color.getGreen());
        attributes[COLOR_B] = colorToFloat(color.getBlue());
    }

    /**
     * Sets the color of the Fragment.
     * @param r the red component of the color in [0, 1]
     * @param g the green component of the color in [0, 1]
     * @param b the blue component of the color in [0, 1]
     */
    public void setColor(double r, double g, double b) {
        if (!inRange(r, 0, 1)
            || !inRange(g, 0, 1)
            || !inRange(b, 0, 1)) {
            throw new IllegalArgumentException("Color values must be between 0 and 1");
        }
        attributes[COLOR_R] = r;
        attributes[COLOR_G] = g;
        attributes[COLOR_B] = b;
    }

    /**
     * Checks if a value is in the given range [min, max].
     * @param value the value to check
     * @param min the minimum value of the range (included)
     * @param max the maximum value of the range(included)
     * @return true if the value is in the range [min, max], false otherwise
     */
    private static boolean inRange(double value, double min, double max) {
        return MathUtils.isInRange(value, min, max);
    }

    /**
     * Returns a string representation of the Fragment.
     * @return a string representation of the Fragment
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public Fragment clone() {
        final Fragment res = new Fragment(x, y);
        for (int i = 0; i < attributes.length; i++) {
            res.attributes[i] = attributes[i];
        }
        return res;
    }

}
