package renderer.core.camera;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;


/**
 * The Transformation class represents a transformation in 3D space.
 * author: cdehais
 */
public class Transformation {

    /**
     * The world to camera matrix.
     */
    private Matrix worldToCamera;
    /**
     * The 3x4 projection matrix.
     */
    private Matrix projection;
    /**
     * The 3x3 calibration matrix.
     */
    private Matrix calibration;

    /**
     * Creates a new Transformation object.
     */
    public Transformation() {
        final int w2cDim = 4;
        worldToCamera = Matrix.createIdentity("W2C", w2cDim);
        final int projRows = 3;
        final int projCols = 4;
        projection = new Matrix("P", projRows, projCols);
        final int calibDim = 3;
        calibration = Matrix.createIdentity("K", calibDim);
    }

    /**
     * Sets the lookAt transformation.
     * @param eye a 3D vector representing the eye position
     * @param lookAtPoint a 3D vector representing the point to look at
     * @param up a 3D vector representing the up direction
     */
    public void setLookAt(final Vector eye, final Vector lookAtPoint, final Vector up) {
        try {
            // compute rotation
            Vector z_temp = lookAtPoint.subtract(eye);
            Vector z = z_temp.normalize();
            Vector x = (up.cross(z)).normalize();
            Vector y = z.cross(x);

            Matrix R_cw = Matrix.createIdentity(3);
            /*
            R_cw.setCol(1, x);
            R_cw.setCol(2, y);
            R_cw.setCol(3, z);
            */
           for (int i = 0; i < 3; i++) {
               for (int j = 0; j < 3; j++) {
                   R_cw.set(i, j, switch (j) {
                       case 0 -> x.get(i);
                       case 1 -> y.get(i);
                       case 2 -> z.get(i);
                       default -> throw new IllegalStateException("Unexpected value: " + j);
                   });
               }
           }
            Matrix R_wc = R_cw.transpose();

            Vector t = (R_wc.multiply(eye)).scale(-1);

            worldToCamera.set(3,3,1);
            for (int i = 0; i<3; i++){
                for (int j = 0; j<3; j++){
                    worldToCamera.set(i, j, R_wc.get(i,j));
                }
                worldToCamera.set(i,3,t.get(i));
                worldToCamera.set(3,i, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Modelview matrix:\n" + worldToCamera);
    }

    /**
     * Sets the projection matrix.
     */
    public void setProjection() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                projection.set(i, j, 0);
            }
        }
        projection.set(0, 0, 1);
        projection.set(1, 1, 1);
        projection.set(2, 2, 1);


        System.out.println("Projection matrix:\n" + projection);
    }

    /**
     * Sets the calibration matrix.
     * @param focal the focal length
     * @param width the width of the image
     * @param height the height of the image
     */
    public void setCalibration(double focal, double width, double height) {

        calibration.set(0, 0, focal);
        calibration.set(1, 1, focal);
        calibration.set(0, 2, width / 2);
        calibration.set(1, 2, height / 2);
        calibration.set(2, 2, 1);
        System.out.println("Calibration matrix:\n" + calibration);
    }

    /**
     * Projects the given 3 dimensional point onto the screen.
     * The resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
     * is the depth of the point in the camera coordinate system.
     * @param p a 3d vector representing a point
     * @return the projected point as a 3d vector, with (x,y) the pixel
     * coordinates and z the depth
     * @throws SizeMismatchException if the size of the input vector is not 3
     */
    public Vector projectPoint(Vector p) throws SizeMismatchException {
        // 1. Convertir le point en coordonnées homogènes (3 -> 4)
        Vector ph = p.homogeneousPoint();

        // 2. Appliquer la matrice de transformation du monde vers la caméra
        Vector pcam = worldToCamera.multiply(ph);

        // 3. Appliquer la matrice de projection
        Vector pproj = projection.multiply(pcam);

        // 4. Appliquer la matrice de calibration
        Vector pcalib = calibration.multiply(pproj);

        // 5. Division par z pour la perspective
        double depth = pcalib.get(2);
        Vector ps = new Vector(3);
        ps.set(0, pcalib.get(0) / depth);
        ps.set(1, pcalib.get(1) / depth);
        ps.set(2, depth);

        return ps;
    }

    /**
     * Transform a vector from world to camera coordinates.
     * @param v the vector to transform
     * @return the transformed vector
     * @throws SizeMismatchException if the size of the input vector is not 3
     */
    public Vector transformVector(final Vector v) {
        // Doing nothing special here because there is no scaling
        final Matrix m = worldToCamera.getSubMatrix(0, 0, 3, 3);
        return m.multiply(v);
    }

}
