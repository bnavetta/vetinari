package org.vetinari;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import org.vetinari.render.Renderer;
import org.vetinari.template.Context;
import org.vetinari.template.TemplateEngine;

import java.util.function.BiFunction;

/**
 * Guice module for core Vetinari objects
 */
public class BaseModule extends AbstractModule
{
	private static TypeLiteral<BiFunction<Object[], Context, String>> functionType = new TypeLiteral<BiFunction<Object[], Context, String>>() {};
	private static TypeLiteral<String> stringType = new TypeLiteral<String>() {};

	@Override
	protected void configure()
	{
		bind(SiteBuilder.class).in(Scopes.SINGLETON);

		// Create bindings so they exist for Guice even if no implementations are added

		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);

		Multibinder<TemplateEngine> templateEngineBinder = Multibinder.newSetBinder(binder(), TemplateEngine.class);

		MapBinder<String, BiFunction<Object[], Context, String>> functionBinder = MapBinder.newMapBinder(binder(), stringType, functionType);
	}
}
