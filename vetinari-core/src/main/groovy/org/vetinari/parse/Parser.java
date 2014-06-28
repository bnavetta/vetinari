package org.vetinari.parse;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.vetinari.Configuration;
import org.vetinari.Page;
import org.vetinari.render.Renderer;
import org.vetinari.template.TemplateEngine;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Parse files into {@link org.vetinari.Page} objects.
 */
public class Parser
{
	public static final String FRONT_MATTER_DELIM = "+++";
	public static final String TEMPLATE_KEY = "template";
	public static final String RENDERER_KEY = "renderer";

	private final Set<TemplateEngine> templateEngines;
	private final Set<Renderer> renderers;

	private final Configuration configuration;

	@Inject
	public Parser(Set<TemplateEngine> templateEngines, Set<Renderer> renderers, Configuration configuration)
	{
		this.templateEngines = templateEngines;
		this.renderers = renderers;
		this.configuration = configuration;
	}

	public Page parse(Path source) throws IOException
	{
		Path relative = configuration.getContentRoot().relativize(source);

		try(BufferedReader reader = Files.newBufferedReader(source, configuration.getContentEncoding()))
		{
			ConfigObject metadata = new ConfigObject();

			final String firstLine = reader.readLine();
			if(firstLine.trim().endsWith(FRONT_MATTER_DELIM))
			{
				final StringBuilder frontmatter = new StringBuilder();
				String line = reader.readLine();
				while(line != null && !FRONT_MATTER_DELIM.equals(line))
				{
					frontmatter.append(line).append('\n');
					line = reader.readLine();
				}

				//TODO: ConfigSlurper is nice and powerful, but it could be a bit unfamiliar
				// Typesafe Config has ConfigFactory.parseString(), which I could use for
				// HOCON, and then support JSON and YAML with different delimiters
				metadata = new ConfigSlurper().parse(frontmatter.toString());
			}

			String content = CharStreams.toString(reader);

			Renderer renderer = detect(source, )
		}
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
