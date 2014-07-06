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
package com.bennavetta.vetinari.build.phase;

import com.bennavetta.vetinari.Page;
import com.bennavetta.vetinari.Site;
import com.bennavetta.vetinari.build.BuildPhase;
import com.bennavetta.vetinari.render.Renderer;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

import java.util.Set;

import static com.google.common.io.Files.getFileExtension;

/**
 * Render pages to produce HTML fragments.
 */
public class RenderPhase implements BuildPhase
{
	private final Set<Renderer> renderers;

	@Inject
	public RenderPhase(Set<Renderer> renderers)
	{
		this.renderers = renderers;
	}

	@Override
	public int getOrder()
	{
		return BuildPhase.ORDER_RENDER;
	}

	@Override
	public Site process(Site input) throws Exception
	{
		return input.transformPages(page -> page.withContent(getRenderer(page, input).render(page.getContent())));
	}

	private Renderer getRenderer(Page page, Site site)
	{
		Renderer renderer = null;
		// The last file extension is always the renderer, if anything.
		final String extension = getFileExtension(page.getPath().toString());
		Optional<Renderer> rendererFromExtension = Iterables.tryFind(renderers, r -> Iterables.contains(r.getFileExtensions(), extension));
		if(rendererFromExtension.isPresent())
		{
			renderer = rendererFromExtension.get();
		}
		else if(page.getMetadata().hasPath("renderer"))
		{
			final String rendererName = page.getMetadata().getString("renderer");
			renderer = Iterables.find(renderers, r -> rendererName.equals(r.getName()));
		}
		else
		{
			renderer = Iterables.find(renderers, r -> site.getDefaultRenderer().equals(r.getName()));
		}
		return renderer;
	}
}
