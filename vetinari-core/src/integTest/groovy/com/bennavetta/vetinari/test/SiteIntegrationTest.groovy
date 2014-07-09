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
import spock.lang.Specification

class SiteIntegrationTest extends Specification
{
	def "frontmatter parsed properly"()
	{
		given:
		def site = testSite("frontmatter")

		when:
		buildSite(site)

		then:
		site.hasContent "properties.html", "Good old Java Properties"
		site.hasContent "json.html", "JSON, or JavaScript Object Notation, is like JavaScript but data."
		site.hasContent "hocon.html", "Like any good human-readable configuration format, HOCON is similar to all the others but still different."
	}

	TestSite testSite(String name)
	{
		return new TestSite(name: name)
	}

	def buildSite(TestSite site)
	{
		VetinariLauncher launcher = new VetinariLauncher()
		site.configure(launcher)
		launcher.run()
	}
}
