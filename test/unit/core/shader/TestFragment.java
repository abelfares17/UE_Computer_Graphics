package core.shader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.awt.Color;

import renderer.algebra.Vector;
import renderer.core.shader.Fragment;

/**
 * Unit tests for the Fragment class.
 */
public class TestFragment {

    /** The X coordinate used for testing. */
    private static final int X_COORD = 10;
    /** The Y coordinate used for testing. */
    private static final int Y_COORD = 20;
    /** The new X coordinate used for testing. */
    private static final int NEW_X_COORD = 30;
    /** The new Y coordinate used for testing. */
    private static final int NEW_Y_COORD = 40;
    /** The number of attributes in a Fragment. */
    private static final int NUM_ATTRIBUTES = 9;
    /** The depth value used for testing. */
    private static final double DEPTH_VALUE = 0.5;
    /** The X component of the normal vector used for testing. */
    private static final double NORMAL_X = 1.0;
    /** The Y component of the normal vector used for testing. */
    private static final double NORMAL_Y = 0.0;
    /** The Z component of the normal vector used for testing. */
    private static final double NORMAL_Z = 0.0;
    /** The red component of the color used for testing. */
    private static final double COLOR_R = 0.5;
    /** The green component of the color used for testing. */
    private static final double COLOR_G = 0.25;
    /** The blue component of the color used for testing. */
    private static final double COLOR_B = 0.125;
    /** The red component of the color used for testing as an integer. */
    private static final int COLOR_R_INT = 128;
    /** The green component of the color used for testing as an integer. */
    private static final int COLOR_G_INT = 64;
    /** The blue component of the color used for testing as an integer. */
    private static final int COLOR_B_INT = 32;
    /** An invalid attribute index used for testing. */
    private static final int INVALID_INDEX = 9;
    /** The dimension used for testing. */
    private static final int DIMENSION = 3;
    /** The epsilon used for testing. */
    private static final double EPSILON = 1e-2;

    /**
     * Test the creation of a Fragment.
     */
    @Test
    public void testFragmentCreation() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        assertNotNull(fragment);
        assertEquals(X_COORD, fragment.getX());
        assertEquals(Y_COORD, fragment.getY());
        assertEquals(NUM_ATTRIBUTES, fragment.getNumAttributes());
    }

    /**
     * Test setting and getting scalar attributes.
     */
    @Test
    public void testSetAndGetAttributes() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setAttribute(Fragment.DEPTH, DEPTH_VALUE);
        fragment.setAttribute(Fragment.COLOR_R, COLOR_R);
        fragment.setAttribute(Fragment.COLOR_G, COLOR_G);
        fragment.setAttribute(Fragment.COLOR_B, COLOR_B);
        fragment.setAttribute(Fragment.NORMAL_X, NORMAL_X);
        fragment.setAttribute(Fragment.NORMAL_Y, NORMAL_Y);
        fragment.setAttribute(Fragment.NORMAL_Z, NORMAL_Z);


        assertEquals(DEPTH_VALUE, fragment.getAttribute(Fragment.DEPTH), EPSILON);
        assertEquals(COLOR_R, fragment.getAttribute(Fragment.COLOR_R), EPSILON);
        assertEquals(COLOR_G, fragment.getAttribute(Fragment.COLOR_G), EPSILON);
        assertEquals(COLOR_B, fragment.getAttribute(Fragment.COLOR_B), EPSILON);
        assertEquals(NORMAL_X, fragment.getAttribute(Fragment.NORMAL_X), EPSILON);
        assertEquals(NORMAL_Y, fragment.getAttribute(Fragment.NORMAL_Y), EPSILON);
        assertEquals(NORMAL_Z, fragment.getAttribute(Fragment.NORMAL_Z), EPSILON);
    }

    /**
     * Test setting and getting position.
     */
    @Test
    public void testSetAndGetPosition() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setPosition(NEW_X_COORD, NEW_Y_COORD);
        assertEquals(NEW_X_COORD, fragment.getX());
        assertEquals(NEW_Y_COORD, fragment.getY());

        final int[] position = fragment.getPosition();
        assertEquals(NEW_X_COORD, position[0]);
        assertEquals(NEW_Y_COORD, position[1]);
    }

    /**
     * Test setting and getting depth.
     */
    @Test
    public void testSetAndGetDepth() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setDepth(DEPTH_VALUE);
        assertEquals(DEPTH_VALUE, fragment.getDepth(), EPSILON);
    }

    /**
     * Test setting and getting normal vectors.
     */
    @Test
    public void testSetAndGetNormal() {
        final Fragment fragment = new Fragment(X_COORD, Y_COORD);
        final Vector normal = new Vector(NORMAL_X, NORMAL_Y, NORMAL_Z);
        fragment.setNormal(normal);
        final Vector retrievedNormal = fragment.getNormal();
        assertEquals(NORMAL_X, retrievedNormal.getX(), EPSILON);
        assertEquals(NORMAL_Y, retrievedNormal.getY(), EPSILON);
        assertEquals(NORMAL_Z, retrievedNormal.getZ(), EPSILON);
    }

    /**
     * Test setting and getting color using Color objects.
     */
    @Test
    public void testSetAndGetColor() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        Color color = new Color(COLOR_R_INT, COLOR_G_INT, COLOR_B_INT);
        fragment.setColor(color);
        Color retrievedColor = fragment.getColor();
        assertEquals(COLOR_R_INT, retrievedColor.getRed());
        assertEquals(COLOR_G_INT, retrievedColor.getGreen());
        assertEquals(COLOR_B_INT, retrievedColor.getBlue());
    }

    /**
     * Test the colorToInt method with valid inputs.
     */
    @Test
    public void testColorToIntValid() {
        final double[] inputs = {0.0, 1.0, 0.5, 0.25, 0.125};
        final int[] expected = {0, 255, 127, 63, 31};
        for (int i = 0; i < inputs.length; i++) {
            assertEquals(expected[i], Fragment.colorToInt(inputs[i]));
        }
    }

    /**
     * Test the colorToInt method with invalid inputs.
     */
    @Test
    public void testColorToIntInvalid() {
        final double[] invalidInputs = {-0.1, 1.1};
        for (double invalidInput : invalidInputs) {
            assertThrows(IllegalArgumentException.class,
                        () -> Fragment.colorToInt(invalidInput));
        }
    }

    /**
     * Test the colorToFloat method with valid inputs.
     */
    @Test
    public void testColorToFloatValid() {
        final int[] inputs = {0, 255, 128, 64, 32};
        final double[] expected = {0.0, 1.0, 0.5, 0.25, 0.125};
        for (int i = 0; i < inputs.length; i++) {
            assertEquals(expected[i], Fragment.colorToFloat(inputs[i]), EPSILON);
        }
    }

    /**
     * Test the colorToFloat method with invalid inputs.
     */
    @Test
    public void testColorToFloatInvalid() {
        final int[] invalidInputs = {-1, 256};
        for (int invalidInput : invalidInputs) {
            assertThrows(IllegalArgumentException.class,
                        () -> Fragment.colorToFloat(invalidInput));
        }
    }

    /**
     * Test setting an invalid color not in the range [0, 1].
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetColorInvalidColor() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setColor(-1.0, 0.0, COLOR_G_INT);
    }

    /**
     * Test setting and getting color using individual components.
     */
    @Test
    public void testSetAndGetColorComponents() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setColor(COLOR_R, COLOR_G, COLOR_B);
        Color retrievedColor = fragment.getColor();
        assertEquals(Fragment.colorToInt(COLOR_R), retrievedColor.getRed());
        assertEquals(Fragment.colorToInt(COLOR_G), retrievedColor.getGreen());
        assertEquals(Fragment.colorToInt(COLOR_B), retrievedColor.getBlue());
    }

    /**
     * Test the string representation of the Fragment.
     */
    @Test
    public void testToString() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        assertEquals("(" + X_COORD + "," + Y_COORD + ")", fragment.toString());
    }

    /**
     * Test setting an invalid attribute index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetInvalidAttribute() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setAttribute(INVALID_INDEX, DEPTH_VALUE); // Invalid index
    }

    /**
     * Test getting an invalid attribute index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidAttribute() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.getAttribute(INVALID_INDEX); // Invalid index
    }

    /**
     * Test getting an attribute with a specified dimension.
     */
    @Test
    public void testGetAttributeWithDimension() {
        Fragment fragment = new Fragment(X_COORD, Y_COORD);
        fragment.setAttribute(Fragment.NORMAL_X, NORMAL_X);
        fragment.setAttribute(Fragment.NORMAL_Y, NORMAL_Y);
        fragment.setAttribute(Fragment.NORMAL_Z, NORMAL_Z);
        double[] normal = fragment.getAttribute(Fragment.NORMAL_X, DIMENSION);
        assertEquals(NORMAL_X, normal[0], EPSILON);
        assertEquals(NORMAL_Y, normal[1], EPSILON);
        assertEquals(NORMAL_Z, normal[2], EPSILON);
    }
}
