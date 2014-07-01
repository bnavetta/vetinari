package org.vetinari;

/**
 * Some kind of engine that will be applied to content. This interface is mainly to help
 * with detecting which engines to use for a page.
 */
public interface Engine
{
	public String getName();

	public Iterable<String> getFileExtensions();
}
