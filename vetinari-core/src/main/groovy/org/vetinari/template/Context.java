package org.vetinari.template;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import org.vetinari.Configuration;
import org.vetinari.Site;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

/**
 * A context for template compilation.
 */
public class Context
{
	private final Configuration configuration;
	private final Set<TemplateEngine> engines;

	private final Site site;

	private Map<String, BiFunction<Object[], Context, String>> functions;

	private LoadingCache<String, Template> templateCache = CacheBuilder.newBuilder()
			.build(new TemplateLoader());

	public Context(Configuration configuration, Set<TemplateEngine> engines, Site site)
	{
		this.configuration = configuration;
		this.engines = engines;
		this.site = site;
	}

	public Site getSite()
	{
		return site;
	}

	public Map<String, BiFunction<Object[], Context, String>> getFunctions()
	{
		return functions;
	}

	public Template getTemplate(String name) throws MissingTemplateException
	{
		try
		{
			return templateCache.get(name);
		}
		catch (ExecutionException e)
		{
			Throwables.propagateIfInstanceOf(e.getCause(), MissingTemplateException.class);
			throw Throwables.propagate(e);
		}
	}

	private class TemplateLoader extends CacheLoader<String, Template>
	{
		@Override
		public Template load(String templateName) throws Exception
		{
			final Path basePath = configuration.getTemplateRoot().resolve(templateName);
			Path templatePath = Files.list(basePath.getParent())
			                         .filter(path -> path.getFileName().startsWith(basePath.getFileName()))
			                         .findFirst()
			                         .orElseThrow(() -> new MissingTemplateException(configuration.getTemplateRoot(), templateName));

			final String extension = com.google.common.io.Files.getFileExtension(templatePath.toString());
			TemplateEngine engine = engines.stream().filter(e -> Iterables.contains(e.getFileExtensions(), extension)).findAny().get();
			try(Reader in = Files.newBufferedReader(templatePath))
			{
				String source = CharStreams.toString(in);
				return engine.compile(source, Context.this);
			}
		}
	}
}
