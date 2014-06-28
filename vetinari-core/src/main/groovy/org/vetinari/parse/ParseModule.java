package org.vetinari.parse;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import org.vetinari.parse.config.ConfigParser;
import org.vetinari.parse.config.GroovyConfigParser;
import org.vetinari.parse.config.HOCONParser;
import org.vetinari.parse.config.JSONParser;

/**
 * Define bindings for parser classes
 */
public class ParseModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Parser.class).in(Scopes.SINGLETON);

		Multibinder<ConfigParser> frontmatterBinder = Multibinder.newSetBinder(binder(), ConfigParser.class);
		frontmatterBinder.addBinding().to(HOCONParser.class);
		frontmatterBinder.addBinding().to(JSONParser.class);
		frontmatterBinder.addBinding().to(GroovyConfigParser.class);
	}
}
