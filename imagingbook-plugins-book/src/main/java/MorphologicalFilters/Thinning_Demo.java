/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/

package MorphologicalFilters;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import imagingbook.common.morphology.BinaryThinning;

/**
 * This ImageJ plugin demonstrates morphological thinning
 * on binary images. Pixels with value 0 are considered
 * background, values &gt; 0 are foreground. The plugin 
 * modifies the supplied image.
 * 
 * @author WB
 * @version 2022/01/24
 *
 */
public class Thinning_Demo implements PlugInFilter {
	
	private int maxIterations;
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
	}

	@Override
	public void run(ImageProcessor ip) {
		maxIterations = Math.max(ip.getWidth(), ip.getHeight());
		
		if (!showDialog()) {
			return;
		}
		
		BinaryThinning thin = new BinaryThinning();
		thin.applyTo((ByteProcessor) ip, maxIterations);
		IJ.log("Iterations performed: " + thin.getIterations());
	}
	
	private boolean showDialog() {
		GenericDialog gd = new GenericDialog(this.getClass().getSimpleName());
		gd.addNumericField("max. iterations", maxIterations, 0);

		gd.showDialog();
		if (gd.wasCanceled())
			return false;
		
		maxIterations = (int) gd.getNextNumber();
		return true;
	}
	
}

