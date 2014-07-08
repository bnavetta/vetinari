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
