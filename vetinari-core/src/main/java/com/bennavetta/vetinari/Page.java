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

import com.typesafe.config.Config;
import lombok.Value;
import lombok.experimental.Wither;

import java.nio.file.Path;

/**
 * A content page in the site.
 */
@Wither
@Value
public class Page
{
	/**
	 * Returns the page title, which is given in the frontmatter under the {@code title} property.
	 */
	public String getTitle()
	{
		return metadata.getString("title");
	}

	/**
	 * The identifier of a page is its path without any file extensions. For example, {@code <content root>/foo/bar.md.hb} would be {@code foo/bar}.
	 */
	public String getIdentifier()
	{
		String pathString = path.toString();
		int lastDot = pathString.indexOf('.');
		return lastDot == -1 ? pathString : pathString.substring(0, pathString.indexOf("."));
	}

	/**
	 * The information given at the top of the page in the frontmatter section.
	 */
	private Config metadata;

	/**
	 * The path to this page, relative to the content root.
	 */
	private Path path;

	/**
	 * The unprocessed content of this page, without frontmatter.
	 */
	private String content;

	@Override
	public String toString()
	{
		return getIdentifier();
	}
}
