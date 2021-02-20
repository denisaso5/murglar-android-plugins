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
    String libVersion
}