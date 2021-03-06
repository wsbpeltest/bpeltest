package betsy.bpmn;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import betsy.bpmn.engines.AbstractBPMNEngine;
import betsy.bpmn.engines.BPMNProcessStarter;
import betsy.bpmn.model.BPMNProcess;
import betsy.common.model.engine.EngineExtended;
import configuration.bpmn.BPMNProcessRepository;
import pebl.ProcessLanguage;
import pebl.benchmark.test.Test;

public class BPMNBetsyTests {
    @org.junit.Test
    public void simulateATestRun() throws Exception {
        AbstractBPMNEngine engine = new MockEngine();

        List<Test> processes = new BPMNProcessRepository().getByName("ALL");

        BPMNBetsy betsy = new BPMNBetsy();

        betsy.setEngines(Collections.singletonList(engine));
        betsy.setProcesses(processes);
        betsy.setTestFolder("test");
        betsy.execute();
    }

    public class MockEngine extends AbstractBPMNEngine {
        public void install() {
        }

        public void startup() {
        }

        public void shutdown() {
        }

        public boolean isRunning() {
            return false;
        }

        public void deploy(BPMNProcess process) {
        }

        public void storeLogs(BPMNProcess process) {
        }

        @Override
        public void deploy(String name, Path path) {

        }

        @Override public boolean isDeployed(QName process) {
            return false;
        }

        @Override public void undeploy(QName process) {

        }

        public Path buildArchives(BPMNProcess process) {
            return process.getTargetProcessFilePath();
        }

        @Override
        public String getEndpointUrl(String name) {
            return null;
        }

        public String getEndpointUrl(String name, Path path) {
            return "myendpoint";
        }

        @Override
        public EngineExtended getEngineObject() {
            return new EngineExtended(ProcessLanguage.BPMN, "mock","1.0", LocalDate.of(1, 1, 1), "Apache-2.0");
        }

        public Path getXsltPath() {
            return Paths.get("unused_path");
        }

        @Override
        public void buildTest(BPMNProcess process) {

        }

        @Override
        public void testProcess(BPMNProcess process) {

        }

        @Override public BPMNProcessStarter getProcessStarter() {
            return null;
        }

        @Override public Path getLogForInstance(String processName, String instanceId) {
            return null;
        }

        @Override
        public List<Path> getLogs() {
            return Collections.emptyList();
        }
    }
}
