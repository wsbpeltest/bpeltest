# betsy (BPEL Engine Test System) [![Build Status](https://travis-ci.org/uniba-dsg/betsy.png?branch=master)](https://travis-ci.org/uniba-dsg/betsy)

Betsy is a tool to check the degree of conformance of a BPEL engine against the BPEL standard.

This software is licensed under the LGPL Version 3 Open Source License!

## Releases

- [Release December 2013 (latest)](https://github.com/uniba-dsg/betsy/archive/icsoc-2013.zip)
- [Release December 2012](https://github.com/uniba-dsg/betsy/archive/soca-2012.zip)
- [Release July 2012](https://github.com/uniba-dsg/betsy/archive/techrep-july-2012.zip)

## Software Requirements
- Windows 7
- JDK 1.7.0_03 (64 Bit) or higher
  - `JAVA_HOME` should point to the jdk directory
  - `PATH` should include `JAVA_HOME/bin`

## Licensing
LGPL Version 3: http://www.gnu.org/licenses/lgpl-3.0.html

## Usage for BPEL engine testing

Requirements (see above) have to be fulfilled to execute `betsy` on the command line.

See `Config.groovy` for more detailed configuration options.

```
usage: betsy [options] <engines> <processes>

Options:
 -p,--partner-address <ip-and-port>    Partner IP and Port (defaults to
                                       141.13.4.93:2000)
 -t,--to-core-bpel <transformations>   Transform to Core BPEL
 -o,--open-results-in-browser          Opens results in default browser
 -c,--check-deployment                 Verifies deployment instead of test
                                       success
 -b,--build-only                       Builds only the artifacts. Does
                                       nothing else.
 -e,--use-external-partner-service     Use external partner service instead of internal one
 -h,--help                             Print out usage information

GROUPS for <engines> and <processes> are in CAPITAL LETTERS.
<engines>:
LOCALS (install and execute all engines locally),
RECENT (install and execute all engines, in their most recent versions only, locally)
VMS (install and execute all engines in virtual machines),
ALL (install and execute all engines, in all versions supported, locally and in virtual machines),
ode, bpelg, openesb, petalsesb, orchestra,
active-bpel, openesb23, petalsesb41, ode_v, bpelg_v, openesb_v,
petalsesb_v, orchestra_v, active_bpel_v

<processes>: ALL, BASIC_ACTIVITIES_WAIT, BASIC_ACTIVITIES_THROW,
BASIC_ACTIVITIES_RECEIVE, BASIC_ACTIVITIES_INVOKE,
BASIC_ACTIVITIES_ASSIGN, BASIC_ACTIVITIES, SCOPES_EVENT_HANDLERS,
SCOPES_FAULT_HANDLERS, SCOPES, STRUCTURED_ACTIVITIES_FLOW,
STRUCTURED_ACTIVITIES_IF, STRUCTURED_ACTIVITIES_FOR_EACH,
STRUCTURED_ACTIVITIES_PICK, STRUCTURED_ACTIVITIES, CONTROL_FLOW_PATTERNS,
STATIC_ANALYSIS, FAULTS, WITH_EXIT_ASSERTION

# Examples
$ betsy # Running all tests for all engines
$ betsy ode # Running all tests for Apache ODE
$ betsy ode,bpelg # Running all tests for Apache ODE and bpel-g
$ betsy ALL Sequence # Running Sequence test for all engines
$ betsy ALL Sequence,While # Running Sequence and While test for all engines
$ betsy ode Sequence # Running Sequence test for Apache ODE
$ betsy ode Invoke-Catch # Running Invoke-Catch test for Apache ODE
$ betsy -t sequence.xsl,pick.xsl ode_v # Running all tests for the virtualised Apache ODE with sequence.xsl and pick.xsl CoreBPEL transformations
$ betsy -o # Opens the results in the default browser after a successful run
```

## Usage for BPMN engine testing

Requirements (see above) have to be fulfilled to execute `betsy-bpmn` on the command line.

See `Config.groovy` for more detailed configuration options.

```
usage: betsy-bpmn [options] <engines> <processes>

Options:
 -o,--open-results-in-browser          Opens results in default browser
 -b,--build-only                       Builds only the artifacts. Does
                                       nothing else.
 -h,--help                             Print out usage information

GROUPS for <engines> and <processes> are in CAPITAL LETTERS.
<engines>:
ALL (install and execute all engines),
camunda, jbpm

<processes>: ALL, GATEWAYS, TASKS, EVENTS, SUBPROCESSES, MISCS

# Examples
$ betsy-bpmn # Running all tests for all engines
$ betsy-bpmn camunda # Running all tests for Camunda
$ betsy-bpmn ALL SequenceFlow # Running SequenceFlow test ( = basic test) for all engines
$ betsy-bpmn ALL SequenceFlow,ExclusiveGateway # Running SequenceFlow and ExclusiveGateway test for all engines
$ betsy-bpmn camunda SequenceFlow # Running SequenceFlow test for Camunda
$ betsy-bpmn -o # Opens the results in the default browser after a successful run
```

## Administrative gradlew tasks
```
$ gradlew idea # Generating Intellij IDEA project files
$ gradlew eclipse # Generating Eclipse project files
$ gradlew groovydoc # Generating GroovyDoc
$ gradlew enginecontrol # Opens a Swing GUI that allows to install, start and stop supported engines
```

## Downloads

From public subversion directory https://lspi.wiai.uni-bamberg.de/svn/betsy/

## Project Structure

    downloads/ # downloads of the engines
    server/ # engine installation directory
    test/ # execution results and reports
    src/main/tests/ # the bpel, wsdl, xsd files and test configuration
    src/main/tests/files/bpmnRes # the bpmn and bpmn engine specific files
    src/main/xslt/[engine/] # common and engine specific xslt scripts
    src/main/resources/[engine/] # bpel common and engine specific xsds and other resources
    src/main/groovy # the main source code
    src/main/java # mock web service implementation

## Test Structure for BPEL Engines

	test/
	test/reports/
    test/reports/html/ # html junit reports
	test/$engine/
	test/$engine/$process/
	test/$engine/$process/bpel/ # bpel file(s), wsdl file(s), xsd file(s)
	test/$engine/$process/pgk/ # deployable zip files
	test/$engine/$process/soapui/ # soapUI test suite
	test/$engine/$process/reports/ # soapUI test reports

    Optional directories
	[test/$engine/$process/binding/ # binding package]
	[test/$engine/$process/composite/ # composite package]

## Test Structure for BPMN Engines

	test/
	test/reports/
    test/reports/html/ # html junit reports
    test/reports/imgs/ # process images
	test/$engine/
	test/$engine/$process/
	test/$engine/$process/war|project/ # the deployable war (camunda) or maven project (jbpm) files
	test/$engine/$process/test*/ # the junit test bin and source files
	test/$engine/$process/logs/ # server and engine log files and test case log files
	test/$engine/$process/reports/ # junit test reports for each test case and a combined one

# Authors (in alphabetical order)

Mathias Casar, [Simon Harrer](http://www.uni-bamberg.de/pi/team/harrer/), [Joerg Lenhard](http://www.uni-bamberg.de/pi/team/lenhard-joerg/), Christian Preißinger, Cedric Röck and Andreas Vorndran

# Publications
The following scientific publications are either about betsy, have used betsy to present benchmarks or use and build upon data obtained through betsy:
 - Harrer, S., Lenhard, J.: [Betsy - A BPEL Engine Test System](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/12-a1-harrer-lenhard/), Bamberger Beiträge zur Wirtschaftsinformatik und Angewandten Informatik Nr. 90, Bamberg University, July 2012. ISSN 0937-3349, this is betsy's original architectural white paper
 - Harrer, S., Lenhard, J., Wirtz, G.: [BPEL Conformance in Open Source Engines](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/12-02-lenhard-wirtz-harrer/), Proceedings of the 5th IEEE International Conference on Service-Oriented Computing and Applications (SOCA'12), Taipei, Taiwan, December 17-19, 2012, see also the [presentation](https://lspi.wiai.uni-bamberg.de/svn/betsy/betsy-presentation-soca-2012.pdf) for which these [test results](https://svn.lspi.wiai.uni-bamberg.de/svn/betsy/test-results-soca-2012.zip) have been used.
 - Lenhard, J., Wirtz, G.: [Detecting Portability Issues in Model-Driven BPEL Mappings](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/13-03-lenhard-wirtz/), Proceedings of the 25th International Conference on Software Engineering and Knowledge Engineering (SEKE'2013), Boston, Massachusetts, USA, Knowledge Systems Institute, June 27 - 29, 2013
 - Lenhard, J., Wirtz, G.: [Measuring the Portability of Executable Service-Oriented Processes](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/13-05-lenhard-wirtz/), Proceedings of the 17th IEEE International EDOC Conference, Vancouver, Canada, September 9 - 13, 2013, Awarded Best Student Conference Paper in Service Science
 - Harrer, S., Lenhard, J., Wirtz, G.: [Open Source versus Proprietary Software in Service-Orientation: The Case of BPEL Engines](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/13-07-harrer-lenhard-wirtz/), Proceedings of the 11th International Conference on Service Oriented Computing (ICSOC '13), Berlin, Germany, December 2 - 5, 2013
 - Lenhard, J. Harrer, S., Wirtz, G.: [Measuring the Installability of Service Orchestrations Using the SQuaRE Method](http://www.uni-bamberg.de/pi/bereich/forschung/publikationen/13-08-harrer-lenhard-wirtz/), Proceedings of the 6th IEEE International Conference on Service-Oriented Computing and Applications (SOCA'13), Kauai, Hawaii, USA, December 16 - 18, 2013, Awarded Best Conference Paper

# Contribution Guide
- Fork
- Send Pull Request
