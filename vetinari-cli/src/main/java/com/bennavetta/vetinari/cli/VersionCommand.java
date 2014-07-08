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

import com.beust.jcommander.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

/**
 * Display version information.
 */
@Parameters(commandDescription = "Display version information")
public class VersionCommand
{
	private Logger log; //NOSONAR

	private Manifest readManifest() throws IOException
	{
		for(URL manifestUrl : Collections.list(getClass().getClassLoader().getResources("META-INF/MANIFEST.MF")))
		{
			log.debug("Investigating manifest {}", manifestUrl);
			try(InputStream in = manifestUrl.openStream())
			{
				Manifest manifest = new Manifest(in);
				String implementationTitle = manifest.getMainAttributes().getValue(Name.IMPLEMENTATION_TITLE);
				if(implementationTitle != null && implementationTitle.startsWith("com.bennavetta.vetinari"))
				{
					log.debug("Found Vetinari manifest: {}", manifestUrl);
					return manifest;
				}
			}
		}
		throw new IllegalStateException("Cannot find Vetinari MANIFEST.MF");
	}

	public void run()
	{
		log = LoggerFactory.getLogger(VersionCommand.class);
		try
		{
			Manifest manifest = readManifest();
			log.debug("Found Vetinari manifest {}", manifest.getMainAttributes());

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

			log.info(result.toString());
		}
		catch (IOException e)
		{
			log.error("Unable to load version information", e);
		}
	}
}
