package org.vetinari;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.SneakyThrows;
import org.vetinari.config.ConfigParser;
import org.vetinari.render.Renderer;
import org.vetinari.template.TemplateEngine;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Loads a site and its content pages
 */
public class SiteLoader implements Provider<Site>
{
	public static final String TEMPLATE_KEY = "template_engine";
	public static final String RENDERER_KEY = "renderer";

	private final Set<TemplateEngine> templateEngines;
	private final Set<Renderer> renderers;
	private final Set<ConfigParser> configParsers;
	private final Map<String, BiFunction<Object[], Site, String>> functions;

	private final Configuration configuration;
	private final Config siteConfig;

	@Inject
	public SiteLoader(Configuration configuration, @Named("siteConfig") Config siteConfig,
	                  Set<TemplateEngine> templateEngines, Set<Renderer> renderers, Set<ConfigParser> configParsers, Map<String, BiFunction<Object[], Site, String>> functions)
	{
		this.templateEngines = templateEngines;
		this.renderers = renderers;
		this.configParsers = configParsers;
		this.functions = functions;
		this.configuration = configuration;
		this.siteConfig = siteConfig;
	}

	public Site load() throws IOException
	{
		Site.SiteBuilder siteBuilder = Site.builder();
		siteBuilder.siteConfig(siteConfig);

		Renderer defaultRenderer = Iterables.find(renderers, r -> r.getName().equals(siteConfig.getString("defaultRenderer")));
		TemplateEngine defaultTemplateEngine = Iterables.find(templateEngines, t -> t.getName().equals(siteConfig.getString("defaultTemplateEngine")));

		ImmutableMap.Builder<String, Page> pagesBuilder = ImmutableMap.builder();
		Files.walk(configuration.getContentRoot()).filter(Files::isRegularFile).forEach(file -> {
			Page page = parsePage(file, configuration, defaultRenderer, defaultTemplateEngine);
			pagesBuilder.put(page.getIdentifier(), page);
		});
		siteBuilder.pages(pagesBuilder.build());

		siteBuilder.configuration(configuration)
				.functions(functions)
				.renderers(renderers)
				.templateEngines(templateEngines)
				.defaultRenderer(defaultRenderer)
				.defaultTemplateEngine(defaultTemplateEngine);


		return siteBuilder.build();
	}

	@SneakyThrows(IOException.class)
	private Page parsePage(Path file, Configuration configuration, Renderer defaultRenderer, TemplateEngine defaultTemplateEngine)
	{
		Path relativePath = configuration.getContentRoot().relativize(file);

		try(BufferedReader reader = Files.newBufferedReader(file))
		{
			Config metadata = ConfigFactory.empty();
			String content = "";
			Renderer renderer = defaultRenderer;
			TemplateEngine templateEngine = defaultTemplateEngine;

			String firstLine = reader.readLine();

			if(!Strings.isNullOrEmpty(firstLine))
			{
				boolean foundMetadata = false;
				firstLine = firstLine.trim();
				for(ConfigParser parser : configParsers)
				{
					if(parser.getStartDelimiter().equals(firstLine))
					{
						final StringBuilder frontMatter = new StringBuilder();
						String line = reader.readLine();
						while(line != null && !parser.getEndDelimiter().equals(line.trim()))
						{
							frontMatter.append(line).append('\n');
							line = reader.readLine();
						}
						metadata = parser.parse(frontMatter.toString());
						foundMetadata = true;
						break;
					}
				}

				content = CharStreams.toString(reader);
				if(!foundMetadata)
				{
					content = firstLine + '\n' + content;
				}

				final Iterable<String> extensions = getExtensions(file.toString());

				if(metadata.hasPath("renderer"))
				{
					final String rendererName = metadata.getString("renderer");
					renderer = Iterables.find(renderers, r -> r.getName().equals(rendererName));
				}
				else
				{
					Optional<Renderer> maybeRenderer = Iterables.tryFind(renderers, r -> Iterables.any(extensions, e -> Iterables.contains(r.getFileExtensions(), e)));
					if(maybeRenderer.isPresent())
					{
						renderer = maybeRenderer.get();
					}
				}

				if(metadata.hasPath("templateEngine"))
				{
					final String templateEngineName = metadata.getString("templateEngine");
					templateEngine = Iterables.find(templateEngines, e -> e.getName().equals(templateEngineName));
				}
				else
				{
					Optional<TemplateEngine> maybeTemplateEngine = Iterables.tryFind(templateEngines, t -> Iterables.any(extensions, e -> Iterables.contains(t.getFileExtensions(), e)));
					if(maybeTemplateEngine.isPresent())
					{
						templateEngine = maybeTemplateEngine.get();
					}
				}
			}

			return new Page(metadata, relativePath, templateEngine, renderer, content);
		}
	}

	private Iterable<String> getExtensions(String name)
	{
		List<String> parts = Splitter.on('.').splitToList(name);
		if(!parts.get(0).isEmpty()) // if the first part is empty, then it's a dotfile. Otherwise, remove the base name
		{
			parts = parts.subList(1, parts.size()); // splitToLists returns an immutable list
		}
		return parts;
	}

	@Override
	public Site get()
	{
		try
		{
			return load();
		}
		catch(IOException e)
		{
			throw Throwables.propagate(e);
		}
	}
}
