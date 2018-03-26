<a href="https://blog.csdn.net/liangyihuai"><img src="https://travis-ci.org/corsoft/esper-demo-nuclear.svg"/></a>

Esper4SpingJMS
==================

<img src="http://www.adrianmilne.com/wp-content/uploads/2013/02/feature-image-template-esper-cep.png"/>

This is simple demonstration on message processing using `Esper`, `Sping` and `JMS`(java message service). 


Simple project that illustrates the use of the Esper Complex Event Processing Engine. Purposefully left Unit Tests out to reduce size of code.

When the demo runs it will just simulate sending random temperature events through the processing engine. It will print debug messages to the console when it detects a sequence of events matching any of the 3 criteria statements we have defined (Critical, Warning, Monitor). 

To generate input data, JMS(ActivMQ) is used as a remote event provider.


requirements
============

You will need Maven installed and working.

It is suggested to using the IDE of `Intelij IDEA` to run the project quickly. 

setup
=====

To run demo:

- method 1: 

1. Open a terminal window

2. Navigate to the root directory of the project (where the pom.xml is)

3. 'mvn clean install' (this will compile and build the project)

4. 'mvn exec:java' (this will start running the demo - sending random temperature events)

- method 2:

1. Use Git URL.	
copy `https://github.com/liangyihuai/Esper4SpingJMS.git`

2. build a IDEA project by using above URL.
