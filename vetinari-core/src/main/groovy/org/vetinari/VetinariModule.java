package org.vetinari;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import org.vetinari.config.ConfigParser;
import org.vetinari.config.GroovyConfigParser;
import org.vetinari.config.HOCONParser;
import org.vetinari.config.JSONParser;
import org.vetinari.render.NoOpRenderer;
import org.vetinari.render.Renderer;
import org.vetinari.template.NoOpTemplateEngine;
import org.vetinari.template.TemplateEngine;

import java.util.function.BiFunction;

/**
 * Guice module for core Vetinari objects
 */
public class VetinariModule extends AbstractModule
{
	private static TypeLiteral<BiFunction<Object[], Site, String>> functionType = new TypeLiteral<BiFunction<Object[], Site, String>>() {};
	private static TypeLiteral<String> stringType = new TypeLiteral<String>() {};

	@Override
	protected void configure()
	{
		bind(SiteLoader.class);
		bind(SiteBuilder.class);

		// Create bindings so they exist for Guice even if no implementations are added

		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		rendererBinder.addBinding().to(NoOpRenderer.class);

		Multibinder<TemplateEngine> templateEngineBinder = Multibinder.newSetBinder(binder(), TemplateEngine.class);
		templateEngineBinder.addBinding().to(NoOpTemplateEngine.class);

		MapBinder<String, BiFunction<Object[], Site, String>> functionBinder = MapBinder.newMapBinder(binder(), stringType, functionType);

		Multibinder<ConfigParser> configParserBinder = Multibinder.newSetBinder(binder(), ConfigParser.class);
		configParserBinder.addBinding().to(GroovyConfigParser.class);
		configParserBinder.addBinding().to(HOCONParser.class);
		configParserBinder.addBinding().to(JSONParser.class);
	}
}
