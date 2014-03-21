package betsy.data

import betsy.data.steps.BPMNTestStep
import org.json.JSONObject

class BPMNTestCase {

    int number
    List<BPMNTestStep> testSteps = []

    boolean selfStarting = false
    int delay = 0

//variables to be sent for a test case
    JSONObject variables

    public BPMNTestCase(){
        number = 1
    }

    public BPMNTestCase(int number){
        this.number = number
    }

    public BPMNTestCase addStep(BPMNTestStep testStep){
        testSteps.add(testStep)

        this
    }

    public BPMNTestCase buildSimple(){

        variables = new JSONObject()
        JSONObject value = new JSONObject()
        value.put("value", number)
        value.put("type","Integer")
        variables.put("testCaseNumber", value)

        addStep(new BPMNTestStep().assertSuccess())
    }

    public BPMNTestCase buildMulti3(){

        addStep(new BPMNTestStep().assertSuccess().assertMulti().assertMulti().assertMulti())
    }

    public BPMNTestCase buildSubprocess(){
        addStep(new BPMNTestStep().assertSuccess().assertSubprocess())
    }

    public BPMNTestCase buildSignaled(){

        variables = new JSONObject()
        JSONObject value = new JSONObject()
        value.put("value", number)
        value.put("type","Integer")
        variables.put("testCaseNumber", value)

        addStep(new BPMNTestStep().assertSuccess().assertSignaled())
    }

    public BPMNTestCase buildSimpleError(){

        variables = new JSONObject()
        JSONObject value = new JSONObject()
        value.put("value", number)
        value.put("type","Integer")
        variables.put("testCaseNumber", value)

        addStep(new BPMNTestStep().assertSuccess().assertThrownErrorEvent())
    }

    public BPMNTestCase buildAnd(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "a")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertAnd())
    }

    public BPMNTestCase buildXorTrue(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "a")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertXorTrue())

    }

    public BPMNTestCase buildXorFalse(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "b")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertXorFalse())
    }

    public BPMNTestCase buildBothFalse(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "c")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertRuntimeException())
    }

    public BPMNTestCase buildXorWithDefaultBothFalse(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "c")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertDefault())
    }

    public BPMNTestCase buildXorBothTrue(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "ab")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertXorTrue())
    }

    public BPMNTestCase buildOrSingleFlow1(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "a")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertOrSingleFlow1())
    }

    public BPMNTestCase buildOrSingleFlow2(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "b")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertOrSingleFlow2())
    }

    public BPMNTestCase buildOrMultiFlow(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "ab")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertOrMultiFlow())
    }

    public BPMNTestCase buildParallelInExclusiveOut(){

        variables = new JSONObject()
        JSONObject value2 = new JSONObject()
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertParallelInExclusiveOut())
    }

    public BPMNTestCase buildDefault(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "c")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertDefault())
    }

    public BPMNTestCase buildExclusiveInParallelOutTrue(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "a")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertTrue())
    }

    public BPMNTestCase buildExclusiveInParallelOutFalse(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "b")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertFalse())
    }

    public BPMNTestCase buildExclusiveInParallelOutBothTrue(){

        variables = new JSONObject()
        JSONObject value1 = new JSONObject()
        JSONObject value2 = new JSONObject()
        value1.put("value", "ab")
        value1.put("type", "String")
        value2.put("value", number)
        value2.put("type","Integer")
        variables.put("test", value1)
        variables.put("testCaseNumber", value2)

        addStep(new BPMNTestStep().assertTrue())
    }

    public BPMNTestCase buildTwoLanes(){

        addStep(new BPMNTestStep().assertTwoLanes())
    }

    //Getter and Setter

    boolean getSelfStarting() {
        return selfStarting
    }

    void setSelfStarting(boolean selfStarting) {
        this.selfStarting = selfStarting
    }

    int getNumber() {
        return number
    }

    void setNumber(int number) {
        this.number = number
    }

    int getDelay() {
        return delay
    }

    void setDelay(int delay) {
        this.delay = delay
    }

}
