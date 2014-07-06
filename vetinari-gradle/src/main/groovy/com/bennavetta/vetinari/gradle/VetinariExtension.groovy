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

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

/**
 * Extension for configuring Vetinari builds
 */
class VetinariExtension
{
	String vetinariVersion = VetinariPlugin.getVersion()

	final NamedDomainObjectContainer<VetinariSite> sites

	VetinariExtension(NamedDomainObjectContainer<VetinariSite> sites)
	{
		this.sites = sites
	}

	public sites(Closure closure)
	{
		sites.configure(closure)
	}

	public sites(Action<? super NamedDomainObjectContainer<VetinariSite>> action)
	{
		action.execute(sites)
	}
}
