/*
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
package com.bennavetta.vetinari.test

import com.bennavetta.vetinari.Page
import com.bennavetta.vetinari.VetinariContext
import com.google.common.base.Charsets
import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import com.typesafe.config.ConfigFactory
import spock.lang.Specification

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

/**
 * Base class for Spock specifications.
 */
abstract class AbstractVetinariSpec extends Specification
{
	FileSystem fs

	def setup()
	{
		// TODO: some way to switch between OS X, Windows, and UNIX configurations
		fs = Jimfs.newFileSystem("vetinari-test-${getClass().simpleName}", Configuration.unix())
		println fs
	}

	def cleanup()
	{
		fs.close()
	}

	Path getPath(String first, String... more)
	{
		return fs.getPath(first, more)
	}

	Path mkdirs(String first, String... more)
	{
		return Files.createDirectories(getPath(first, more))
	}

	Path createFile(String first, String... more)
	{
		Path path = getPath(first, more)
		Files.createDirectories(path.getParent())
		return Files.createFile(path)
	}

	boolean fileExists(String first, String... more)
	{
		return Files.isRegularFile(getPath(first, more))
	}

	boolean directoryExists(String first, String... more)
	{
		return Files.isDirectory(getPath(first, more))
	}

	VetinariContext createContext(String name = 'vetinari-test', config = [:])
	{
		Path contentRoot = mkdirs(name, 'content')
		Path outputRoot = mkdirs(name, 'output')
		Path templateRoot = mkdirs(name, 'templates')


		return VetinariContext.builder()
			.contentEncoding(Charsets.UTF_8)
			.contentRoot(contentRoot)
			.outputRoot(outputRoot)
			.templateRoot(templateRoot)
			.siteConfig(ConfigFactory.parseMap(config))
			.build()
	}
}
