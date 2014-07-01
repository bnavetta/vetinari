package org.vetinari.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

/**
 * Parse JSON frontmatter.
 */
public class JSONParser implements ConfigParser
{
	private static final ConfigParseOptions PARSE_OPTIONS = ConfigParseOptions
			.defaults().setAllowMissing(false).setSyntax(ConfigSyntax.JSON);

	@Override
	public Config parse(String source)
	{
		// need to re-insert the braces since the parser drops them as delimiters
		return ConfigFactory.parseString("{" + source + "}", PARSE_OPTIONS);
	}

	@Override
	public String getStartDelimiter()
	{
		return "{";
	}

	@Override
	public String getEndDelimiter()
	{
		return "}";
	}
}
