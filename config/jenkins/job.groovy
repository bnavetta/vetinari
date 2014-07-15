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
    name 'Vetinari-check'

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
        archiveJunit '*/build/integTest-results/*.xml'

        publishHtml {
            report 'build/reports/tests', 'Test Results'
            report 'build/reports/jacoco/html', 'Code Coverage'
        }

        jacocoCodeCoverage {
            execPattern 'build/jacoco/merged.exec'
        }

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
}
