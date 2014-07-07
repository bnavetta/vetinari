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
	private boolean displayVersion = false;

	@Parameter(names = {"-h", "--help"}, help = true)
	private boolean help;

	@Parameter(names = "--verbose", description = "Enable verbose log output")
	private boolean verbose = false;

	@Parameter(names = "--debug", description = "Enable debug log output")
	private boolean debug = false;

	/*
	Commands
	 */

	VersionCommand versionCommand = new VersionCommand();
	BuildCommand buildCommand = new BuildCommand();

	public void run(String[] args)
	{
		JCommander jc = new JCommander(this);
		jc.setProgramName("vetinari");
		jc.addCommand("version", versionCommand);
		jc.addCommand("build", buildCommand);

		jc.parse(args);

		if(verbose)
		{
			System.setProperty("vetinari.verbosity", "verbose");
		}
		else if(debug)
		{
			System.setProperty("vetinari.verbosity", "debug");
		}
		else
		{
			System.setProperty("vetinari.verbosity", "normal");
		}

		if(help)
		{
			jc.usage();
		}

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
