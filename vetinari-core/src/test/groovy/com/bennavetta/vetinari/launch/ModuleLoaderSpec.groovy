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
package com.bennavetta.vetinari.launch

import com.bennavetta.vetinari.build.internal.BuildModule
import com.bennavetta.vetinari.parse.internal.ParseModule
import com.bennavetta.vetinari.render.internal.RenderModule
import com.bennavetta.vetinari.template.internal.TemplateModule
import spock.lang.Specification

class ModuleLoaderSpec extends Specification
{
	def "can parse listing file"()
	{
		given:
		def listingFile = File.createTempFile('vetinari-modules', 'txt')
		listingFile.write 'first\n# comment\nsecond\n\nthird'

		when:
		def listing = new ModuleLoader().invokeMethod('parseListing', listingFile.toURI().toURL())

		then:
		listing == ['first', 'second', 'third']
	}

	// This is a bit tricky to do because of ModuleLoader uses its own classpath
	// I could try adding a bad vetinari-modules.txt to make sure an exception is thrown
	def "can detect modules"()
	{
		when:
		def modules = new ModuleLoader().loadModules()

		then:
		modules.any { it instanceof ParseModule }
		modules.any { it instanceof BuildModule }
		modules.any { it instanceof RenderModule }
		modules.any { it instanceof TemplateModule }
	}

	// TODO: some kind of integration test for the launch stuff
}
