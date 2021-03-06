package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import junit.framework.TestCase;

/** 
 * Unit tests for the <code>DeferredFileOutputStream</code> class.
 * 
 * @version $Id$
 */
public class DeferredFileOutputStreamTest extends TestCase {
    /** 
     * The test data as a string (which is the simplest form).
     */
private String testString = "0123456789";

    /** 
     * The test data as a byte array, derived from the string.
     */
private byte[] testBytes = testString.getBytes();

    /** 
     * Standard JUnit test case constructor.
     * 
     * @param name The name of the test case.
     */
public DeferredFileOutputStreamTest(String name) {
        super(name);
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
@Test(timeout = 1000)
    public void testBelowThreshold_add980() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_add980");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
@Test(timeout = 1000)
    public void testBelowThreshold_add981() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_add981");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 41) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3400() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3400");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 43) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3401() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3401");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 21) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3402() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3402");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 84) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3404() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3404");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , null);
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3405() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3405");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , null);
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data falls below the threshold, and
     * is therefore confined to memory.
     */
public void testBelowThreshold_literalMutation3406() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testBelowThreshold_literalMutation3406");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1406,dfos,1405,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1407,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1408,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1410,null,1409,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data is exactly the same as the
     * threshold. The behavior should be the same as that for the amount of
     * data being below (i.e. not exceeding) the threshold.
     */
@Test(timeout = 1000)
    public void testAtThreshold_add978() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAtThreshold_add978");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(testBytes.length , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1400,dfos,1399,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1401,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1402,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1404,null,1403,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data is exactly the same as the
     * threshold. The behavior should be the same as that for the amount of
     * data being below (i.e. not exceeding) the threshold.
     */
@Test(timeout = 1000)
    public void testAtThreshold_add979() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAtThreshold_add979");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(testBytes.length , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1400,dfos,1399,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1401,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1402,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1404,null,1403,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data is exactly the same as the
     * threshold. The behavior should be the same as that for the amount of
     * data being below (i.e. not exceeding) the threshold.
     */
public void testAtThreshold() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAtThreshold");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(testBytes.length , null);
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1400,dfos,1399,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1401,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1402,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1404,null,1403,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data is exactly the same as the
     * threshold. The behavior should be the same as that for the amount of
     * data being below (i.e. not exceeding) the threshold.
     */
public void testAtThreshold_literalMutation3397() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAtThreshold_literalMutation3397");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(testBytes.length , null);
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1400,dfos,1399,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1401,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1402,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1404,null,1403,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data is exactly the same as the
     * threshold. The behavior should be the same as that for the amount of
     * data being below (i.e. not exceeding) the threshold.
     */
public void testAtThreshold_literalMutation3398() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAtThreshold_literalMutation3398");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(testBytes.length , null);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1400,dfos,1399,dfos.isInMemory());
        byte[] resultBytes = dfos.getData();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1401,testBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1402,resultBytes.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1404,null,1403,java.util.Arrays.equals(resultBytes, testBytes));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_add973() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_add973");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_add974() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_add974");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_add975() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_add975");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_add976() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_add976");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_add977() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_add977");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold");
        File testFile = new File("bar");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3388() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3388");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 4) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3389() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3389");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 2) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3390() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3390");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 10) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3391() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3391");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 6) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3392() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3392");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3393() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3393");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
public void testAboveThreshold_literalMutation3394() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_literalMutation3394");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_remove800() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_remove800");
        File testFile = new File("testAboveThreshold.dat");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_remove801() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_remove801");
        File testFile = new File("testAboveThreshold.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where the amount of data exceeds the threshold, and is
     * therefore written to disk. The actual data written to disk is verified,
     * as is the file itself.
     */
@Test(timeout = 1000)
    public void testAboveThreshold_remove802() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testAboveThreshold_remove802");
        File testFile = new File("testAboveThreshold.dat");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , testFile);
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1396,dfos,1395,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1398,dfos,1397,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add992() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add992");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add993() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add993");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add994() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add994");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add995() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add995");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add996() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add996");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add997() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add997");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_add998() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_add998");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached");
        File testFile = new File("foo");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3445() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3445");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 1) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3446() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3446");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 4) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3447() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3447");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 3) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3448() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3448");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 4;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3449() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3449");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 2;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3450() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3450");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 1;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3451() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3451");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 6;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3452() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3452");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 1, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3453() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3453");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, -1, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3454() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3454");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3455() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3455");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 1), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3456() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3456");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 4), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3457() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3457");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 3), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3458() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3458");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 1)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3459() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3459");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 4)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
public void testThresholdReached_literalMutation3460() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_literalMutation3460");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 3)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_remove807() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_remove807");
        File testFile = new File("testThresholdReached.dat");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_remove808() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_remove808");
        File testFile = new File("testThresholdReached.dat");
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests the case where there are multiple writes beyond the threshold, to
     * ensure that the <code>thresholdReached()</code> method is only called
     * once, as the threshold is crossed for the first time.
     */
@Test(timeout = 1000)
    public void testThresholdReached_remove809() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testThresholdReached_remove809");
        File testFile = new File("testThresholdReached.dat");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        int chunkSize = (testBytes.length) / 3;
        try {
            dfos.write(testBytes, 0, chunkSize);
            dfos.write(testBytes, chunkSize, chunkSize);
            dfos.write(testBytes, (chunkSize * 2), ((testBytes.length) - (chunkSize * 2)));
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1450,dfos,1449,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1452,dfos,1451,dfos.getData());
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1006() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1006");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1007() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1007");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1008() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1008");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1009() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1009");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1010() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1010");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_add1011() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_add1011");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
public void testWriteToSmall() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall");
        File testFile = new File("foo");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
public void testWriteToSmall_literalMutation3466() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_literalMutation3466");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 1) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
public void testWriteToSmall_literalMutation3467() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_literalMutation3467");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 4) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
public void testWriteToSmall_literalMutation3468() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_literalMutation3468");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 3) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_remove813() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_remove813");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes small content.
     */
@Test(timeout = 1000)
    public void testWriteToSmall_remove814() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToSmall_remove814");
        File testFile = new File("testWriteToMem.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) * 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1460,testFile,1459,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1462,dfos,1461,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1464,null,1463,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1000() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1000");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1001() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1001");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1002() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1002");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1003() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1003");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1004() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1004");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add1005() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add1005");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_add999() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_add999");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
public void testWriteToLarge() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge");
        File testFile = new File("bar");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
public void testWriteToLarge_literalMutation3462() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_literalMutation3462");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 1) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
public void testWriteToLarge_literalMutation3463() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_literalMutation3463");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 4) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
public void testWriteToLarge_literalMutation3464() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_literalMutation3464");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 3) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_remove810() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_remove810");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_remove811() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_remove811");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        testFile.delete();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test wether writeTo() properly writes large content.
     */
@Test(timeout = 1000)
    public void testWriteToLarge_remove812() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testWriteToLarge_remove812");
        File testFile = new File("testWriteToFile.dat");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) / 2) , testFile);
        try {
            dfos.write(testBytes);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1454,testFile,1453,testFile.exists());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1456,dfos,1455,dfos.isInMemory());
            try {
                dfos.writeTo(baos);
            } catch (IOException ioe) {
            }
            dfos.close();
            dfos.writeTo(baos);
        } catch (IOException ioe) {
        }
        byte[] copiedBytes = baos.toByteArray();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1458,null,1457,java.util.Arrays.equals(testBytes, copiedBytes));
        verifyResultFile(testFile);
        testFile.delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
@Test(timeout = 1000)
    public void testTempFileBelowThreshold_add990() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_add990");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
@Test(timeout = 1000)
    public void testTempFileBelowThreshold_add991() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_add991");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold");
        String prefix = "bar";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3428() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3428");
        String prefix = "commons-io-test";
        String suffix = "bar";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3429() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3429");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File("foo");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3430() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3430");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 41) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3431() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3431");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 43) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3432() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3432");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 21) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3433() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3433");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 84) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3434() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3434");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3435() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3435");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold not reached.
     */
public void testTempFileBelowThreshold_literalMutation3436() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileBelowThreshold_literalMutation3436");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) + 42) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1444,dfos,1443,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1446,dfos,1445,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1448,dfos,1447,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_add982() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_add982");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_add983() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_add983");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_add984() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_add984");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_add985() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_add985");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold");
        String prefix = "foo";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3408() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3408");
        String prefix = "commons-io-test";
        String suffix = "bar";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3409() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3409");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File("bar");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3410() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3410");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 4) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3411() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3411");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 2) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3412() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3412");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 10) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3413() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3413");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 6) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3414() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3414");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3415() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3415");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThreshold_literalMutation3416() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_literalMutation3416");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_remove803() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_remove803");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThreshold_remove804() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThreshold_remove804");
        String prefix = "commons-io-test";
        String suffix = ".out";
        File tempDir = new File(".");
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1426,dfos,1425,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1428,dfos,1427,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1430,dfos,1429,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1432,dfos,1431,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1434,dfos.getFile(),1433,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1436,dfos.getFile().getName(),1435,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1438,dfos.getFile().getName(),1437,dfos.getFile().getName().endsWith(suffix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1440,tempDir,1439,tempDir.getPath());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1442,dfos.getFile(),1441,dfos.getFile().getParent());
        verifyResultFile(dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_add986() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_add986");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_add987() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_add987");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_add988() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_add988");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_add989() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_add989");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly");
        String prefix = "bar";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3420() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3420");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 4) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3421() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3421");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 2) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3422() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3422");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 10) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3423() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3423");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 6) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3424() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3424");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3425() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3425");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, -1, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileAboveThresholdPrefixOnly_literalMutation3426() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_literalMutation3426");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_remove805() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_remove805");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        dfos.getFile().delete();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
@Test(timeout = 1000)
    public void testTempFileAboveThresholdPrefixOnly_remove806() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileAboveThresholdPrefixOnly_remove806");
        String prefix = "commons-io-test";
        String suffix = null;
        File tempDir = null;
        DeferredFileOutputStream dfos = new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1412,dfos,1411,dfos.getFile());
        try {
            dfos.write(testBytes, 0, testBytes.length);
            dfos.close();
        } catch (IOException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1414,dfos,1413,dfos.isInMemory());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1416,dfos,1415,dfos.getData());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1418,dfos,1417,dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1420,dfos.getFile(),1419,dfos.getFile().exists());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1422,dfos.getFile().getName(),1421,dfos.getFile().getName().startsWith(prefix));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1424,dfos.getFile().getName(),1423,dfos.getFile().getName().endsWith(".tmp"));
        verifyResultFile(dfos.getFile());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError");
        String prefix = null;
        String suffix = "foo";
        File tempDir = new File(".");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError_literalMutation3439() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError_literalMutation3439");
        String prefix = null;
        String suffix = ".out";
        File tempDir = new File("foo");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 5) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError_literalMutation3440() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError_literalMutation3440");
        String prefix = null;
        String suffix = ".out";
        File tempDir = new File(".");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 4) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError_literalMutation3441() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError_literalMutation3441");
        String prefix = null;
        String suffix = ".out";
        File tempDir = new File(".");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 2) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError_literalMutation3442() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError_literalMutation3442");
        String prefix = null;
        String suffix = ".out";
        File tempDir = new File(".");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 10) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test specifying a temporary file and the threshold is reached.
     */
public void testTempFileError_literalMutation3443() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testTempFileError_literalMutation3443");
        String prefix = null;
        String suffix = ".out";
        File tempDir = new File(".");
        try {
            new DeferredFileOutputStream(((testBytes.length) - 6) , prefix , suffix , tempDir);
        } catch (IllegalArgumentException e) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that the specified file contains the same data as the original
     * test data.
     * 
     * @param testFile The file containing the test output.
     */
private void verifyResultFile(File testFile) {
        try {
            FileInputStream fis = new FileInputStream(testFile);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1465,testBytes.length);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1467,fis,1466,fis.available());
            byte[] resultBytes = new byte[testBytes.length];
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1468,testBytes.length);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1470,fis,1469,fis.read(resultBytes));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1472,null,1471,java.util.Arrays.equals(resultBytes, testBytes));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1473,-1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1475,fis,1474,fis.read(resultBytes));
            try {
                fis.close();
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}

