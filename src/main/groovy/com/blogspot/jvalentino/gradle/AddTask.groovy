package com.blogspot.jvalentino.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * <p>A Task.</p>
 * @author jvalentino2
 */
@SuppressWarnings(['Println'])
class AddTask extends DefaultTask {

    String group = 'demo'
    String description = 'Adds some numbers together'

    @TaskAction
    void perform() {
        HelloExtension ex =
                project.extensions.findByType(HelloExtension)
        ex.sum = ex.alpha + ex.bravo + ex.bye.charlie + ex.bye.delta
        println "${ex.alpha} + ${ex.bravo} + " +
                "${ex.bye.charlie} + ${ex.bye.delta} = ${ex.sum}"
    }
}
