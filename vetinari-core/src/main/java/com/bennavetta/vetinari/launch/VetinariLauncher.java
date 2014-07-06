package com.bennavetta.vetinari.launch;

import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.build.SiteBuilder;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 * Load Vetinari and launch a build.
 */
public class VetinariLauncher
{
	private VetinariContextModule contextModule = new VetinariContextModule();

	public VetinariLauncher withContentEncoding(Charset contentEncoding)
	{
		contextModule.setContentEncoding(contentEncoding);
		return this;
	}

	public VetinariLauncher withContentRoot(Path contentRoot)
	{
		contextModule.setContentRoot(contentRoot);
		return this;
	}

	public VetinariLauncher withTemplateRoot(Path templateRoot)
	{
		contextModule.setTemplateRoot(templateRoot);
		return this;
	}

	public VetinariLauncher withOutputRoot(Path outputRoot)
	{
		contextModule.setOutputRoot(outputRoot);
		return this;
	}

	public VetinariLauncher withSiteConfig(Path siteConfig)
	{
		contextModule.setSiteConfig(siteConfig);
		return this;
	}

	private Injector createInjector() throws IOException
	{
		List<Module> classpathModules = new ModuleLoader().loadModules();
		List<Module> allModules = Lists.newArrayList(classpathModules);
		allModules.add(contextModule);

		return Guice.createInjector(allModules);
	}

	private void build(Injector injector) throws VetinariException
	{
		SiteBuilder builder = injector.getInstance(SiteBuilder.class);
		builder.build();
	}

	public void run() throws VetinariException
	{
		try
		{
			build(createInjector());
		}
		catch (IOException e)
		{
			throw new VetinariException("Error creating Guice injector", e);
		}
	}
}
