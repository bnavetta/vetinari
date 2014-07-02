package org.vetinari.renderers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.vetinari.Site;
import org.vetinari.render.Renderer;

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
