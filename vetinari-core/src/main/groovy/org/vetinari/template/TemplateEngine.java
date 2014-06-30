package org.vetinari.template;

import org.vetinari.parse.Engine;

/**
 * A {@code TemplateEngine} applies logic to page content.
 */
public interface TemplateEngine extends Engine
{
	public Template compile(String source, Context context);
}
