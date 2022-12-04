## 4.3 Compound Extensions

The purpose of this project is to demonstrate how to setup an extension to contain other extensions.

 

#### build.gradle

```properties
version = '1.0.0'
group = 'com.blogspot.jvalentino.gradle'
archivesBaseName = 'ext-demo-comp'
```

#### src/main/groovy/com/blogspot/jvalentino/HelloExtension.groovy

```groovy
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

```

**Line 10: The project**

For the extension class to be able to contain another extension, it must be able to make use of the **Project** instance in a later to be defined method. This requires that the instance be storied as a member variable in this extension.

 

**Line 14: The child extension instance**

To be able to access the child extension relative to its parent, it must be provided as a member variable. It is recommended to instantiate it inline, so that if the using **build.gradle** does not define it, it will be provided with default values.

 

**Lines 16-18: The constructor**

To get the **Project** instance populated, it must now be passed via the constructor. Note that this will change the way the extension is created in the plugin class.

 

**Line 20: Confusing method name**

The purpose of this annotation is to suppress the Codenarc violation about the method name being the same as the member variable. The reason for this naming is keep the name of the child extension the same between the class and the implementation in the applying **build.gradle**, specifically “bye”.

 

**Line 21: The method**

The method must be of the return type of the child extension and take a Groovy closure instance as a parameter.

 

**Lines 22-23: The object generator**

The project member variable uses the configure method to associate the child extension class instance with the closure representation.

 

**Lines 24-25: The child extension member variable**

The member variable must be assigned the instance from the object generator, and the generator must be returned as the result of this method.

 

#### src/main/groovy/com/blogspot/jvalentino/ByeExtension.groovy

```groovy
package com.blogspot.jvalentino.gradle

/**
 * <p>A child extension</p>
 * @author jvalentino2
 */
class ByeExtension {
    int charlie = 5
    int delta = 6
}

```

The child extension class does not require any special handling.

 

#### src/main/groovy/com/blogspot/jvalentino/ExtDemoPlugin.groovy

```groovy
class ExtDemoPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create 'hello',
                HelloExtension, project
        project.task('add', type:AddTask)
    }
}
```

**Lines 12-13: An additional parameter**

The method used to create extension must now include the **Project** instance. This results in the constructor for the **HelloExtension** being called with the project parameter.

 

#### src/main/groovy/com/blogspot/jvalentino/AddTask.groovy

```groovy
    @TaskAction
    void perform() {
        HelloExtension ex =
                project.extensions.findByType(HelloExtension)
        ex.sum = ex.alpha + ex.bravo + ex.bye.charlie + ex.bye.delta
        println "${ex.alpha} + ${ex.bravo} + " +
                "${ex.bye.charlie} + ${ex.bye.delta} = ${ex.sum}"
    }

```

The **AddTask** was changed to add the numbers form both extensions, parent and child, to sum member variable. Notice that no special handling is required to get to the **ByeExtension** instance via the **HelloExtension** instance.

 

#### plugin-tests/local/build.gradle

```groovy
apply plugin: 'ext-demo'

hello {
    alpha = 5
    bravo = 6
    bye {
        charlie = 10
        delta = 11
    }
}
```

The “hello” extension is now able to contain the “bye” extension, where its values can be set.

 

#### Manual Testing

```bash
plugin-tests/local$ gradlew add

> Task :add 
5 + 6 + 10 + 11 = 32


BUILD SUCCESSFUL

```

Running the task in the manual testing project shows all the values from the build.gradle’s extensions being summed.



