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

import com.bennavetta.vetinari.parse.PageParser;
import com.bennavetta.vetinari.parse.SiteLoader;
import com.bennavetta.vetinari.render.NoOpRenderer;
import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.NoOpTemplateEngine;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.bennavetta.vetinari.template.groovy.GroovyTemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice module for core Vetinari objects.
 */
public class VetinariModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(PageParser.class);
		bind(SiteLoader.class);

		bind(SiteBuilder.class);

		// Create bindings so they exist for Guice even if no implementations are added

		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		rendererBinder.addBinding().to(NoOpRenderer.class);

		Multibinder<TemplateEngine> templateEngineBinder =
				Multibinder.newSetBinder(binder(), TemplateEngine.class);
		templateEngineBinder.addBinding().to(NoOpTemplateEngine.class);
		templateEngineBinder.addBinding().to(GroovyTemplateEngine.class);
	}
}
