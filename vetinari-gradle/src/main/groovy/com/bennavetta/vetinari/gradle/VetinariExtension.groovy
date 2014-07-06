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
package com.bennavetta.vetinari.gradle

import org.gradle.api.Project

import java.nio.charset.Charset

/**
 * Extension for configuring Vetinari builds
 */
class VetinariExtension
{
	private Charset contentEncoding

	def contentRoot
	def templateRoot
	def outputRoot
	def siteConfig

	def modules

	private Project project

	public VetinariExtension(Project project)
	{
		this.project = project
	}

	public void setContentEncoding(def encoding)
	{
		if(encoding instanceof Charset)
		{
			contentEncoding = encoding
		}
		else
		{
			contentEncoding = Charset.forName(encoding.toString())
		}
	}

	public Charset getContentEncoding()
	{
		return contentEncoding
	}
}
