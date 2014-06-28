package org.vetinari;

import org.vetinari.render.Renderer;
import org.vetinari.template.TemplateEngine;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Core settings for a Vetinari build.
 */
public class Configuration
{
	private Charset contentEncoding;

	private Path contentRoot;

	private Path templateRoot;

	private Path outputRoot;

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
	 * @return
	 */
	public Path getOutputRoot()
	{
		return outputRoot;
	}

	public void setOutputRoot(Path outputRoot)
	{
		this.outputRoot = outputRoot;
	}

	public Charset getContentEncoding()
	{
		return contentEncoding;
	}

	public void setContentEncoding(Charset contentEncoding)
	{
		this.contentEncoding = contentEncoding;
	}

	public Renderer getDefaultRenderer()
	{
		return defaultRenderer;
	}

	public void setDefaultRenderer(Renderer defaultRenderer)
	{
		this.defaultRenderer = defaultRenderer;
	}

	public TemplateEngine getDefaultTemplateEngine()
	{
		return defaultTemplateEngine;
	}

	public void setDefaultTemplateEngine(TemplateEngine defaultTemplateEngine)
	{
		this.defaultTemplateEngine = defaultTemplateEngine;
	}
}
