package com.bennavetta.vetinari.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.base.Strings;

/**
 * Main class for Vetinari.
 */
public class VetinariMain
{
	/*
	Parameters
	 */

	@Parameter(names = {"-v", "--version"}, description = "Display version information")
	public boolean displayVersion = false;

	/*
	Commands
	 */

	VersionCommand versionCommand = new VersionCommand();
	BuildCommand buildCommand = new BuildCommand();

	public void run(String[] args)
	{

		JCommander jc = new JCommander(this);
		jc.addCommand("version", versionCommand);
		jc.addCommand("build", buildCommand);

		jc.parse(args);

		if("version".equals(jc.getParsedCommand()) || displayVersion)
		{
			versionCommand.run();
		}
		else if(Strings.isNullOrEmpty(jc.getParsedCommand()) || "build".equals(jc.getParsedCommand()))
		{
			buildCommand.run();
		}
	}

	public static void main(String[] args)
	{
		new VetinariMain().run(args);
	}
}
