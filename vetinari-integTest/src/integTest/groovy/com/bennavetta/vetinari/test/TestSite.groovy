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

import com.bennavetta.vetinari.cli.VetinariMain
import com.google.common.io.Resources
import groovy.json.JsonOutput

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Models a site for testing
 */
class TestSite
{
	private final Path root

	final Path contentRoot
	final Path templateRoot
	final Path outputRoot
	final Path siteConfigPath

	/**
	 * Construct a {@code TestSite} by copying the classpath location {@code sourceName} into {@code buildRoot}.
	 */
	public TestSite(Path buildRoot, String sourceName)
	{
		Path sourcePath = Paths.get(Resources.getResource(sourceName).toURI())
		Files.walk(sourcePath).forEach({ Path path ->
			// Don't copy the source root, since it already exists as the destination root
			if(path != sourcePath)
			{
				Files.copy(path, buildRoot.resolve(sourcePath.relativize(path)))
			}
		})

		this.root = buildRoot
		this.contentRoot = root.resolve('content')
		this.templateRoot = root.resolve('templates')
		this.outputRoot = root.resolve('output')
		this.siteConfigPath = root.resolve('site-config.json')
	}

	/**
	 * Construct a {@code TestSite} with the standard layout under {@code root}.
	 */
	public TestSite(Path root)
	{
		this.root = root

		this.contentRoot = Files.createDirectories(root.resolve('content'))
		this.templateRoot = Files.createDirectories(root.resolve('templates'))
		this.outputRoot = root.resolve('output')
		this.siteConfigPath = Files.createFile(root.resolve('site-config.json'))
	}

	public void build()
	{
		AntBuilder ant = new AntBuilder()
		// TODO: code coverage with fork
		ant.java(classname: VetinariMain.name, fork: true, clonevm: true) {
			['build',
		        '--content-root', contentRoot, '--template-root', templateRoot,
		        '--output-root', outputRoot, '--site-config', siteConfigPath
			].each {
				arg value: it
			}
		}
	}

	public void setSiteConfig(Map config)
	{
		siteConfigPath.write JsonOutput.toJson(config), StandardCharsets.UTF_8
	}

	void outputFileExists(String path, String content = null)
	{
		Path expected = outputRoot.resolve(path)

		assert Files.isRegularFile(expected)

		if(content)
		{
			assert new String(expected.readBytes(), StandardCharsets.UTF_8) == content
		}
	}

	void outputDirectoryExists(String path)
	{
		assert Files.isDirectory(outputRoot.resolve(path))
	}
}
