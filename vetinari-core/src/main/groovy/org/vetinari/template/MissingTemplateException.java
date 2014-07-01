package org.vetinari.template;

import java.nio.file.Path;

/**
 * Indicates that a template could not be found
 */
public class MissingTemplateException extends RuntimeException
{
	public MissingTemplateException(Path templateDir, String templatePath)
	{
		super("Template " + templatePath + " not found in " + templateDir);
	}
}
