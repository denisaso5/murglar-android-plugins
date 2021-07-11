package com.badmanners.build.publish

import com.badmanners.build.Index
import com.badmanners.build.Plugin
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.kohsuke.github.GitHubBuilder

import static com.badmanners.build.Index.PluginEntry.fromPluginAndUrl
import static com.badmanners.build.publish.BuildUtils.getReleaseApk
import static com.badmanners.build.publish.Git.commitAndTagAndPush


@CompileStatic
class PublishPluginTask extends DefaultTask {

    @Input
    Plugin plugin

    @Input
    String githubRepo

    @Input
    String githubToken

    @InputFile
    File indexFile


    @TaskAction
    void publishPlugin() throws IOException {
        def index = Index.fromFile(indexFile)

        def entry = index.getById(plugin.id)
        if (entry != null && entry.version >= plugin.version && entry.libVersion >= plugin.libVersion)
            throw new GradleException("Plugin '$plugin.id' with version '$plugin.fullVersion' or higher is already in the index!")

        String apkUrl = "https://github.com/$githubRepo/releases/download/$plugin.gitTag/$plugin.apkName"

        index.addOrUpdate(fromPluginAndUrl(plugin, apkUrl))
        index.save(indexFile)


        commitAndTagAndPush(project, plugin, githubToken)


        def github = new GitHubBuilder()
                .withOAuthToken(githubToken)
                .build()

        def repository = github.getRepository(githubRepo)

        def release = repository.createRelease(plugin.getGitTag())
                .body("New ${plugin.getName()} plugin version: ${plugin.getFullVersion()}")
                .create()

        File apk = getReleaseApk(project)
        def asset = release.uploadAsset(apk, "'application/vnd.android.package-archive")
        println "Apk uploaded: $asset.browserDownloadUrl"
    }
}
