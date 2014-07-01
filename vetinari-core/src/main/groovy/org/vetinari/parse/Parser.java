package org.vetinari.parse;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.vetinari.Configuration;
import org.vetinari.Page;
import org.vetinari.parse.config.ConfigParser;
import org.vetinari.render.Renderer;
import org.vetinari.template.TemplateEngine;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Parse files into {@link org.vetinari.Page} objects.
 */
public class Parser
{
	public static final String TEMPLATE_KEY = "template_engine";
	public static final String RENDERER_KEY = "renderer";

	private final Set<TemplateEngine> templateEngines;
	private final Set<Renderer> renderers;
	private final Set<ConfigParser> configParsers;

	private final Configuration configuration;

	@Inject
	public Parser(Configuration configuration, Set<TemplateEngine> templateEngines, Set<Renderer> renderers, Set<ConfigParser> configParsers)
	{
		this.templateEngines = templateEngines;
		this.renderers = renderers;
		this.configParsers = configParsers;
		this.configuration = configuration;
	}

	public Page parse(Path source) throws IOException
	{
		Path relative = configuration.getContentRoot().relativize(source);

		try(BufferedReader reader = Files.newBufferedReader(source, configuration.getContentEncoding()))
		{
			Config metadata = extractMetadata(reader);

			String content = CharStreams.toString(reader);

			String rendererName = metadata.hasPath(RENDERER_KEY) ? metadata.getString(RENDERER_KEY) : null;
			Renderer renderer = detect(source, rendererName, renderers, configuration.getDefaultRenderer());

			String templateEngineName = metadata.hasPath(TEMPLATE_KEY) ? metadata.getString(TEMPLATE_KEY) : null;
			TemplateEngine templateEngine = detect(source, templateEngineName, templateEngines, configuration.getDefaultTemplateEngine());

			return new Page(metadata, relative, templateEngine, renderer, content);
		}
	}

	private Config extractMetadata(BufferedReader reader) throws IOException
	{
		Config metadata = ConfigFactory.empty();

		final String firstLine = reader.readLine().trim();
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
				break;
			}
		}

		return metadata;
	}

	private <T extends Engine> T detect(Path path, String name, Iterable<T> candidates, T defaultImplementation)
	{
		// Resolution order: name, path, default

		if(!Strings.isNullOrEmpty(name))
		{
			// Use find(), since it's an error if nothing matches the specified name
			return Iterables.find(candidates, c -> name.equalsIgnoreCase(c.getName()));
		}

		for(String extension : getExtensions(path.getFileName().toString()))
		{
			Optional<T> match = Iterables.tryFind(candidates, c -> Iterables.contains(c.getFileExtensions(), extension));
			if(match.isPresent())
			{
				return match.get();
			}
		}

		return defaultImplementation;
	}

	private Iterable<String> getExtensions(String name)
	{
		List<String> parts = Splitter.on('.').splitToList(name);
		if(!parts.get(0).isEmpty()) // if the first part is empty, then it's a dotfile. Otherwise, remove the base name
		{
			parts.remove(0);
		}
		return parts;
	}
}
