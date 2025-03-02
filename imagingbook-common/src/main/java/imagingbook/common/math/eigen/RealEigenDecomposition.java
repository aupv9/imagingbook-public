package imagingbook.common.math.eigen;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Common interface for eigendecompositions capable of
 * delivering solutions when all eigenvalues are real.
 * TODO: add 'ComplexEigenDecomposition' interface and implementations
 *  
 * @author WB
 * @version 2022/07/08
 */
public interface RealEigenDecomposition {
	
	/**
     * Returns whether the calculated eigenvalues are complex or real.
     *
     * @return {@code true} if any of the eigenvalues is complex, {@code false} otherwise
     */
	public boolean hasComplexEigenvalues();
	
	/**
	 * Returns the real part of the k-th eigenvalue
	 * 
	 * @param k index of the eigenvalue @param k index of the eigenvector (0-based)
	 * @return real part of the k-th eigenvalue
	 */
	public double getRealEigenvalue(int k);
	
//	/**
//	 * Returns the imaginary part of the k-th eigenvalue
//	 * @return imaginary part of the k-th eigenvalue
//	 */
//	public default double getImagEigenvalue(int k) {
//		throw new UnsupportedOperationException();
//	}
	
	/**
	 * Returns a vector holding the real parts of the eigenvalues
	 * @return real(diag(D))
	 */
	public double[] getRealEigenvalues();
	
//	/**
//	 * Returns a vector holding the imaginary parts of the eigenvalues
//	 * @return imag(diag(D))
//	 */
//	public default double[] getImagEigenvalues() {
//		throw new UnsupportedOperationException();
//	}
	
	 /**
     * Returns the k-th eigenvector, i.e., the
     * k-th column vector of the matrix returned
     * by {@link #getV()}.
     * 
     * @param k index of the eigenvector (0-based)
     * @return the k-th eigenvector (instance of {@link RealVector})
     */
	public RealVector getEigenvector(int k);
	
	/**
	 * Return the matrix of eigenvectors, which are its column vectors.
	 * 
	 * @return the matrix of eigenvectors
	 */
	public RealMatrix getV();
	
	
	/**
     * Gets the block diagonal matrix D of the decomposition.
     * Real eigenvalues are on the diagonal while complex values are on
     * 2x2 blocks {{real pos imaginary}, {neg imaginary, real}}.
     *
	 * @return matrix D
	 */
	public default RealMatrix getD() {
		return MatrixUtils.createRealDiagonalMatrix(getRealEigenvalues());
	}
	

}
