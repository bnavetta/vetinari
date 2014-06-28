package org.vetinari

import com.typesafe.config.Config
import groovy.transform.Immutable
import groovy.transform.builder.Builder
import org.vetinari.render.Renderer
import org.vetinari.template.TemplateEngine

import java.nio.file.Path

/**
 * A content page in the site.
 */
@Builder
@Immutable
public class Page
{
	/**
	 * The information given at the top of the page. Note that the {@link Config} object from the Typesafe Config library is used,
	 * the data can come from other sources, such as YAML, and that the {@link Config} interface was chosen because it has a
	 * nice API.
	 */
	@Delegate // make frontmatter metadata available in templates
	Config metadata

	/**
	 * The path to this page, relative to the content root.
	 */
	Path path

	TemplateEngine templateEngine
	Renderer renderer

	/*
	 * Some required properties are given as methods for easier Java access
	 */

	public String getTitle()
	{
		return metadata.get("title").toString()
	}
}
