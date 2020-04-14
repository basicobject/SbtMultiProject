# Setting up a multi build sbt project

Setting up a scala project can be done in many ways, the easiest way would be to create a project using IntelliJ IDEA,
the super cool IDE will guide you on setting up the project and it will set up all the sensible defaults.

Another way is to use a template Giter8 template https://github.com/foundweekends/giter8. You can create different
templates one for your organization so that all the developers within the organization can follow some convention to
create uniform code structuring. This also reduces double efforts in duplicating certain configurations and used by
each organization such a deployment automation config, database connection, testing framework etc. The developer can
simply create a project using such templates and directly start working on business logic.

But if you have to set up one manually or want to understand how this works it would be an interesting thing to
understand how it is done. Fundamentally in order to set up a scala project using SBT all you need is to create a
project directory and touch a build.sbt file within that directory. By doing this the directory will become an SBT
project. This is the bare minimum requirement to create an SBT project. Once you have done that you can navigate to the
directory and you can run the command `sbt` and SBT will generate the whole project structure for you.

Essentially the following structure will be generated.

    + SbtMultiProject/
      - build.sbt
      + project/
        - build.properties
        + target/
      + target/

You can ignore the target directory for now. The build.properties file will contain the SBT version as follows

    sbt.version=1.3.8

Now the question is where do we write our code? All you need to do is to create the following default directory
structure to organize your code on a top-level. In a project directory, src directory will contain the source files. The
src directory is organized into main and test, where the main will have our code and test will keep our tests. The main and
test are further divided into java and scala directories to keep the java and scala files separate in a project. Within
scala and java directory you can organize your code as per the design philosophy. Some prefer the code to be organized in
to features. Some prefer it in a layered way. Organizing code is a topic for a big controversial blog post.

    + src/
      + main/
        + scala/
          + your package directory/
            ...
          - your scala file
          ...
        + java/
          + your package directory/
            ...
          - your java files
          ...
        + resources/
          + your resource dir/
            ...
          + your resource file
          ...
      + test
        + scala
          ...
        + java
          ...

Ok, But where do we put the src directory? it can be put right in the project directory. General practice is to
create a multi-project set up with a root project that contains no code. The rest of the projects will be created
under this root project. If your codebase gets bigger it is a good idea to organize your code into a multi-project
setup. You can create each of the services as a small project and deploy it independently of other parts. You don't
have to touch other parts of the code as well but still, you can keep the entire codebase of your project in a single
directory organized into sub-projects.

The root project will act as an aggregator of sub-projects so that you can compile and build all the subprojects
in a single command. You can choose to compile and run each of the sub-projects independently so that to save time and
can work in isolation.

In our example, we have a root project named SbtMultiProject and sub-projects are Common, ServiceA and ServiceB.
Here ServiceA and ServiceB are two independent services of the bigger project and both of them depend on a common
project. Basically common will have the code that can be reused in other sub-projects such as ServiceA or ServiceB.

You can reuse the code even outside the project by packaging the sub-projects and importing it.

The build.sbt can have some information such as scalaVersion, organization, version, etc. If you take a closer look at
the build file you can see a project is defined as follows


        lazy val common = project in file("Common")

This means a project common can be found in directory Common. SBT allows you to define if a project depends on another

        lazy val serviceA = (project in file("ServiceA")) dependsOn common

One you create dependency like this you will be able to import classes from the common project to the ServiceA project.
The root project here doesn't have a src directory of its own. It is acting as an aggregator for the sub-projects Common,
ServiceA and ServiceB. One you run the sbt command on the root project the commands will be broadcast to each
sub-project.

lazy val root = (project in file(".")).aggregate(common, serviceA, serviceB)


PS:

SBT can use the scalas reflection library to figure out the project directory names. If your sub-project directory has the
name says core you can define the projects as follows.

        lazy val core = project

You need not specify the directory name, scala will figure it out from the value name.
