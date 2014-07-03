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
package com.bennavetta.vetinari;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Core settings for a Vetinari build. Note that this defines settings needed to load and build the site, whereas the
 * site settings provide information for pages, like the site title and author.
 */
public class Configuration
{
	private Charset contentEncoding;

	private Path contentRoot;

	private Path templateRoot;

	private Path outputRoot;

	private Path siteConfig;

	/**
	 * Returns the path storing content files.
	 */
	public Path getContentRoot()
	{
		return contentRoot;
	}

	public void setContentRoot(Path contentRoot)
	{
		this.contentRoot = contentRoot;
	}

	/**
	 * Returns the path storing templates. This includes both
	 * layouts and partials.
	 */
	public Path getTemplateRoot()
	{
		return templateRoot;
	}

	public void setTemplateRoot(Path layoutRoot)
	{
		this.templateRoot = layoutRoot;
	}

	/**
	 * Returns the path that generated files will be stored in.
	 */
	public Path getOutputRoot()
	{
		return outputRoot;
	}

	public void setOutputRoot(Path outputRoot)
	{
		this.outputRoot = outputRoot;
	}

	/**
	 * Returns the character encoding used for all content files.
	 */
	public Charset getContentEncoding()
	{
		return contentEncoding;
	}

	public void setContentEncoding(Charset contentEncoding)
	{
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Returns the path to the site configuration file.
	 */
	public Path getSiteConfig()
	{
		return siteConfig;
	}

	public void setSiteConfig(Path siteConfig)
	{
		this.siteConfig = siteConfig;
	}
}
