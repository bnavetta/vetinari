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
package com.bennavetta.vetinari.build.internal.phase;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.VetinariContext;
import com.bennavetta.vetinari.VetinariException;
import com.bennavetta.vetinari.build.BuildPhase;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Output pages to the default locations. This can be disabled by setting {@code outputDefaultFile}
 * to {@code false} in the site configuration file if other output mechanisms are used, like pretty URLs.
 */
@Slf4j
public class DefaultOutputPhase implements BuildPhase
{
	private final VetinariContext context;

	@Inject
	public DefaultOutputPhase(VetinariContext context)
	{
		this.context = context;
	}

	@Override
	public int getOrder()
	{
		return BuildPhase.ORDER_OUTPUT;
	}

	@Override
	public Site process(Site site) throws VetinariException
	{
		try
		{
			for (Page page : site.getPages())
			{
				Path outputPath = context.getOutputRoot().resolve(page.getIdentifier() + ".html");
				log.info("Writing {} to {}", page, outputPath);
				Files.write(outputPath, page.getContent().getBytes(context.getContentEncoding()));
			}
			return site; // allow for more outputs/processing
		}
		catch(IOException e)
		{
			throw new VetinariException("Unable to write output file", e);
		}
	}
}
