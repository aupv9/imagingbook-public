/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.geometry.fitting.ellipse.algebraic;

import static imagingbook.common.math.Arithmetic.sqr;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import imagingbook.common.geometry.basic.Pnt2d;
import imagingbook.common.geometry.basic.PntUtils;
import imagingbook.common.math.Matrix;
import imagingbook.common.math.eigen.GeneralizedSymmetricEigenDecomposition;

/**
 * <p>
 * Algebraic ellipse fit based on Taubin's method [1]. Version 1 uses the full
 * scatter and constraint matrix (6x6), the solution is found by a generalized
 * symmetric eigendecomposition. Note that the constraint matrix (C) is not
 * positive definite. 
 * See [3, Sec. 11.2.1] for a detailed description (Alg. 11.7).
 * This implementation performs data centering or,
 * alternatively, accepts a specific reference point.
 * <p>
 * [1] G. Taubin, G. Taubin. "Estimation of planar curves, surfaces, and
 * nonplanar space curves defined by implicit equations with applications to
 * edge and range image segmentation", IEEE Transactions on Pattern Analysis and
 * Machine Intelligence 13(11), 1115–1138 (1991).
 * <br>
 * [2] W. Burger, M.J. Burge, <em>Digital Image Processing - An Algorithmic Approach</em>, 
 * 3rd ed, Springer (2022).
 * </p>
 * 
 * @author WB
 * @version 2021/11/09
 * @see EllipseFitTaubin2
 */
public class EllipseFitTaubin1 implements EllipseFitAlgebraic {
	
	private final double[] q;	// = (A,B,C,D,E,F) ellipse parameters
	
	public EllipseFitTaubin1(Pnt2d[] points, Pnt2d xref) {
		if (points.length < 5) {
			throw new IllegalArgumentException("at least 5 points must be supplied for ellipse fitting");
		}
		this.q = fit(points, xref);
		
	}
	
	public EllipseFitTaubin1(Pnt2d[] points) {
		this(points, PntUtils.centroid(points));
	}

	@Override
	public double[] getParameters() {
		return this.q;
	}
	
	// --------------------------------------------------------------------------
	
	private double[] fit(Pnt2d[] points, Pnt2d xref) {
		final int n = points.length;
		
		// reference point
		final double xr = xref.getX();
		final double yr = xref.getY();

		// design matrix (same as Fitzgibbon):
		RealMatrix X = MatrixUtils.createRealMatrix(n, 6);
		for (int i = 0; i < n; i++) {
			final double x = points[i].getX() - xr;	// center data set
			final double y = points[i].getY() - yr;
			double[] fi = {sqr(x), x*y, sqr(y), x, y, 1};
			X.setRow(i, fi);
		}
		
		// scatter matrix S (normalized by 1/n)
		RealMatrix S = X.transpose().multiply(X).scalarMultiply(1.0/n);
			
		double a = S.getEntry(0, 5);
		double b = S.getEntry(2, 5);
		double c = S.getEntry(1, 5);	//2*s[1][5];
		double d = S.getEntry(3, 5);
		double e = S.getEntry(4, 5);
		
		// constraint matrix
		RealMatrix C = Matrix.makeRealMatrix(6, 6, 
				4*a, 2*c, 0,   2*d, 0,   0 , 
				2*c, a+b, 2*c, e,   d,   0 , 
				0,   2*c, 4*b, 0,   2*e, 0 , 
				2*d, e,   0,   1,   0,   0 ,
				0,   d,   2*e, 0,   1,   0 , 
				0,   0,   0,   0,   0,   0 );
		
		// solve C*p = lambda*S*p (note: C is not positive definite!)
		GeneralizedSymmetricEigenDecomposition eigen = 
				new GeneralizedSymmetricEigenDecomposition(C, S, 1e-15, 1e-15);	
		
		double[] evals = eigen.getRealEigenvalues();
		
		int k = Matrix.idxMax(evals);	// index of largest eigenvalue
		double[] qopt = eigen.getEigenvector(k).toArray();
		
		RealMatrix U = getDataOffsetCorrectionMatrix(xr, yr);
		return U.operate(qopt);
	}
}
