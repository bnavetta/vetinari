package com.bennavetta.vetinari.template;

import com.bennavetta.vetinari.template.groovy.GroovyTemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Module providing template functionality.
 */
public class TemplateModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<TemplateEngine> templateEngineBinder =
				Multibinder.newSetBinder(binder(), TemplateEngine.class);
		templateEngineBinder.addBinding().to(NoOpTemplateEngine.class);
		templateEngineBinder.addBinding().to(GroovyTemplateEngine.class);

		bind(TemplateLoader.class);
	}
}
