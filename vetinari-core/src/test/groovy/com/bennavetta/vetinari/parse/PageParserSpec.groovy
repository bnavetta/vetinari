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

import com.bennavetta.vetinari.Page
import com.bennavetta.vetinari.VetinariContext
import com.bennavetta.vetinari.test.AbstractVetinariSpec
import com.google.common.base.Charsets
import com.typesafe.config.ConfigFactory

import java.nio.file.Files
import java.nio.file.Path

class PageParserSpec extends AbstractVetinariSpec
{
	PageParser parser = new PageParser()

	def "handles empty file"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')
		Files.createFile(file)

		when:
		Page page = parser.parsePage(file, context)

		then:
		page.content == ''
		page.identifier == 'myfile'
		page.metadata == ConfigFactory.empty()
		page.path == getPath('myfile.md')
	}

	def "handles content without metadata"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')
		file.write 'Hello, World!', context.contentEncoding.name()

		when:
		Page page = parser.parsePage(file, context)

		then:
		page.content == 'Hello, World!\n' // PageParser adds a newline since BufferedReader removes them
		page.identifier == 'myfile'
		page.metadata == ConfigFactory.empty()
		page.path == getPath('myfile.md')
	}

	def "handles metadata without content"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')
		file.write frontmatter, context.contentEncoding.name()

		when:
		Page page = parser.parsePage(file, context)

		then:
		page.content == ''
		page.identifier == 'myfile'
		page.metadata.getString('foo') == 'bar'
		page.path == getPath('myfile.md')

		where:
		frontmatter << ['+++\nfoo="bar"\n+++', '---\nfoo=bar\n---', '~~~\n{"foo":"bar"}\n~~~']
	}

	def "handles metadata and content"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')
		file.write '---\nfoo=bar\n---\nHello, World!', context.contentEncoding.name()

		when:
		Page page = parser.parsePage(file, context)

		then:
		page.content == 'Hello, World!'
		page.metadata.getString('foo') == 'bar'
	}

	def "throws exception on missing end delimiter"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')
		file.write '+++\nfoo=bar\n# This is markdown', context.contentEncoding.name()

		when:
		parser.parsePage(file, context)

		then:
		def e = thrown(PageParseException)
		e.message.startsWith("Reached EOF before end delimiter '+++' in ")
	}

	def "throws exception on unreadable file"()
	{
		given:
		VetinariContext context = createContext()
		Path file = context.contentRoot.resolve('myfile.md')

		when:
		parser.parsePage(file, context)

		then:
		def e = thrown(PageParseException)
		e.message.startsWith('Error reading page content for ')
		e.cause instanceof IOException
	}
}
