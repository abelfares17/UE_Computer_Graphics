package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import renderer.controller.ShaderFactory;
import renderer.core.shader.PainterShader;
import renderer.core.shader.Shader;
import renderer.core.shader.SimpleShader;
import renderer.core.shader.TextureShader;

/**
 * Exhaustive test suite for the ShaderFactory class.
 * Tests initialization, shader discovery, creation, and all factory methods.
 */
public class ShaderFactoryTest {

    // ==================== Test Constants ====================

    /**
     * Flag to control whether optional shaders (DepthShader,
     * NormalMapShader) are expected to be present.
     * Set to false for student versions where these shaders are not
     * provided.
     */

    private static final boolean EXPECT_OPTIONAL_SHADERS = true;

    /**
     * Minimum expected number of shader implementations.
     * When optional shaders are expected: 3 required shaders.
     * When optional shaders are NOT expected: relaxed to 2 to allow
     * for variations.
     */
    private static final int MIN_EXPECTED_SHADERS =
            EXPECT_OPTIONAL_SHADERS ? 3 : 2;

    /** Number of threads for concurrency test. */
    private static final int NUM_THREADS = 10;

    /** Timeout for concurrency test in milliseconds. */
    private static final int CONCURRENCY_TEST_TIMEOUT = 5000;

    /** Invalid shader names for edge case testing. */
    private static final String[] INVALID_SHADER_NAMES = {null, "",
            "   ", "Invalid", "123Shader", "Simple-Shader",
            "Simple.Shader",
            "renderer.core.shader.SimpleShader"};

    /** List of known shader implementations. */
    private static final ExpectedShader[] EXPECTED_SHADERS =
            buildExpectedShaders();

    // ==================== Test Data Structures ====================

    /**
     * Test case data structure for shader creation tests.
     */
    private static class ShaderTestCase {
        /** The shader name to test. */
        private final String shaderName;
        /** The expected shader class. */
        private final Class<? extends Shader> expectedClass;
        /** Whether the shader should be found. */
        private final boolean shouldExist;
        /** Description of the test case. */
        private final String description;

        ShaderTestCase(String shaderName, Class<? extends Shader> expectedClass,
                boolean shouldExist, String description) {
            this.shaderName = shaderName;
            this.expectedClass = expectedClass;
            this.shouldExist = shouldExist;
            this.description = description;
        }

        /**
         * Gets the shader name.
         * @return the shader name
         */
        public String getShaderName() {
            return shaderName;
        }

        /**
         * Gets the expected shader class.
         * @return the expected shader class
         */
        public Class<? extends Shader> getExpectedClass() {
            return expectedClass;
        }

        /**
         * Gets whether the shader should exist.
         * @return true if shader should exist, false otherwise
         */
        public boolean getShouldExist() {
            return shouldExist;
        }

        /**
         * Gets the description.
         * @return the description
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * Expected shader implementation that should be discovered.
     */
    private static class ExpectedShader {
        /** The simple name of the shader class. */
        private final String simpleName;
        /** The full class object. */
        private final Class<? extends Shader> shaderClass;
        /** Whether this shader is optional (may not be present). */
        private final boolean optional;

        ExpectedShader(String simpleName,
                Class<? extends Shader> shaderClass) {
            this(simpleName, shaderClass, false);
        }

        ExpectedShader(String simpleName,
                Class<? extends Shader> shaderClass, boolean optional) {
            this.simpleName = simpleName;
            this.shaderClass = shaderClass;
            this.optional = optional;
        }

        /**
         * Gets the simple name.
         * @return the simple name
         */
        public String getSimpleName() {
            return simpleName;
        }

        /**
         * Gets the shader class.
         * @return the shader class
         */
        public Class<? extends Shader> getShaderClass() {
            return shaderClass;
        }

        /**
         * Checks if this shader is optional.
         * @return true if optional, false if required
         */
        public boolean isOptional() {
            return optional;
        }
    }

    /**
     * Builds the expected shaders array, including optional shaders
     * only if EXPECT_OPTIONAL_SHADERS is true.
     * @return array of expected shaders
     */
    private static ExpectedShader[] buildExpectedShaders() {
        final java.util.List<ExpectedShader> shaders =
                new java.util.ArrayList<>();

        // Required shaders
        shaders.add(new ExpectedShader("SimpleShader",
                SimpleShader.class));
        shaders.add(new ExpectedShader("PainterShader",
                PainterShader.class));
        shaders.add(new ExpectedShader("TextureShader",
                TextureShader.class));

        // Optional shaders - only add if expected and available
        if (EXPECT_OPTIONAL_SHADERS) {
            try {
                final Class<? extends Shader> depthShaderClass =
                        Class.forName("renderer.core.shader.DepthShader")
                        .asSubclass(Shader.class);
                shaders.add(new ExpectedShader("DepthShader",
                        depthShaderClass, true));
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                System.out.println(
                        "DepthShader not found (optional, skipping)");
            }

            try {
                final Class<? extends Shader> normalMapShaderClass =
                        Class.forName(
                                "renderer.core.shader.NormalMapShader")
                        .asSubclass(Shader.class);
                shaders.add(new ExpectedShader("NormalMapShader",
                        normalMapShaderClass, true));
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                System.out.println(
                        "NormalMapShader not found (optional, skipping)");
            }
        }

        return shaders.toArray(new ExpectedShader[shaders.size()]);
    }

    /**
     * Gets test cases for shader creation, including only available
     * shaders based on EXPECT_OPTIONAL_SHADERS flag.
     * @return array of test cases
     */
    private static ShaderTestCase[] getCreationTestCases() {
        final java.util.List<ShaderTestCase> testCases =
                new java.util.ArrayList<>();

        // Required shaders
        testCases.add(new ShaderTestCase("SimpleShader",
                SimpleShader.class, true, "Valid SimpleShader creation"));
        testCases.add(new ShaderTestCase("PainterShader",
                PainterShader.class, true,
                "Valid PainterShader creation"));
        testCases.add(new ShaderTestCase("TextureShader",
                TextureShader.class, true,
                "Valid TextureShader creation"));

        // Optional shaders - only add if expected and available
        if (EXPECT_OPTIONAL_SHADERS) {
            try {
                final Class<? extends Shader> depthShaderClass =
                        Class.forName("renderer.core.shader.DepthShader")
                        .asSubclass(Shader.class);
                if (isShaderAvailable("DepthShader")) {
                    testCases.add(new ShaderTestCase("DepthShader",
                            depthShaderClass, true,
                            "Valid DepthShader creation"));
                }
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // DepthShader not available, skip
            }

            try {
                final Class<? extends Shader> normalMapShaderClass =
                        Class.forName(
                                "renderer.core.shader.NormalMapShader")
                        .asSubclass(Shader.class);
                if (isShaderAvailable("NormalMapShader")) {
                    testCases.add(new ShaderTestCase("NormalMapShader",
                            normalMapShaderClass, true,
                            "Valid NormalMapShader creation"));
                }
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // NormalMapShader not available, skip
            }
        }

        // Negative test cases
        testCases.add(new ShaderTestCase("NonExistentShader", null, false,
                "Non-existent shader should return empty Optional"));
        testCases.add(new ShaderTestCase("", null, false,
                "Empty string should return empty Optional"));
        testCases.add(new ShaderTestCase("Shader", null, false,
                "Abstract base class should not be instantiable"));
        testCases.add(new ShaderTestCase("shader", null, false,
                "Case-sensitive name (lowercase) should not match"));
        testCases.add(new ShaderTestCase("SIMPLESHADER", null, false,
                "Case-sensitive name (uppercase) should not match"));
        testCases.add(new ShaderTestCase("SimpleShader ", null, false,
                "Name with trailing space should not match"));
        testCases.add(new ShaderTestCase(" SimpleShader", null, false,
                "Name with leading space should not match"));

        return testCases.toArray(
                new ShaderTestCase[testCases.size()]);
    }

    /**
     * Checks if a shader is available in the discovered set.
     * @param shaderName the simple name of the shader
     * @return true if available, false otherwise
     */
    private static boolean isShaderAvailable(String shaderName) {
        final Set<Class<? extends Shader>> shaderSet =
                ShaderFactory.getShaderSet();
        for (Class<? extends Shader> shader : shaderSet) {
            if (shader.getSimpleName().equals(shaderName)) {
                return true;
            }
        }
        return false;
    }

    // ==================== Setup ====================

    /**
     * Initialize the ShaderFactory before all tests.
     * This is done once for all tests to ensure consistent state.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        ShaderFactory.init();
    }

    /**
     * Verify initialization state before each test.
     */
    @Before
    public void setUp() {
        assertNotNull("ShaderFactory.getShaderSet() should not return null",
                ShaderFactory.getShaderSet());
        assertFalse("ShaderFactory should have discovered shaders after init",
                ShaderFactory.getShaderSet().isEmpty());
    }

    // ==================== Initialization Tests ====================

    /**
     * Tests that init() successfully discovers shader implementations.
     */
    @Test
    public void testInitDiscoversShaders() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        final String message = "ShaderFactory should discover at least "
                + MIN_EXPECTED_SHADERS + " shaders";
        assertTrue(message, shaderSet.size() >= MIN_EXPECTED_SHADERS);

        System.out.println("Discovered " + shaderSet.size()
                + " shader implementations");
    }

    /**
     * Tests that all expected shader implementations are discovered.
     * Optional shaders are checked but not required.
     */
    @Test
    public void testAllExpectedShadersDiscovered() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();
        Set<String> discoveredNames = new HashSet<>();

        for (Class<? extends Shader> shader : shaderSet) {
            discoveredNames.add(shader.getSimpleName());
        }

        for (ExpectedShader expected : EXPECTED_SHADERS) {
            final boolean isDiscovered = discoveredNames.contains(
                    expected.getSimpleName());

            if (expected.isOptional()) {
                if (isDiscovered) {
                    assertTrue("Optional shader class should be in set",
                            shaderSet.contains(expected.getShaderClass()));
                    System.out.println("Optional shader '"
                            + expected.getSimpleName() + "' is present");
                } else {
                    System.out.println("Optional shader '"
                            + expected.getSimpleName()
                            + "' is not present (OK)");
                }
            } else {
                final String msg = "Required shader '"
                        + expected.getSimpleName() + "' should be discovered";
                assertTrue(msg, isDiscovered);
                assertTrue("Expected shader class should be in shader set",
                        shaderSet.contains(expected.getShaderClass()));
            }
        }
    }

    /**
     * Tests that the abstract Shader base class is not included in the set.
     */
    @Test
    public void testAbstractShaderNotIncluded() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        for (Class<? extends Shader> shader : shaderSet) {
            final String msg = "Abstract Shader base class should not be "
                    + "in shader set";
            assertFalse(msg, shader.getSimpleName().equals("Shader"));
        }
    }

    /**
     * Tests that non-Shader classes are not included in the set.
     */
    @Test
    public void testOnlyShaderSubclassesIncluded() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        for (Class<? extends Shader> shader : shaderSet) {
            assertEquals("All discovered classes should extend Shader",
                    Shader.class, shader.getSuperclass());
        }
    }

    /**
     * Tests that init() can be called multiple times safely.
     */
    @Test
    public void testMultipleInitCalls() {
        final Set<Class<? extends Shader>> initialSet =
                new HashSet<>(ShaderFactory.getShaderSet());
        int initialSize = initialSet.size();

        // Call init again
        ShaderFactory.init();

        Set<Class<? extends Shader>> afterSet = ShaderFactory.getShaderSet();
        int afterSize = afterSet.size();

        assertTrue("Shader set should not be empty after re-init",
                afterSize > 0);
        final String msg = "Shader set size should remain consistent "
                + "after re-init";
        assertEquals(msg, initialSize, afterSize);
    }

    // ==================== Shader Creation Tests ====================

    /**
     * Tests shader creation using parameterized test cases.
     */
    @Test
    public void testShaderCreationWithTestCases() {
        final ShaderTestCase[] testCases = getCreationTestCases();
        for (ShaderTestCase testCase : testCases) {
            Optional<Shader> result = ShaderFactory.create(
                    testCase.getShaderName());

            if (testCase.getShouldExist()) {
                final String msg1 = testCase.getDescription()
                        + " - should return present Optional";
                assertTrue(msg1, result.isPresent());
                final String msg2 = testCase.getDescription()
                        + " - shader instance should not be null";
                assertNotNull(msg2, result.get());
                final String msg3 = testCase.getDescription()
                        + " - should return correct shader type";
                assertEquals(msg3, testCase.getExpectedClass(),
                        result.get().getClass());
            } else {
                final String msg = testCase.getDescription()
                        + " - should return empty Optional";
                assertFalse(msg, result.isPresent());
            }
        }
    }

    /**
     * Tests that created shaders are properly initialized.
     */
    @Test
    public void testCreatedShadersAreInitialized() {
        Optional<Shader> simpleShader = ShaderFactory.create("SimpleShader");

        assertTrue("SimpleShader should be created successfully",
                simpleShader.isPresent());
        assertNotNull("Created shader should not be null",
                simpleShader.get());
        assertTrue("Created shader should be instance of SimpleShader",
                simpleShader.get() instanceof SimpleShader);
    }

    /**
     * Tests that multiple creations return different instances.
     */
    @Test
    public void testMultipleCreationsReturnDifferentInstances() {
        Optional<Shader> shader1 = ShaderFactory.create("SimpleShader");
        Optional<Shader> shader2 = ShaderFactory.create("SimpleShader");

        assertTrue("First creation should succeed", shader1.isPresent());
        assertTrue("Second creation should succeed", shader2.isPresent());
        final String msg = "Multiple creations should return different "
                + "instances";
        assertNotEquals(msg, shader1.get(), shader2.get());
    }

    /**
     * Tests creation with null shader name.
     */
    @Test
    public void testCreateWithNullName() {
        Optional<Shader> result = ShaderFactory.create(null);

        final String msg = "Creating with null name should return empty "
                + "Optional";
        assertFalse(msg, result.isPresent());
    }

    /**
     * Tests creation with all invalid names.
     */
    @Test
    public void testCreateWithInvalidNames() {
        for (String invalidName : INVALID_SHADER_NAMES) {
            Optional<Shader> result = ShaderFactory.create(invalidName);

            final String msg = "Creating with invalid name '" + invalidName
                    + "' should return empty Optional";
            assertFalse(msg, result.isPresent());
        }
    }

    /**
     * Tests that create returns empty Optional for partial name matches.
     */
    @Test
    public void testCreateWithPartialNameMatch() {
        final String[] partialNames = {"Simple", "Painter", "Depth",
                "Texture", "Normal"};

        for (String partialName : partialNames) {
            Optional<Shader> result = ShaderFactory.create(partialName);

            final String msg = "Creating with partial name '" + partialName
                    + "' should return empty Optional";
            assertFalse(msg, result.isPresent());
        }
    }

    // ==================== getShaderSet() Tests ====================

    /**
     * Tests that getShaderSet() returns a non-null set.
     */
    @Test
    public void testGetShaderSetNotNull() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        assertNotNull("getShaderSet() should not return null", shaderSet);
    }

    /**
     * Tests that getShaderSet() returns a non-empty set after init.
     */
    @Test
    public void testGetShaderSetNotEmpty() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        final String msg1 = "getShaderSet() should return non-empty set "
                + "after init";
        assertFalse(msg1, shaderSet.isEmpty());
        final String msg2 = "getShaderSet() should return at least "
                + MIN_EXPECTED_SHADERS + " shaders";
        assertTrue(msg2, shaderSet.size() >= MIN_EXPECTED_SHADERS);
    }

    /**
     * Tests that getShaderSet() returns consistent results.
     */
    @Test
    public void testGetShaderSetConsistency() {
        Set<Class<? extends Shader>> set1 = ShaderFactory.getShaderSet();
        Set<Class<? extends Shader>> set2 = ShaderFactory.getShaderSet();

        assertEquals("getShaderSet() should return same reference",
                set1, set2);
        assertEquals("getShaderSet() should return consistent size",
                set1.size(), set2.size());
    }

    /**
     * Tests that getShaderSet() returns only valid shader classes.
     */
    @Test
    public void testGetShaderSetContainsOnlyValidShaders() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        for (Class<? extends Shader> shaderClass : shaderSet) {
            assertNotNull("Shader class should not be null", shaderClass);
            assertTrue("Shader class should be assignable from Shader",
                    Shader.class.isAssignableFrom(shaderClass));
            final String msg = "Shader class should not be abstract";
            assertFalse(msg, java.lang.reflect.Modifier.isAbstract(
                    shaderClass.getModifiers()));
        }
    }

    // ==================== getShaderSetAsStringArray() Tests ====================

    /**
     * Tests that getShaderSetAsStringArray() returns a non-null array.
     */
    @Test
    public void testGetShaderSetAsStringArrayNotNull() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();

        final String msg = "getShaderSetAsStringArray() should not "
                + "return null";
        assertNotNull(msg, shaderNames);
    }

    /**
     * Tests that getShaderSetAsStringArray() returns correct size.
     */
    @Test
    public void testGetShaderSetAsStringArraySize() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        assertEquals("Array size should match shader set size",
                shaderSet.size(), shaderNames.length);
    }

    /**
     * Tests that getShaderSetAsStringArray() returns simple class names.
     */
    @Test
    public void testGetShaderSetAsStringArrayContents() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        // Collect expected names from shader set
        Set<String> expectedNames = new HashSet<>();
        for (Class<? extends Shader> shader : shaderSet) {
            expectedNames.add(shader.getSimpleName());
        }

        // Verify all names in array are in expected set
        for (String name : shaderNames) {
            assertNotNull("Shader name should not be null", name);
            assertFalse("Shader name should not be empty", name.isEmpty());
            assertTrue("Shader name '" + name + "' should be in shader set",
                    expectedNames.contains(name));
        }

        // Verify all expected names are in array
        Set<String> arrayNames = new HashSet<>(Arrays.asList(shaderNames));
        for (String expected : expectedNames) {
            final String msg = "Expected shader name '" + expected
                    + "' should be in array";
            assertTrue(msg, arrayNames.contains(expected));
        }
    }

    /**
     * Tests that getShaderSetAsStringArray() contains expected shaders.
     */
    @Test
    public void testGetShaderSetAsStringArrayContainsExpectedShaders() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();
        Set<String> nameSet = new HashSet<>(Arrays.asList(shaderNames));

        for (ExpectedShader expected : EXPECTED_SHADERS) {
            if (!expected.isOptional()) {
                assertTrue("Array should contain '"
                        + expected.getSimpleName() + "'",
                        nameSet.contains(expected.getSimpleName()));
            }
        }
    }

    /**
     * Tests that getShaderSetAsStringArray() does not contain duplicates.
     */
    @Test
    public void testGetShaderSetAsStringArrayNoDuplicates() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();
        Set<String> uniqueNames = new HashSet<>();

        for (String name : shaderNames) {
            final String msg = "Shader name '" + name
                    + "' should not be duplicated";
            assertTrue(msg, uniqueNames.add(name));
        }

        assertEquals("Array should have no duplicates",
                shaderNames.length, uniqueNames.size());
    }

    /**
     * Tests that getShaderSetAsStringArray() returns consistent results.
     */
    @Test
    public void testGetShaderSetAsStringArrayConsistency() {
        String[] array1 = ShaderFactory.getShaderSetAsStringArray();
        String[] array2 = ShaderFactory.getShaderSetAsStringArray();

        assertEquals("Multiple calls should return same size",
                array1.length, array2.length);

        Set<String> set1 = new HashSet<>(Arrays.asList(array1));
        Set<String> set2 = new HashSet<>(Arrays.asList(array2));

        assertEquals("Multiple calls should return same content",
                set1, set2);
    }

    /**
     * Tests that all names in array can be used to create shaders.
     */
    @Test
    public void testGetShaderSetAsStringArrayNamesAreValid() {
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();

        for (String name : shaderNames) {
            Optional<Shader> shader = ShaderFactory.create(name);

            final String msg = "Shader name '" + name
                    + "' from array should be creatable";
            assertTrue(msg, shader.isPresent());
        }
    }

    // ==================== Integration Tests ====================

    /**
     * Tests complete workflow: init -> get names -> create shaders.
     */
    @Test
    public void testCompleteWorkflow() {
        // Get all available shader names
        String[] shaderNames = ShaderFactory.getShaderSetAsStringArray();

        assertTrue("Should have discovered shaders",
                shaderNames.length > 0);

        // Try to create each discovered shader
        for (String name : shaderNames) {
            Optional<Shader> shader = ShaderFactory.create(name);

            assertTrue("Should be able to create shader '" + name + "'",
                    shader.isPresent());
            assertNotNull("Created shader should not be null",
                    shader.get());
            final String msg = "Created shader should have correct "
                    + "simple name";
            assertEquals(msg, name,
                    shader.get().getClass().getSimpleName());
        }
    }

    /**
     * Tests that all discovered shaders can be instantiated.
     */
    @Test
    public void testAllDiscoveredShadersAreInstantiable() {
        Set<Class<? extends Shader>> shaderSet = ShaderFactory.getShaderSet();

        for (Class<? extends Shader> shaderClass : shaderSet) {
            String simpleName = shaderClass.getSimpleName();
            Optional<Shader> shader = ShaderFactory.create(simpleName);

            final String msg1 = "Discovered shader '" + simpleName
                    + "' should be creatable";
            assertTrue(msg1, shader.isPresent());
            assertEquals("Created shader should match discovered class",
                    shaderClass, shader.get().getClass());
        }
    }

    /**
     * Tests factory behavior with concurrent access (basic thread safety check).
     */
    @Test(timeout = CONCURRENCY_TEST_TIMEOUT)
    public void testConcurrentAccess() throws InterruptedException {
        final int numThreads = NUM_THREADS;
        final Thread[] threads = new Thread[numThreads];
        final boolean[] results = new boolean[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Optional<Shader> shader = ShaderFactory.create("SimpleShader");
                    results[index] = shader.isPresent();
                } catch (Exception e) {
                    results[index] = false;
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (int i = 0; i < numThreads; i++) {
            assertTrue("Concurrent creation #" + i + " should succeed",
                    results[i]);
        }
    }

    // ==================== Edge Case Tests ====================

    /**
     * Tests that shader set remains unmodified after shader creation.
     */
    @Test
    public void testShaderSetImmutabilityAfterCreation() {
        final Set<Class<? extends Shader>> beforeSet =
                new HashSet<>(ShaderFactory.getShaderSet());
        int beforeSize = beforeSet.size();

        // Create multiple shaders
        ShaderFactory.create("SimpleShader");
        ShaderFactory.create("PainterShader");
        ShaderFactory.create("NonExistent");

        Set<Class<? extends Shader>> afterSet = ShaderFactory.getShaderSet();
        int afterSize = afterSet.size();

        final String msg1 = "Shader set size should not change after "
                + "creation";
        assertEquals(msg1, beforeSize, afterSize);
        final String msg2 = "Shader set content should not change after "
                + "creation";
        assertEquals(msg2, beforeSet, afterSet);
    }

    /**
     * Tests factory with case-sensitive shader names.
     */
    @Test
    public void testCaseSensitivity() {
        Optional<Shader> correct = ShaderFactory.create("SimpleShader");
        Optional<Shader> lowercase = ShaderFactory.create("simpleshader");
        Optional<Shader> uppercase = ShaderFactory.create("SIMPLESHADER");
        Optional<Shader> mixedCase = ShaderFactory.create("sImPlEsHaDeR");

        assertTrue("Correct case should succeed", correct.isPresent());
        assertFalse("Lowercase should fail", lowercase.isPresent());
        assertFalse("Uppercase should fail", uppercase.isPresent());
        assertFalse("Mixed case should fail", mixedCase.isPresent());
    }

    /**
     * Tests that all expected shader types are actually usable.
     */
    @Test
    public void testExpectedShaderTypesAreUsable() {
        for (ExpectedShader expected : EXPECTED_SHADERS) {
            Optional<Shader> shader = ShaderFactory.create(
                    expected.getSimpleName());

            if (expected.isOptional() && !shader.isPresent()) {
                System.out.println("Optional shader '"
                        + expected.getSimpleName()
                        + "' not available (skipped)");
                continue;
            }

            final String msg1 = "Expected shader '" + expected.getSimpleName()
                    + "' should be creatable";
            assertTrue(msg1, shader.isPresent());
            final String msg2 = "Created shader should be instance of "
                    + "expected class";
            assertTrue(msg2, expected.getShaderClass().isInstance(
                    shader.get()));
        }
    }
}
