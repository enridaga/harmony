package harmony.integration.pddl.ipc2014;

import harmony.integration.pddl.AbstractPddlIT;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Taken from IPC 2015
 * barman-sequential-satisficing
 * @See https://github.com/potassco/pddl-instances/tree/master/ipc-2014/domains/barman-sequential-satisficing
 */
public class BarmanSequentialSatisficing extends AbstractPddlIT {
    String folder = "pddl" + System.getProperty("file.separator") + "ipc-2014" + System.getProperty("file.separator")
      +  "barman-sequential-satisficing";

    @Ignore // FIXME to reproduce issue #3
    @Test
    public void test_instance_1() throws Exception {
        runTest("instance-1.pddl");
    }

    private void runTest(String instanceName) throws Exception {
        runTest(1, folder + System.getProperty("file.separator") + "domain.pddl", folder + System.getProperty("file.separator") + "instances" +System.getProperty("file.separator") + instanceName);
    }
}
