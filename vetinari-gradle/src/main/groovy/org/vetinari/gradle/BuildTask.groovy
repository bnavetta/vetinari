package org.vetinari.gradle

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import org.gradle.api.tasks.TaskAction
import org.vetinari.BaseModule
import org.vetinari.Configuration
import org.vetinari.SiteBuilder
import org.vetinari.parse.ParseModule

/**
 * Build a site
 */
class BuildTask
{
	Configuration configuration

	Iterable modules = []

	@TaskAction
	def build()
	{
		def realModules = modules.collect { m ->
			if(m instanceof Module) return m
			if(m instanceof Class) return m.newInstance()
			return Class.forName(m.toString()).newInstance()
		}

		if(!realModules.any { m -> m instanceof ParseModule})
		{
			realModules << new ParseModule()
		}
		if(!realModules.any { m -> m instanceof BaseModule })
		{
			realModules << new BaseModule()
		}

		realModules << new ConfigModule(config: configuration)

		Injector injector = Guice.createInjector(realModules)
		SiteBuilder builder = injector.getInstance(SiteBuilder)
		builder.build()
	}

	private static class ConfigModule extends AbstractModule
	{
		private final Configuration config

		@Override
		protected void configure()
		{
			bind(Configuration).toInstance(config)
		}
	}
}
