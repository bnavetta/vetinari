package org.vetinari.template;

import org.vetinari.Engine;
import org.vetinari.Site;

/**
 * A {@code TemplateEngine} applies logic to page content.
 */
public interface TemplateEngine extends Engine
{
	public Template compile(String source, Site site);
}
