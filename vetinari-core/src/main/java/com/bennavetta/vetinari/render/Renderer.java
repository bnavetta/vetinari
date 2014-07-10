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
package com.bennavetta.vetinari.render;

/**
 * A {@code Renderer} converts input markup, like Markdown, and generates HTML content.
 */
public interface Renderer
{
	/**
	 * Returns the unique name identifying this renderer. It is used mainly for settings like
	 * {@code renderer} in page metadata or {@code defaultRenderer} in the site configuration.
	 */
	public String getName();

	/**
	 * Returns the file extensions associated with this renderer. These are mainly to detect the renderer
	 * to use from path names.
	 */
	public Iterable<String> getFileExtensions();

	/**
	 * Process the given content source and generate the output content.
	 * @param source the source, usually markup of some kind.
	 * @return the rendered content, almost always HTML.
	 */
	public String render(String source);
}
