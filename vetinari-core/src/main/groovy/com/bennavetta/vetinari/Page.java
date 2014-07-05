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
		return pathString.substring(0, pathString.indexOf("."));
	}

	/**
	 * The information given at the top of the page. Note that the {@link com.typesafe.config.Config} object from the Typesafe Config library is used,
	 * the data can come from other sources, such as YAML, and that the {@link com.typesafe.config.Config} interface was chosen because it has a
	 * nice API.
	 */
	private Config metadata;

	/**
	 * The path to this page, relative to the content root.
	 */
	private Path path;

	private TemplateEngine templateEngine;
	private Renderer renderer;
	private String content;
}
