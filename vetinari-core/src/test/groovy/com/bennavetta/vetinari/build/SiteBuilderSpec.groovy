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
package com.bennavetta.vetinari.build

import com.bennavetta.vetinari.parse.PageParser
import com.bennavetta.vetinari.parse.SiteLoader
import com.bennavetta.vetinari.test.AbstractVetinariSpec

import java.nio.file.Files

class SiteBuilderSpec extends AbstractVetinariSpec
{
	def "build phase order respected"()
	{
		given:
		def phaseA = Mock(BuildPhase)
		def phaseB = Mock(BuildPhase)
		
		phaseA.getName() >> "phaseA"
		phaseB.getName() >> "phaseB"

		def context = createContext('my-context', [phases: ['phaseA', 'phaseB']])
		def builder = new SiteBuilder(new SiteLoader(new PageParser()), context, [phaseA, phaseB] as Set)
		Files.createFile(context.contentRoot.resolve('my-file.txt')).write 'Initial content'

		when:
		builder.build()

		then:
		1 * phaseA.process({site -> site.getPage('my-file').content == 'Initial content\n'}) >> {args -> args[0].transformPages({p -> p.withContent('Phase A Result')})}
		1 * phaseB.process({site -> site.getPage('my-file').content == 'Phase A Result'})
	}
}
