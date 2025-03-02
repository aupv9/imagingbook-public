/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/

package Fourier_Shape_Descriptors;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Locale;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.ShapeRoi;
import ij.gui.TextRoi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import imagingbook.common.color.iterate.CssColorSequencer;
import imagingbook.common.geometry.basic.Pnt2d;
import imagingbook.common.math.Complex;
import imagingbook.common.regions.BinaryRegion;
import imagingbook.common.regions.Contour;
import imagingbook.common.regions.segment.RegionContourSegmentation;
import imagingbook.spectral.fd.Colors;
import imagingbook.spectral.fd.FourierDescriptor;
import imagingbook.spectral.fd.FourierDescriptorUniform;
import imagingbook.spectral.fd.PolygonSampler;

/**
 * @version 2020/04/01
 * @author WB
 *
 */
public class Private_Ellipse_Superposition_Animation implements PlugInFilter {
	
	static int StepsPerFullRevolution = 500;
	static int StepsPerSecond = 10;
	
	static String PostFix = "a";
	static int NumberOfContourSamples = 125;
	static int NumberOfFourierDescriptorPairs = 2;
	
	static boolean ShowPathPosition = true;
	static boolean ShowOriginalContour = true;
	static boolean ShowContourSamples = false;
	static boolean MarkContourCentroid = true;
	
	
	static Color 	SampleColor = Colors.Magenta;
	static double 	SampleStrokeWidth = 1.0;
	static double 	SampleRadius = 2.5; //1.5;
	
	static Color 	ContourColor = Colors.Blue;
	static double 	ContourStrokeWidth = 0.25;
	
	static double   ReconstructionRadius = 1.0;
	static Color 	ReconstructionColor = Colors.Green;
	static double 	ReconstructionStrokeWidth = 0.5; //0.75;
	
	//static Font font = new Font("Serif", Font.PLAIN, 10);
	static Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	
	static AffineTransform CtrShift = new AffineTransform(1, 0, 0, 1, 0.5, 0.5);	// shift by (0.5,0.5) to move to pixel centers
	
	ImagePlus origImage = null;
	String origTitle = null;
	static boolean verbose = true;
	
	public int setup(String arg, ImagePlus im) { 
    	origImage = im;
		origTitle = im.getShortTitle();
		//RegionLabeling.setVerbose(verbose);
		return DOES_8G + NO_CHANGES; 
	}
	
	public void run(ImageProcessor ip1) {
		
		GenericDialog dlg = new GenericDialog("Fourier Shape Composition Parameters");
		dlg.addMessage("Fourier decomosition parameters");
		dlg.addNumericField("Number contour samples", NumberOfContourSamples, 0);
		dlg.addNumericField("Number of FD pairs (min. 1)", NumberOfFourierDescriptorPairs, 0);
		dlg.addMessage("Animation parameters");
		dlg.addNumericField("Angular steps per revolution", StepsPerFullRevolution, 0);
		dlg.addNumericField("Steps per second", StepsPerSecond, 0);
		dlg.showDialog();
		if(dlg.wasCanceled()) return;
		NumberOfContourSamples = (int) dlg.getNextNumber();
		NumberOfFourierDescriptorPairs = Math.max(1, (int) dlg.getNextNumber());
		StepsPerFullRevolution = (int) dlg.getNextNumber();
		StepsPerSecond = (int) dlg.getNextNumber();
		dlg.addMessage("Press ESC or close window to stop animation.");
		
		// ------------------------------------------------------------------------
		
		ByteProcessor ip = (ByteProcessor) ip1.duplicate();
		
		//  label regions and trace contours
		RegionContourSegmentation tracer = new RegionContourSegmentation((ByteProcessor)ip);
		//IJ.log("tracing done");
		
		// extract contours and regions
		List<? extends Contour> outerContours = tracer.getOuterContours(true);
		if (outerContours.isEmpty()) {
			IJ.error("no regions found");
			return;
		}
		
		Contour contr = outerContours.get(0);	// contour of largest region
		Pnt2d[] polygon = new PolygonSampler().samplePolygonUniformly(contr.getPointArray(), NumberOfContourSamples);
		FourierDescriptor fd = new FourierDescriptorUniform(polygon);
		//fd.print();
		
		String title = origTitle + "-partial" +
				String.format("-%03d",NumberOfContourSamples) +
				String.format("-%03d",NumberOfFourierDescriptorPairs) +
				//String.format("-%03d",(int)(100*t)) +
				PostFix;
		ImagePlus im = new ImagePlus(title, ip);	
		if (ip.isInvertedLut()) {
			ip.invert();
			ip.invertLut();
		}
		brighten(ip, 220);
		im.show();

		long frameTimeMs = Math.round(1000.0/StepsPerSecond);
		
		boolean done = false;

		while (!done) {
			for (int k = 0; k < StepsPerFullRevolution; k++) {
				
				if (IJ.escapePressed() || im.getWindow() == null || im.getWindow().isClosed()) {
					done = true;
					break;
				}
				
				double t = (double) k / StepsPerFullRevolution;
				long startTime = System.currentTimeMillis();
				Overlay oly = new Overlay();
				
				if (ShowPathPosition) {
					TextRoi troi = new TextRoi(5, 0, String.format(Locale.US, "t=%.4f", t), font);
					troi.setStrokeColor(Color.black);
					oly.add(troi);
				}

				if (ShowOriginalContour) { // draw the original contour ------------------------------------
					oly.add(makeCentroidShape(contr));
				}

				if (ShowContourSamples) { // draw the contour sample points ------------------------------
					drawSamples(oly, fd.getSamples(), 0.5, 0.5);
				}

				if (MarkContourCentroid) { // mark the center ---------------------------------------------
					oly.add(makeCentroidShape(fd));
				}

				// --------------------------------------------------------------

				{ // draw the reconstructed shape from FD-pairs 0,...,mMax -------
					int mMax = NumberOfFourierDescriptorPairs;
					Path2D path = fd.makeFourierPairsReconstruction(mMax);
					path.transform(CtrShift);
					ShapeRoi roi = new ShapeRoi(path);
					roi.setStrokeColor(ReconstructionColor);
					roi.setStrokeWidth(ReconstructionStrokeWidth);
					oly.add(roi);
				}

				//  draw superposition of ellipses ----------------------

				Complex cc = fd.getCoefficient(0);	// current ellipse center
				int mMax = NumberOfFourierDescriptorPairs;
				//			RandomColorGenerator rcg = new RandomColorGenerator(1);

				CssColorSequencer csq = new CssColorSequencer();
				for (int m = 1; m <= mMax; m++) {
					//IJ.log("drawing ellipse " + m);
					//				Color color = rcg.nextColor(); //makeRandomColor();
					Color color = csq.next();
					Complex c1 = fd.getCoefficient(-m);
					Complex c2 = fd.getCoefficient(m);
					Path2D path = fd.makeEllipse(c1, c2, m, cc.re + 0.5, cc.im + 0.5);
					ShapeRoi rpoly = new ShapeRoi(path);
					rpoly.setStrokeColor(color);
					rpoly.setStrokeWidth(ReconstructionStrokeWidth/2);
					oly.add(rpoly);

					// mark the path position for t
					Complex c12 = fd.getEllipsePoint(c1, c2, m, t);
					//IJ.log("re: " + c12.re + " im: " + c12.im);
					double rad = ReconstructionRadius;
					Ellipse2D oval = 
							new Ellipse2D.Double(c12.re + cc.re - rad + 0.5, c12.im + cc.im - rad + 0.5, 2 * rad, 2 * rad);
					Roi roi = new ShapeRoi(oval);
					roi.setFillColor(color);
					//roi.setStrokeWidth(ReconstructionStrokeWidth/2);
					oly.add(roi);
					cc = cc.add(c12);
				}

				im.setOverlay(oly);
				im.updateAndDraw();
				
				long elapsedTime = System.currentTimeMillis() - startTime;
				int remainingTime = (int) (frameTimeMs - elapsedTime);
				
				if (remainingTime > 0) {
					//IJ.log("waiting " + remainingTime);
					IJ.wait(remainingTime);
				}
			}
		}
	}

	
	
	void drawSamples(Overlay oly, Complex[] samples, double dx, double dy) {
		double rad = SampleRadius;
		for (Complex c : samples) {
			Ellipse2D oval = new Ellipse2D.Double(c.re + dx - rad, c.im + dy - rad, 2 * rad, 2 * rad);
			Roi roi = new ShapeRoi(oval);
			roi.setFillColor(SampleColor);
			roi.setStrokeColor(SampleColor);
			roi.setStrokeWidth(SampleStrokeWidth);
			oly.add(roi);
		}
	}
	
	void printRegions(List<BinaryRegion> regions) {
		for (BinaryRegion r: regions) {
			IJ.log("" + r);
		}
	}
	
	void brighten(ByteProcessor ip, int minGray) {
		 float scale = (255 - minGray) / 255f;
		 int[] table = new int[256];
		 for (int i=0; i<256; i++) {
			 table[i] = (int) Math.round(minGray + scale * i);
			 
		 }
		 ip.applyTable(table);
	}
	
	void darken(ByteProcessor ip, int maxGray) {
		 float scale = maxGray / 255f;
		 int[] table = new int[256];
		 for (int i=0; i<256; i++) {
			 table[i] = (int) Math.round(scale * i);
			 
		 }
		 ip.applyTable(table);
	}
	
	// ----------------------------------------------------------------------
	
	ShapeRoi makeCentroidShape(FourierDescriptor fd) { 
		int crossSize = 2;
		Complex G0 = fd.getCoefficient(0);
		double xc = G0.re;
		double yc = G0.im;
		Path2D path = new Path2D.Double();
		path.moveTo(xc-crossSize, yc);
		path.lineTo(xc+crossSize, yc);
		path.moveTo(xc, yc-crossSize);
		path.lineTo(xc, yc+crossSize);
		path.transform(CtrShift);
		ShapeRoi roi = new ShapeRoi(path);
		roi.setStrokeColor(ReconstructionColor);
		roi.setStrokeWidth(ReconstructionStrokeWidth);
		return roi;
	}
	
	ShapeRoi makeCentroidShape(Contour contr) { // draw the original contour ------------------------------------
		Path2D path = contr.getPolygonPath();
		path.transform(CtrShift);
		ShapeRoi roi = new ShapeRoi(path);
		roi.setStrokeColor(ContourColor);
		roi.setStrokeWidth(ContourStrokeWidth);
		return roi;
	}

	
//	private float h0 = 0;
//	Color makeRandomColor() {
//		double saturation = 0.5;
//		double brightness = 0.75;
//		float h = h0;  h0 = (float) (h0 + Math.PI / 10) % 1.0f; //r.nextFloat();
//		IJ.log("h=" + h);
//		float s = (float) saturation;
//		float b = (float) brightness;
//		return new Color(Color.HSBtoRGB(h, s, b));
//	}
	
}
