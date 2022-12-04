package com.blogspot.jvalentino.gradle

import org.gradle.api.Project

/**
 * <p>A basic extension</p>
 * @author jvalentino2
 */
class HelloExtension {
    Project project
    int alpha = 1
    int bravo = 2
    int sum = 0
    ByeExtension bye = new ByeExtension()

    HelloExtension(Project project) {
        this.project = project
    }

    @SuppressWarnings(['ConfusingMethodName'])
    ByeExtension bye(Closure closure) {
        Object generator = project.configure(
                new ByeExtension(), closure)
        this.bye = generator
        generator
    }
}
