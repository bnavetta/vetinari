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
import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.VetinariContext;
import com.bennavetta.vetinari.VetinariException;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Parse a {@link Site} and its content pages.
 */
public class SiteLoader
{
	private final PageParser pageParser;

	@Inject
	public SiteLoader(PageParser pageParser)
	{
		this.pageParser = pageParser;
	}

	public Site load(VetinariContext context) throws VetinariException
	{
		Site.SiteBuilder siteBuilder = Site.builder();

		// The default template engine and renderer names should be in VetinariContext
		// The actual objects can't be used because that could create a cycle between VetinariContext and the implementations

		ImmutableMap.Builder<String, Page> pageBuilder = ImmutableMap.builder();
		try
		{
			for(Path file : Files.walk(context.getContentRoot()).collect(Collectors.toList()))
			{
				Page page = pageParser.parsePage(file, context);
				pageBuilder.put(page.getIdentifier(), page);
			}
		}
		catch (IOException e)
		{
			throw new VetinariException("Unable to load content pages", e);
		}
		siteBuilder.pages(pageBuilder.build());

		return siteBuilder.build();
	}
}
