package org.apache.commons.io.output;

import java.io.IOException;
import org.junit.Test;
import junit.framework.TestCase;

/** 
 * Really not a lot to do here, but checking that no
 * Exceptions are thrown.
 * 
 * @version $Id$
 */
public class NullOutputStreamTest extends TestCase {
    public NullOutputStreamTest(String name) {
        super(name);
    }

    @Test(timeout = 1000)
    public void testNull_add1048() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1048");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1049() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1049");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1050() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1050");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1051() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1051");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1052() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1052");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1053() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1053");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1054() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1054");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_add1055() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_add1055");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull");
        NullOutputStream nos = new NullOutputStream();
        nos.write("bar".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3503() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3503");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("foo".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3504() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3504");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 4, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3505() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3505");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 2, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3506() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3506");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 1, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3507() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3507");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 6, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3508() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3508");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 4);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3509() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3509");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 2);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3510() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3510");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 10);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3511() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3511");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 6);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3512() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3512");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(2);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3513() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3513");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(0);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3514() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3514");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(0);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3515() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3515");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(16);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3516() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3516");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(14);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3517() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3517");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(7);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3518() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3518");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(30);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3519() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3519");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("bar".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3520() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3520");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(256);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3521() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3521");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(254);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3522() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3522");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(127);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testNull_literalMutation3523() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_literalMutation3523");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(510);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove815() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove815");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove816() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove816");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove817() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove817");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove818() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove818");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove819() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove819");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove820() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove820");
        NullOutputStream nos = new NullOutputStream();
        nos.write("string".getBytes());
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove821() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove821");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testNull_remove822() throws IOException {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testNull_remove822");
        NullOutputStream nos = new NullOutputStream();
        nos.write("some string".getBytes(), 3, 5);
        nos.write(1);
        nos.write(15);
        nos.flush();
        nos.close();
        nos.write("allowed".getBytes());
        nos.write(255);
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }
}

