package org.vetinari;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Guice module for core Vetinari objects
 */
public class BaseModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(SiteBuilder.class).in(Scopes.SINGLETON);
	}
}
