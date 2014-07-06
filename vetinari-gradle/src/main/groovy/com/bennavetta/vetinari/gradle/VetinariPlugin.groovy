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

import java.util.jar.Manifest

/**
 * Plugin for Vetinari builds from Gradle
 */
class VetinariPlugin implements Plugin<Project>
{
	static final String CONFIGURATION_NAME = "vetinari"
	static final String EXTENSION_NAME = "vetinari"

	@Override
	void apply(Project project)
	{
		VetinariExtension extension = project.extensions.create(EXTENSION_NAME, VetinariExtension, project)


		project.afterEvaluate {
			project.dependencies.add(CONFIGURATION_NAME, "com.bennavetta.vetinari:vetinari-cli:${extension.vetinariVersion}")
		}
	}

	private void createVetinariConfiguration(Project project)
	{
		project.configurations.create(CONFIGURATION_NAME)
	}

	public static String getVersion()
	{
		InputStream manifestStream = null
		try
		{
			manifestStream = VetinariPlugin.class.getResourceAsStream('/META-INF/MANIFEST.MF')
			return new Manifest(manifestStream).getMainAttributes().get("Implementation-Version")
		}
		finally
		{
			manifestStream?.close()
		}
	}
}
