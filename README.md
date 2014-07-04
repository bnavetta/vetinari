[ ![Download](https://api.bintray.com/packages/ben-navetta/maven/vetinari/images/download.png) ](https://bintray.com/ben-navetta/maven/vetinari/_latestVersion)

# Vetinari

Vetinari is yet another static site generator. Unlike (most) others, it is implemented in Java and Groovy.
Currently, Vetinari has to be run through [Gradle](http://www.gradle.org) 2.0, but the core implementation is
isolated enough that it could be run through another build system or on its own.

In theory, Vetinari can be made to support any template system and any renderer.

See [`test-project`](https://github.com/roguePanda/vetinari/tree/master/test-project) for a simple setup, and
[`vetinari-integration`](https://github.com/roguePanda/vetinari/tree/master/test-project) for implementing renderers and template engines.

## Template Engines

* `no-op` - Does absolutely nothing and just returns the template source. This is fine for content pages but isn't useful for layouts.
* `groovyTemplate` - Uses Groovy's [SimpleTemplateEngine](http://beta.groovy-lang.org/docs/groovy-2.3.2/html/documentation/#_simpletemplateengine).
  It recognizes the `.gtpl` file extension.

## Renderers

* `no-op` - Does nothing, so it should be used when the input is already HTML.
* `markdown` - Render Markdown with the [Pegdown](https://github.com/sirthias/pegdown) parser. It recognizes the `.md` and `.markdown` file
  extensions. `markdown.extensions` can be set to a list of Pegdown extension names (lowercased, with underscores replaced by dashes).

## Extending

Currently, custom frontmatter parsers, template engines, renderers, and template functions can be added. More extension points
are planned, including the ability to modify the site before it is built. Extensions are added via Guice multibindings and map bindings.
