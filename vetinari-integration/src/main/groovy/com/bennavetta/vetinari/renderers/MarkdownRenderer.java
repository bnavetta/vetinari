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
package com.bennavetta.vetinari.renderers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import com.bennavetta.vetinari.render.Renderer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * Render Markdown with Pegdown
 */
public class MarkdownRenderer implements Renderer
{
	private static final Map<String, Integer> EXTENSION_MAPPING = ImmutableMap.<String, Integer>builder()
			.put("smarts", Extensions.SMARTS)
			.put("quotes", Extensions.QUOTES)
			.put("smartypants", Extensions.SMARTYPANTS)
			.put("abbreviations", Extensions.ABBREVIATIONS)
			.put("hardwraps", Extensions.HARDWRAPS)
			.put("autolinks", Extensions.AUTOLINKS)
			.put("tables", Extensions.TABLES)
			.put("definitions", Extensions.DEFINITIONS)
			.put("fenced-code-blocks", Extensions.FENCED_CODE_BLOCKS)
			.put("suppress-all-html", Extensions.SUPPRESS_ALL_HTML)
			.put("suppress-html-blocks", Extensions.SUPPRESS_HTML_BLOCKS)
			.put("suppress-inline-html", Extensions.SUPPRESS_INLINE_HTML)
			.put("wikilinks", Extensions.WIKILINKS)
			.put("strikethrough", Extensions.STRIKETHROUGH)
			.put("all", Extensions.ALL)
			.build();


	private PegDownProcessor pegdown;

	@Inject
	public MarkdownRenderer(@Named("siteConfig") Config siteConfig)
	{

		int extensions = Extensions.NONE;
		if(siteConfig.hasPath("markdown.extensions"))
		{
			extensions = siteConfig.getStringList("markdown.extensions").stream()
			    .map(EXTENSION_MAPPING::get).reduce(Extensions.NONE, (partial, next) -> partial | next);
		}
		this.pegdown = new PegDownProcessor(extensions);
	}

	@Override
	public String render(String source)
	{
		return pegdown.markdownToHtml(source);
	}

	@Override
	public String getName()
	{
		return "markdown";
	}

	@Override
	public Iterable<String> getFileExtensions()
	{
		return ImmutableList.of("md", "markdown");
	}
}
