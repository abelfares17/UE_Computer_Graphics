
import org.junit.Test;
import static org.junit.Assert.fail;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;


public class TestAlgebra {

    /**
     * Test various algebra operations.
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        System.out.println("Algebra\n# Test Start");

        Vector v = new Vector(3);
        Vector v1 = new Vector(1);
        v1.setName("up");
        try {
            new Vector(0);
            fail("Expected exception for zero size vector not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong size exception caught OK");
        }
        try {
            new Vector("named", 0);
            fail("Expected exception for zero size vector not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong size exception caught OK");
        }

        System.out.println(v);
        System.out.println(v1);

        Vector u1 = new Vector("u1", 3);
        u1.set(0, 1.0);
        u1.set(1, 2.0);
        u1.set(2, 3.0);
        Vector u2 = new Vector(3);
        u2.set(new double[3]);
        u2.set(1, 2.0);

        System.out.println(u1);
        System.out.println("norm(u1) = " + u1.norm());
        System.out.println(u2);
        System.out.println("u1.u2 = " + u1.dot(u2));

        try {
            u1 = new Vector("u1", 3);
            u2 = new Vector("u2", 4);
            u1.dot(u2);
            fail("Size mismatch exception not thrown");
        } catch (SizeMismatchException e) {
            System.out.println("Caught exception: " + e);
        }

        Vector r1 = new Vector("u1", 1.0, 2.0, 3.0);
        Vector r2 = new Vector("u2", 1.0, 3.0, 1.0);

        Vector r = r1.cross(r2);

        System.out.println(r1);
        System.out.println(r2);
        System.out.println("r1.r2 = " + r1.dot(r2));
        System.out.println("r1.r2 = " + r1.dot((Vector) r2) + " (as Vector)");
        System.out.println("r1 x r2 = " + r);
        System.out.println("norm(" + r.getName() + ") = " + r.norm());
        r = r.normalize();
        System.out.println("norm(" + r.getName() + ") (after normalize()) = " + r.norm());

        // -------------------------------------
        System.out.println("--\n-- Matrix tests\n--");

        Matrix M = new Matrix(3, 5);
        System.out.println(M);
        Matrix M1 = new Matrix("M1", 2, 5);
        M1.set(0, 0, 2.0);
        M1.set(1, 4, -8.0);
        M1.set(1, 2, 5.0);

        try {
            new Matrix("M2", 0, 2);
            fail("Expected exception for zero size matrix not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong size exception caught OK");
        }

        try {
            M.multiply(M1);
        } catch (SizeMismatchException e) {
            System.out.println("Caught exception: " + e);
        }

        Matrix mr = Matrix.createRandom("mr", 3, 5);
        System.out.println(mr);
        System.out.println(mr.transpose());

        Matrix M2 = Matrix.createIdentity(5);
        System.out.println(M2);

        M = M1.multiply(M2);
        System.out.println(M1);
        System.out.println(M2);
        System.out.println(M);

        M1 = M1.transpose();
        M1.setName("M1'");
        System.out.println(M1);

        Vector u = new Vector("u", 5);
        u.ones();
        v = M.multiply(u);
        System.out.println(M);
        System.out.println(u);
        System.out.println("M.u = " + v);

        try {
            u = new Vector("u", 3);
            v = M.multiply(u);
        } catch (SizeMismatchException e) {
            System.out.println("Caught exception: " + e);
        }

        Matrix I = Matrix.createIdentity(5);
        Matrix S = I.getSubMatrix(0, 2, 3, 3);
        System.out.println(I);
        System.out.println("submatrix:\n" + S);

    }

}
