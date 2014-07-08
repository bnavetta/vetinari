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
package com.bennavetta.vetinari.parse

import com.bennavetta.vetinari.Site
import com.bennavetta.vetinari.VetinariContext
import com.bennavetta.vetinari.VetinariException
import com.bennavetta.vetinari.test.AbstractVetinariSpec

import java.nio.file.Files
import java.nio.file.Path

class SiteLoaderSpec extends AbstractVetinariSpec
{
	SiteLoader loader = new SiteLoader(new PageParser())

	def "throws exception if unable to load pages"()
	{
		given:
		VetinariContext context = createContext()
		Files.delete(context.contentRoot)

		when:
		loader.load(context)

		then:
		def e = thrown(VetinariException)
		e.message == 'Unable to load content pages'
		e.cause instanceof IOException
	}

	def "ignores directories"()
	{
		given:
		VetinariContext context = createContext()
		Path dir = context.contentRoot.resolve('mydir')
		Files.createDirectory(dir)
		Path file = context.contentRoot.resolve('myfile')
		Files.createFile(file)

		when:
		Site site = loader.load(context)

		then:
		site.getPage('myfile') != null
		site.getPage('mydir') == null
	}

	def "can load single page"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('page.txt')
		file.write 'This is a page', context.contentEncoding.name()

		when:
		Site site = loader.load(context)

		then:
		site.pages.size() == 1
		site.getPage('page').content == 'This is a page\n'
	}

	def "can load multiple pages"()
	{
		given:
		VetinariContext context = createContext()
		Path fileA = context.contentRoot.resolve('fileA.html')
		fileA.write 'This is file A'
		Path fileB = context.contentRoot.resolve('fileB.html')
		fileB.write 'This is file B'

		when:
		Site site = loader.load(context)

		then:
		site.pages.size() == 2
		site.getPage('fileA').content == 'This is file A\n'
		site.getPage('fileB').content == 'This is file B\n'
	}

	def "can load pages in subdirectories"()
	{
		given:
		VetinariContext context = createContext()
		Path rootFile = context.contentRoot.resolve('root.html')
		rootFile.write 'In content root'
		Path dir = context.contentRoot.resolve('subdir')
		Files.createDirectory(dir)
		Path subFile = dir.resolve('sub.html')
		subFile.write 'In subdir'

		when:
		Site site = loader.load(context)

		then:
		site.pages.size() == 2
		site.getPage('root').content == 'In content root\n'
		site.getPage('subdir/sub').content == 'In subdir\n'
	}
}
