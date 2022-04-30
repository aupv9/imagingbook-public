/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.geometry.ellipse;


public interface Ellipse {
	
	/**
	 * Return a vector of parameters for this ellipse.
	 * The length of the vector and the meaning of the parameters depends
	 * on the concrete ellipse type.
	 * 
	 * @return a vector of parameters
	 * @see AlgebraicEllipse
	 * @see GeometricEllipse
	 */
	public double[] getParameters();

}
