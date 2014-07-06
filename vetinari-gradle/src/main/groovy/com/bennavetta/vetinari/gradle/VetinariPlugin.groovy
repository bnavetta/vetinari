/*
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
package com.bennavetta.vetinari.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.jar.Attributes
import java.util.jar.Manifest

/**
 * Plugin for Vetinari builds from Gradle
 */
class VetinariPlugin implements Plugin<Project>
{
	static final String CONFIGURATION_NAME = "vetinari"
	static final String EXTENSION_NAME = "vetinari"

	private VetinariExtension extension

	@Override
	void apply(Project project)
	{
		extension = createVetinariExtension(project)
		createVetinariConfiguration(project)

		extension.sites.all { site ->
			VetinariBuild task = project.tasks.create("build${site.name.capitalize()}Site", VetinariBuild)
			task.site = site
		}
	}

	private void createVetinariConfiguration(Project project)
	{
		project.configurations.create(CONFIGURATION_NAME)
		project.afterEvaluate {
			project.logger.debug("Using Vetinari version $extension.vetinariVersion")
			project.dependencies.add(CONFIGURATION_NAME, "com.bennavetta.vetinari:vetinari-cli:${extension.vetinariVersion}")
		}
	}

	private VetinariExtension createVetinariExtension(Project project)
	{
		return project.extensions.create(EXTENSION_NAME, VetinariExtension, project.container(VetinariSite))
	}

	public static String getVersion()
	{
		return readManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION)
	}

	private static Manifest readManifest() throws IOException
	{
		Manifest ourManifest = null
		List<URL> manifests = Collections.list(VetinariPlugin.class.getClassLoader().getResources("META-INF/MANIFEST.MF"))
		for(URL manifestUrl : manifests)
		{
			manifestUrl.withInputStream { stream ->
				Manifest manifest = new Manifest(stream)
				if(manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE)?.startsWith("com.bennavetta.vetinari"))
				{
					ourManifest = manifest;
				}
			}
		}
		if(ourManifest == null)
		{
			throw new IllegalStateException("Cannot find Vetinari MANIFEST.MF")
		}
		else
		{
			return ourManifest
		}
	}
}
