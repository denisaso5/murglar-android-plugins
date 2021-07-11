package com.badmanners.build

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic
import groovy.transform.Immutable


@CompileStatic
class Index {

    @Immutable
    @CompileStatic
    static class PluginEntry {

        String id
        String appId
        String name
        String apkUrl
        int version
        int libVersion
        long timestamp

        static PluginEntry fromPluginAndUrl(Plugin plugin, String apkUrl) {
            return new PluginEntry(
                    plugin.id,
                    plugin.appId,
                    plugin.name,
                    apkUrl,
                    plugin.version,
                    plugin.libVersion,
                    System.currentTimeMillis()
            )
        }
    }

    static Index fromFile(File file) {
        return new Gson().fromJson(file.text, Index.class)
    }

    List<PluginEntry> plugins

    PluginEntry getById(String pluginId) {
        return plugins.find { it.id == pluginId }
    }

    def addOrUpdate(PluginEntry entry) {
        def index = plugins.findIndexOf { it.id == entry.id }
        if (index == -1)
            plugins.add(entry)
        else
            plugins[index] = entry
    }

    def save(File file) {
        plugins = plugins.sort { a, b -> a.id.compareToIgnoreCase(b.id) }

        def text = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this)
        file.text = text
    }
}