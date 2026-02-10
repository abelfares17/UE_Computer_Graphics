package renderer.core.mesh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import renderer.algebra.Vector;

/**
 * Defines a triangle based mesh.
 * A mesh is constructed by interpreting the data given in an OFF file.
 * @author smondet gg cdehais
 */
public class Mesh {

    /**
     * The number of vertices per face.
     */
    private static final int VERTICES_PER_FACE = 3;
    /**
     * The number of color components per vertex.
     */
    private static final int COLOR_COMPONENTS_PER_VERTEX = 3;
    /**
     * The dimension of the vertices of the mesh.
     */
    private static final int VERTEX_DIMENSION = 3;
    /**
     * The vertices of the mesh.
     */
    private Vector[] vertices;
    /**
     * The faces of the mesh.
     */
    private int[] faces;
    /**
     * The colors of the vertices of the mesh.
     */
    private double[] colors;
    /**
     * The normals of the vertices of the mesh.
     */
    private Vector[] normals;
    /**
     * The texture coordinates of the vertices of the mesh.
     */
    private double[] texCoords;

    private static String nextLine(BufferedReader in) throws IOException {
        String r = in.readLine();

        while (r.matches("\\s*#.*")) {
            r = in.readLine();
        }
        return r;
    }

    /**
     * Builds a Mesh object by reading in an OFF file.
     * Does not support non triangular meshes.
     * @param filename path to OFF file.
     * @throws IOException if the file cannot be read.
     */
    public Mesh(String filename) throws IOException  {
        BufferedReader in = new BufferedReader(new FileReader(filename));

        String r = nextLine(in);
        if (!r.equals("OFF")) {
            throw new IOException("Invalid OFF file !");
        }

        r = nextLine(in);

        String[] sar = r.split("\\s+");

        // Parse object properties
        int nbVert = Integer.parseInt(sar[0]);
        int nbFaces = Integer.parseInt(sar[1]);

        // Parse vertices and attributes
        vertices = new Vector[nbVert];
        faces = new int[VERTICES_PER_FACE * nbFaces];
        colors = new double[COLOR_COMPONENTS_PER_VERTEX * nbVert];
        for (int i = 0; i < nbVert; i++) {

            r = nextLine(in);
            sar = r.split("\\s+");

            vertices[i] = new Vector("v" + i, VERTEX_DIMENSION);
            vertices[i].set(0, Double.parseDouble(sar[0]));
            vertices[i].set(1, Double.parseDouble(sar[1]));
            vertices[i].set(2, Double.parseDouble(sar[2]));
            colors[COLOR_COMPONENTS_PER_VERTEX * i + 0] = Double.parseDouble(sar[3]);
            colors[COLOR_COMPONENTS_PER_VERTEX * i + 1] = Double.parseDouble(sar[4]);
            colors[COLOR_COMPONENTS_PER_VERTEX * i + 2] = Double.parseDouble(sar[5]);

            // optional texture coordinates
            if (sar.length >= 8) {
                if (texCoords == null) {
                    texCoords = new double[2 * nbVert];
                }
                texCoords[2 * i] = Double.parseDouble(sar[6]);
                texCoords[2 * i + 1] = Double.parseDouble(sar[7]);
            }
        }

        // Parse faces
        for (int i = 0; i < nbFaces; i++) {

            r = nextLine(in);
            sar = r.split("\\s+");

            final int en = Integer.parseInt(sar[0]);
            if (en != VERTICES_PER_FACE) {
                throw new IOException("Non-triangular meshes not supported.");
            }
            for (int j = 0; j < en; j++) {
                faces[VERTICES_PER_FACE * i + j] = Integer.parseInt(sar[j + 1]);
            }

        }
        in.close();
    }

    /**
     * Gets the number of vertices in the mesh.
     * @return the number of vertices in the mesh
     */
    public int getNumVertices() {
        return vertices.length;
    }

    /**
     * Gets the number of faces in the mesh.
     * @return the number of faces in the mesh
     */
    public int getNumFaces() {
        return faces.length / VERTICES_PER_FACE;
    }

    /**
     * Constructs a normal for each vertex of the mesh
     * by averaging the normals of the faces that share the vertex.
     * @return an array of Vector containing the normals of each vertex.
     */
    private Vector[] computeNormals() {

        normals = new Vector[vertices.length];

        // Compute per face normals and set the vertex normal to the average normals
        // across faces to the vertex.
        final int numFaceElements = VERTICES_PER_FACE * getNumFaces();
        for (int i = 0; i < numFaceElements; i += VERTICES_PER_FACE) {
            // TODO
            Vector n = new Vector(3);










            // add the calculated normal n to each vertex of the face
            for (int j = 0; j < VERTICES_PER_FACE; j++) {
                Vector nj = normals[faces[i + j]];

                if (nj == null) {
                    normals[faces[i + j]] = new Vector(n);
                    normals[faces[i + j]].setName("n" + faces[i + j]);
                } else {
                    // add() returns a new vector, so we assign the result to the normal.
                    normals[faces[i + j]] = nj.add(n);
                }
            }
        }

        // final round of normalization
        for (int i = 0; i < normals.length; i++) {
            // deal with orphans vertices
            if (normals[i] == null) {
                normals[i] = new Vector("n_orphan", 3);
            } else {
                normals[i] = normals[i].normalize();
            }
        }

        return normals;
    }

    /**
     * Returns the vertices of the mesh.
     * @return an array of Vector containing the vertices of the mesh
     */
    public Vector[] getVertices() {
        return vertices;
    }

    /**
     * Return the normals associated to the vertices.
     * If the normals have not been computed yet, they are computed.
     * @return an array of Vector containing the normals of the mesh
     */
    public Vector[] getNormals() {
        if (normals == null) {
            normals = computeNormals();
        }

        return normals;
    }

    /**
     * Returns the faces of the mesh. The returned array contains 3*n integers, with
     * n the number of faces.
     * Each integer is an index into the array of Vector.
     * The indices are grouped by 3, each group representing a face.
     * @return an array of int containing the faces of the mesh
     */
    public int[] getFaces() {
        return faces;
    }

    /**
     * Returns the colors of each vertex in the mesh.
     * The returned array contains 3*n doubles, with n the number of vertices.
     * Each group of 3 doubles represents the color of a vertex.
     * @return an array of double containing the colors of the mesh
     */
    public double[] getColors() {
        return colors;
    }

    /**
     * Returns the texture coordinates of each vertex in the mesh.
     * The returned array contains 2*n doubles, with n the number of vertices.
     * Each group of 2 doubles represents the texture coordinates of a vertex.
     * @return an array of double containing the texture coordinates of the mesh
     */
    public double[] getTextureCoordinates() {
        return texCoords;
    }

}
