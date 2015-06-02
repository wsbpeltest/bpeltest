package betsy.bpel;

import betsy.bpel.engines.AbstractBPELEngine;
import betsy.bpel.model.BPELProcess;
import betsy.bpel.model.BPELTestSuite;
import betsy.bpel.validation.BPELValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BPELBetsy {
    private List<AbstractBPELEngine> engines = new ArrayList<>();
    private List<BPELProcess> processes = new ArrayList<>();
    private BPELComposite composite = new BPELComposite();

    public void execute() {
        validate();

        Collections.sort(processes);

        BPELTestSuite testSuite = BPELTestSuite.createTests(engines, processes);

        composite.setTestSuite(testSuite);
        composite.execute();
    }

    private void validate() {
        new BPELValidator(processes).validate();
    }

    public List<AbstractBPELEngine> getEngines() {
        return engines;
    }

    public void setEngines(List<AbstractBPELEngine> engines) {
        this.engines = engines;
    }

    public List<BPELProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<BPELProcess> processes) {
        this.processes = processes;
    }

    public BPELComposite getComposite() {
        return composite;
    }

    public void setComposite(BPELComposite composite) {
        this.composite = composite;
    }
}
