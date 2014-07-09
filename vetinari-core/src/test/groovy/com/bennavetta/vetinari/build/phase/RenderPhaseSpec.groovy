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
package com.bennavetta.vetinari.build.phase

import com.bennavetta.vetinari.Page
import com.bennavetta.vetinari.Site
import com.bennavetta.vetinari.render.Renderer
import com.bennavetta.vetinari.test.AbstractVetinariSpec
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.typesafe.config.ConfigFactory

class RenderPhaseSpec extends AbstractVetinariSpec
{
	def phase = new RenderPhase([new UpperCaseRenderer()] as Set)

	def "can use renderer from file extension"()
	{
		given:
		def site = Site.builder().pages(ImmutableMap.of(
				"my-page", new Page(ConfigFactory.empty(), getPath('my-page.uc'), 'words words words')
		)).build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'WORDS WORDS WORDS'
	}

	def "can use renderer from frontmatter"()
	{
		given:
		def site = Site.builder().pages(ImmutableMap.of(
				"my-page", new Page(ConfigFactory.parseMap([renderer: 'upperCase']), getPath('my-page.html'), 'words words words')
		)).build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'WORDS WORDS WORDS'
	}

	def "can use default renderer"()
	{
		given:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultRenderer: 'upperCase'])).pages(ImmutableMap.of(
				"my-page", new Page(ConfigFactory.empty(), getPath('my-page.html'), 'words words words')
		)).build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'WORDS WORDS WORDS'
	}

	private static class UpperCaseRenderer implements Renderer
	{

		@Override
		String render(String source)
		{
			return source.toUpperCase()
		}

		@Override
		String getName()
		{
			return "upperCase"
		}

		@Override
		Iterable<String> getFileExtensions()
		{
			return ImmutableSet.of("uc")
		}
	}
}
