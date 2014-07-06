package com.bennavetta.vetinari;

/**
 * An exception produced while building a Vetinari site.
 */
public class VetinariException extends Exception
{
	public VetinariException(String message)
	{
		super(message);
	}

	public VetinariException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public VetinariException(Throwable cause)
	{
		super(cause);
	}
}
