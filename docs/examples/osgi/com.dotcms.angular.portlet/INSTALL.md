
# README
----
This is an example of how to create and load an angular 2 webcomponent portlet that uses a
Jersey Based REST resource as an angular service in dotCMS via OSGi. You need to install 
nodejs in your environment in other to execute the npm commands


## How to build this example
----

To install all you need to do is build the JAR. To do this run from this directory:

`./gradlew jar`

or for windows

`.\gradlew.bat jar`

This will build a jar in the build/libs directory

### To install this bundle

Copy the bundle jar file inside the Felix OSGI container (dotCMS/felix/load).
        OR
Upload the bundle jar file using the dotCMS UI (CMS Admin->Dynamic Plugins->Upload Plugin).

### To uninstall this bundle:

Remove the bundle jar file from the Felix OSGI container (dotCMS/felix/load).
        OR
Undeploy the bundle using the dotCMS UI (CMS Admin->Dynamic Plugins->Undeploy).



## How to test
----

Once installed, you can access this resource by adding the angular portlet into a cms tab
and role. Once added, you can click on the portlet link
