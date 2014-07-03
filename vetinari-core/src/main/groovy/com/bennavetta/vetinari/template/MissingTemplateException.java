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

import java.nio.file.Path;

/**
 * Indicates that a template could not be found
 */
public class MissingTemplateException extends RuntimeException
{
	public MissingTemplateException(Path templateDir, String templatePath)
	{
		super("Template " + templatePath + " not found in " + templateDir);
	}
}
