package com.bennavetta.vetinari;

import com.typesafe.config.Config;
import lombok.Getter;
import lombok.experimental.Builder;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Common configuration and logic for site building.
 */
@Builder
public class VetinariContext
{
	@Getter
	private final Charset contentEncoding;

	@Getter
	private final Path contentRoot;

	@Getter
	private final Path templateRoot;

	@Getter
	private final Path outputRoot;

	@Getter
	private final Config siteConfig;
}
