package org.vetinari.render;

import com.google.common.collect.ImmutableSet;

/**
 * Does nothing.
 */
public class NoOpRenderer implements Renderer
{
	@Override
	public String render(String source)
	{
		return source;
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
}
