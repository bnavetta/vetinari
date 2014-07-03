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

import com.google.common.base.Charsets
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for Vetinari builds from Gradle
 */
class VetinariPlugin implements Plugin<Project>
{
	@Override
	void apply(Project project)
	{
		VetinariExtension extension = project.extensions.create("vetinari", VetinariExtension, project)
		extension.contentEncoding = Charsets.UTF_8
		extension.conventionMapping.map("contentRoot") { 'src/vetinari/content' }
		extension.conventionMapping.map("templateRoot") { 'src/vetinari/templates' }
		extension.conventionMapping.map("siteConfig") { 'src/vetinari/site.conf' }
		extension.conventionMapping.map("outputRoot") { "$project.buildDir/vetinari" }

		BuildTask buildTask = project.tasks.create("vetinariBuild", BuildTask)

		project.tasks.withType(BuildTask) { task ->
			task.conventionMapping.map("configuration") { extension.toConfiguration() }
			task.conventionMapping.map("modules") { extension.modules }
		}
	}
}