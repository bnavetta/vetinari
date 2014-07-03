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

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import com.bennavetta.vetinari.Configuration
import com.bennavetta.vetinari.IntegrationModule
import com.bennavetta.vetinari.SiteBuilder
import com.bennavetta.vetinari.VetinariModule

/**
 * Build a site
 */
class BuildTask extends DefaultTask
{
	Configuration configuration

	Iterable modules = []

	@InputDirectory
	public File getContentRoot()
	{
		return getConfiguration().contentRoot.toFile()
	}

	@InputDirectory
	public File getTemplateRoot()
	{
		return getConfiguration().templateRoot.toFile()
	}

	@InputFile
	public File getSiteConfig()
	{
		return getConfiguration().siteConfig.toFile()
	}

	@OutputDirectory
	public File getOutputRoot()
	{
		return getConfiguration().outputRoot.toFile()
	}

	@TaskAction
	def build()
	{
		def realModules = getModules().collect { m ->
			if(m instanceof Module) return m
			if(m instanceof Class) return m.newInstance()
			return Class.forName(m.toString()).newInstance()
		}

		if(!realModules.any { m -> m instanceof VetinariModule })
		{
			realModules << new VetinariModule()
		}
		if(!realModules.any { m -> m instanceof IntegrationModule })
		{
			realModules << new IntegrationModule()
		}

		realModules << new ConfigModule(getConfiguration())

		Injector injector = Guice.createInjector(realModules)
		SiteBuilder builder = injector.getInstance(SiteBuilder)
		builder.build()
	}

	private static class ConfigModule extends AbstractModule
	{
		private final Configuration config

		public ConfigModule(Configuration config)
		{
			this.config = config;
		}

		@Override
		protected void configure()
		{
			bind(Configuration).toInstance(config)
		}
	}
}
