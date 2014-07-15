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
	/**
	 * Each build phase has a unique name. This name is used in the site configuration file to determine
	 * which phases to run. 
	 * @return the phase name
	 */
	public String getName();

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
}
