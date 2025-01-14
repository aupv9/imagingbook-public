/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.sampleimages;

import imagingbook.core.resource.ImageResource;

public enum RansacTestImage implements ImageResource {
	
	NoisyLines("noisy-lines.png"), 
	NoisyCircles("noisy-circles.png"), 
	NoisyEllipses("noisy-ellipses.png");
	
	private static final String BASEDIR = "ransac/";
	
	private final String relPath;
	
	@Override
	public String getRelativePath() {
		return relPath;
	}
	
	RansacTestImage(String filename) {
		this.relPath = BASEDIR + filename;
	}
}
