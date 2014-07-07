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

import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Register integration classes if their dependencies are present.
 */
public class IntegrationModule extends AbstractModule
{
	private final Logger log = LoggerFactory.getLogger(IntegrationModule.class);

	@Override
	protected void configure()
	{
		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		tryBind(rendererBinder, "com.bennavetta.vetinari.renderers.MarkdownRenderer");

		Multibinder<TemplateEngine> templateEngineBinder = Multibinder.newSetBinder(binder(), TemplateEngine.class);
	}

	private <T> void tryBind(Multibinder<T> multibinder, String className)
	{
		log.debug("Trying to load {}", className);
		try
		{
			multibinder.addBinding().to((Class<? extends T>) Class.forName(className));
			log.debug("Successfully loaded {}", className);
		}
		catch (ClassNotFoundException e)
		{
			log.debug("Failed to load class", e);
		}
	}
}
