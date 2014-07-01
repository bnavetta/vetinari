package org.vetinari.gradle

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
