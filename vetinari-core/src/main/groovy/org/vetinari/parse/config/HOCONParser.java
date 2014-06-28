package org.vetinari.parse.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

/**
 * Parse Typesafe's HOCON format.
 */
public class HOCONParser implements ConfigParser
{
	private static final ConfigParseOptions PARSE_OPTIONS = ConfigParseOptions
			.defaults().setAllowMissing(false).setSyntax(ConfigSyntax.CONF);

	@Override
	public Config parse(String source)
	{
		return ConfigFactory.parseString(source, PARSE_OPTIONS);
	}

	@Override
	public String getStartDelimiter()
	{
		return "+++";
	}

	@Override
	public String getEndDelimiter()
	{
		return "+++";
	}
}
