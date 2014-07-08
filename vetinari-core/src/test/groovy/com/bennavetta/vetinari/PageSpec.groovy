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
import com.typesafe.config.ConfigFactory

class PageSpec extends AbstractVetinariSpec
{
	def "can retrieve title from metadata"()
	{
		given:
		def page = new Page(ConfigFactory.parseMap([title: 'Foo']), getPath('foo.txt'), '')

		expect:
		page.title == 'Foo'
	}

	def "identifier computed correctly"()
	{
		given:
		def page = new Page(ConfigFactory.empty(), getPath(path), '')

		expect:
		page.identifier == identifier

		where:
		path      || identifier
		'foo.txt' || 'foo'
		'bar'     || 'bar'
		''        || ''
		'a/b.txt' || 'a/b'
		'a/b'     || 'a/b'
	}
}
