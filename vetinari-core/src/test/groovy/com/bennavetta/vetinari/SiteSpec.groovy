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
package com.bennavetta.vetinari

import com.bennavetta.vetinari.test.AbstractVetinariSpec
import com.google.common.collect.ImmutableMap
import com.typesafe.config.ConfigFactory

class SiteSpec extends AbstractVetinariSpec
{
	def "can transform pages"()
	{
		given:
		Page firstPage = new Page(ConfigFactory.empty(), getPath('first-page.txt'), 'first content')
		Page secondPage = new Page(ConfigFactory.empty(), getPath('second-page.txt'), 'second content')
		def site = Site.builder().pages(ImmutableMap.of("first-page", firstPage,
				"second-page", secondPage)).build()

		when:
		def transformed = site.transformPages({ page -> return page.withContent(page.content.toUpperCase()) })

		then:
		// Old site unmodified
		site.getPage('first-page') == firstPage
		site.getPage('second-page') == secondPage
		site.pages.size() == 2

		// New site modified
		transformed.getPage('first-page').content == 'FIRST CONTENT'
		transformed.getPage('second-page').content == 'SECOND CONTENT'
	}

	def "can retrieve default template engine"()
	{
		when:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultTemplateEngine: 'foo'])).build()

		then:
		site.defaultTemplateEngine == 'foo'
	}

	def "can retrieve default renderer"()
	{
		when:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultRenderer: 'foo'])).build()

		then:
		site.defaultRenderer == 'foo'
	}

	def "can retrieve default layout"()
	{
		when:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([defaultLayout: 'foo'])).build()

		then:
		site.defaultLayout == 'foo'
	}

	def "can retrieve title"()
	{
		when:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([title: 'My Site'])).build()

		then:
		site.title == 'My Site'
	}

	def "can retrieve base url"()
	{
		when:
		def site = Site.builder().siteConfig(ConfigFactory.parseMap([baseUrl: 'http://www.mysite.com'])).build()

		then:
		site.baseUrl == URI.create('http://www.mysite.com')
	}
}
