package com.bennavetta.vetinari;

import com.bennavetta.vetinari.render.Renderer;
import com.bennavetta.vetinari.template.TemplateEngine;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
	 * Returns the page layout to use, or {@code null} if none was specified. This is set in the
	 * {@code layout} frontmatter property.
	 */
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
		return pathString.substring(0, pathString.indexOf("."));
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
	 * The template engine to run this page through.
	 */
	private TemplateEngine templateEngine;

	/**
	 * The renderer to render this page with.
	 */
	private Renderer renderer;

	/**
	 * The unprocessed content of this page, without frontmatter.
	 */
	private String content;
}
