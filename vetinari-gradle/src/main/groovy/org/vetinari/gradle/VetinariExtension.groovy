package org.vetinari.gradle

import org.gradle.api.Project
import org.vetinari.Configuration

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

	public Configuration toConfiguration()
	{
		Configuration conf = new Configuration()
		conf.contentEncoding = contentEncoding
		conf.contentRoot = project.file(contentRoot).toPath()
		conf.templateRoot = project.file(templateRoot).toPath()
		conf.outputRoot = project.file(outputRoot).toPath()
		conf.siteConfig = project.file(siteConfig).toPath()
		return conf
	}
}
