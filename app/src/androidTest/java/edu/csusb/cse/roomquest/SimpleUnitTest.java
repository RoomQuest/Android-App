package edu.csusb.cse.roomquest;

import junit.framework.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Michael on 3/12/2015.
 */
public class SimpleUnitTest extends TestCase {
    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    public void run(TestResult testResult) {
        testResult.wasSuccessful();
    }
}
