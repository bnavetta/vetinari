package org.vetinari.template;

import java.util.Map;

/**
 * Abstraction for a template class.
 */
public interface Template
{
	public String render(Map<String, Object> variables);
}
