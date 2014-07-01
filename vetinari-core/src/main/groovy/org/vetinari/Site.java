package org.vetinari;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.vetinari.parse.Parser;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Models all the content pages of a static site.
 */
public class Site
{
	private final Configuration configuration;

	private final Parser pageParser;

	private ImmutableMap<String, Page> pages;
	private Config siteConfig;

	public Site(Configuration configuration, Parser pageParser)
	{
		this.configuration = configuration;
		this.pageParser = pageParser;
	}

	/**
	 * Load the content of the site into memory.
	 */
	public void load() throws IOException
	{
		ImmutableMap.Builder<String, Page> pagesBuilder = ImmutableMap.builder();

		// I'd use a lambda with Files.walk(), but pageParser.parse() throws an exception
		Files.walkFileTree(configuration.getContentRoot(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException
			{
				Page page = pageParser.parse(file);
				pagesBuilder.put(page.getIdentifier(), page);
				return FileVisitResult.CONTINUE;
			}
		});

		pages = pagesBuilder.build();

		// TODO: add ConfigParser.parseFile() and use that
		siteConfig = ConfigFactory.parseFileAnySyntax(configuration.getSiteConfig().toFile());
	}

	public Page getPage(String path)
	{
		return pages.get(path);
	}

	public ImmutableCollection<Page> getPages()
	{
		return pages.values();
	}

	public Config getSiteConfig()
	{
		return siteConfig;
	}

	public String getTitle()
	{
		return siteConfig.hasPath("title") ? siteConfig.getString("title") : null;
	}

	public String getDefaultLayout()
	{
		return siteConfig.hasPath("layout") ? siteConfig.getString("layout") : null;
	}
}
