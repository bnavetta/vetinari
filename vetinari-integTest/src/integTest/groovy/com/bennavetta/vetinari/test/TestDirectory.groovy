package com.bennavetta.vetinari.test

import org.junit.rules.ExternalResource
import org.junit.runner.Description
import org.junit.runners.model.Statement

import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

/**
 * JUnit rule for a temporary directory using the NIO Path API.
 */
class TestDirectory extends ExternalResource
{
	private static Path root = Paths.get("build", "tmp", "test-files")

	private Description description
	private Path dir

	@Override
	public Statement apply(Statement base, Description description)
	{
		super.apply(base, description)
		this.description = description
	}

	@Override
	protected void before() throws IOException
	{
		dir = Files.createDirectories(root.resolve(description.className + "-" + description.methodName))
	}

	@Override
	protected void after() throws IOException
	{
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			{
				Files.delete(file)
				return FileVisitResult.CONTINUE
			}

			@Override
			FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
			{
				Files.delete(file)
			}

			@Override
			FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
			{
				if(exc == null)
				{
					Files.delete(dir)
					return FileVisitResult.CONTINUE
				}
				else
				{
					throw exc
				}
			}
		})
	}

	Path root()
	{
		return dir
	}

	Path createFile(String path, String content=null) throws IOException
	{
		Path file = root().resolve(path)
		Files.createDirectories(file.parent)
		Files.createFile(file)
		if(content)
		{
			Files.write(file, content.getBytes(StandardCharsets.UTF_8))
		}
		return file
	}

	Path createDirectory(String path) throws IOException
	{
		Path file = root().resolve(path)
		return Files.createDirectories(file)
	}

	boolean isFile(String path)
	{
		return Files.isRegularFile(root().resolve(path))
	}

	boolean isDirectory(String path)
	{
		return Files.isDirectory(root().resolve(path))
	}

	boolean hasContent(String path, String content)
	{
		return new String(Files.readAllBytes(root().resolve(path)), StandardCharsets.UTF_8) == content
	}
}
