/**
 * Copyright 2014 Ben Navetta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bennavetta.vetinari.build;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.VetinariException;

/**
 * Implements a step in the process of building a site.
 */

public interface BuildPhase
{
	public static final int ORDER_TEMPLATE = 200;
	public static final int ORDER_RENDER = 400;
	public static final int ORDER_LAYOUT = 600;
	public static final int ORDER_OUTPUT = 800;

	/**
	 * Returns the order in which this step is to be run. For reference, constants are defined for built-in phases.
	 * Phase ordering is defined numerically instead of through dependencies since an absolute positioning is somewhat
	 * easier to understand in this case. Each phase is a function of the previous, and thus of all phases prior to it,
	 * so a numerical order is sort of like having a dependency on each phase that conceptually should run first.
	 */
	public int getOrder();

	/**
	 * Process the site. Sites and pages can be seen as
	 * <a href="http://en.wikipedia.org/wiki/Persistent_data_structure">persistent data structures</a> where each phase
	 * is a transformation on the data structure that produces a new structure. The {@code with*} methods on {@link Site}
	 * and {@link Page} can be used for these incremental updates.
	 * @param input the site produced by the previous phase
	 * @return the processed site, to pass into the next phase
	 * @throws VetinariException if there is a problem processing the site
	 */
	public Site process(Site input) throws VetinariException;

	/**
	 * Returns the display name of this phase. By default it is the simple class name without the {@code Phase}
	 * suffix if present.
	 * @see Class#getSimpleName()
	 */
	default String getName() //NOSONAR
	{
		String baseName = getClass().getSimpleName();
		return baseName.endsWith("Phase") ? baseName.substring(0, baseName.length() - 5) : baseName;
	}
}
