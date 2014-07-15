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
 * Writes pages out using their {@code outputPath} values and the site's {@code outputRoot}.
 */
@Slf4j
public class OutputPhase implements BuildPhase
{
	private final VetinariContext context;

	@Inject
	public OutputPhase(VetinariContext context)
	{
		this.context = context;
	}

	@Override
	public String getName()
	{
		return "output";
	}

	@Override
	public Site process(Site site) throws VetinariException
	{
		try
		{
			for(Page page : site.getPages())
			{
				Path outputPath = context.getOutputRoot().resolve(page.getMetadata().getString("outputPath"));
				log.info("Writing {} to {}", page, outputPath);
				Files.write(outputPath, page.getContent().getBytes(context.getContentEncoding()));
			}
		}
		catch(IOException e)
		{
			throw new VetinariException("Unable to write output file", e);
		}

		return site;
	}
}
