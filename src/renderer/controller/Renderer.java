package renderer.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.Optional;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;
import renderer.controller.ColorMapFactory.Maps;
import renderer.core.shader.Fragment;
import renderer.core.camera.Transformation;
import renderer.core.light.Lighting;
import renderer.core.mesh.Mesh;
import renderer.core.mesh.Scene;
import renderer.core.rasterizer.PerspectiveCorrectRasterizer;
import renderer.core.rasterizer.Rasterizer;
import renderer.core.shader.Shader;
import renderer.core.shader.TextureShader;

/**
 * The Renderer class drives the rendering pipeline: read in a scene, projects
 * the vertices and rasterizes every faces / edges.
 *
 * @author cdehais
 */
public final class Renderer {

    /**
     * The default scene filename.
     */
    private static final String DEFAULT_FILENAME = "data/example0.scene";

    /** The divider of the normal length. */
    private static final double DIVIDER = 100;

    /** The length of the normal. */
    private static double normalLength;

    /** The scene. */
    private Scene scene;

    /** The mesh. */
    private Mesh mesh;

    /** The rasterizer. */
    private Rasterizer rasterizer;

    /** The shader. */
    private Shader shader;

    /** The transformation. */
    private Transformation xform;

    /** The lighting. */
    private final Lighting lighting;

    /** Whether lighting is enabled. */
    private boolean lightingEnabled;

    /** Whether the normals are drawn. */
    private boolean normalsRendered;

    /** Whether the image contains vertex. */
    private boolean vertexRendered;

    /** Whether the image contains edges. */
    private boolean wiredRendered;

    /** Whether the image contains faces. */
    private boolean solidRendered;

    /**
     * Store the last texture set.
     */
    private String texture;

    /**
     * Whether a TextureShader has to combine colors in render.
     */
    private boolean combineColorState;

    /**
     * A default shader that throws an exception when used.
     */
    private static final class DefaultShader extends Shader {

        private DefaultShader() {
            super();
        }

        @Override
        public void shade(final Fragment fragment) {
            throw new IllegalArgumentException("Any Shader has been set.");
        }

        @Override
        public void reset() {
            // Nothing to reset
        }

    }

    /**
     * Creates a renderer, a controller with default values.
     *
     * @throws IOException if files doesn't exist
     */
    public Renderer() throws IOException {
        // set the shader Factory up
        ShaderFactory.init();

        // creates a lighting
        lighting = new Lighting();

        // set default scene : cube
        setScene(DEFAULT_FILENAME);

        // set a default shader that shouldn't been used.
        shader = new DefaultShader();
        rasterizer = new Rasterizer(shader);

        // draw nothing
        wiredRendered = false;
        solidRendered = false;
        lightingEnabled = false;
        normalsRendered = false;
    }

    /**
     * Renders the normals of the mesh.
     */
    public void renderNormal() {
        final Vector[] vertices = mesh.getVertices();
        final Fragment[] fragments = projectVertices();

        for (int i = 0; i < vertices.length; i++) {
            final Vector vertex = vertices[i];
            final Fragment fragment = fragments[i];
            final Vector normal = fragment.getNormal();

            final Vector destVector = new Vector(
                    vertex.get(0) + normalLength * normal.get(0),
                    vertex.get(1) + normalLength * normal.get(1),
                    vertex.get(2) + normalLength * normal.get(2));

            final Vector destVectorPoint = xform.projectPoint(destVector);

            int x = (int) Math.round(destVectorPoint.get(0));
            int y = (int) Math.round(destVectorPoint.get(1));

            final Fragment destFragment = new Fragment(x, y);
            destFragment.setColor(Color.RED);
            destFragment.setNormal(normal);
            destFragment.setDepth(destVectorPoint.get(2));

            final Fragment originFragment = fragment.clone();
            originFragment.setColor(Color.RED);

            rasterizer.rasterizeEdge(originFragment, destFragment);

        }
    }

    /**
     * Enables or disables lighting.
     *
     * @param enabled true to enable lighting, false to disable it
     */
    public void setLightingEnabled(final boolean enabled) {
        lightingEnabled = enabled;
    }

    /**
     * Sets the scene with the given filename.
     *
     * @param fileName the filename of the scene
     * @throws IOException if the file doesn't exist
     */
    public void setScene(final String fileName) throws IOException {
        scene = new Scene(fileName);
        // update mesh
        mesh = new Mesh(this.scene.getMeshFileName());
        // update transformation
        xform = new Transformation();
        xform.setLookAt(scene.getCameraPosition(),
                scene.getCameraLookAt(),
                scene.getCameraUp());
        xform.setProjection();
        xform.setCalibration(scene.getCameraFocal(),
                scene.getScreenW(),
                scene.getScreenH());

        // add lights of the scene
        lighting.reset();
        lighting.addAmbientLight(scene.getAmbientI());
        final double[] lightCoord = scene.getSourceCoord();
        lighting.addPointLight(lightCoord[0],
                lightCoord[1],
                lightCoord[2],
                scene.getSourceI());

        // determine the normal length
        initNormalLength();
    }

    /**
     * Sets the rasterizer with a Rasterizer.
     */
    public void setRasterizer() {
        if (this.rasterizer instanceof PerspectiveCorrectRasterizer) {
            this.rasterizer = new Rasterizer(shader);
        }
    }

    /**
     * Sets the rasterizer with a PerspectiveCorrectRasterizer.
     */
    public void setPerspectiveCorrectRasterizer() {
        if (!(this.rasterizer instanceof PerspectiveCorrectRasterizer)) {
            this.rasterizer = new PerspectiveCorrectRasterizer(shader);
        }
    }

    /**
     * Sets the shader to the given values.
     *
     * @param shader the new shader.
     */
    public void setShader(final Shader shader) {
        this.shader = shader;
        rasterizer.setShader(shader);
    }

    /**
     * Render an image from the current parameters.
     *
     * @return the rendered image.
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    public ImageWrapper render()
            throws SizeMismatchException {

        // returned image
        final ImageWrapper res = new ImageWrapper(scene);

        // initialize the shader with the Image Wrapper
        shader.init(this, res);

        if (vertexRendered) {
            // render vertices if needed
            renderVertices();
        }

        if (wiredRendered) {
            // render edges if needed
            renderWireframe();
            renderVertices();
        }
        if (solidRendered) {
            // render faces if needed
            renderSolid();
        }

        // render the normals if needed
        if (normalsRendered) {
            renderNormal();
        }

        return res;
    }

    /**
     * Projects the vertices of the mesh into the screen space.
     *
     * @return an array of fragments
     */
    public Fragment[] projectVertices() {
        final Vector[] vertices = mesh.getVertices();
        final Vector[] normals = mesh.getNormals();
        final double[] colors = mesh.getColors();

        final Fragment[] fragments = new Fragment[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            final Vector pVertex = xform.projectPoint(vertices[i]);
            // Vector pNormal = xform.transformVector (normals[i]);
            final Vector pNormal = normals[i];

            final int x = (int) Math.round(pVertex.get(0));
            final int y = (int) Math.round(pVertex.get(1));
            fragments[i] = new Fragment(x, y);
            fragments[i].setDepth(pVertex.get(2));
            fragments[i].setNormal(pNormal);

            final double[] texCoords = mesh.getTextureCoordinates();
            if (texCoords != null) {
                fragments[i].setAttribute(7, texCoords[2 * i]);
                fragments[i].setAttribute(8, texCoords[2 * i + 1]);
            }

            if (!lightingEnabled) {
                fragments[i].setColor(
                        colors[3 * i],
                        colors[3 * i + 1],
                        colors[3 * i + 2]);
            } else {
                final double[] color = new double[3];
                color[0] = colors[3 * i];
                color[1] = colors[3 * i + 1];
                color[2] = colors[3 * i + 2];
                final double[] material = scene.getMaterial();
                final double[] litColor = lighting.applyLights(
                        vertices[i].getSubVector(0, 3), pNormal, color,
                        scene.getCameraPosition(),
                        material[0], material[1], material[2], material[3]);
                fragments[i].setColor(litColor[0], litColor[1], litColor[2]);
            }
        }

        return fragments;
    }

    /**
     * Gets the transformation to convert the world reference to the camera
     * reference.
     *
     * @return the transformation
     */
    public Transformation getTransformation() {
        return xform;
    }

    /**
     * Sets whether the normals should be drawn.
     *
     * @param normalsRendered the normals should be normalsRendered
     */
    public void setNormalsRendered(final boolean normalsRendered) {
        this.normalsRendered = normalsRendered;
    }

    /**
     * Sets whether the wireFrameRender should be done.
     *
     * @param wiredRendered the new value
     */
    public void setWiredRendered(final boolean wiredRendered) {
        this.wiredRendered = wiredRendered;
    }

    /**
     * Sets whether the solidRender should be done.
     *
     * @param solidRendered the new value
     */
    public void setSolidRendered(final boolean solidRendered) {
        this.solidRendered = solidRendered;
    }

    /**
     * Computes the length of the normals for the rendering.
     */
    private void initNormalLength() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Vector vertex : mesh.getVertices()) {
            if (vertex.get(0) < minX) {
                minX = vertex.get(0);
            }
            if (vertex.get(0) > maxX) {
                maxX = vertex.get(0);
            }
            if (vertex.get(1) < minY) {
                minY = vertex.get(1);
            }
            if (vertex.get(1) > maxY) {
                maxY = vertex.get(1);
            }
            if (vertex.get(2) < minZ) {
                minZ = vertex.get(2);
            }
            if (vertex.get(2) > maxZ) {
                maxZ = vertex.get(2);
            }
        }

        // The length of the normal is approximately equal to 1/100 of the diagonal
        // length of the bounding box
        normalLength = (new Vector(maxX - minX, maxY - minY, maxZ - minZ)).norm() / DIVIDER;
    }

    /**
     * Renders the wireframe of the mesh.
     */
    private void renderWireframe() {
        final Fragment[] fragment = projectVertices();
        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            for (int j = 0; j < 3; j++) {
                final Fragment v1 = fragment[faces[i + j]];
                final Fragment v2 = fragment[faces[i + ((j + 1) % 3)]];
                rasterizer.rasterizeEdge(v1, v2);
            }
        }
    }

    /**
     * Renders the vertices of the mesh.
     */
    private void renderVertices() {
        final Fragment[] fragment = projectVertices();
        for (Fragment vertex : fragment) {
            rasterizer.rasterizeVertex(vertex);
        }
    }

    /**
     * Renders the solid of the mesh.
     *
     * @throws SizeMismatchException if the size of the fragments do not match
     */
    private void renderSolid()
            throws SizeMismatchException {
        final Fragment[] fragments = projectVertices();
        final int[] faces = mesh.getFaces();

        for (int i = 0; i < 3 * mesh.getNumFaces(); i += 3) {
            final Fragment v1 = fragments[faces[i]];
            final Fragment v2 = fragments[faces[i + 1]];
            final Fragment v3 = fragments[faces[i + 2]];

            rasterizer.rasterizeFace(v1, v2, v3);
        }
    }

    /**
     * Sets the shader to a instance of the given shader value.
     *
     * @param shaderSelected the name of a implementation of Shader
     * @return whether the operation is successful
     */
    public boolean setShader(final String shaderSelected) {
        final Optional<Shader> optionalShader = ShaderFactory.create(shaderSelected);
        if (optionalShader.isPresent()) {
            final Shader newShader = optionalShader.get();
            setShader(newShader);
            setTexture(texture);
            setCombineWithBaseColor(combineColorState);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the parameter combine with base color of the Texture shader.
     *
     * @param selected the new value
     */
    public void setCombineWithBaseColor(final boolean selected) {
        if (!(shader instanceof TextureShader)) {
            return;
        }
        ((TextureShader) shader).setCombineWithBaseColor(selected);
    }

    /**
     * Set the texture from the file given.
     *
     * @param path the path of the file
     * @return whether the operation as been correctly made.
     */
    public boolean setTexture(final String path) {
        if (path == null) {
            return true;
        }
        texture = path;
        if (!(shader instanceof TextureShader)) {
            return true;
        }
        return ((TextureShader) shader).setTexture(path);
    }

    /**
     * Sets the Vertex render on te given value.
     *
     * @param selected the new value.
     */
    public void setVertexRendered(boolean selected) {
        vertexRendered = selected;
    }

    /**
     * Sets the color map for depth shader.
     *
     * @param map the new colormap to use.
     */
    public void setColorMap(Maps map) {
        if (shader.supportsColorMap()) {
            shader.setColorMap(map);
        }
    }
}
