package org.vetinari.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import groovy.util.ConfigSlurper;

/**
 * Parse the Groovy {@link ConfigSlurper} syntax.
 */
public class GroovyConfigParser implements ConfigParser
{
	private ConfigSlurper slurper;

	@Override
	public Config parse(String source)
	{
		return ConfigFactory.parseMap(slurper.parse(source).flatten());
	}

	@Override
	public String getStartDelimiter()
	{
		return "$$$";
	}

	@Override
	public String getEndDelimiter()
	{
		return "$$$";
	}
}
