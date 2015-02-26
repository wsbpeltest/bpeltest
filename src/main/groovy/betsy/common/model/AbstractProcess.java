package betsy.common.model;

import betsy.common.HasName;
import betsy.common.HasPath;
import betsy.common.tasks.FileTasks;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProcess<TC extends TestCase, E extends HasPath> implements Comparable, HasName {

    public void setTestCases(List<TC> testCases) {
        uniqueifyTestCaseNames(testCases);
        this.testCases = testCases;
    }

    private void uniqueifyTestCaseNames(List<TC> testCases) {
        // group by name of test case
        for (int counter = 1; counter <= testCases.size(); counter++) {
            TestCase testCase = testCases.get(counter - 1);
            testCase.setName(testCase.getName() + "-" + counter);
            testCase.setNumber(counter);
        }
    }

    public abstract AbstractProcess<TC,E> createCopyWithoutEngine();

    @Override
    public String toString() {
        return getNormalizedId();
    }

    /**
     * An id as "language_features/structured_activities/Sequence" is transformed to
     * "language_features__structured_activities__Sequence"
     *
     * @return
     */
    public String getNormalizedId() {
        return getGroup() + "__" + getName();
    }

    /**
     * A bpel path as "language_features/structured_activities/Sequence.bpel" is used to extract "Sequence.bpel"
     *
     * @return
     */
    public String getProcessFileName() {
        return process.getFileName().toString();
    }

    public String getGroup() {
        return getProcess().getParent().getFileName().toString();
    }

    public String getName() {
        return getProcessFileNameWithoutExtension();
    }


    public String getProcessFileNameWithoutExtension() {
        return FileTasks.getFilenameWithoutExtension(process);
    }

    /**
     * The path <code>test/$engine/$process</code>
     *
     * @return the path <code>test/$engine/$process</code>
     */
    public Path getTargetPath() {
        return engine.getPath().resolve(getNormalizedId());
    }

    public Path getTargetLogsPath() {
        return getTargetPath().resolve("logs");
    }

    /**
     * The path <code>test/$engine/$process/pkg</code>
     *
     * @return the path <code>test/$engine/$process/pkg</code>
     */
    public Path getTargetPackagePath() {
        return getTargetPath().resolve("pkg");
    }

    /**
     * The path <code>test/$engine/$process/tmp</code>
     *
     * @return the path <code>test/$engine/$process/tmp</code>
     */
    public Path getTargetTmpPath() {
        return getTargetPath().resolve("tmp");
    }

    /**
     * The path <code>test/$engine/$process/reports</code>
     *
     * @return the path <code>test/$engine/$process/reports</code>
     */
    public Path getTargetReportsPath() {
        return getTargetPath().resolve("reports");
    }

    /**
     * The path <code>test/$engine/$process/bpel</code>
     *
     * @return the path <code>test/$engine/$process/bpel</code>
     */
    public Path getTargetProcessPath() {
        return getTargetPath().resolve("process");
    }

    public Path getTargetProcessFilePath() {
        return getTargetProcessPath().resolve(getProcessFileName());
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!getClass().equals(o.getClass())) {
            return false;
        }

        AbstractProcess that = (AbstractProcess) o;

        return process.toString().equals(that.process.toString());

    }

    public int hashCode() {
        return process.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return process.compareTo(((AbstractProcess) o).process);
    }

    private Path process;
    private String description = "";
    private List<TC> testCases = new ArrayList<>();
    private E engine;

    public Path getProcess() {
        return process;
    }

    public void setProcess(Path process) {
        this.process = process;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TC> getTestCases() {
        return testCases;
    }

    public E getEngine() {
        return engine;
    }

    public void setEngine(E engine) {
        this.engine = engine;
    }
}