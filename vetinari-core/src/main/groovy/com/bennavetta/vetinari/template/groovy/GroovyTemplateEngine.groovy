/*
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
package com.bennavetta.vetinari.template.groovy

import com.google.common.collect.ImmutableList
import groovy.text.SimpleTemplateEngine
import groovy.text.TemplateEngine as GTemplateEngine
import groovy.text.Template as GTemplate
import com.bennavetta.vetinari.Site
import com.bennavetta.vetinari.template.Template
import com.bennavetta.vetinari.template.TemplateEngine

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
