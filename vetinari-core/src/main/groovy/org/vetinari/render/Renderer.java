package org.vetinari.render;

import org.vetinari.Engine;

/**
 * A {@code Renderer} converts input markup, like Markdown, and generates HTML content.
 */
public interface Renderer extends Engine
{
	public String render(String source);
}
