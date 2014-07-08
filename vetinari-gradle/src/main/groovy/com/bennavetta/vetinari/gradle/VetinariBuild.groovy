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

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Task to build a Vetinari site
 */
public class VetinariBuild extends DefaultTask
{
	VetinariSite site

	@Input
	public String getContentEncoding()
	{
		return site.contentEncoding
	}

	@InputDirectory
	public File getContentRoot()
	{
		return project.file(site.contentRoot)
	}

	@InputDirectory
	public File getTemplateRoot()
	{
		return project.file(site.templateRoot)
	}

	@InputFile
	public File getSiteConfig()
	{
		return project.file(site.siteConfig)
	}

	@OutputDirectory
	public File getOutputRoot()
	{
		return project.file(site.outputRoot)
	}

	@InputFiles
	public FileCollection getBuildClasspath()
	{
		return project.configurations.getByName(VetinariPlugin.CONFIGURATION_NAME)
	}

	@TaskAction
	public void build()
	{
		project.javaexec {
			classpath getBuildClasspath()
			main = 'com.bennavetta.vetinari.cli.VetinariMain'
			args('build',
					'--content-encoding', getContentEncoding(),
					'--content-root', getContentRoot(),
					'--template-root', getTemplateRoot(),
					'--site-config', getSiteConfig(),
					'--output-root', getOutputRoot())
		}
	}
}
