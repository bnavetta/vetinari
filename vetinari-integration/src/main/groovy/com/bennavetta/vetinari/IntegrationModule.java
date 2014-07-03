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

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.bennavetta.vetinari.config.ConfigParser;
import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.TemplateEngine;

import java.util.function.BiFunction;

/**
 * Register integration classes if their dependencies are present.
 */
public class IntegrationModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		tryBind(rendererBinder, "com.bennavetta.vetinari.renderers.MarkdownRenderer");

		Multibinder<TemplateEngine> templateEngineBinder = Multibinder.newSetBinder(binder(), TemplateEngine.class);

		MapBinder<String, BiFunction<Object[], Site, String>> functionBinder = MapBinder.newMapBinder(binder(), VetinariModule.STRING_TYPE, VetinariModule.FUNCTION_TYPE);

		Multibinder<ConfigParser> configParserBinder = Multibinder.newSetBinder(binder(), ConfigParser.class);
	}

	private <T> void tryBind(Multibinder<T> multibinder, String className)
	{
		try
		{
			multibinder.addBinding().to((Class<? extends T>) Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			// Assume this means some required class was missing
		}
	}
}
