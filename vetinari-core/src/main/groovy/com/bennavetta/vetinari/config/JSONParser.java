/**
 * Copyright 2014 Ben Navetta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bennavetta.vetinari.config;

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
