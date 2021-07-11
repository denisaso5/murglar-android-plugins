package com.badmanners.build.publish

import org.gradle.api.GradleException
import org.gradle.api.Project


class BuildUtils {

    static File getReleaseApk(Project project) {
        File apk = null
        project.extensions.android.applicationVariants.all { variant ->
            if (variant.name == 'release') {
                variant.outputs.each { output ->
                    apk = output.outputFile
                }
            }
        }

        if (apk == null)
            throw new GradleException("Generated apk not found!")

        println 'Found generated apk: ' + apk

        return apk
    }
}