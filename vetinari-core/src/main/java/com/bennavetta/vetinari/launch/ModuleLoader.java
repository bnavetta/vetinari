package com.bennavetta.vetinari.launch;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.Module;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads Guice modules for Vetinari. Modules are listed in {@code META-INF/vetinari-modules.txt} files. Each file
 * is UTF-8 encoded and contains a list of module class names, one on each line. Empty lines and lines starting
 * with {@code #} are ignored. Module classes must have no-argument constructors.
 */
@Slf4j
public class ModuleLoader
{
	private List<URL> findListingFiles(ClassLoader classLoader) throws IOException
	{
		Iterator<URL> iter = Iterators.forEnumeration(classLoader.getResources("META-INF/vetinari-modules.txt"));
		return Lists.newArrayList(iter);
	}

	private List<String> parseListing(URL listingUrl) throws IOException
	{
		return Resources.readLines(listingUrl, Charsets.UTF_8).stream().map(String::trim)
				.filter(l -> !l.startsWith("#")).collect(Collectors.toList());
	}

	/**
	 * Loads Guice modules from the classpath as described above.
	 * @throws IOException if thrown reading classpath resources
	 */
	public List<Module> loadModules() throws IOException
	{
		List<String> classNames = Lists.newArrayList();
		for(URL listing : findListingFiles(getClass().getClassLoader()))
		{
			log.debug("Reading module list from {}", listing);
			classNames.addAll(parseListing(listing));
		}

		ImmutableList.Builder<Module> modulesBuilder = ImmutableList.builder();
		try
		{
			for(String className : classNames)
			{
				log.debug("Instantiating module class {}", className);
				modulesBuilder.add((Module) Class.forName(className).newInstance());
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			throw Throwables.propagate(e); // Something is probably wrong with the setup if this happens.
		}

		return modulesBuilder.build();
	}
}
