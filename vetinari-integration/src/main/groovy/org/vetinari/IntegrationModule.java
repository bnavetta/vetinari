package org.vetinari;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import org.vetinari.config.ConfigParser;
import org.vetinari.render.NoOpRenderer;
import org.vetinari.render.Renderer;
import org.vetinari.template.NoOpTemplateEngine;
import org.vetinari.template.TemplateEngine;

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
		tryBind(rendererBinder, "org.vetinari.renderers.MarkdownRenderer");

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
