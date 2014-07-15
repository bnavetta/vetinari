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
package com.bennavetta.vetinari.build.internal.phase;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.build.BuildPhase;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import javax.inject.Inject;

import java.util.Set;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

/**
 * Apply standard templating to content pages.
 */
public class TemplatePhase implements BuildPhase
{
	private final Set<TemplateEngine> templateEngines;

	@Inject
	public TemplatePhase(Set<TemplateEngine> templateEngines)
	{
		this.templateEngines = templateEngines;
	}

	@Override
	public Site process(Site input)
	{
		return input.transformPages(page -> {
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", input, "page", page);
			final TemplateEngine engine = getTemplateEngine(page, input);
			return page.withContent(engine.compile(page.getContent()).render(variables));
		});
	}

	private TemplateEngine getTemplateEngine(Page page, Site site)
	{
		TemplateEngine templateEngine = null;
		// Use the second file extension, if any. The first is for the renderer.
		final String templateExtension = getFileExtension(getNameWithoutExtension(page.getPath().toString()));
		Optional<TemplateEngine> templateEngineFromExtension = Iterables.tryFind(templateEngines, t -> Iterables.contains(t.getFileExtensions(), templateExtension));
		if(templateEngineFromExtension.isPresent())
		{
			templateEngine = templateEngineFromExtension.get();
		}
		else if(page.getMetadata().hasPath("templateEngine"))
		{
			final String templateEngineName = page.getMetadata().getString("templateEngine");
			templateEngine = Iterables.find(templateEngines, t -> templateEngineName.equals(t.getName()));
		}
		else
		{
			templateEngine = Iterables.find(templateEngines, t -> site.getDefaultTemplateEngine().equals(t.getName()));
		}
		return templateEngine;
	}

	@Override
	public String getName()
	{
		return "template";
	}
}
