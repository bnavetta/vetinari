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
package com.bennavetta.vetinari;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Builder;
import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.MissingTemplateException;
import com.bennavetta.vetinari.template.Template;
import com.bennavetta.vetinari.template.TemplateEngine;

import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

/**
 * Models all the content pages of a static site.
 */
@Builder
public class Site
{
	private ImmutableMap<String, Page> pages;

	@Getter
	private Configuration configuration;

	@Getter
	private Config siteConfig;

	@Getter
	private Map<String, BiFunction<Object[], Site, String>> functions;

	@Getter
	private Set<Renderer> renderers;

	@Getter
	private Renderer defaultRenderer;

	@Getter
	private Set<TemplateEngine> templateEngines;

	@Getter
	private TemplateEngine defaultTemplateEngine;

	private LoadingCache<String, Template> templateCache;

	public Page getPage(String path)
	{
		return pages.get(path);
	}

	public ImmutableCollection<Page> getPages()
	{
		return pages.values();
	}

	public String getTitle()
	{
		return siteConfig.hasPath("title") ? siteConfig.getString("title") : null;
	}

	public String getDefaultLayout()
	{
		return siteConfig.getString("defaultLayout");
	}

	public URI getBaseUrl()
	{
		return URI.create(siteConfig.getString("baseUrl"));
	}

	public Template getTemplate(String name) throws MissingTemplateException
	{
		// Seems to be some issue with Lombok's builder
		if(templateCache == null)
		{
			templateCache = CacheBuilder.newBuilder().build(new TemplateLoader(this));
		}

		try
		{
			return templateCache.get(name);
		}
		catch (ExecutionException e)
		{
			Throwables.propagateIfInstanceOf(Throwables.getRootCause(e), MissingTemplateException.class);
			throw Throwables.propagate(e);
		}
	}

	@AllArgsConstructor
	private static class TemplateLoader extends CacheLoader<String, Template>
	{
		private Site site;

		@Override
		public Template load(String templateName) throws Exception
		{
			final Path basePath = site.configuration.getTemplateRoot().resolve(templateName);
			final Path templatePath = Files.list(basePath.getParent())
			                               .filter(p -> com.google.common.io.Files.getNameWithoutExtension(p.toString()).equals(basePath.getFileName().toString()))
			                               .findFirst()
			                               .orElseThrow(() -> new MissingTemplateException(site.configuration.getTemplateRoot(), templateName));
			final String fileExtension = com.google.common.io.Files.getFileExtension(templatePath.toString());
			final TemplateEngine engine = Iterables.tryFind(site.templateEngines, t -> Iterables.contains(t.getFileExtensions(), fileExtension)).or(site.defaultTemplateEngine);
			try(Reader in = Files.newBufferedReader(templatePath))
			{
				String source = CharStreams.toString(in);
				return engine.compile(source, site);
			}
		}
	}
}
