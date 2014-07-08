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
package com.bennavetta.vetinari.template.groovy;

import com.bennavetta.vetinari.template.Template;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
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
			// Make a copy since SimpleTemplateEngine's internals modify the binding, which can be an ImmutableMap
			return template.make(Maps.newHashMap(variables)).toString();
		}
	}
}
