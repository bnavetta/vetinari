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

	@Parameter(names = {"--content-root"}, description = "Directory containing page content")
	public String contentRoot;

	@Parameter(names = {"--template-root"}, description = "Directory containing template files")
	public String templateRoot;

	@Parameter(names = {"--output-root"}, description = "Directory to place generated files")
	public String outputRoot;

	@Parameter(names = {"--site-config"}, description = "Site configuration file")
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
