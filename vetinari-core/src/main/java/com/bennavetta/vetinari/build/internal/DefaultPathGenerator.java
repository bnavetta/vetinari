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
package com.bennavetta.vetinari.build.internal;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.build.PathGenerator;

/**
 * Basic implementation that just writes {@code my/page} to {@code my/page.html}.
 */
public class DefaultPathGenerator implements PathGenerator
{
	@Override
	public String getName()
	{
		return "default";
	}

	@Override
	public String outputPath(Page page)
	{
		return page.getIdentifier() + ".html";
	}

	@Override
	public String linkTo(Page page)
	{
		return outputPath(page);
	}
}
