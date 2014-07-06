/**
 * Copyright 2014 Ben Navetta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
