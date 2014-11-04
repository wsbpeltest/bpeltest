package configuration.bpel

import betsy.bpel.model.BetsyProcess
import betsy.bpel.model.steps.SoapTestStep
import betsy.bpel.model.assertions.ExitAssertion
import betsy.bpel.model.assertions.SoapFaultTestAssertion
import betsy.common.repositories.Repository

import java.lang.reflect.Field

import static BasicActivityProcesses.BASIC_ACTIVITIES
import static PatternProcesses.CONTROL_FLOW_PATTERNS

class BPELProcessRepository {

    private Repository<BetsyProcess> repo = new Repository<>();

    public BPELProcessRepository() {
        repo.put("ALL",
                BASIC_ACTIVITIES +
                        ScopeProcesses.SCOPES +
                        StructuredActivityProcesses.STRUCTURED_ACTIVITIES +
                        CONTROL_FLOW_PATTERNS
                as List<BetsyProcess>)

        Field[] fields = [
                BasicActivityProcesses.class.declaredFields +
                        ScopeProcesses.class.declaredFields +
                        StructuredActivityProcesses.class.declaredFields +
                        PatternProcesses.class.declaredFields
        ].flatten()

        fields.each { Field f ->
            // adds only the static fields that are lists (groups)
            if (f.type == List.class) {
                // f.get(null) returns the value of the field. the null parameter is ignored as the field is static.
                repo.put(f.name, f.get(null) as List<BetsyProcess>)
            }
        }

        repo.put("STATIC_ANALYSIS", StaticAnalysisProcesses.STATIC_ANALYSIS)
        Map<String, List<BetsyProcess>> ruleGroups = StaticAnalysisProcesses.getGroupsPerRuleForSAProcesses(StaticAnalysisProcesses.STATIC_ANALYSIS)
        for(Map.Entry<String, List<BetsyProcess>> entry : ruleGroups) {
            repo.put(entry.getKey(), entry.getValue())
        }

        repo.put(
                "FAULTS",
                repo.getByName("ALL").findAll { BetsyProcess process ->
                    process.testCases.any {
                        it.testSteps.any { it instanceof SoapTestStep && it.assertions.any { it instanceof SoapFaultTestAssertion } }
                    }
                }
        )

        repo.put("ERRORS", ErrorProcesses.processes)
        // insert every process into the map
        repo.getByName("ERRORS").each { BetsyProcess process ->
            repo.put(process.name, [process])
        }

        repo.put(
                "WITH_EXIT_ASSERTION",
                repo.getByName("ALL").findAll { process ->
                    process.testCases.any { it.testSteps.any { it instanceof SoapTestStep && it.assertions.any { it instanceof ExitAssertion } } }
                }
        )

        // insert every process into the map
        repo.getByName("ALL").each { BetsyProcess process ->
            repo.put(process.name, [process])
        }

        // insert every process into the map
        repo.getByName("STATIC_ANALYSIS").each { BetsyProcess process ->
            repo.put(process.name, [process])
        }
    }

    public List<BetsyProcess> getByName(String name) {
        return repo.getByName(name);
    }

    public List<BetsyProcess> getByNames(String[] names) {
        return repo.getByNames(names);
    }

    public List<String> getNames() {
        return repo.getNames();
    }

}
