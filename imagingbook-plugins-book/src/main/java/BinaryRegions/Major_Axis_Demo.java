/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package BinaryRegions;

import static imagingbook.common.math.Arithmetic.sqr;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import imagingbook.common.geometry.basic.Pnt2d;
import imagingbook.common.ij.IjUtils;
import imagingbook.common.regions.BinaryRegion;
import imagingbook.common.regions.segment.RegionContourSegmentation;

/**
 * Shows each region's major axis as a vector scaled by the region's eccentricity.
 * Also demonstrates the use of the region property scheme, i.e., how
 * to assign numeric properties to regions and retrieve them afterwards.
 * Requires a binary (segmented) image.
 * 
 * TODO: convert to overlay display
 * 
 * @author W. Burger
 * @version 2020/12/17
 */
public class Major_Axis_Demo implements PlugInFilter {
	
	static double AxisLength = 50;
	static Color CenterColor = Color.blue;
	static Color MajorAxisColor = CenterColor;
	static int LineWidth = 1;
	
	private ImagePlus im;
	
	public int setup(String arg, ImagePlus im) {
		this.im = im;
		return DOES_8G + NO_CHANGES; 
	}

	public void run(ImageProcessor ip) {
		
		if (!IjUtils.isBinary(ip)) {
			IJ.showMessage("Plugin requires a binary image!");
			return;
		}
		
		// perform region segmentation:
		RegionContourSegmentation segmenter = new RegionContourSegmentation((ByteProcessor) ip);
		List<BinaryRegion> regions = segmenter.getRegions(true);

		// calculate and register certain region properties:
		for (BinaryRegion r : regions) {
			calculateRegionProperties(r);
		}
		
		// create the output (color) image:
		ColorProcessor cp = ip.convertToColorProcessor();
		cp.add(210);
		
		// draw major axis vectors (scaled by eccentricity): 
		
		for (BinaryRegion r : regions) {
			if (r.getSize() > 10) {
				Pnt2d xc = r.getCenter();
				int u0 = (int) Math.round(xc.getX());
				int v0 = (int) Math.round(xc.getY());
				
				double dx = r.getProperty("dx");
				double dy = r.getProperty("dy");
				int u1 = (int) Math.round(xc.getX() + AxisLength * dx);
				int v1 = (int) Math.round(xc.getY() + AxisLength * dy);
				
				drawCenter(cp,  u0,  v0);
				drawAxis(cp, u0, v0, u1, v1);
			}
		}
		
		// display the output image
		new ImagePlus(im.getShortTitle() + "-major-axis", cp).show();
	}
	
	private void calculateRegionProperties(BinaryRegion r) {
		// calculate central moment mu11, mu20, mu02:
		Pnt2d xctr = r.getCenter();
		double xc = xctr.getX();
		double yc = xctr.getY();
		double mu11 = 0;
		double mu20 = 0;
		double mu02 = 0;
		for (Pnt2d p : r) {
			double dx = (p.getX() - xc);
			double dy = (p.getY() - yc);
			mu11 = mu11 + dx * dy;
			mu20 = mu20 + dx * dx;
			mu02 = mu02 + dy * dy;
		}
		
		double A = 2 * mu11;
		double B = mu20 - mu02;
		
		double normAB = Math.sqrt(sqr(A) + sqr(B));
		double scale = sqrt(2 * (sqr(A) + sqr(B) + B * sqrt(sqr(A) + sqr(B))));
		
		double dx = B + normAB;
		double dy = A;
		
		r.setProperty("dx", dx / scale);
		r.setProperty("dy", dy / scale);
		
		// --------------------------------------------------
		
		// calculate 2 versions of eccentricity:
		double a = mu20 + mu02;
		double b = Math.sqrt(Math.pow(mu20 - mu02, 2) + 4 * mu11 * mu11);
		r.setProperty("ecc1", (a + b) / (a - b));
		r.setProperty("ecc2", (Math.pow(mu20 - mu02,  2) + 4 * mu11 * mu11) / Math.pow(mu20 + mu02, 2));
	}
	
	private void drawCenter(ImageProcessor ip, int uc, int vc) {
		ip.setColor(CenterColor);
		ip.setLineWidth(LineWidth);
		ip.drawRect(uc - 2, vc - 2, 5, 5);
	}
	
	private void drawAxis(ImageProcessor ip, int u0, int v0, int u1, int v1) {
		ip.setColor(MajorAxisColor);
		ip.setLineWidth(LineWidth);
		ip.drawLine(u0, v0, u1, v1);
	}

}