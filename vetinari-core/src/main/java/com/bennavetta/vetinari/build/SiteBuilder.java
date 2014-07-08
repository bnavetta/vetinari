/**
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
package com.bennavetta.vetinari.build;

import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.VetinariContext;
import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.parse.SiteLoader;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Build a site by applying {@link BuildPhase}s in order.
 */
@Singleton
@Slf4j
public class SiteBuilder
{
	private final SiteLoader siteLoader;
	private final VetinariContext context;

	private final Set<BuildPhase> phases;

	@Inject
	public SiteBuilder(SiteLoader siteLoader, VetinariContext context, Set<BuildPhase> phases)
	{
		this.siteLoader = siteLoader;
		this.context = context;
		this.phases = phases;
	}

	/**
	 * Build the site. The site loaded from a {@link SiteLoader} is passed through the ordered sequence
	 * of build phases, each receiving the result of the previous.
	 * @throws Exception if any phase throws an exception, or there is an error loading the site
	 */
	public void build() throws VetinariException
	{
		Site site = siteLoader.load(context);

		List<BuildPhase> orderedPhases = Lists.newArrayList(phases);
		Collections.sort(orderedPhases, (a, b) -> Integer.compare(a.getOrder(), b.getOrder()));
		log.debug("Build Phases: {}", orderedPhases);

		for(BuildPhase phase : orderedPhases)
		{
			log.info("Running phase {}", phase.getName());
			site = phase.process(site);
		}
		// File output should be a phase, so we don't have to handle it here.
	}
}
