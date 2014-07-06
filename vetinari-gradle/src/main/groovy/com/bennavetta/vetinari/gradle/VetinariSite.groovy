package com.bennavetta.vetinari.gradle
/**
 * Created by ben on 7/6/14.
 */
class VetinariSite
{
	String name

	public def contentEncoding;

	public def contentRoot;

	public def templateRoot;

	public def outputRoot;

	public def siteConfig;

	VetinariSite(String name)
	{
		this.name = name

		// Set defaults
		this.contentEncoding = 'UTF-8'
		this.contentRoot = "src/$name/content"
		this.templateRoot = "src/$name/templates"
		this.outputRoot = "build/sites/$name"
		this.siteConfig = "src/$name/site.conf"
	}
}
