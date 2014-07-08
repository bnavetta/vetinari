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
package com.bennavetta.vetinari.cli;

import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.launch.VetinariLauncher;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.file.Paths;

/**
 * Command to run a Vetinari build;
 */
public class BuildCommand
{
	private VetinariLauncher launcher = new VetinariLauncher();

	@Parameter(names = {"--content-encoding"}, description = "Content encoding for source files")
	public String contentEncoding = "UTF-8";

	@Parameter(names = {"--content-root"}, description = "Directory containing page content", required=true)
	public String contentRoot;

	@Parameter(names = {"--template-root"}, description = "Directory containing template files", required=true)
	public String templateRoot;

	@Parameter(names = {"--output-root"}, description = "Directory to place generated files", required=true)
	public String outputRoot;

	@Parameter(names = {"--site-config"}, description = "Site configuration file", required=true)
	public String siteConfig;

	public void run()
	{
		final Logger log = LoggerFactory.getLogger(BuildCommand.class);

		launcher.withContentEncoding(Charset.forName(contentEncoding));
		launcher.withContentRoot(Paths.get(contentRoot));
		launcher.withTemplateRoot(Paths.get(templateRoot));
		launcher.withOutputRoot(Paths.get(outputRoot));
		launcher.withSiteConfig(Paths.get(siteConfig));

		log.debug("Content encoding: {}", contentEncoding);
		log.debug("Content root: {}", contentRoot);
		log.debug("Template root: {}", templateRoot);
		log.debug("Output root: {}", outputRoot);
		log.debug("Site config: {}", siteConfig);

		try
		{
			log.info("Starting build");
			launcher.run();
			log.info("Build complete");
		}
		catch (VetinariException e)
		{
			log.error("Error building site", e);
		}
	}
}
