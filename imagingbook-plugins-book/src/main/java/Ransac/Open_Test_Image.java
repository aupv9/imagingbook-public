/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package Ransac;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import imagingbook.sampleimages.RansacTestImage;

/**
 * Select and open a local RANSAC-related test image.
 * @author WB
 *
 */
public class Open_Test_Image implements PlugIn {
	
	static RansacTestImage selection = RansacTestImage.NoisyCircles;

	@Override
	public void run(String arg) {
		
		GenericDialog gd = new GenericDialog(getClass().getSimpleName());
		gd.addEnumChoice("Select image", selection);
		
		gd.showDialog();
		if (gd.wasCanceled())
			return;
		
		selection = gd.getNextEnumChoice(RansacTestImage.class);
		
		ImagePlus im = selection.getImage();
		im.show();
	}

}
