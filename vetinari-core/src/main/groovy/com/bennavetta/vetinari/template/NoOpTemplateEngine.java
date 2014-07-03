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
package com.bennavetta.vetinari.template;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import com.bennavetta.vetinari.Site;

import java.util.Map;

/**
 * Does nothing.
 */
public class NoOpTemplateEngine implements TemplateEngine
{
	@Override
	public Template compile(String source, Site site)
	{
		return new NoOpTemplate(source);
	}

	@Override
	public String getName()
	{
		return "no-op";
	}

	@Override
	public Iterable<String> getFileExtensions()
	{
		return ImmutableSet.of();
	}

	@AllArgsConstructor
	private static final class NoOpTemplate implements Template
	{
		private final String source;

		@Override
		public String render(Map<String, Object> variables)
		{
			return source;
		}
	}
}
