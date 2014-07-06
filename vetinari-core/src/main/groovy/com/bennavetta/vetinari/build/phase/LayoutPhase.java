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
package com.bennavetta.vetinari.build.phase;

import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.build.BuildPhase;
import com.bennavetta.vetinari.template.Template;
import com.bennavetta.vetinari.template.TemplateLoader;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

/**
 * Layout pages with the configured layout templates.
 */
public class LayoutPhase implements BuildPhase
{
	private final TemplateLoader loader;

	@Inject
	public LayoutPhase(TemplateLoader loader)
	{
		this.loader = loader;
	}

	@Override
	public int getOrder()
	{
		return BuildPhase.ORDER_LAYOUT;
	}

	@Override
	public Site process(Site input) throws Exception
	{
		return input.transformPages(page -> {
			Template layout = loader.get(page.getMetadata().hasPath("layout") ? page.getMetadata().getString("layout") : input.getDefaultLayout());
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", input, "page", page, "content", page.getContent());
			return page.withContent(layout.render(variables));
		});
	}
}
