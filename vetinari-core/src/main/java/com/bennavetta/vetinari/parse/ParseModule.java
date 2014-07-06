package com.bennavetta.vetinari.parse;

import com.google.inject.AbstractModule;

/**
 * Guice module for parsing and loading.
 */
public class ParseModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(PageParser.class);
		bind(SiteLoader.class);
	}
}
