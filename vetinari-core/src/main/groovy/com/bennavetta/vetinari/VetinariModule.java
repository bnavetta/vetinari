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
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.bennavetta.vetinari.config.ConfigParser;
import com.bennavetta.vetinari.config.GroovyConfigParser;
import com.bennavetta.vetinari.config.HOCONParser;
import com.bennavetta.vetinari.config.JSONParser;
import com.bennavetta.vetinari.render.NoOpRenderer;
import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.NoOpTemplateEngine;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.bennavetta.vetinari.template.groovy.GroovyTemplateEngine;

import javax.inject.Named;
import java.util.function.BiFunction;

/**
 * Guice module for core Vetinari objects
 */
public class VetinariModule extends AbstractModule
{
	public static final TypeLiteral<BiFunction<Object[], Site, String>> FUNCTION_TYPE = new TypeLiteral<BiFunction<Object[], Site, String>>() {};
	public static final TypeLiteral<String> STRING_TYPE = new TypeLiteral<String>() {};

	@Override
	protected void configure()
	{
		bind(Site.class).toProvider(SiteLoader.class);
		bind(SiteBuilder.class);

		// Create bindings so they exist for Guice even if no implementations are added

		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		rendererBinder.addBinding().to(NoOpRenderer.class);

		Multibinder<TemplateEngine> templateEngineBinder = Multibinder.newSetBinder(binder(), TemplateEngine.class);
		templateEngineBinder.addBinding().to(NoOpTemplateEngine.class);
		templateEngineBinder.addBinding().to(GroovyTemplateEngine.class);

		MapBinder<String, BiFunction<Object[], Site, String>> functionBinder = MapBinder.newMapBinder(binder(), STRING_TYPE, FUNCTION_TYPE);

		Multibinder<ConfigParser> configParserBinder = Multibinder.newSetBinder(binder(), ConfigParser.class);
		configParserBinder.addBinding().to(GroovyConfigParser.class);
		configParserBinder.addBinding().to(HOCONParser.class);
		configParserBinder.addBinding().to(JSONParser.class);
	}

	/**
	 * Load the site config file. This is done separately from loading the site itself, so the config can be used in
	 * plugins without creating a circular dependency on the site.
	 */
	@Provides
	@Named("siteConfig")
	Config siteConfig(Configuration configuration)
	{
		// TODO: add ConfigParser.parseFile() and use that
		return ConfigFactory.parseFileAnySyntax(configuration.getSiteConfig().toFile());
	}
}
