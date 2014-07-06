package com.bennavetta.vetinari.launch;

import com.bennavetta.vetinari.VetinariContext;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Guice module that creates a {@link VetinariContext}. This allows for runtime
 * configuration, e.g. from the command line.
 */
@Getter
@Setter
public class VetinariContextModule extends AbstractModule
{
	private Charset contentEncoding;

	/**
	 * Directory containing pages.
	 */
	private Path contentRoot;

	/**
	 * Directory containing templates.
	 */
	private Path templateRoot;

	/**
	 * Directory to place generated files.
	 */
	private Path outputRoot;

	/**
	 * General site configuration.
	 */
	private Path siteConfig;

	@Override
	protected void configure()
	{
		VetinariContext.VetinariContextBuilder contextBuilder = VetinariContext.builder();
		contextBuilder
				.contentEncoding(contentEncoding)
				.contentRoot(contentRoot)
				.templateRoot(templateRoot)
				.outputRoot(outputRoot)
				.siteConfig(loadSiteConfig());

		bind(VetinariContext.class).toInstance(contextBuilder.build());
	}

	private Config loadSiteConfig()
	{
		Config defaults = ConfigFactory.parseResources("vetinari-defaults.conf");
		// TODO: read ourselves to allow non-filesystem paths
		Config userDefined = ConfigFactory.parseFile(siteConfig.toFile());

		return userDefined.withFallback(defaults);
	}
}
