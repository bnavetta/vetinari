package org.vetinari;

import org.vetinari.render.Renderer;
import org.vetinari.template.TemplateEngine;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Core settings for a Vetinari build. Note that this defines settings needed to load and build the site, whereas the
 * site settings provide information for pages, like the site title and author.
 */
public class Configuration
{
	private Charset contentEncoding;

	private Path contentRoot;

	private Path templateRoot;

	private Path outputRoot;

	private Path siteConfig;

	private Renderer defaultRenderer;
	private TemplateEngine defaultTemplateEngine;

	/**
	 * Returns the path storing content files.
	 */
	public Path getContentRoot()
	{
		return contentRoot;
	}

	public void setContentRoot(Path contentRoot)
	{
		this.contentRoot = contentRoot;
	}

	/**
	 * Returns the path storing templates. This includes both
	 * layouts and partials.
	 */
	public Path getTemplateRoot()
	{
		return templateRoot;
	}

	public void setTemplateRoot(Path layoutRoot)
	{
		this.templateRoot = layoutRoot;
	}

	/**
	 * Returns the path that generated files will be stored in.
	 */
	public Path getOutputRoot()
	{
		return outputRoot;
	}

	public void setOutputRoot(Path outputRoot)
	{
		this.outputRoot = outputRoot;
	}

	/**
	 * Returns the character encoding used for all content files.
	 */
	public Charset getContentEncoding()
	{
		return contentEncoding;
	}

	public void setContentEncoding(Charset contentEncoding)
	{
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Returns the renderer that will be used when no renderer is specified for a page.
	 */
	public Renderer getDefaultRenderer()
	{
		return defaultRenderer;
	}

	public void setDefaultRenderer(Renderer defaultRenderer)
	{
		this.defaultRenderer = defaultRenderer;
	}

	/**
	 * Returns the template engine that will be used when none is specified for a page.
	 */
	public TemplateEngine getDefaultTemplateEngine()
	{
		return defaultTemplateEngine;
	}

	public void setDefaultTemplateEngine(TemplateEngine defaultTemplateEngine)
	{
		this.defaultTemplateEngine = defaultTemplateEngine;
	}

	/**
	 * Returns the path to the site configuration file.
	 */
	public Path getSiteConfig()
	{
		return siteConfig;
	}

	public void setSiteConfig(Path siteConfig)
	{
		this.siteConfig = siteConfig;
	}
}
