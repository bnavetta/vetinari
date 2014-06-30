package org.vetinari.render;

import org.vetinari.parse.Engine;

import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A {@code Renderer} converts input markup, like Markdown, and generates HTML content.
 */
public interface Renderer extends Engine
{
	public String render(String source, Writer out);
}
