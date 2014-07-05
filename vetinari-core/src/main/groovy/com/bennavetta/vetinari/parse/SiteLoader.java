package com.bennavetta.vetinari.parse;

import com.bennavetta.vetinari.Configuration;
import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.Site;
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

	public Site load(Configuration configuration) throws IOException, PageParseException
	{
		Site.SiteBuilder siteBuilder = Site.builder();

		// Configuration will be replaced with VetinariContext, which will provide the parsed site config
	 	// The site config needs to be provided to the Site object

		// The default template engine and renderer names should be in VetinariContext
		// The actual objects can't be used because that could create a cycle between VetinariContext and the implementations

		ImmutableMap.Builder<String, Page> pageBuilder = ImmutableMap.builder();
		for(Path file : Files.walk(configuration.getContentRoot()).collect(Collectors.toList()))
		{
			Page page = pageParser.parsePage(file, configuration);
			pageBuilder.put(page.getIdentifier(), page);
		}
		siteBuilder.pages(pageBuilder.build());

		return siteBuilder.build();
	}
}
