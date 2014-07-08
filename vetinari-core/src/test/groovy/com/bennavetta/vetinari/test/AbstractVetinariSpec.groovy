package com.bennavetta.vetinari.test

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import spock.lang.Specification

import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

/**
 * Base class for Spock specifications.
 */
abstract class AbstractVetinariSpec extends Specification
{
	// TODO: some way to switch between OS X, Windows, and UNIX configurations
	FileSystem fs = Jimfs.newFileSystem("vetinari-test-${getClass().simpleName}", Configuration.unix())

	Path getPath(String first, String... more)
	{
		return fs.getPath(first, more)
	}

	Path mkdirs(String first, String... more)
	{
		return Files.createDirectories(getPath(first, more))
	}

	Path createFile(String first, String... more)
	{
		Path path = getPath(first, more)
		Files.createDirectories(path.getParent())
		return Files.createFile(path)
	}

	boolean fileExists(String first, String... more)
	{
		return Files.isRegularFile(getPath(first, more))
	}

	boolean directoryExists(String first, String... more)
	{
		return Files.isDirectory(getPath(first, more))
	}
}
