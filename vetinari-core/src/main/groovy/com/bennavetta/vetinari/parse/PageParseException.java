package com.bennavetta.vetinari.parse;

/**
 * A {@code PageParseException} is thrown when something goes wrong parsing a page.
 */
public class PageParseException extends Exception
{
	public PageParseException(String message)
	{
		super(message);
	}

	public PageParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PageParseException(Throwable cause)
	{
		super(cause);
	}
}
