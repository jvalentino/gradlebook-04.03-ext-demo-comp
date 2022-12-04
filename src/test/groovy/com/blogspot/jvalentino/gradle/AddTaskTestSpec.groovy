package com.blogspot.jvalentino.gradle

import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Subject

class AddTaskTestSpec extends Specification {

    @Subject
    AddTask task
    Project p
    HelloExtension extension
    
    def setup() {
        p = ProjectBuilder.builder().build()
        p.apply(ImmutableMap.of("plugin", ExtDemoPlugin.class))
        task = p.tasks['add']
        extension = p.extensions.findByType(HelloExtension)
    }
    
    void "Test peform (default)"() {
        when:
        task.perform()
        
        then:
        extension.sum == 14
    }
    
    void "Test peform with some values"() {
        given:
        extension.alpha = 3
        extension.bravo = 4
        extension.bye.charlie = 5
        extension.bye.delta = 6
        
        when:
        task.perform()
        
        then:
        extension.sum == 18
    }
}
