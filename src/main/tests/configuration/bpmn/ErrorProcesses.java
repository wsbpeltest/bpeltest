package configuration.bpmn;

import betsy.bpmn.model.BPMNProcess;
import betsy.bpmn.model.BPMNTestCase;

import java.util.Arrays;
import java.util.List;

/**
 * This class bundles processes that are somewhat problematic, but engines should behave correctly by ignoring certain attributes or similar behavior
 */
class ErrorProcesses {

    public static final BPMNProcess PARALLEL_GATEWAY_WITH_CONDITIONS = BPMNProcessBuilder.buildErrorProcess(
            "ParallelGateway_Conditions", "A process with four scriptTasks and two parallelGateways. " +
                    "Two of the scriptTasks are surrounded by the parallelGateways and the sequenceFlows pointing to the mergine gateway have conditions. " +
                    "These conditions should be ignored by an engine.",
            new BPMNTestCase().inputA().assertTask1().assertTask2().assertTask3(),
            new BPMNTestCase().inputB().assertTask1().assertTask2().assertTask3(),
            new BPMNTestCase().inputAB().assertTask1().assertTask2().assertTask3(),
            new BPMNTestCase().inputC().assertTask1().assertTask2().assertTask3()
    );

    public static final BPMNProcess EXCLUSIVE_DIVERGING_PARALLEL_CONVERGING = BPMNProcessBuilder.buildErrorProcess(
            "ExclusiveDiverging_ParallelConverging", "A process with four scriptTasks, a diverging exclusiveGateway and a converging parallelGateway. " +
            "Two scriptTasks are enclosed by the gateways and the execution should deadlock, because only one incoming branch of the parallelGateway " +
            "should ever be executed. Hence, the scriptTask following the parallelGateway should never be executed.",
            new BPMNTestCase().inputA().assertTask1(),
            new BPMNTestCase().inputB().assertTask2(),
            new BPMNTestCase().inputAB().assertTask1()
    );

    public static final BPMNProcess INCLUSIVE_DIVERGING_PARALLEL_CONVERGING = BPMNProcessBuilder.buildErrorProcess(
            "InclusiveDiverging_ParallelConverging", "A process with four scriptTasks, a diverging inclusiveGateway and a converging parallelGateway. " +
            "Two scriptTasks are enclosed by the gateways and the execution should deadlock if only one incoming branch of the parallelGateway " +
            "is enabled. Hence, the scriptTask following the parallelGateway should only be executed in a single case.",
            new BPMNTestCase().inputA().assertTask1(),
            new BPMNTestCase().inputB().assertTask2(),
            new BPMNTestCase().inputAB().assertTask1().assertTask2().assertTask3()
    );


    public static final List<BPMNProcess> ERRORS = Arrays.asList(
            PARALLEL_GATEWAY_WITH_CONDITIONS,

            EXCLUSIVE_DIVERGING_PARALLEL_CONVERGING,
            INCLUSIVE_DIVERGING_PARALLEL_CONVERGING
    );
}