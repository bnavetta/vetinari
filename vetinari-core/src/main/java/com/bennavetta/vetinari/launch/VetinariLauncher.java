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
package com.bennavetta.vetinari.launch;

import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.build.SiteBuilder;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 * Load Vetinari and launch a build.
 */
@Slf4j
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

		log.debug("Creating Guice injector with modules: {}", allModules);

		return Guice.createInjector(allModules);
	}

	private void build(Injector injector) throws VetinariException
	{
		SiteBuilder builder = injector.getInstance(SiteBuilder.class);
		log.debug("Building with {}", builder);
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
