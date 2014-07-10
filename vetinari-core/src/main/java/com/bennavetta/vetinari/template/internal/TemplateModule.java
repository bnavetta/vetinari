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
package com.bennavetta.vetinari.template.internal;

import com.bennavetta.vetinari.template.TemplateEngine;
import com.bennavetta.vetinari.template.TemplateLoader;
import com.bennavetta.vetinari.template.internal.groovy.GroovyTemplateEngine;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Module providing template functionality.
 */
public class TemplateModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		Multibinder<TemplateEngine> templateEngineBinder =
				Multibinder.newSetBinder(binder(), TemplateEngine.class);
		templateEngineBinder.addBinding().to(NoOpTemplateEngine.class);
		templateEngineBinder.addBinding().to(GroovyTemplateEngine.class);

		bind(TemplateLoader.class);
	}
}
