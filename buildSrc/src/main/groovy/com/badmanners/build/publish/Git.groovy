package com.badmanners.build.publish

import com.badmanners.build.Plugin
import org.ajoberstar.grgit.Commit
import org.ajoberstar.grgit.Credentials
import org.ajoberstar.grgit.Grgit
import org.gradle.api.Project


class Git {

    static Commit commitAndTagAndPush(Project project, Plugin plugin, String githubToken) {
        println "Git commit, tag and push started..."

        def grgit = Grgit.open {
            dir = project.rootProject.projectDir.absolutePath
            credentials = new Credentials(githubToken)
        }

        def commit = grgit.commit {
            message = "New $plugin.name version: $plugin.fullVersion"
            all = true
        }
        println "Committed $commit.abbreviatedId"

        grgit.tag.add {
            name = plugin.gitTag
            annotate = false
        }
        println "Tag $plugin.gitTag added"

        grgit.push {
            refsOrSpecs = ["refs/heads/master:master"]
            tags = true
        }
        println "Pushed"

        return commit
    }
}