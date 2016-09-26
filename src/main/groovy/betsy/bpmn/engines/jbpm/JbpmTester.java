package betsy.bpmn.engines.jbpm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import betsy.bpmn.engines.BPMNEnginesUtil;
import betsy.bpmn.engines.BPMNProcessInstanceOutcomeChecker;
import betsy.bpmn.engines.BPMNTester;
import betsy.bpmn.model.BPMNAssertions;
import betsy.bpmn.model.BPMNProcess;
import betsy.common.tasks.FileTasks;
import betsy.common.tasks.WaitTasks;
import org.apache.log4j.Logger;
import pebl.test.TestCase;
import pebl.test.TestStep;
import pebl.test.assertions.Trace;
import pebl.test.assertions.TraceTestAssertion;
import pebl.test.steps.DelayTestStep;
import pebl.test.steps.DeployableCheckTestStep;
import pebl.test.steps.GatherTracesTestStep;
import pebl.test.steps.vars.ProcessStartWithVariablesTestStep;
import pebl.test.steps.vars.Variable;

public class JbpmTester {

    private final BPMNProcess bpmnProcess;
    private final TestCase testCase;
    private final BPMNTester bpmnTester;
    private final JbpmApiBasedProcessInstanceOutcomeChecker processInstanceOutcomeChecker;
    private final Path logFile;
    private final Path serverLogFile;

    private static final Logger LOGGER = Logger.getLogger(JbpmTester.class);

    public JbpmTester(BPMNProcess bpmnProcess, TestCase testCase, BPMNTester bpmnTester,
                      JbpmApiBasedProcessInstanceOutcomeChecker processInstanceOutcomeChecker,
                      Path logFile, Path serverLogFile) {
        this.bpmnProcess = bpmnProcess;
        this.testCase = testCase;
        this.bpmnTester = bpmnTester;
        this.processInstanceOutcomeChecker = processInstanceOutcomeChecker;
        this.logFile = logFile;
        this.serverLogFile = serverLogFile;
    }

    /**
     * Runs a single test
     */
    public void runTest() {

        for (TestStep testStep : testCase.getTestSteps()) {
            if (testStep instanceof DeployableCheckTestStep) {
                BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome outcomeBeforeTest =
                        processInstanceOutcomeChecker.checkProcessOutcome(bpmnProcess.getName());
                if (outcomeBeforeTest == BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome.UNDEPLOYED_PROCESS) {
                    BPMNAssertions.appendToFile(logFile, BPMNAssertions.ERROR_DEPLOYMENT);
                }
            }
            if (testStep instanceof ProcessStartWithVariablesTestStep) {
                try {
                    ProcessStartWithVariablesTestStep processStartWithVariablesTestStep = (ProcessStartWithVariablesTestStep) testStep;
                    List<Variable> variables = processStartWithVariablesTestStep.getVariables();
                    new JbpmProcessStarter().start(processStartWithVariablesTestStep.getProcess(), variables);
                } catch (Exception e) {
                    LOGGER.info("Could not start process", e);
                    BPMNAssertions.appendToFile(logFile, BPMNAssertions.ERROR_RUNTIME);
                }
            } else if (testStep instanceof DelayTestStep) {
                WaitTasks.sleep(((DelayTestStep) testStep).getTimeToWaitAfterwards());
            } else if (testStep instanceof GatherTracesTestStep) {
                // Skip further processing if process is expected to be undeployed
                if(testStep.getAssertions().stream().filter(a -> a instanceof TraceTestAssertion && ((TraceTestAssertion) a).getTrace().equals(new Trace(BPMNAssertions.ERROR_DEPLOYMENT.toString())))
                        .findFirst().isPresent()) {
                    if(!Files.exists(logFile)) {
                        try {
                            Files.createFile(logFile);
                        } catch (IOException e) {
                            LOGGER.warn("Creation of log file failed.", e);
                        }
                    }
                    break;
                }

                // Check on parallel execution
                BPMNEnginesUtil.checkParallelExecution(testCase, logFile);

                // Check whether MARKER file exists
                BPMNEnginesUtil.checkMarkerFileExists(testCase, logFile);

                // Check data type
                BPMNEnginesUtil.checkDataLog(testCase, logFile);

                BPMNEnginesUtil.substituteSpecificErrorsForGenericError(testCase, logFile);

                BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome outcome = processInstanceOutcomeChecker.checkProcessOutcome(
                        bpmnProcess.getName());
                if (outcome == BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome.PROCESS_INSTANCE_ABORTED) {
                    BPMNAssertions.appendToFile(logFile, BPMNAssertions.ERROR_PROCESS_ABORTED);
                } else if (outcome == BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome.PROCESS_INSTANCE_ABORTED_BECAUSE_ERROR_EVENT_THROWN) {
                    BPMNAssertions.appendToFile(logFile, BPMNAssertions.ERROR_THROWN_ERROR_EVENT);
                } else if (outcome == BPMNProcessInstanceOutcomeChecker.ProcessInstanceOutcome.PROCESS_INSTANCE_ABORTED_BECAUSE_ESCALATION_EVENT_THROWN) {
                    BPMNAssertions.appendToFile(logFile, BPMNAssertions.ERROR_THROWN_ESCALATION_EVENT);
                }
            }
        }

        LOGGER.info("contents of log file " + logFile + ": " + FileTasks.readAllLines(logFile));

        bpmnTester.test();
    }

}
