versions {
	// Logging
	log4j          = '2.+'
	slf4j          = '1.7.+'

	// Core
	guice          = '4.+'
	typesafeConfig = '1.+'

	// CLI
	jcommander     = '1.+'

	// Common
	guava          = '17.+'
	groovy         = '2.3.+'

	// Extensions
	pegdown        = '1.+'

	// Testing
	spock          = '0.7-groovy-2.0'
	jimfs          = '1.+'

	lombok         = '1.+'
	ant            = '1.9.+'
}

libs {
	// Logging
	log4j {
		api = "org.apache.logging.log4j:log4j-api:$versions.log4j"
		core = "org.apache.logging.log4j:log4j-core:$versions.log4j"
		slf4j = "org.apache.logging.log4j:log4j-slf4j-impl:$versions.log4j"
	}

	slf4j {
		api = "org.slf4j:slf4j-api:$versions.slf4j"
		simple = "org.slf4j:slf4j-simple:$versions.slf4j"
	}

	// Core
	guice = "com.google.inject:guice:$versions.guice"
	guiceMultibindings = "com.google.inject.extensions:guice-multibindings:$versions.guice"
	typesafeConfig = "com.typesafe:config:$versions.typesafeConfig"

	// CLI
	jcommander = "com.beust:jcommander:$versions.jcommander"

	// Common
	guava = "com.google.guava:guava:$versions.guava"
	groovy = "org.codehaus.groovy:groovy-all:$versions.groovy"

	// Extensions
	pegdown = "org.pegdown:pegdown:$versions.pegdown"

	// Testing
	spock = "org.spockframework:spock-core:$versions.spock"
	jimfs = "com.google.jimfs:jimfs:$versions.jimfs"

	lombok = "org.projectlombok:lombok:$versions.lombok"

	ant = "org.apache.ant:ant:$versions.ant"
}