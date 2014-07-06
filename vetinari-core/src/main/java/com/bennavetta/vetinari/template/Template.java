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

import java.util.Map;

/**
 * Abstraction for a template class.
 */
public interface Template
{
	/**
	 * Render this template in the given context. This usually involves things like variable interpolation,
	 * conditionals, and loops.
	 * @param variables the variables to make available to the template
	 * @return the result of template rendering
	 */
	public String render(Map<String, Object> variables);
}
