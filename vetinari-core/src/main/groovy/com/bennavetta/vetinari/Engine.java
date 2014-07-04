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
package com.bennavetta.vetinari;

/**
 * Some kind of engine that will be applied to content. This interface is mainly to help
 * with detecting which engines to use for a page.
 */
public interface Engine
{
	/**
	 * Returns the unique, display-suitable name of this engine.
	 */
	public String getName();

	/**
	 * Returns file extensions associated with this engine, such as {@code md} for Markdown.
	 */
	public Iterable<String> getFileExtensions();
}
