package betsy.bpel.engines.ode;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import betsy.bpel.engines.AbstractLocalBPELEngine;
import betsy.bpel.model.BPELProcess;
import betsy.common.engines.tomcat.Tomcat;
import betsy.common.model.engine.EngineExtended;
import betsy.common.tasks.FileTasks;
import betsy.common.tasks.XSLTTasks;
import betsy.common.util.ClasspathHelper;
import pebl.ProcessLanguage;

public class OdeEngine extends AbstractLocalBPELEngine {

    public static final String TEST_INTERFACE_SERVICE = "TestInterfaceService";

    @Override
    public Path getXsltPath() {
        return ClasspathHelper.getFilesystemPathFromClasspathPath("/bpel/ode");
    }

    @Override
    public EngineExtended getEngineObject() {
        return new EngineExtended(ProcessLanguage.BPEL, "ode", "1.3.5", LocalDate.of(2011,2,6), "Apache-2.0");
    }

    @Override
    public String getEndpointUrl(String name) {
        return getTomcat().getTomcatUrl() + "/ode/processes/" + name + "TestInterface";
    }

    public Path getDeploymentDir() {
        return getTomcat().getTomcatDir().resolve("webapps/ode/WEB-INF/processes");
    }

    public Tomcat getTomcat() {
        return Tomcat.v7(getServerPath());
    }

    @Override
    public void startup() {
        getTomcat().startup();
    }

    @Override
    public void shutdown() {
        getTomcat().shutdown();
    }

    @Override
    public List<Path> getLogs() {
        List<Path> result = new LinkedList<>();

        result.addAll(FileTasks.findAllInFolder(getTomcat().getTomcatLogsDir()));

        return result;
    }

    @Override
    public void install() {
        new OdeInstaller(getServerPath(), "apache-ode-war-1.3.5.zip").install();
    }

    @Override
    public void deploy(String name, Path path) {
        OdeDeployer deployer = new OdeDeployer(getDeploymentDir(), getTomcat().getTomcatLogsDir().resolve("ode.log"));
        deployer.deploy(path, name);
    }

    @Override
    public boolean isDeployed(QName process) {
        OdeDeployer deployer = new OdeDeployer(getDeploymentDir(), getTomcat().getTomcatLogsDir().resolve("ode.log"));
        return deployer.isDeployed(process.getLocalPart());
    }

    @Override
    public void undeploy(QName process) {
        OdeDeployer deployer = new OdeDeployer(getDeploymentDir(), getTomcat().getTomcatLogsDir().resolve("ode.log"));
        deployer.undeploy(process.getLocalPart());
    }

    @Override
    public Path buildArchives(final BPELProcess process) {
        getPackageBuilder().createFolderAndCopyProcessFilesToTarget(process);

        // engine specific steps
        XSLTTasks.transform(getXsltPath().resolve("bpel_to_ode_deploy_xml.xsl"), process.getProcess(), process.getTargetProcessPath().resolve("deploy.xml"));

        FileTasks.replaceTokenInFile(process.getTargetProcessPath().resolve("TestInterface.wsdl"), TEST_INTERFACE_SERVICE, process.getName() + TEST_INTERFACE_SERVICE);
        FileTasks.replaceTokenInFile(process.getTargetProcessPath().resolve("deploy.xml"), TEST_INTERFACE_SERVICE, process.getName() + TEST_INTERFACE_SERVICE);

        getPackageBuilder().replaceEndpointTokenWithValue(process);
        getPackageBuilder().replacePartnerTokenWithValue(process);

        getPackageBuilder().bpelFolderToZipFile(process);

        return process.getTargetPackageFilePath();
    }

    @Override
    public boolean isRunning() {
        return getTomcat().checkIfIsRunning();
    }

}
