package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import renderer.controller.ImageWrapper;
import renderer.core.mesh.Scene;
import renderer.core.shader.Fragment;

/**
 * Exhaustive test suite for the ImageWrapper class.
 * Tests all constructors, methods, and edge cases.
 */
public class ImageWrapperTest {

    /** Default image size for tests. */
    private static final int DEFAULT_SIZE = 100;
    /** Test coordinate value. */
    private static final int TEST_COORD = 50;
    /** Test coordinate value for color array. */
    private static final int COLOR_ARRAY_STEP = 10;
    /** Custom background color for testing (different from default). */
    private static final int CUSTOM_BACKGROUND_COLOR = 0x333333;
    /** RGB value for test color red component. */
    private static final int TEST_COLOR_R = 123;
    /** RGB value for test color green component. */
    private static final int TEST_COLOR_G = 45;
    /** RGB value for test color blue component. */
    private static final int TEST_COLOR_B = 67;
    /** Large out of bounds coordinate. */
    private static final int LARGE_COORD = 1000;
    /** Small negative coordinate. */
    private static final int SMALL_NEG_COORD = -10;
    /** Medium negative coordinate. */
    private static final int MEDIUM_NEG_COORD = -20;
    /** Test timeout in milliseconds. */
    private static final int TEST_TIMEOUT = 5000;
    /** Number of test iterations. */
    private static final int TEST_ITERATIONS = 1000;
    /** Test pixel coordinates. */
    private static final int TEST_PIXEL_10 = 10;
    /** Test pixel coordinates. */
    private static final int TEST_PIXEL_20 = 20;
    /** Test pixel coordinates. */
    private static final int TEST_PIXEL_30 = 30;
    /** Modulo value for color calculation. */
    private static final int COLOR_MOD = 256;
    /** Full alpha channel mask for ARGB colors. */
    private static final int ALPHA_MASK = 0xFF000000;

    /** The default image wrapper for testing. */
    private ImageWrapper defaultWrapper;
    /** The scene-based image wrapper for testing. */
    private ImageWrapper sceneWrapper;
    /** The test scene for testing. */
    private Scene testScene;

    /**
     * Sets up test fixtures before each test.
     *
     * @throws IOException if scene file cannot be loaded
     */
    @Before
    public void setUp() throws IOException {
        defaultWrapper = new ImageWrapper();
        testScene = new Scene("data/example0.scene");
        sceneWrapper = new ImageWrapper(testScene, CUSTOM_BACKGROUND_COLOR);
    }

    // ==================== Constructor Tests ====================

    /**
     * Tests the default constructor creates an image with default dimensions.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull("Default wrapper should not be null", defaultWrapper);
        assertEquals("Default width should be 100",
                DEFAULT_SIZE, defaultWrapper.getWidth());
        assertEquals("Default height should be 100",
                DEFAULT_SIZE, defaultWrapper.getHeight());
        assertEquals("Image type should be TYPE_3BYTE_BGR",
                BufferedImage.TYPE_3BYTE_BGR, defaultWrapper.getType());
    }

    /**
     * Tests that default constructor initializes all pixels to default background color.
     */
    @Test
    public void testDefaultConstructorInitializesBackgroundColor() {
        // Sample a few pixels to verify default background color
        // getRGB returns ARGB, so we need to mask out the RGB part
        int expectedRgb = ImageWrapper.getDefaultBackgroundColor() | ALPHA_MASK;
        assertEquals("Top-left pixel should be default background color",
                expectedRgb, defaultWrapper.getRGB(0, 0));
        int centerX = defaultWrapper.getWidth() / 2;
        int centerY = defaultWrapper.getHeight() / 2;
        assertEquals("Center pixel should be default background color",
                expectedRgb,
                defaultWrapper.getRGB(centerX, centerY));
        int maxX = defaultWrapper.getWidth() - 1;
        int maxY = defaultWrapper.getHeight() - 1;
        assertEquals("Bottom-right pixel should be default background color",
                expectedRgb,
                defaultWrapper.getRGB(maxX, maxY));
    }

    /**
     * Tests the Scene constructor creates an image with scene dimensions.
     */
    @Test
    public void testSceneConstructor() {
        assertNotNull("Scene wrapper should not be null", sceneWrapper);
        assertEquals("Width should match scene width",
                testScene.getScreenW(), sceneWrapper.getWidth());
        assertEquals("Height should match scene height",
                testScene.getScreenH(), sceneWrapper.getHeight());
        assertEquals("Image type should be TYPE_3BYTE_BGR",
                BufferedImage.TYPE_3BYTE_BGR, sceneWrapper.getType());
    }

    /**
     * Tests that Scene constructor initializes all pixels to custom background color.
     */
    @Test
    public void testSceneConstructorInitializesBackgroundColor() {
        // Sample a few pixels to verify custom background color
        // getRGB returns ARGB, so we need to mask out the RGB part
        int expectedRgb = CUSTOM_BACKGROUND_COLOR | ALPHA_MASK;
        assertEquals("Top-left pixel should be custom background color",
                expectedRgb, sceneWrapper.getRGB(0, 0));
        int centerX = testScene.getScreenW() / 2;
        int centerY = testScene.getScreenH() / 2;
        assertEquals("Center pixel should be custom background color",
                expectedRgb,
                sceneWrapper.getRGB(centerX, centerY));
        int maxX = testScene.getScreenW() - 1;
        int maxY = testScene.getScreenH() - 1;
        assertEquals("Bottom-right pixel should be custom background color",
                expectedRgb,
                sceneWrapper.getRGB(maxX, maxY));
    }

    // ==================== setPixel Method Tests ====================

    /**
     * Tests setting a pixel at valid coordinates.
     */
    @Test
    public void testSetPixelValidCoordinates() {
        Color red = Color.RED;
        defaultWrapper.setPixel(TEST_COORD, TEST_COORD, red);
        assertEquals("Pixel should be set to red",
                red.getRGB(), defaultWrapper.getRGB(TEST_COORD, TEST_COORD));
    }

    /**
     * Tests setting a pixel at top-left corner (0, 0).
     */
    @Test
    public void testSetPixelAtTopLeftCorner() {
        Color blue = Color.BLUE;
        defaultWrapper.setPixel(0, 0, blue);
        assertEquals("Top-left pixel should be blue",
                blue.getRGB(), defaultWrapper.getRGB(0, 0));
    }

    /**
     * Tests setting a pixel at bottom-right corner (width-1, height-1).
     */
    @Test
    public void testSetPixelAtBottomRightCorner() {
        Color green = Color.GREEN;
        int maxX = defaultWrapper.getWidth() - 1;
        int maxY = defaultWrapper.getHeight() - 1;
        defaultWrapper.setPixel(maxX, maxY, green);
        assertEquals("Bottom-right pixel should be green",
                green.getRGB(), defaultWrapper.getRGB(maxX, maxY));
    }

    /**
     * Tests setting a pixel with negative x coordinate (should be clipped).
     */
    @Test
    public void testSetPixelNegativeX() {
        Color red = Color.RED;
        int originalColor = defaultWrapper.getRGB(0, TEST_COORD);
        defaultWrapper.setPixel(-1, TEST_COORD, red);
        // Pixel should not be set (clipped)
        assertEquals("Pixel at (0, 50) should not change",
                originalColor, defaultWrapper.getRGB(0, TEST_COORD));
    }

    /**
     * Tests setting a pixel with negative y coordinate (should be clipped).
     */
    @Test
    public void testSetPixelNegativeY() {
        Color red = Color.RED;
        int originalColor = defaultWrapper.getRGB(TEST_COORD, 0);
        defaultWrapper.setPixel(TEST_COORD, -1, red);
        // Pixel should not be set (clipped)
        assertEquals("Pixel at (50, 0) should not change",
                originalColor, defaultWrapper.getRGB(TEST_COORD, 0));
    }

    /**
     * Tests setting a pixel with x coordinate >= width (should be clipped).
     */
    @Test
    public void testSetPixelXOutOfBounds() {
        Color red = Color.RED;
        int maxX = defaultWrapper.getWidth() - 1;
        int originalColor = defaultWrapper.getRGB(maxX, TEST_COORD);
        defaultWrapper.setPixel(defaultWrapper.getWidth(), TEST_COORD, red);
        // Pixel should not be set (clipped)
        assertEquals("Pixel at max x should not change",
                originalColor, defaultWrapper.getRGB(maxX, TEST_COORD));
    }

    /**
     * Tests setting a pixel with y coordinate >= height (should be clipped).
     */
    @Test
    public void testSetPixelYOutOfBounds() {
        Color red = Color.RED;
        int maxY = defaultWrapper.getHeight() - 1;
        int originalColor = defaultWrapper.getRGB(TEST_COORD, maxY);
        defaultWrapper.setPixel(TEST_COORD, defaultWrapper.getHeight(), red);
        // Pixel should not be set (clipped)
        assertEquals("Pixel at max y should not change",
                originalColor, defaultWrapper.getRGB(TEST_COORD, maxY));
    }

    /**
     * Tests setting a pixel with both coordinates out of bounds.
     */
    @Test
    public void testSetPixelBothCoordinatesOutOfBounds() {
        Color red = Color.RED;
        int width = defaultWrapper.getWidth();
        int height = defaultWrapper.getHeight();

        // These should be silently ignored (clipped), not throw exceptions
        try {
            defaultWrapper.setPixel(-1, -1, red);
            defaultWrapper.setPixel(width, height, red);
            // If we reach here, no exception was thrown (expected behavior)
        } catch (Exception e) {
            fail("setPixel should not throw exception for out-of-bounds coordinates: "
                    + e.getMessage());
        }
    }

    /**
     * Tests setting pixels with various colors.
     */
    @Test
    public void testSetPixelVariousColors() {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.WHITE, Color.BLACK};
        for (int i = 0; i < colors.length; i++) {
            int coord = i * COLOR_ARRAY_STEP;
            defaultWrapper.setPixel(coord, coord, colors[i]);
            assertEquals("Pixel should be set to correct color",
                    colors[i].getRGB(), defaultWrapper.getRGB(coord, coord));
        }
    }

    /**
     * Tests setting the same pixel multiple times (overwriting).
     */
    @Test
    public void testSetPixelOverwrite() {
        defaultWrapper.setPixel(TEST_COORD, TEST_COORD, Color.RED);
        assertEquals("Pixel should be red",
                Color.RED.getRGB(), defaultWrapper.getRGB(TEST_COORD, TEST_COORD));

        defaultWrapper.setPixel(TEST_COORD, TEST_COORD, Color.BLUE);
        assertEquals("Pixel should now be blue",
                Color.BLUE.getRGB(), defaultWrapper.getRGB(TEST_COORD, TEST_COORD));

        defaultWrapper.setPixel(TEST_COORD, TEST_COORD, Color.GREEN);
        assertEquals("Pixel should now be green",
                Color.GREEN.getRGB(), defaultWrapper.getRGB(TEST_COORD, TEST_COORD));
    }

    // ==================== isClipped Method Tests ====================

    /**
     * Tests isClipped with a fragment at valid coordinates.
     */
    @Test
    public void testIsClippedValidFragment() {
        Fragment fragment = new Fragment(TEST_COORD, TEST_COORD);
        assertFalse("Fragment at (50, 50) should not be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at top-left corner (0, 0).
     */
    @Test
    public void testIsClippedAtTopLeftCorner() {
        Fragment fragment = new Fragment(0, 0);
        assertFalse("Fragment at (0, 0) should not be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at bottom-right corner.
     */
    @Test
    public void testIsClippedAtBottomRightCorner() {
        Fragment fragment = new Fragment(
                defaultWrapper.getWidth() - 1,
                defaultWrapper.getHeight() - 1);
        assertFalse("Fragment at bottom-right corner should not be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment having negative x coordinate.
     */
    @Test
    public void testIsClippedNegativeX() {
        Fragment fragment = new Fragment(-1, TEST_COORD);
        assertTrue("Fragment with negative x should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment having negative y coordinate.
     */
    @Test
    public void testIsClippedNegativeY() {
        Fragment fragment = new Fragment(TEST_COORD, -1);
        assertTrue("Fragment with negative y should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment having both negative coordinates.
     */
    @Test
    public void testIsClippedBothNegative() {
        Fragment fragment = new Fragment(SMALL_NEG_COORD, MEDIUM_NEG_COORD);
        assertTrue("Fragment with both negative coordinates should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at x = width (just outside).
     */
    @Test
    public void testIsClippedXEqualsWidth() {
        Fragment fragment = new Fragment(defaultWrapper.getWidth(), TEST_COORD);
        assertTrue("Fragment at x = width should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at y = height (just outside).
     */
    @Test
    public void testIsClippedYEqualsHeight() {
        Fragment fragment = new Fragment(TEST_COORD, defaultWrapper.getHeight());
        assertTrue("Fragment at y = height should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at x = width - 1 (should not be clipped).
     */
    @Test
    public void testIsClippedXEqualsWidthMinusOne() {
        Fragment fragment = new Fragment(defaultWrapper.getWidth() - 1, TEST_COORD);
        assertFalse("Fragment at x = width - 1 should not be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at y = height - 1 (should not be clipped).
     */
    @Test
    public void testIsClippedYEqualsHeightMinusOne() {
        Fragment fragment = new Fragment(TEST_COORD, defaultWrapper.getHeight() - 1);
        assertFalse("Fragment at y = height - 1 should not be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment way outside bounds (large positive).
     */
    @Test
    public void testIsClippedFarOutsideBoundsPositive() {
        Fragment fragment = new Fragment(LARGE_COORD, LARGE_COORD);
        assertTrue("Fragment far outside bounds should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment way outside bounds (large negative).
     */
    @Test
    public void testIsClippedFarOutsideBoundsNegative() {
        Fragment fragment = new Fragment(-LARGE_COORD, -LARGE_COORD);
        assertTrue("Fragment far outside bounds should be clipped",
                defaultWrapper.isClipped(fragment));
    }

    /**
     * Tests isClipped with fragment at boundaries (edge cases).
     */
    @Test
    public void testIsClippedBoundaryConditions() {
        // Test all four corners and just outside
        assertFalse("(0, 0) should not be clipped",
                defaultWrapper.isClipped(new Fragment(0, 0)));
        assertTrue("(-1, 0) should be clipped",
                defaultWrapper.isClipped(new Fragment(-1, 0)));
        assertTrue("(0, -1) should be clipped",
                defaultWrapper.isClipped(new Fragment(0, -1)));

        int maxX = defaultWrapper.getWidth() - 1;
        int maxY = defaultWrapper.getHeight() - 1;

        assertFalse("(maxX, maxY) should not be clipped",
                defaultWrapper.isClipped(new Fragment(maxX, maxY)));
        assertTrue("(maxX + 1, maxY) should be clipped",
                defaultWrapper.isClipped(new Fragment(maxX + 1, maxY)));
        assertTrue("(maxX, maxY + 1) should be clipped",
                defaultWrapper.isClipped(new Fragment(maxX, maxY + 1)));
    }

    // ==================== Integration Tests ====================

    /**
     * Tests that setPixel respects isClipped for out-of-bounds fragments.
     */
    @Test
    public void testSetPixelRespectsClipping() {
        Fragment[] clippedFragments = {
                new Fragment(-1, TEST_COORD),
                new Fragment(TEST_COORD, -1),
                new Fragment(defaultWrapper.getWidth(), TEST_COORD),
                new Fragment(TEST_COORD, defaultWrapper.getHeight()),
                new Fragment(SMALL_NEG_COORD, SMALL_NEG_COORD),
                new Fragment(LARGE_COORD, LARGE_COORD)
        };

        for (Fragment fragment : clippedFragments) {
            assertTrue("Fragment should be clipped: " + fragment,
                    defaultWrapper.isClipped(fragment));
            // setPixel should handle this gracefully without throwing exception
            defaultWrapper.setPixel(fragment.getX(), fragment.getY(), Color.RED);
        }
    }

    /**
     * Tests setting all pixels in the image.
     */
    @Test
    public void testSetAllPixels() {
        Color testColor = new Color(TEST_COLOR_R, TEST_COLOR_G, TEST_COLOR_B);
        for (int x = 0; x < defaultWrapper.getWidth(); x++) {
            for (int y = 0; y < defaultWrapper.getHeight(); y++) {
                defaultWrapper.setPixel(x, y, testColor);
            }
        }

        // Verify all pixels are set
        for (int x = 0; x < defaultWrapper.getWidth(); x++) {
            for (int y = 0; y < defaultWrapper.getHeight(); y++) {
                assertEquals("All pixels should be set to test color",
                        testColor.getRGB(), defaultWrapper.getRGB(x, y));
            }
        }
    }

    /**
     * Tests that ImageWrapper inherits BufferedImage functionality.
     */
    @Test
    public void testBufferedImageInheritance() {
        assertTrue("ImageWrapper should be instance of BufferedImage",
                defaultWrapper instanceof BufferedImage);

        // Test inherited methods
        assertNotNull("Graphics should be accessible", defaultWrapper.getGraphics());
        assertNotNull("ColorModel should be accessible", defaultWrapper.getColorModel());
        assertNotNull("Raster should be accessible", defaultWrapper.getRaster());
    }

    /**
     * Tests creating multiple ImageWrappers with different scenes.
     */
    @Test
    public void testMultipleSceneWrappers() throws IOException {
        Scene scene1 = new Scene("data/example0.scene");
        Scene scene2 = new Scene("data/example1.scene");

        ImageWrapper wrapper1 = new ImageWrapper(scene1);
        ImageWrapper wrapper2 = new ImageWrapper(scene2);

        assertEquals("Wrapper1 width should match scene1",
                scene1.getScreenW(), wrapper1.getWidth());
        assertEquals("Wrapper2 width should match scene2",
                scene2.getScreenW(), wrapper2.getWidth());

        // Verify they are independent
        wrapper1.setPixel(TEST_PIXEL_10, TEST_PIXEL_10, Color.RED);
        wrapper2.setPixel(TEST_PIXEL_10, TEST_PIXEL_10, Color.BLUE);

        assertEquals("Wrapper1 should have red pixel",
                Color.RED.getRGB(), wrapper1.getRGB(TEST_PIXEL_10, TEST_PIXEL_10));
        assertEquals("Wrapper2 should have blue pixel",
                Color.BLUE.getRGB(), wrapper2.getRGB(TEST_PIXEL_10, TEST_PIXEL_10));
    }

    /**
     * Tests performance with many pixel operations.
     */
    @Test(timeout = TEST_TIMEOUT)
    public void testPerformanceManyPixelOperations() {
        // Should complete within 5 seconds
        int lastX = 0;
        int lastY = 0;
        int lastR = 0;
        int lastG = 0;
        int lastB = 0;

        for (int i = 0; i < TEST_ITERATIONS; i++) {
            int x = i % defaultWrapper.getWidth();
            int y = (i / defaultWrapper.getWidth()) % defaultWrapper.getHeight();
            int r = i % COLOR_MOD;
            int g = (i * 2) % COLOR_MOD;
            int b = (i * 2 + 1) % COLOR_MOD;
            defaultWrapper.setPixel(x, y, new Color(r, g, b));

            // Remember last values
            lastX = x;
            lastY = y;
            lastR = r;
            lastG = g;
            lastB = b;
        }

        // Verify the last pixel was set correctly
        // Use getRGB() which returns ARGB (signed int with alpha channel)
        Color expectedColor = new Color(lastR, lastG, lastB);
        int actualRgb = defaultWrapper.getRGB(lastX, lastY);
        int expectedRgb = expectedColor.getRGB();
        assertEquals("Last pixel should be set correctly",
                expectedRgb, actualRgb);
    }

    /**
     * Tests that the image can be saved and loaded.
     */
    @Test
    public void testImageSaveAndLoad() throws IOException {
        // Set a pattern
        defaultWrapper.setPixel(TEST_PIXEL_10, TEST_PIXEL_10, Color.RED);
        defaultWrapper.setPixel(TEST_PIXEL_20, TEST_PIXEL_20, Color.GREEN);
        defaultWrapper.setPixel(TEST_PIXEL_30, TEST_PIXEL_30, Color.BLUE);

        // Save to file
        File tempFile = File.createTempFile("test_image_wrapper", ".png");
        tempFile.deleteOnExit();
        ImageIO.write(defaultWrapper, "png", tempFile);

        // Load and verify
        BufferedImage loaded = ImageIO.read(tempFile);
        assertNotNull("Loaded image should not be null", loaded);
        assertEquals("Loaded image should have same width",
                defaultWrapper.getWidth(), loaded.getWidth());
        assertEquals("Loaded image should have same height",
                defaultWrapper.getHeight(), loaded.getHeight());
        assertEquals("Red pixel should be preserved",
                Color.RED.getRGB(), loaded.getRGB(TEST_PIXEL_10, TEST_PIXEL_10));
        assertEquals("Green pixel should be preserved",
                Color.GREEN.getRGB(), loaded.getRGB(TEST_PIXEL_20, TEST_PIXEL_20));
        assertEquals("Blue pixel should be preserved",
                Color.BLUE.getRGB(), loaded.getRGB(TEST_PIXEL_30, TEST_PIXEL_30));
    }
}
