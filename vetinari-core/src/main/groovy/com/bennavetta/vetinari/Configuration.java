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
	 * @see #setContentRoot(java.nio.file.Path)
	 */
	public Path getContentRoot()
	{
		return contentRoot;
	}

	/**
	 * Sets the content root.
	 * @param contentRoot the new content root
	 * @see #getContentRoot()
	 */
	public void setContentRoot(Path contentRoot)
	{
		this.contentRoot = contentRoot;
	}

	/**
	 * Returns the path storing templates. This includes both layouts and partials.
	 * @see #setTemplateRoot(java.nio.file.Path)
	 */
	public Path getTemplateRoot()
	{
		return templateRoot;
	}

	/**
	 * Sets the template root.
	 * @param layoutRoot the new template root
	 * @see #getTemplateRoot()
	 */
	public void setTemplateRoot(Path layoutRoot)
	{
		this.templateRoot = layoutRoot;
	}

	/**
	 * Returns the path that generated files will be stored in.
	 * @see #setOutputRoot(java.nio.file.Path)
	 */
	public Path getOutputRoot()
	{
		return outputRoot;
	}

	/**
	 * Set the output root.
	 * @param outputRoot the new output root
	 * @see #getOutputRoot()
	 */
	public void setOutputRoot(Path outputRoot)
	{
		this.outputRoot = outputRoot;
	}

	/**
	 * Returns the character encoding used for all content files.
	 * @see #setContentEncoding(java.nio.charset.Charset)
	 */
	public Charset getContentEncoding()
	{
		return contentEncoding;
	}

	/**
	 * Set the content encoding.
	 * @param contentEncoding the new content encoding
	 * @see #getContentEncoding()
	 */
	public void setContentEncoding(Charset contentEncoding)
	{
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Returns the path to the site configuration file.
	 * @see #setSiteConfig(java.nio.file.Path)
	 */
	public Path getSiteConfig()
	{
		return siteConfig;
	}

	/**
	 * Set the site configuration path.
	 * @param siteConfig the path to the new site configuration file
	 * @see #getSiteConfig()
	 */
	public void setSiteConfig(Path siteConfig)
	{
		this.siteConfig = siteConfig;
	}
}
