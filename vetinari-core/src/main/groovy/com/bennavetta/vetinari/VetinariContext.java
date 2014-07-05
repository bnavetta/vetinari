package com.bennavetta.vetinari;

import com.typesafe.config.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Builder;
import lombok.experimental.FieldDefaults;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Common configuration and logic for site building.
 */
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter // Use @Getter(level = AccessLevel.NONE) on a field to disable getter generation
public class VetinariContext
{
	/**
	 * Encoding used for all source files.
	 */
	private final Charset contentEncoding;

	/**
	 * Directory containing pages.
	 */
	private final Path contentRoot;

	/**
	 * Directory containing templates.
	 */
	private final Path templateRoot;

	/**
	 * Directory to place generated files.
	 */
	private final Path outputRoot;

	/**
	 * General site configuration.
	 */
	private final Config siteConfig;
}
