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
package com.bennavetta.vetinari;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.bennavetta.vetinari.template.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This defines the set of steps for actually building a site.
 */
public class SiteBuilder
{
	private final Configuration configuration;

	private Site site;

	@Inject
	public SiteBuilder(Site site, Configuration configuration)
	{
		this.site = site;
		this.configuration = configuration;
	}

	/*
	Steps to compile a site:
	 */

	// Step 1: Templating
	private Stream<BuildingPage> templatePages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", site, "page", bp.page);
			//TODO: extension point for additional variables

			bp.update(bp.page.getTemplateEngine().compile(bp.currentContent, site).render(variables));
			return bp;
		});
	}

	// Step 2: Render pages
	private Stream<BuildingPage> renderPages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			bp.update(bp.page.getRenderer().render(bp.currentContent));
			return bp;
		});
	}

	// Step 3: Apply layouts (layouts don't get rendered - have to be HTML)
	private Stream<BuildingPage> layoutPages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			Template layoutTemplate = site.getTemplate(Objects.firstNonNull(bp.page.getLayout(), site.getDefaultLayout()));
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", site, "page", bp.page, "content", bp.currentContent);
			bp.update(layoutTemplate.render(variables));
			return bp;
		});
	}

	public void build() throws IOException
	{
		// Convert to page stream
		// In theory, I can do parallel builds by using parallelStream() instead of stream()
		Stream<BuildingPage> pages = site.getPages().stream().map(page -> new BuildingPage(page, page.getContent()));

		// TODO: add a stage right after the site load that can change the model (i.e. set parameters, add pages)

		// Pipeline Steps
		final UnaryOperator<Stream<BuildingPage>> templateStep = this::templatePages;
		final UnaryOperator<Stream<BuildingPage>> renderStep = this::renderPages;
		final UnaryOperator<Stream<BuildingPage>> layoutStep = this::layoutPages;

		// TODO: add a stage to post-process the finished pages

		final Function<Stream<BuildingPage>, Stream<BuildingPage>> pipeline =  templateStep
				.andThen(renderStep)
				.andThen(layoutStep);

		for(BuildingPage output : pipeline.apply(pages).collect(Collectors.toList()))
		{
			//TODO: extension point for output paths (enable plugins for e.g. pretty urls)
			Path outputPath = configuration.getOutputRoot().resolve(output.page.getIdentifier() + ".html");

			Files.write(outputPath, output.currentContent.getBytes(configuration.getContentEncoding()));
		}
	}

	/**
	 * Model a page in the process of being built. This enables state to be passed
	 * along the pipeline in the form of page objects while various compilation stages are applied.
	 */
	private static final class BuildingPage
	{
		public Page page;
		public String currentContent;

		public BuildingPage(Page page, String currentContent)
		{
			this.page = page;
			this.currentContent = currentContent;
		}

		public BuildingPage update(String newContent)
		{
			this.currentContent = newContent;
			return this;
		}
	}
}
