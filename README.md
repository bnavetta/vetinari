[![Build Status](https://travis-ci.org/roguePanda/vetinari.svg)](https://travis-ci.org/roguePanda/vetinari)
[![Coverage Status](https://img.shields.io/coveralls/roguePanda/vetinari.svg)](https://coveralls.io/r/roguePanda/vetinari?branch=master)
[![Download](https://api.bintray.com/packages/ben-navetta/maven/vetinari/images/download.png) ](https://bintray.com/ben-navetta/maven/vetinari/_latestVersion)

# Vetinari

Vetinari is yet another static site generator. It can be made to support any template system and any renderer.

See [`test-project`](https://github.com/roguePanda/vetinari/tree/master/test-project) for a simple setup, and
[`vetinari-integration`](https://github.com/roguePanda/vetinari/tree/master/test-project) for implementing renderers and template engines.

## Template Engines

### `noOp`

This does absolutely nothing and just returns the template source. It can be used for content pages, but not layouts.

### `groovyTemplate`

This uses standard Groovy templating as defined by [SimpleTemplateEngine](http://beta.groovy-lang.org/docs/groovy-2.3.2/html/documentation/#_simpletemplateengine).
It recognizes the `gtpl` file extension and is the default template engine.

## Renderers

### `noOp`

This does no rendering, so it can be used when the input is already HTML.

### `markdown`

It renders Markdown using the [Pegdown](https://github.com/sirthias/pegdown) parser. It recognizes the `md` and `markdown` file extensions.
The `markdown.extensions` site configuration property can be set to a list of Pegdown extensions. The extension names are lowercased
and dashes are used instead of underscores.

## Extending

Currently, custom template engines, renderers, and build phases can be added.
Extensions are added via Guice multibindings.

## Gradle

The `vetinari-gradle` plugin integrates Vetinari with Gradle. It uses the command-line interface for classpath isolation.

```groovy
buildscript {
	repositories {
		jcenter()
	}

	dependencies {
		classpath "com.bennavetta.vetinari:vetinari-gradle:<version>"
	}
}

apply plugin: 'com.bennavetta.vetinari'

repositories {
	jcenter()
}

dependencies {
	vetinari "com.bennavetta.vetinari:vetinari-integration:$vetinari.vetinariVersion"
}

vetinari {
	sites {
		main
	}
}
```

This will create a `vetinari` configuration to which extension dependencies can be added (the plugin only adds `vetinari-cli`). It also adds a `buildMainSite` task.
The `vetinari` extension has a `vetinariVersion` property which defaults to the plugin version and can be used to download different `vetinari-cli` versions.
Each site has the following properties:

Property          | Default
------------------|---------------------
`contentEncoding` | UTF-8
`contentRoot`     | src/$name/content
`templateRoot`    | src/$name/templates
`outputRoot`      | build/sites/$name
`siteConfig`      | src/$name/site.conf

## Command-Line

The `vetinari-cli` command line can be used as follows:

```shellsession
vetinari build --content-encoding UTF-8 --content-root content --template-root templates --output-root dist --site-config mysite.conf
```
The `--verbose` and `--debug` flags can be used for more output.

## Roadmap / TODO

* Rename `vetinari-integration` to `vetinari-impl` and move **all** implementations of core interfaces there. It should also be a dependency of `vetinari-cli`.
* Add a `BuildPhase` implementation for phases that just process each page individually
* Use more Nebula plugins instead of custom logic, i.e. for optional dependencies and Bintray publishing
