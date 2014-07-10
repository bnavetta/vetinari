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
package com.bennavetta.vetinari.build.internal.phase

import com.bennavetta.vetinari.Page
import com.bennavetta.vetinari.Site
import com.bennavetta.vetinari.template.internal.groovy.GroovyTemplateEngine
import com.bennavetta.vetinari.test.AbstractVetinariSpec
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Sets
import com.typesafe.config.ConfigFactory

import java.nio.file.Paths

class TemplatePhaseSpec extends AbstractVetinariSpec
{
	def phase = new TemplatePhase(Sets.newHashSet(new GroovyTemplateEngine()))

	def "applies template logic"()
	{
		given:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultTemplateEngine: 'groovyTemplate']))
				.pages(ImmutableMap.of("my-page",
					new Page(ConfigFactory.parseMap([title: 'My Page']), Paths.get('my-page.html'), 'This is ${page.title}')))
				.build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'This is My Page'
	}

	def "can detect template engine from extension"()
	{
		given:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultTemplateEngine: 'does not exist']))
				.pages(ImmutableMap.of("my-page",
				new Page(ConfigFactory.parseMap([title: 'My Page']), Paths.get('my-page.gtpl.html'), 'This is ${page.title}')))
				.build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'This is My Page'
	}

	def "can detect template engine from frontmatter"()
	{
		given:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultTemplateEngine: 'does not exist']))
				.pages(ImmutableMap.of("my-page",
				new Page(ConfigFactory.parseMap([title: 'My Page', templateEngine: 'groovyTemplate']), Paths.get('my-page.html'), 'This is ${page.title}')))
				.build()

		when:
		def output = phase.process(site)

		then:
		output.getPage('my-page').content == 'This is My Page'
	}
}
