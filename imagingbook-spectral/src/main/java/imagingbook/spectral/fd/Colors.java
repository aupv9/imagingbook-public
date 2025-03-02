/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/

package imagingbook.spectral.fd;

import java.awt.Color;

public class Colors {
	
	public static Color Red		= new Color(240, 0, 0);
	public static Color Green 	= new Color(0, 185, 15);
	public static Color Blue 	= new Color(0, 60, 255);
	public static Color Magenta = new Color(255, 0, 200);
	public static Color Yellow 	= new Color(255, 155, 0);
	public static Color Brown 	= new Color(128, 66, 36);
	
	public static Color[] defaultDisplayColors = {
		Red, Green, Blue, Magenta, Yellow, Brown
		};
	
	public static class ColorSequencer {
		
		private int nextColorIndex = 0;
		
		public ColorSequencer() {
		}
		
		public ColorSequencer(int firstIndex) {
			nextColorIndex = firstIndex % defaultDisplayColors.length;
		}
		
		public Color nextColor() {
			Color c = defaultDisplayColors[nextColorIndex];
			nextColorIndex = (nextColorIndex + 1) % defaultDisplayColors.length;
			return c;
		}
		
	}

}
