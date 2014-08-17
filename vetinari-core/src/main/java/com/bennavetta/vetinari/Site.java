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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Builder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.net.URI;
import java.util.function.UnaryOperator;

/**
 * Models all the content pages of a static site.
 */
@Builder
@Wither
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Site
{
	private ImmutableMap<String, Page> pages;

	/**
	 * General site configuration.
	 */
	@Getter
	private Config siteConfig;

	/**
	 * Looks up a page at the given path. The path does not include file extensions.
	 * @see Page#getIdentifier()
	 */
	public Page getPage(String path)
	{
		return pages.get(path);
	}

	/**
	 * Returns all pages on the site.
	 */
	public ImmutableCollection<Page> getPages()
	{
		return pages.values();
	}

	/**
	 * Returns a mapping of path names to pages.
	 * @see #getPage(String)
	 * @see #getPages()
	 */
	public ImmutableMap<String, Page> getPageMap()
	{
		return pages;
	}

	/**
	 * Returns the site title, as specified in the site configuration file under the {@code title} property.
	 */
	public String getTitle()
	{
		return siteConfig.getString("title");
	}

	/**
	 * Returns the default page layout. This is set with the {@code defaultLayout} property in the site configuration
	 * file.
	 */
	public String getDefaultLayout()
	{
		return siteConfig.getString("defaultLayout");
	}

	/**
	 * Returns the name of the default renderer. This is set to a renderer name with the {@code defaultRenderer}
	 * property in the site configuration file.
	 */
	public String getDefaultRenderer()
	{
		return siteConfig.getString("defaultRenderer");
	}

	/**
	 * Returns the name of the default template engine. This is set with the {@code defaultTemplateEngine} property
	 * in the site configuration file.
	 */
	public String getDefaultTemplateEngine()
	{
		return siteConfig.getString("defaultTemplateEngine");
	}

	/**
	 * Returns the base URL that the site will be served under. This is set with the {@code baseUrl} property in
	 * the site configuration file.
	 */
	public URI getBaseUrl()
	{
		return URI.create(siteConfig.getString("baseUrl"));
	}

	/**
	 * Returns a new {@link Site} by applying the given transformation function to each page.
	 * @param operator a function to apply to each page
	 * @return a site with the updated pages
	 */
	public Site transformPages(UnaryOperator<Page> operator)
	{
		ImmutableMap.Builder<String, Page> newPagesBuilder = ImmutableMap.builder();
		pages.forEach((path, page) -> newPagesBuilder.put(path, operator.apply(page)));
		return withPages(newPagesBuilder.build());
	}
}
