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
package com.bennavetta.vetinari.parse;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.VetinariContext;
import com.bennavetta.vetinari.render.Renderer;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * Parse content pages.
 */
@Slf4j
public class PageParser
{
	private static final ImmutableMap<String, ConfigSyntax> DELIMITERS =
			ImmutableMap.of("+++", ConfigSyntax.CONF, "---", ConfigSyntax.PROPERTIES, "~~~", ConfigSyntax.JSON);

	public Page parsePage(Path file, VetinariContext context) throws PageParseException
	{
		log.info("Parsing content file {}", file);
		Path relativePath = context.getContentRoot().relativize(file);

		try(BufferedReader reader = Files.newBufferedReader(file, context.getContentEncoding()))
		{
			Config metadata = ConfigFactory.empty();
			StringBuilder contentBuilder = new StringBuilder();

			final String firstLine = reader.readLine();
			if(!Strings.isNullOrEmpty(firstLine)) // only look for metadata if there's actually content
			{
				ConfigSyntax syntax = DELIMITERS.get(firstLine.trim());
				if(syntax != null) // frontmatter present
				{
					log.debug("Detected frontmatter syntax: {}", syntax);

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
					log.trace("Read frontmatter {}", metadata);
				}
				else
				{
					contentBuilder.append(firstLine).append('\n');
				}
			}

			contentBuilder.append(CharStreams.toString(reader));
			log.trace("Read page content: \"{}\"", contentBuilder);

			return new Page(metadata, relativePath, contentBuilder.toString());
		}
		catch (IOException e)
		{
			throw new PageParseException("Error reading page content for " + file, e);
		}
	}
}
