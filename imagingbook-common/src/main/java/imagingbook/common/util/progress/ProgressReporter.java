/*******************************************************************************
 * This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause 
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause). 
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge. 
 * All rights reserved. Visit http://www.imagingbook.com for additional details.
 *******************************************************************************/
package imagingbook.common.util.progress;

import imagingbook.common.filter.generic.GenericFilter;

/**
 * Monitored objects (tasks) must implement this interface.
 * See {@link GenericFilter} for an example.
 * See also {@link ProgressMonitor}.
 * 
 */
public interface ProgressReporter {
	
	/**
	 * Returns a value in [0,1) indicating to which degree this
	 * task is complete.
	 * @return a value between 0 and 1
	 */
	public double getProgress();

}
