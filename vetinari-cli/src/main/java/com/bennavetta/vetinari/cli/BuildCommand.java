package com.bennavetta.vetinari.cli;

import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.launch.VetinariLauncher;
import com.beust.jcommander.Parameter;

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
		launcher.withContentEncoding(Charset.forName(contentEncoding));
		launcher.withContentRoot(Paths.get(contentRoot));
		launcher.withTemplateRoot(Paths.get(templateRoot));
		launcher.withOutputRoot(Paths.get(outputRoot));
		launcher.withSiteConfig(Paths.get(siteConfig));

		try
		{
			launcher.run();
		}
		catch (VetinariException e)
		{
			// TODO: logging (here and elsewhere)
			e.printStackTrace();
		}
	}
}
