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
import com.bennavetta.vetinari.template.TemplateLoader
import com.bennavetta.vetinari.template.internal.groovy.GroovyTemplateEngine
import com.bennavetta.vetinari.test.AbstractVetinariSpec
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Sets
import com.typesafe.config.ConfigFactory

class LayoutPhaseSpec extends AbstractVetinariSpec
{
	def "can use page-specific layout"()
	{
		given:
		def context = createContext('layout-spec', [defaultTemplateEngine: 'groovyTemplate'])
		def templateLoader = new TemplateLoader(context, Sets.newHashSet(new GroovyTemplateEngine()))
		def site = Site.builder().siteConfig(context.siteConfig).pages(ImmutableMap.of(
				'my-page', new Page(ConfigFactory.parseMap([layout: 'foo.gtpl']), context.contentRoot.resolve('my-page.html'), 'Stuff')
		)).build()
		context.templateRoot.resolve('foo.gtpl').write 'Behold: ${content}'

		when:
		def output = new LayoutPhase(templateLoader).process(site)

		then:
		output.getPage('my-page').content == 'Behold: Stuff'
	}
}
