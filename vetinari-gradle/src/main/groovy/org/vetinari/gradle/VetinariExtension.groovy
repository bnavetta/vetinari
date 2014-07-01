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
		conf.contentEncoding = getContentEncoding()
		conf.contentRoot = project.file(getContentRoot()).toPath()
		conf.templateRoot = project.file(getTemplateRoot()).toPath()
		conf.outputRoot = project.file(getOutputRoot()).toPath()
		conf.siteConfig = project.file(getSiteConfig()).toPath()
		return conf
	}
}
