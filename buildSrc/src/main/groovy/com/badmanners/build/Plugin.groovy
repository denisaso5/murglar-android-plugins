package com.badmanners.build

import groovy.transform.CompileStatic
import groovy.transform.Immutable


@Immutable
@CompileStatic
class Plugin {
    String id
    String name
    String murglarClass
    int version
    int libVersion

    String appId = "com.badmanners.murglar.plugin.$id"
    String fullName = "Murglar plugin for $name"
    String fullVersion = "$libVersion.$version"
    String gitTag = "${id}_v$fullVersion"
}