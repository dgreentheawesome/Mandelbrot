/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math.numbertypes;

import static java.lang.Double.doubleToRawLongBits;
import java.util.HashSet;

/**
 *
 * @author David
 */
public class DoubleNT extends NumberType {

    public static final DoubleNT ZERO = new DoubleNT(0);
    public static final DoubleNT ONE = new DoubleNT(1);
    public static final DoubleNT TEN = new DoubleNT(10);
    public static final int MAX_ZOOM = 13;
    double u;

    public DoubleNT(double u) {
        this.u = u;
    }

    @Override
    public NumberType add(NumberType addend) {
        return new DoubleNT(u + ((DoubleNT) addend).u);
    }

    @Override
    public NumberType subtract(NumberType subtrahend) {
        return new DoubleNT(u - ((DoubleNT) subtrahend).u);
    }

    @Override
    public NumberType multiply(NumberType multiplicand) {
        return new DoubleNT(u * ((DoubleNT) multiplicand).u);
    }

    @Override
    public NumberType divide(NumberType dividend) {
        return new DoubleNT(u / ((DoubleNT) dividend).u);
    }

    @Override
    public NumberType add(double addend) {
        return new DoubleNT(u + addend);
    }

    @Override
    public NumberType subtract(double subtrahend) {
        return new DoubleNT(u - subtrahend);
    }

    @Override
    public NumberType multiply(double multiplicand) {
        return new DoubleNT(u * multiplicand);
    }

    @Override
    public NumberType divide(double dividend) {
        return new DoubleNT(u / dividend);
    }

    @Override
    public NumberType square() {
        return new DoubleNT(u * u);
    }

    @Override
    public int compareTo(int i) {
        return Double.compare(u, i);

    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DoubleNT other = (DoubleNT) obj;
        if (Double.doubleToLongBits(this.u) != Double.doubleToLongBits(other.u)) {
            return false;
        }
        return true;
    }

    @Override
    public NumberType mult2() {
        return new DoubleNT(2 * u);
    }

    public static int hashCode(double value) {
        long bits = doubleToRawLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }
    
    public int mEscape(NumberType x_curr, NumberType y_curr, HashSet<Integer> hashes, int MAX_ITERATIONS) {
        // System.out.println("esc start");
        double xn, yn, y0, x0, xt;
        int z;
        x0 = xn = ((DoubleNT) x_curr).u;
        yn = y0 = ((DoubleNT) y_curr).u;
        xt = z = 0;
        while (z < MAX_ITERATIONS - 1) {
            xt = xn * xn;

            if (xt + yn * yn > 4) {
                //System.out.println("problem");
                //System.out.println("esc stop");
                return z;
            }
            if (!hashes.add(37 * hashCode(xn + yn))) {
                //System.out.println("saved " + (MAX_ITERATIONS - z));
                //System.out.println("esc stop");

                return MAX_ITERATIONS;
            }
            xt = x0 + xt - yn * yn;
            yn = xn * (yn + yn) + y0;
            xn = xt;
            z++;
        }
        //System.out.println("esc stop");
        return MAX_ITERATIONS;
    }

    @Override
    public NumberType toNextSystem() {
        return new DoubleDouble(u);
    }

    public String toString() {
        return "" + u;
    }

    @Override
    public NumberType toPreviousSystem() {
        return new DoubleNT(u);
    }

    @Override
    public int JEscape(NumberType x, NumberType y, NumberType c0, NumberType c1, HashSet<Integer> hashes, int MAX_ITERATIONS) {
        double xn, yn, y0, x0, xt;
        int z = 0;
        xn = ((DoubleNT) x).u;
        yn = ((DoubleNT) y).u;
        x0 = ((DoubleNT) c0).u;
        y0 = ((DoubleNT) c1).u;
        while (z < MAX_ITERATIONS - 1) {
            xt = xn * xn;

            if (xt + yn * yn == Double.POSITIVE_INFINITY) {
                //System.out.println("problem");
                //System.out.println("esc stop");
                return z;
            }
            if (!hashes.add(37 * hashCode(xn + yn))) {
                //System.out.println("saved " + (MAX_ITERATIONS - z));
                //System.out.println("esc stop");

                return MAX_ITERATIONS;
            }
            xt = x0 + xt - yn * yn;
            yn = xn * (yn + yn) + y0;
            xn = xt;
            z++;
        }
        //System.out.println("esc stop");
        return MAX_ITERATIONS;
    }

    @Override
    public int getRelativePrecision() {
        return 1;
    }

}
