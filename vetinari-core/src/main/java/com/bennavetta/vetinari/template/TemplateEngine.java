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
package com.bennavetta.vetinari.template;

/**
 * A {@code TemplateEngine} applies logic to page content.
 */
public interface TemplateEngine
{
	/**
	 * Returns the unique name identifying this engine. It is used mainly for settings like
	 * {@code templateEngine} in page metadata or {@code defaultTemplateEngine} in the site configuration.
	 */
	public String getName();

	/**
	 * Returns the file extensions associated with this engine. These are mainly to detect the engine
	 * to use from path names.
	 */
	public Iterable<String> getFileExtensions();

	/**
	 * Compile the given template source so that it can later be rendered.
	 * @param source the template source code
	 * @return the compiled template
	 */
	public Template compile(String source);
}
