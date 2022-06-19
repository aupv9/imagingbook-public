/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.math.eigen;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import imagingbook.common.math.Matrix;
import imagingbook.common.math.testing.RandomMatrixGenerator;

public class EigensolverNxNTest {

	@Test
	public void testEigensolverNxNa() {
		double[][] M = {
				{3, -2},
				{-4, 1}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNb() {
		double[][] M = {
				{-0.004710, -0.006970},
				{-0.006970, -0.029195}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNc() {
		double[][] M = {
				{0, 0},
				{0, 1}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNd() {
		double[][] M = {
				{1, 0},
				{0, 0}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNe() {
		double[][] M = {
				{1, 0},
				{-2, 1}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNf() {
		double[][] M = {
				{1, -2},
				{0, 1}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNg() {
		double[][] M = {
				{1, 2},
				{2, 1}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolver2x2H() {
		double[][] M = {
				{0, -1},
				{2, 0}};
		runEigenTest(M, false);
	}
	
	@Test
	public void testEigensolver2x2I() {
		double[][] M = {
				{4, -1},
				{2, 4}};
		runEigenTest(M, false);
	}
	
	@Test
	public void testEigensolverNxNh() {
		double[][] M = {
				{5, 2, 0},
				{2, 5, 0},
				{-3, 4, 6}};
		runEigenTest(M);
	}
	
	@Test
	public void testEigensolverNxNi() {
		double[][] M = {		// has complex eigenvalues!
				{5, 2, 0, 1},
				{2, 5, 0, 7},
				{-3, 4, 6, 0},
				{1 , 2, 3, 4}};
		runEigenTest(M, false);
	}
	
	@Test
	public void testRandomMatrix2x2() {
		RandomMatrixGenerator rg = new RandomMatrixGenerator(17);
		final int N = 1000;
		//int cnt = 0;
		for (int i = 0; i < N; i++) {
			double[][] A = rg.makeRandomSymmetricMatrix(2);
			RealEigensolver solver = new EigensolverNxN(A);
			if (solver.isReal()) {
				//cnt++;
				double[] eigenvals = solver.getEigenvalues();
				for (int k = 0; k < eigenvals.length; k++) {
					double lambda = eigenvals[k];
					double[] x = solver.getEigenvector(k);
					assertArrayEquals(Matrix.multiply(A, x), Matrix.multiply(lambda, x), 1E-6);
				}
			}
		}
		//System.out.println("real solutions: " + cnt + " out of " + N);
	}
	
	// ---------------------------------------------------------
	
	private void runEigenTest(double[][] M) {
		runEigenTest(M, true);
	}

	private void runEigenTest(double[][] M, boolean shouldBeReal) {
		RealEigensolver solver = new EigensolverNxN(M);	

		if (shouldBeReal) {
			assertTrue(solver.isReal());
		}
		else {
			assertFalse(solver.isReal());
			return;
		}
		
		double[] eigenvals = solver.getEigenvalues();
		
		
		for (int k = 0; k < eigenvals.length; k++) {
			if (Double.isNaN(eigenvals[k])) {
				continue;
			}
			//System.out.println("testing " + eigenvals[k]);
			double lambda = eigenvals[k];
			double[] x = solver.getEigenvector(k);
			// check: M * x_k = λ_k * x_k
			assertArrayEquals(Matrix.multiply(M, x), Matrix.multiply(lambda, x), 1E-6);
		}
	}

}
