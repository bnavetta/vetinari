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
package com.bennavetta.vetinari.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
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

		try
		{
			jc.parse(args);
		}
		catch(ParameterException e)
		{
			System.err.println(e.getMessage());
			jc.usage();
			System.exit(1);
		}

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
		else
		{
			if("version".equals(jc.getParsedCommand()) || displayVersion)
			{
				versionCommand.run();
			}
			else if(Strings.isNullOrEmpty(jc.getParsedCommand()) || "build".equals(jc.getParsedCommand()))
			{
				buildCommand.run();
			}
		}
	}

	public static void main(String[] args)
	{
		new VetinariMain().run(args);
	}


}
