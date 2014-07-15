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

/**
 * Determines where to place generated files and how to access them.
 */
public interface PathGenerator
{
	/**
	 * Returns the name of this path generator, for configuration purposes.
	 */
	public String getName();

	/**
	 * Calculate the path, relative to the site output root, where the fully-processed page content should be written.
	 * @param page the page
	 * @return a relative path string
	 */
	public String outputPath(Page page);

	/**
	 * Calculate the address for linking to the given page. The returned address will not be resolved further, so it may
	 * be absolute or relative as desired.
	 * @param page the page
	 * @return an address, suitable for use in links
	 */
	public String linkTo(Page page);
}
