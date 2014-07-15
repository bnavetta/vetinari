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
package com.bennavetta.vetinari.template;

import com.bennavetta.vetinari.VetinariContext;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.google.common.io.Files.getFileExtension;

/**
 * Loads various kinds of templates.
 */
@Singleton
@Slf4j
public class TemplateLoader
{
	private final VetinariContext context;
	private final Set<TemplateEngine> templateEngines;
	private final TemplateEngine defaultTemplateEngine;

	private final LoadingCache<String, Template> templateCache;

	@Inject
	public TemplateLoader(VetinariContext context, Set<TemplateEngine> templateEngines)
	{
		this.context = context;
		this.templateEngines = templateEngines;
		this.defaultTemplateEngine = Iterables.find(this.templateEngines, t -> t.getName().equals(this.context.getSiteConfig().getString("defaultTemplateEngine")));

		this.templateCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Template>()
		{
			@Override
			public Template load(String templateName) throws Exception
			{
				final Path templatePath = context.getTemplateRoot().resolve(templateName);
				final String extension = getFileExtension(templateName);
				final TemplateEngine engine = Iterables.tryFind(templateEngines, t -> Iterables.contains(t.getFileExtensions(), extension)).or(defaultTemplateEngine);
				log.debug("Compiling template from {} with {}", templatePath, engine);
				String source = new String(Files.readAllBytes(templatePath), context.getContentEncoding());
				log.trace("Read template source \"{}\"", source);
				return engine.compile(source);
			}
		});
	}

	public Template get(String path)
	{
		try
		{
			log.debug("Retrieving template {}", path);
			return templateCache.get(path);
		}
		catch (ExecutionException e)
		{
			throw Throwables.propagate(e);
		}
	}
}
