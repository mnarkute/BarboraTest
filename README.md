# BarboraTest

This is an automation test project that covers https://www.barbora.lt/ functionality. 

Tests are written in Java 10, using Selenium, Jupiter (Junit5). All used dependencies are added to pom.xml file.

Tests methods and their implementation are separated for more clarity. Tests run on Google Chrome browser though any other could be used by changing one line.

Tests do not depend on each other, browser is started and closed for each test as the scope of them is relatively small and they're not time consuming.
