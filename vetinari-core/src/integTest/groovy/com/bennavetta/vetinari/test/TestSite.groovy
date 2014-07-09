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

import com.bennavetta.vetinari.launch.VetinariLauncher
import com.google.common.base.Charsets

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Models a site to run as an integration test.
 */
class TestSite
{
	String name
	Path outputDir

	def configure(VetinariLauncher launcher)
	{
		Path baseDir = Paths.get(TestSite.class.getResource("/sites/$name").toURI())
		outputDir = Files.createTempDirectory("test-site-$name") // could put in build dir?

		launcher.withContentEncoding(Charsets.UTF_8)
			.withContentRoot(baseDir.resolve("content"))
			.withTemplateRoot(baseDir.resolve("templates"))
			.withSiteConfig(baseDir.resolve("site.conf"))
			.withOutputRoot(outputDir)
	}

	void hasFile(String path)
	{
		assert Files.isRegularFile(outputDir.resolve(path))
	}

	void hasDirectory(String path)
	{
		assert Files.isDirectory(outputDir.resolve(path))
	}

	void hasContent(String path, String content)
	{
		assert outputDir.resolve(path).getText('UTF-8') == content
	}
}
