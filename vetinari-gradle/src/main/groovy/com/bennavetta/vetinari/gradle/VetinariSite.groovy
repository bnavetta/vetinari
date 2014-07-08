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
/**
 * Created by ben on 7/6/14.
 */
class VetinariSite
{
	String name

	public def contentEncoding;

	public def contentRoot;

	public def templateRoot;

	public def outputRoot;

	public def siteConfig;

	VetinariSite(String name)
	{
		this.name = name

		// Set defaults
		this.contentEncoding = 'UTF-8'
		this.contentRoot = "src/$name/content"
		this.templateRoot = "src/$name/templates"
		this.outputRoot = "build/sites/$name"
		this.siteConfig = "src/$name/site.conf"
	}
}
