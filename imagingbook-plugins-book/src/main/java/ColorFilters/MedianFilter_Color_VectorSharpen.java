/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package ColorFilters;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.common.filter.nonlinear.VectorMedianFilterSharpen;
import imagingbook.common.filter.nonlinear.VectorMedianFilterSharpen.Parameters;
import imagingbook.common.math.VectorNorm.NormType;
import imagingbook.common.util.Enums;

/**
 * This plugin applies a sharpening vector median filter to a RGB color image.
 * @author W. Burger
 * @version 2013/05/30
 */
public class MedianFilter_Color_VectorSharpen implements PlugInFilter {
	
	ImagePlus imp = null;
	
    public int setup(String arg, ImagePlus imp) {
    	this.imp = imp;
        return DOES_RGB;
    }

    public void run(ImageProcessor ip) {
    	Parameters params = new VectorMedianFilterSharpen.Parameters();
    	if (!setParameters(params))
    		return;

    	VectorMedianFilterSharpen filter = new VectorMedianFilterSharpen(params);
    	filter.applyTo(ip);
    	
 //   	IJ.log("Pixels modified: " + filter.modifiedCount + " of " + (ip.getPixelCount()));
    }
    
    boolean setParameters(Parameters params) {
		GenericDialog gd = new GenericDialog("Median Filter");
		gd.addNumericField("Radius", params.radius, 1);
		gd.addNumericField("Sharpen", params.sharpen, 1);
		gd.addNumericField("Threshold", params.threshold, 1);
		String[] normChoices = Enums.getEnumNames(NormType.class);
		gd.addChoice("Distance norm", normChoices, params.distanceNorm.name());
		gd.addCheckbox("Mark modified pixels", params.markModifiedPixels);
		gd.addCheckbox("Show mask", params.showMask);
		
		gd.showDialog();
		if(gd.wasCanceled()) return false;
		params.radius = Math.max(gd.getNextNumber(),0.5);
		params.sharpen = gd.getNextNumber();
		params.threshold = gd.getNextNumber();
		params.distanceNorm = NormType.valueOf(gd.getNextChoice());
		params.markModifiedPixels = gd.getNextBoolean();
		params.showMask = gd.getNextBoolean();
		return true;
    }
}

