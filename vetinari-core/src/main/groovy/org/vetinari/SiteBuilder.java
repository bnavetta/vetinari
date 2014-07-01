package org.vetinari;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.vetinari.parse.Parser;
import org.vetinari.template.Context;
import org.vetinari.template.Template;
import org.vetinari.template.TemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
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

	// Components needed to build the site
	private final Parser pageParser;

	// Extension points
	private final Set<TemplateEngine> templateEngines;
	private final Map<String, BiFunction<Object[], Context, String>> templateFunctions;

	private Context templateContext;
	private Site site;

	@Inject
	public SiteBuilder(Configuration configuration, Parser pageParser,
	                   Set<TemplateEngine> templateEngines, Map<String, BiFunction<Object[], Context, String>> templateFunctions)
	{
		this.configuration = configuration;
		this.pageParser = pageParser;
		this.templateEngines = templateEngines;
		this.templateFunctions = templateFunctions;
	}

	/*
	Steps to compile a site:
	 */

	// Step 1: Load the pages
	private Site loadSite() throws IOException
	{
		Site site = new Site(configuration, pageParser);
		site.load();
		return site;
	}

	// Step 2: Templating
	private Stream<BuildingPage> templatePages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", templateContext.getSite(), "page", bp.page);
			//TODO: extension point for additional variables

			bp.update(bp.page.getTemplateEngine().compile(bp.currentContent, templateContext).render(variables));
			return bp;
		});
	}

	// Step 3: Render pages
	private Stream<BuildingPage> renderPages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			bp.update(bp.page.getRenderer().render(bp.currentContent));
			return bp;
		});
	}

	// Step 4: Apply layouts (layouts don't get rendered - have to be HTML)
	private Stream<BuildingPage> layoutPages(Stream<BuildingPage> pages)
	{
		return pages.map(bp -> {
			Template layoutTemplate = templateContext.getTemplate(Objects.firstNonNull(bp.page.getLayout(), site.getDefaultLayout()));
			final ImmutableMap<String, Object> variables = ImmutableMap.of("site", site, "page", bp.page, "content", bp.currentContent);
			bp.update(layoutTemplate.render(variables));
			return bp;
		});
	}

	public void build() throws IOException
	{
		// Initialize parameters
		site = loadSite();
		templateContext = new Context(configuration, templateEngines, site, templateFunctions);

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
