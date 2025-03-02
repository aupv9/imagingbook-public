/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.geometry.fitting.line;

import java.util.Random;

import imagingbook.common.geometry.basic.Pnt2d;


public class LineSampler {
	
	private final Random rg;
	
	private final Pnt2d p1, p2;
	
	public LineSampler(Pnt2d p1, Pnt2d p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.rg = new Random();
	}
	
	public LineSampler(Pnt2d p1, Pnt2d p2, long seed) {
		this.p1 = p1;
		this.p2 = p2;
		this.rg = new Random(seed);
	}
	
	public Pnt2d[] getPoints(int n, double sigma) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		
		Pnt2d[] pts = new Pnt2d[n];
		
		for (int i = 0; i < n; i++) {
			double x = p1.getX() + dx * i / n + rg.nextGaussian() * sigma;
			double y = p1.getY() + dy * i / n + rg.nextGaussian() * sigma;
			pts[i] = Pnt2d.from(x, y);
		}
		
		return pts;
	}

}
