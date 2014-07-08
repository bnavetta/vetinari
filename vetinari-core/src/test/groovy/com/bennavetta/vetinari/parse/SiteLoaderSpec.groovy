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

	// ignores directories, single page, multiple pages, nested pages

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
}
