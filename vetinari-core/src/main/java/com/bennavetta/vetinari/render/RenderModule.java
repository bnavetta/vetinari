package com.bennavetta.vetinari.render;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice module for content rendering.
 */
public class RenderModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<Renderer> rendererBinder = Multibinder.newSetBinder(binder(), Renderer.class);
		rendererBinder.addBinding().to(NoOpRenderer.class);
	}
}
