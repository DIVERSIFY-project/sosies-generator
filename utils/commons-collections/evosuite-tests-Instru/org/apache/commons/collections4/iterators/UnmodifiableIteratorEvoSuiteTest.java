/*
 * This file was automatically generated by EvoSuite
 */

package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections4.bidimap.UnmodifiableSortedBidiMap;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.junit.BeforeClass;

public class UnmodifiableIteratorEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.utils.LoggingUtils.setLoggingForJUnit(); 
  } 


  //Test case number: 0
  /*
   * 2 covered goals:
   * 1 org.apache.commons.collections4.iterators.UnmodifiableIterator.unmodifiableIterator(Ljava/util/Iterator;)Ljava/util/Iterator;: I3 Branch 1 IFNONNULL L48 - true
   * 2 org.apache.commons.collections4.iterators.UnmodifiableIterator.unmodifiableIterator(Ljava/util/Iterator;)Ljava/util/Iterator;: I15 Branch 2 IFEQ L51 - true
   */

  @Test
  public void test0()  throws Throwable  {
		fr.inria.diversify.sosie.logger.LogWriter.writeTestStart(668,"org.apache.commons.collections4.iterators.UnmodifiableIteratorEvoSuiteTest.test0");
      LinkedList<UnmodifiableCollection<Object>> linkedList0 = new LinkedList<UnmodifiableCollection<Object>>();
      Iterator<UnmodifiableCollection<Object>> iterator0 = linkedList0.descendingIterator();
      Iterator<UnmodifiableCollection<Object>> iterator1 = UnmodifiableIterator.unmodifiableIterator(iterator0);
      assertNotSame(iterator1, iterator0);
      assertNotNull(iterator1);
  }

  //Test case number: 1
  /*
   * 1 covered goal:
   * 1 org.apache.commons.collections4.iterators.UnmodifiableIterator.unmodifiableIterator(Ljava/util/Iterator;)Ljava/util/Iterator;: I3 Branch 1 IFNONNULL L48 - false
   */

  @Test
  public void test1()  throws Throwable  {
      // Undeclared exception!
		fr.inria.diversify.sosie.logger.LogWriter.writeTestStart(669,"org.apache.commons.collections4.iterators.UnmodifiableIteratorEvoSuiteTest.test1");
      try {
        UnmodifiableIterator.unmodifiableIterator((Iterator<UnmodifiableList<Object>>) null);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Iterator must not be null
         //
      }
  }

  //Test case number: 2
  /*
   * 2 covered goals:
   * 1 org.apache.commons.collections4.iterators.UnmodifiableIterator.unmodifiableIterator(Ljava/util/Iterator;)Ljava/util/Iterator;: I15 Branch 2 IFEQ L51 - false
   * 2 org.apache.commons.collections4.iterators.UnmodifiableIterator.unmodifiableIterator(Ljava/util/Iterator;)Ljava/util/Iterator;: I3 Branch 1 IFNONNULL L48 - true
   */

  @Test
  public void test2()  throws Throwable  {
		fr.inria.diversify.sosie.logger.LogWriter.writeTestStart(670,"org.apache.commons.collections4.iterators.UnmodifiableIteratorEvoSuiteTest.test2");
      LinkedList<UnmodifiableSortedBidiMap<Object, Object>> linkedList0 = new LinkedList<UnmodifiableSortedBidiMap<Object, Object>>();
      UnmodifiableList<UnmodifiableSortedBidiMap<Object, Object>> unmodifiableList0 = new UnmodifiableList<UnmodifiableSortedBidiMap<Object, Object>>((List<UnmodifiableSortedBidiMap<Object, Object>>) linkedList0);
      ListIterator<UnmodifiableSortedBidiMap<Object, Object>> listIterator0 = unmodifiableList0.listIterator();
      Iterator<UnmodifiableSortedBidiMap<Object, Object>> iterator0 = UnmodifiableIterator.unmodifiableIterator((Iterator<UnmodifiableSortedBidiMap<Object, Object>>) listIterator0);
      assertEquals(false, iterator0.hasNext());
  }
}