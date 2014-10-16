package configuration.bpmn

import betsy.bpmn.model.BPMNProcess
import betsy.bpmn.model.BPMNTestCase

class BPMNTaskProcesses {
    static BPMNProcessBuilder builder = new BPMNProcessBuilder()

    public static final BPMNProcess SEQUENCE_FLOW = builder.buildTaskProcess(
            "SequenceFlow", "A process with two scriptTasks connected by a sequenceFlow",
            [
                    new BPMNTestCase().assertTask1()
            ]
    )

    public static final BPMNProcess SEQUENCE_FLOW_CONDITIONAL = builder.buildTaskProcess(
            "SequenceFlowConditional", "A process with three scriptTasks connected by sequenceFlows. " +
            "The first scriptTask points to the other task with sequenceFlows. " +
            "One of these sequenceFlows is associated with a conditionExpression",
            [
                    new BPMNTestCase(1).inputA().assertSuccess().assertCondition(),
                    new BPMNTestCase(2).inputB().assertSuccess()
            ]
    )

    public static final BPMNProcess SEQUENCE_FLOW_CONDITIONAL_DEFAULT = builder.buildTaskProcess(
            "SequenceFlowConditionalDefault", "A process with three scriptTasks connected by sequenceFlows. " +
            "The first scriptTask points to the other task with sequenceFlows. " +
            "One of these sequenceFlows is associated with a conditionExpression, the other one is marked as default",
            [
                    new BPMNTestCase(1).inputA().assertCondition(),
                    new BPMNTestCase(2).inputB().assertSuccess()
            ]
    )

    public static final BPMNProcess MULTI_INSTANCE_SEQUENTIAL = builder.buildTaskProcess(
            "MultiInstanceSequentialTask", "A scriptTask that is marked as a sequential multiInstance task and is enabled three times",
            [
                    new BPMNTestCase().assertInstanceExecution().assertInstanceExecution().assertInstanceExecution().assertSuccess()
            ]
    )

    public static final BPMNProcess MULTI_INSTANCE_SEQUENTIAL_NONE = builder.buildTaskProcess(
            "MultiInstanceSequentialTaskNoneBehavior", "A scriptTask that is marked as a sequential multiInstance task and is enabled three times and its behavior set to none",
            [
                    new BPMNTestCase().assertInstanceExecution().assertInstanceExecution().assertInstanceExecution().assertSignaled().assertSignaled().assertSignaled()
            ]
    )

    public static final BPMNProcess MULTI_INSTANCE_SEQUENTIAL_ONE = builder.buildTaskProcess(
            "MultiInstanceSequentialTaskOneBehavior", "A scriptTask that is marked as a sequential multiInstance task and is enabled three times and its behavior set to one",
            [
                    new BPMNTestCase().assertInstanceExecution().assertInstanceExecution().assertInstanceExecution().assertSignaled()
            ]
    )

    public static final BPMNProcess MULTI_INSTANCE_PARALLEL = builder.buildTaskProcess(
            "MultiInstanceParallelTask", "A scriptTask that is marked as a parallel multiInstance task and is enabled three times",
            [
                    new BPMNTestCase().assertInstanceExecution().assertInstanceExecution().assertInstanceExecution().assertSuccess()
            ]
    )

    public static final BPMNProcess LOOP_WITH_LOOP_MAXIMUM = builder.buildTaskProcess(
            "LoopTaskWithLoopMaximum", "A scriptTask with standardLoopCharacteristics and a condition that always evaluates to true. Additionally a loopMaximum is set to three.",
            [
                    new BPMNTestCase().assertTask1().assertTask1().assertTask1().assertTask2()
            ]
    )

    public static final BPMNProcess LOOP_NO_ITERATION = builder.buildTaskProcess(
            "LoopTaskWithLoopMaximum", "A scriptTask with standardLoopCharacteristics and a condition that always evaluates to false. Hence, the task should never be executed.",
            [
                    new BPMNTestCase().assertTask2()
            ]
    )

    public static final List<BPMNProcess> TASKS = [
            SEQUENCE_FLOW,
            SEQUENCE_FLOW_CONDITIONAL,
            SEQUENCE_FLOW_CONDITIONAL_DEFAULT,
            MULTI_INSTANCE_SEQUENTIAL,
            MULTI_INSTANCE_SEQUENTIAL_NONE,
            MULTI_INSTANCE_SEQUENTIAL_ONE,
            MULTI_INSTANCE_PARALLEL,
            LOOP_WITH_LOOP_MAXIMUM,
            LOOP_NO_ITERATION
    ].flatten() as List<BPMNProcess>
}
