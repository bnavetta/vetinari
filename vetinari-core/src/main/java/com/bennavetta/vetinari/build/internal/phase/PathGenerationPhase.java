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
import com.bennavetta.vetinari.VetinariContext;
import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.build.BuildPhase;
import com.bennavetta.vetinari.build.PathGenerator;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.typesafe.config.ConfigValueFactory;

import java.util.Set;

/**
 * Compute output paths and links for pages. The output path will be added to the metadata under the
 * {@code outputPath} key and the link address will be added under the {@code link} key. Neither property
 * will be altered if already set. The {@link PathGenerator} to use will be looked up by name, first in the page's
 * {@code pathGenerator property}, then in the site's {@code defaultPathGenerator} property.
 */
public final class PathGenerationPhase implements BuildPhase
{
	private final VetinariContext context;
	private final Set<PathGenerator> pathGenerators;

	@Inject
	public PathGenerationPhase(VetinariContext context, Set<PathGenerator> pathGenerators)
	{
		this.context = context;
		this.pathGenerators = pathGenerators;
	}

	private Page process(Page page)
	{
		PathGenerator generator = getPathGenerator(page);

		if(!page.getMetadata().hasPath("outputPath"))
		{
			page = page.withMetadata(page.getMetadata().withValue("outputPath", ConfigValueFactory.fromAnyRef(generator.outputPath(page))));
		}
		if(!page.getMetadata().hasPath("link"))
		{
			page = page.withMetadata(page.getMetadata().withValue("link", ConfigValueFactory.fromAnyRef(generator.linkTo(page))));
		}

		return page;
	}

	private PathGenerator getPathGenerator(Page page)
	{
		final String generatorName = page.getMetadata().hasPath("pathGenerator")
		                             ? page.getMetadata().getString("pathGenerator")
		                             : context.getSiteConfig().getString("defaultPathGenerator");

		return Iterables.find(pathGenerators, generator -> generatorName.equals(generator.getName()));
	}

	@Override
	public String getName()
	{
		return "pathGeneration";
	}

	@Override
	public Site process(Site site) throws VetinariException
	{
		return site.transformPages(this::process);
	}
}
