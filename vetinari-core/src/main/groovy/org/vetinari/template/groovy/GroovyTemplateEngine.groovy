package org.vetinari.template.groovy

import com.google.common.collect.ImmutableList
import groovy.text.SimpleTemplateEngine
import groovy.text.TemplateEngine as GTemplateEngine
import groovy.text.Template as GTemplate
import org.vetinari.Site
import org.vetinari.template.Template
import org.vetinari.template.TemplateEngine

/**
 * Render templates with Groovy's SimpleTemplateEngine
 */
class GroovyTemplateEngine implements TemplateEngine
{
	private GTemplateEngine templateEngine

	public GroovyTemplateEngine()
	{
		this.templateEngine = new SimpleTemplateEngine()
	}

	@Override
	Template compile(String source, Site site)
	{
		return new GroovyTemplate(site: site, template: templateEngine.createTemplate(source))
	}

	@Override
	String getName()
	{
		return "groovyTemplate"
	}

	@Override
	Iterable<String> getFileExtensions()
	{
		return ImmutableList.of("gtpl")
	}

	private static class GroovyTemplate implements Template
	{
		GTemplate template
		Site site

		@Override
		String render(Map<String, Object> variables)
		{
			return template.make(site.functions + variables)
		}
	}
}
