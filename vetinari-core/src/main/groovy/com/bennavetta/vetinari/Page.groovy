/*
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
package com.bennavetta.vetinari

import com.typesafe.config.Config
import com.bennavetta.vetinari.render.Renderer
import com.bennavetta.vetinari.template.TemplateEngine

import java.nio.file.Path

/**
 * A content page in the site.
 */
//@Immutable
public class Page
{
	/**
	 * The information given at the top of the page. Note that the {@link Config} object from the Typesafe Config library is used,
	 * the data can come from other sources, such as YAML, and that the {@link Config} interface was chosen because it has a
	 * nice API.
	 */
	Config metadata

	/**
	 * The path to this page, relative to the content root.
	 */
	Path path

	TemplateEngine templateEngine
	Renderer renderer

	String content

	public Page(Config metadata, Path path, TemplateEngine templateEngine, Renderer renderer, String content)
	{
		this.metadata = metadata
		this.path = path
		this.templateEngine = templateEngine
		this.renderer = renderer
		this.content = content
	}

	public String getTitle()
	{
		return metadata.getString("title");
	}

	public String getLayout()
	{
		return metadata.hasPath("layout") ? metadata.getString("layout") : null;
	}

	/**
	 * The identifier of a page is its path without any file extensions. For example, {@code <content root>/foo/bar.md.hb} would be {@code foo/bar}.
	 */
	public String getIdentifier()
	{
		String pathString = path.toString();
		return pathString.substring(0, pathString.indexOf('.'));
	}
}
