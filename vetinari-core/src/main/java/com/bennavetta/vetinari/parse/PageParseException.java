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
package com.bennavetta.vetinari.parse;

import com.bennavetta.vetinari.VetinariException;

/**
 * A {@code PageParseException} is thrown when something goes wrong parsing a page.
 */
public class PageParseException extends VetinariException
{
	public PageParseException(String message)
	{
		super(message);
	}

	public PageParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PageParseException(Throwable cause)
	{
		super(cause);
	}
}
