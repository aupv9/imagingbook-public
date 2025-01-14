/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.filter.linear;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ij.process.ImageProcessor;
import imagingbook.core.resource.ImageResource;
import imagingbook.sampleimages.GeneralTestImage;
import imagingbook.testutils.ImageTestUtils;

public class GaussianFilterTest {
	
	static double SIGMA = 3.0;
	static float TOL = 1f;	// deviations +/-1 are possible due to rounding to integer images

	ImageResource res1A = GeneralTestImage.MonasterySmall;
	ImageResource res1B = GeneralTestImage.MonasterySmallGauss3;
	
	ImageResource res2A = GeneralTestImage.Clown;
	ImageResource res2B = GeneralTestImage.ClownGauss3;
	
	// -----------------------------------------------------------------------
	
	@Test
	public void testGaussianGray() {
		ImageProcessor ipA = res1A.getImage().getProcessor();
		ImageProcessor ipB = res1B.getImage().getProcessor();
		
		//new GaussianFilter(ipA, SIGMA).apply();
		new GaussianFilter(SIGMA).applyTo(ipA);
		assertTrue(ImageTestUtils.match(ipA, ipB, TOL));
	}
	
	@Test
	public void testGaussianRgb() {
		ImageProcessor ipA = res2A.getImage().getProcessor();
		ImageProcessor ipB = res2B.getImage().getProcessor();
		
		new GaussianFilter(SIGMA).applyTo(ipA);
		assertTrue(ImageTestUtils.match(ipA, ipB, TOL));
	}
	
	// -----------------------------------------------------------------------
	
	@Test
	public void testGaussianSeparableGray() {
		ImageProcessor ipA = res1A.getImage().getProcessor();
		ImageProcessor ipB = res1B.getImage().getProcessor();
		
		new GaussianFilter(SIGMA).applyTo(ipA);
		assertTrue(ImageTestUtils.match(ipA, ipB, TOL));
	}

	@Test
	public void testGaussianSeparableRgb() {
		ImageProcessor ipA = res2A.getImage().getProcessor();
		ImageProcessor ipB = res2B.getImage().getProcessor();
		
		new GaussianFilter(SIGMA).applyTo(ipA);
		assertTrue(ImageTestUtils.match(ipA, ipB, TOL));
	}
}
