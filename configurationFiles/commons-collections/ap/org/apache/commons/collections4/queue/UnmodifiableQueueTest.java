package org.apache.commons.collections4.queue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.Test;

/** 
 * Extension of {@link AbstractCollectionTest} for exercising the
 * {@link UnmodifiableQueue} implementation.
 * 
 * @version $Id$
 * @since 4.0
 */
public class UnmodifiableQueueTest<E> extends AbstractQueueTest<E> {
    public UnmodifiableQueueTest(final String testName) {
        super(testName);
    }

    @Override
    public Queue<E> makeObject() {
        return org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(new LinkedList<E>());
    }

    @Override
    public Queue<E> makeFullCollection() {
        final Queue<E> queue = new LinkedList<E>();
        queue.addAll(java.util.Arrays.asList(getFullElements()));
        return org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(queue);
    }

    @Override
    public Collection<E> makeConfirmedCollection() {
        return new LinkedList<E>();
    }

    @Override
    public Collection<E> makeConfirmedFullCollection() {
        final LinkedList<E> list = new LinkedList<E>();
        list.addAll(java.util.Arrays.asList(getFullElements()));
        return list;
    }

    @Override
    public Queue<E> getCollection() {
        return super.getCollection();
    }

    @Override
    public boolean isAddSupported() {
        return false;
    }

    @Override
    public boolean isRemoveSupported() {
        return false;
    }

    @Override
    public boolean isNullSupported() {
        return false;
    }

    @Override
    public void testQueueRemove() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testQueueRemove");
        resetEmpty();
        try {
            getCollection().remove();
        } catch (final UnsupportedOperationException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testQueueRemove_add1917() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testQueueRemove_add1917");
        resetEmpty();
        resetEmpty();
        try {
            getCollection().remove();
        } catch (final UnsupportedOperationException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testQueueRemove_add1918() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testQueueRemove_add1918");
        resetEmpty();
        try {
            getCollection().remove();
            getCollection().remove();
        } catch (final UnsupportedOperationException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testQueueRemove_remove1488() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testQueueRemove_remove1488");
        try {
            getCollection().remove();
        } catch (final UnsupportedOperationException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testUnmodifiable() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testUnmodifiable");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),5557,((makeObject()) instanceof org.apache.commons.collections4.Unmodifiable));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6058,((makeFullCollection()) instanceof org.apache.commons.collections4.Unmodifiable));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    public void testDecorateFactory() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testDecorateFactory");
        final Queue<E> queue = makeFullCollection();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6055,queue);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6057,null,6056,org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(queue));
        try {
            org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(null);
        } catch (final IllegalArgumentException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testDecorateFactory_add1916() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testDecorateFactory_add1916");
        final Queue<E> queue = makeFullCollection();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6055,queue);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6057,null,6056,org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(queue));
        try {
            org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(null);
            org.apache.commons.collections4.queue.UnmodifiableQueue.unmodifiableQueue(null);
        } catch (final IllegalArgumentException ex) {
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }
}

