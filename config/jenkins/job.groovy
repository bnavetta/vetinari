def gitUrl = 'git://github.com/roguePanda/vetinari.git'

job {
    name 'Vetinari-codeQuality'

    scm {
        git gitUrl
    }

    triggers {
        scm '0 */2 * * *'
    }

    steps {
        gradle 'sonarRunner'
    }

    publishers {
        publishHtml {
            report 'build/sonar/issues-report', 'Code Quality'
        }
    }
}

job {
    name 'Vetinari-test'

    scm {
        git gitUrl
    }

    triggers {
        //githubPush() // Can't use github hooks since the Jenkins server is local
        scm('*/15 * * * *')
    }

    steps {
        gradle('check')
    }

    publishers {
        archiveJunit '*/build/test-results/*.xml'

        publishHtml {
            report 'build/reports/tests', 'Test Results'
            report 'build/reports/jacoco/html', 'Code Coverage'
        }

        jacocoCodeCoverage {
            execPattern 'build/jacoco/merged.exec'
        }

        downstream('Vetinari-integTest', 'SUCCESS')
    }
}

job {
    name 'Vetinari-integTest'

    scm {
        git gitUrl
    }

    steps {
        gradle('integrationTest')
    }

    publishers {
        archiveJunit '*/build/integTest-results/*.xml'
        downstream('Vetinari-publish', 'SUCCESS')
    }
}

job {
    name 'Vetinari-publish'

    scm {
        git gitUrl
    }

    steps {
        gradle('publish')
        gradle('artifactoryPublish')
    }

    publishers {
        archiveArtifacts {
            pattern 'distMaven/**/*'
            latestOnly true
        }
    }

    configure { project ->
        project / buildWrappers / 'org.jenkinsci.plugins.credentialsbinding.impl.SecretBuildWrapper'(plugin: 'credentials-binding@1.0') << {

        }

        project / buildWrappers / 'org.jenkinsci.plugins.credentialsbinding.impl.SecretBuildWrapper' << bindings {
            'org.jenkinsci.plugins.credentialsbinding.impl.UsernamePasswordBinding' {
                variable 'JENKINS_BINTRAY_CREDENTIALS'
                credentialsId '96d03dbc-15ad-4289-a82d-19e43dcb9bcb'
            }
        }
    }
}
