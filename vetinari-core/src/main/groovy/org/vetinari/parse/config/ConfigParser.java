package org.vetinari.parse.config;

import com.typesafe.config.Config;

/**
 * A parser for some arbitrary configuration format used in frontmatter.
 */
public interface ConfigParser
{
	public Config parse(String source);

	public String getStartDelimiter();

	public String getEndDelimiter();
}
