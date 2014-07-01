package org.vetinari

import com.typesafe.config.Config
import groovy.transform.Immutable
import groovy.transform.TupleConstructor
import org.vetinari.render.Renderer
import org.vetinari.template.TemplateEngine

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
