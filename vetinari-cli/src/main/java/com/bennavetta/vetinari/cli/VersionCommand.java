package com.bennavetta.vetinari.cli;

import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

/**
 * Display version information.
 */
@Parameters(commandDescription = "Display version information")
public class VersionCommand
{
	private Manifest readManifest() throws IOException
	{
		try(InputStream in = getClass().getResourceAsStream("/META-INF/MANIFEST.MF"))
		{
			return new Manifest(in);
		}
	}

	public void run()
	{
		try
		{
			Manifest manifest = readManifest();

			String version = manifest.getMainAttributes().getValue("Implementation-Version");
			String buildDate = manifest.getMainAttributes().getValue("Build-Date");
			String commit = manifest.getMainAttributes().getValue("Change");
			String gradleVersion = manifest.getMainAttributes().getValue("Gradle-Version");
			String buildJavaVersion = manifest.getMainAttributes().getValue("Build-Java-Version");
			String javaVersion = System.getProperty("java.version");

			StringBuilder result = new StringBuilder();
			result
				.append(String.format("Vetinari %s\n", version))
				.append(String.format("    Built with Gradle %s and Java %s\n", gradleVersion, buildJavaVersion))
				.append(String.format("    Built on %s\n", buildDate))
				.append(String.format("    Commit %s\n", commit))
				.append(String.format("    Running Java %s\n", javaVersion));

			System.out.print(result.toString());
		}
		catch (IOException e)
		{
			System.err.println("Unable to load version information: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}