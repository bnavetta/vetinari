package com.bennavetta.vetinari.parse;

import com.bennavetta.vetinari.Configuration;
import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

// Avoid conflict with java.nio.file.Files
import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;

/**
 * Parse content pages.
 */
public class PageParser
{
	private final Set<Renderer> renderers;
	private final Set<TemplateEngine> templateEngines;

	private static final ImmutableMap<String, ConfigSyntax> DELIMITERS =
			ImmutableMap.of("+++", ConfigSyntax.CONF, "---", ConfigSyntax.PROPERTIES, "~~~", ConfigSyntax.JSON);

	@Inject
	public PageParser(Set<Renderer> renderers, Set<TemplateEngine> templateEngines)
	{
		this.renderers = renderers;
		this.templateEngines = templateEngines;
	}

	public Page parsePage(Path file, Configuration configuration) throws PageParseException
	{
		Path relativePath = configuration.getContentRoot().relativize(file);

		try(BufferedReader reader = Files.newBufferedReader(file, configuration.getContentEncoding()))
		{
			Config metadata = ConfigFactory.empty();
			StringBuilder contentBuilder = new StringBuilder();

			final String firstLine = reader.readLine();
			if(!Strings.isNullOrEmpty(firstLine)) // only look for metadata if there's actually content
			{
				ConfigSyntax syntax = DELIMITERS.get(firstLine.trim());
				if(syntax != null) // frontmatter present
				{
					final String delimiter = firstLine.trim();
					final StringBuilder frontmatterBuilder = new StringBuilder();
					String line = null;
					while(!delimiter.equals(line))
					{
						if(line == null)
						{
							throw new PageParseException("Reached EOF before end delimiter '" + delimiter + "' in " + file);
						}

						frontmatterBuilder.append(line).append('\n');

						line = reader.readLine();
					}

					metadata = ConfigFactory.parseString(frontmatterBuilder.toString(), ConfigParseOptions.defaults()
					                                                                                      .setOriginDescription(file.toString())
					                                                                                      .setSyntax(syntax));
				}
				else
				{
					contentBuilder.append(firstLine).append('\n');
				}
			}

			contentBuilder.append(CharStreams.toString(reader));

			/*
			File extension rules:
            - If there is only one file extension, it is the renderer
            - If there are two file extensions, the first is the template engine and the second is the renderer
			 */

			Renderer renderer = null;
			final String extension = getFileExtension(file.toString());
			Optional<Renderer> rendererFromExtension = Iterables.tryFind(renderers, r -> Iterables.contains(r.getFileExtensions(), extension));
			if(rendererFromExtension.isPresent())
			{
				renderer = rendererFromExtension.get();
			}
			else if(metadata.hasPath("renderer"))
			{
				final String rendererName = metadata.getString("renderer");
				renderer = Iterables.find(renderers, r -> rendererName.equals(r.getName()));
			}
//			else
//			{
//				renderer = defaultRenderer;
//			}

			TemplateEngine templateEngine = null;
			final String templateExtension = getFileExtension(getNameWithoutExtension(file.toString()));
			Optional<TemplateEngine> templateEngineFromExtension = Iterables.tryFind(templateEngines, t -> Iterables.contains(t.getFileExtensions(), templateExtension));
			if(templateEngineFromExtension.isPresent())
			{
				templateEngine = templateEngineFromExtension.get();
			}
			else if(metadata.hasPath("templateEngine"))
			{
				final String templateEngineName = metadata.getString("templateEngine");
				templateEngine = Iterables.find(templateEngines, t -> templateEngineName.equals(t.getName()));
			}
//			else
//			{
//				templateEngine = defaultTemplateEngine;
//			}

			return new Page(metadata, relativePath, templateEngine, renderer, contentBuilder.toString());
		}
		catch (IOException e)
		{
			throw new PageParseException("Error reading page content for " + file, e);
		}
	}
}
