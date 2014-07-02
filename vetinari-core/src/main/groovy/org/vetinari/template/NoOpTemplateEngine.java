package org.vetinari.template;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import org.vetinari.Site;

import java.util.Map;

/**
 * Does nothing.
 */
public class NoOpTemplateEngine implements TemplateEngine
{
	@Override
	public Template compile(String source, Site site)
	{
		return new NoOpTemplate(source);
	}

	@Override
	public String getName()
	{
		return "no-op";
	}

	@Override
	public Iterable<String> getFileExtensions()
	{
		return ImmutableSet.of();
	}

	@AllArgsConstructor
	private static final class NoOpTemplate implements Template
	{
		private final String source;

		@Override
		public String render(Map<String, Object> variables)
		{
			return source;
		}
	}
}
