package com.bennavetta.vetinari.template.groovy;

import com.bennavetta.vetinari.template.Template;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import groovy.text.SimpleTemplateEngine;

import java.io.IOException;
import java.util.Map;

/**
 * Render templates with Groovy's SimpleTemplateEngine.
 */
public class GroovyTemplateEngine implements TemplateEngine
{
	// TODO: I think this is the only part of vetinari-core using Groovy. Maybe put it in vetinari-integration?

	private groovy.text.TemplateEngine templateEngine;

	public GroovyTemplateEngine()
	{
		this.templateEngine = new SimpleTemplateEngine();
	}

	@Override
	public Template compile(String source)
	{
		try
		{
			return new GroovyTemplate(templateEngine.createTemplate(source));
		}
		catch (ClassNotFoundException | IOException e)
		{
			throw Throwables.propagate(e);
		}
	}

	@Override
	public String getName()
	{
		return "groovyTemplate";
	}

	@Override
	public Iterable<String> getFileExtensions()
	{
		return ImmutableList.of("gtpl");
	}

	private static class GroovyTemplate implements Template
	{
		private final groovy.text.Template template;

		public GroovyTemplate(groovy.text.Template template)
		{
			this.template = template;
		}

		@Override
		public String render(Map<String, Object> variables)
		{
			return template.make(variables).toString();
		}
	}
}
