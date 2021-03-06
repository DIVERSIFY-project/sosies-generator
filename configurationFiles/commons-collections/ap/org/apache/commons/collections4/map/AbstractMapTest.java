package org.apache.commons.collections4.map;

import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.AbstractObjectTest;
import org.apache.commons.collections4.set.AbstractSetTest;
import java.util.ArrayList;
import org.apache.commons.collections4.BulkTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/** 
 * Abstract test class for {@link java.util.Map} methods and contracts.
 * <p/>
 * The forces at work here are similar to those in {@link AbstractCollectionTest}.
 * If your class implements the full Map interface, including optional
 * operations, simply extend this class, and implement the
 * {@link #makeObject()} method.
 * <p/>
 * On the other hand, if your map implementation is weird, you may have to
 * override one or more of the other protected methods.  They're described
 * below.
 * <p/>
 * <b>Entry Population Methods</b>
 * <p/>
 * Override these methods if your map requires special entries:
 * <p/>
 * <ul>
 * <li>{@link #getSampleKeys()}
 * <li>{@link #getSampleValues()}
 * <li>{@link #getNewSampleValues()}
 * <li>{@link #getOtherKeys()}
 * <li>{@link #getOtherValues()}
 * </ul>
 * <p/>
 * <b>Supported Operation Methods</b>
 * <p/>
 * Override these methods if your map doesn't support certain operations:
 * <p/>
 * <ul>
 * <li> {@link #isPutAddSupported()}
 * <li> {@link #isPutChangeSupported()}
 * <li> {@link #isSetValueSupported()}
 * <li> {@link #isRemoveSupported()}
 * <li> {@link #isGetStructuralModify()}
 * <li> {@link #isAllowDuplicateValues()}
 * <li> {@link #isAllowNullKey()}
 * <li> {@link #isAllowNullValue()}
 * </ul>
 * <p/>
 * <b>Fixture Methods</b>
 * <p/>
 * For tests on modification operations (puts and removes), fixtures are used
 * to verify that that operation results in correct state for the map and its
 * collection views.  Basically, the modification is performed against your
 * map implementation, and an identical modification is performed against
 * a <I>confirmed</I> map implementation.  A confirmed map implementation is
 * something like <Code>java.util.HashMap</Code>, which is known to conform
 * exactly to the {@link Map} contract.  After the modification takes place
 * on both your map implementation and the confirmed map implementation, the
 * two maps are compared to see if their state is identical.  The comparison
 * also compares the collection views to make sure they're still the same.<P>
 * <p/>
 * The upshot of all that is that <I>any</I> test that modifies the map in
 * <I>any</I> way will verify that <I>all</I> of the map's state is still
 * correct, including the state of its collection views.  So for instance
 * if a key is removed by the map's key set's iterator, then the entry set
 * is checked to make sure the key/value pair no longer appears.<P>
 * <p/>
 * The {@link #map} field holds an instance of your collection implementation.
 * The {@link #entrySet}, {@link #keySet} and {@link #values} fields hold
 * that map's collection views.  And the {@link #confirmed} field holds
 * an instance of the confirmed collection implementation.  The
 * {@link #resetEmpty()} and {@link #resetFull()} methods set these fields to
 * empty or full maps, so that tests can proceed from a known state.<P>
 * <p/>
 * After a modification operation to both {@link #map} and {@link #confirmed},
 * the {@link #verify()} method is invoked to compare the results.  The
 * {@link #verify} method calls separate methods to verify the map and its three
 * collection views ({@link #verifyMap}, {@link #verifyEntrySet},
 * {@link #verifyKeySet}, and {@link #verifyValues}).  You may want to override
 * one of the verification methods to perform additional verifications.  For
 * instance, TestDoubleOrderedMap would want override its
 * {@link #verifyValues()} method to verify that the values are unique and in
 * ascending order.<P>
 * <p/>
 * <b>Other Notes</b>
 * <p/>
 * If your {@link Map} fails one of these tests by design, you may still use
 * this base set of cases.  Simply override the test case (method) your map
 * fails and/or the methods that define the assumptions used by the test
 * cases.  For example, if your map does not allow duplicate values, override
 * {@link #isAllowDuplicateValues()} and have it return <code>false</code>
 * 
 * @version $Id$
 */
public abstract class AbstractMapTest<K, V> extends AbstractObjectTest {
    /** 
     * JDK1.2 has bugs in null handling of Maps, especially HashMap.Entry.toString
     * This avoids nulls for JDK1.2
     */
private static final boolean JDK12;

    static {
        final String str = java.lang.System.getProperty("java.version");
        JDK12 = str.startsWith("1.2");
    }

    /** 
     * Map created by reset().
     */
protected Map<K, V> map;

    /** 
     * Entry set of map created by reset().
     */
protected Set<java.util.Map.Entry<K, V>> entrySet;

    /** 
     * Key set of map created by reset().
     */
protected Set<K> keySet;

    /** 
     * Values collection of map created by reset().
     */
protected Collection<V> values;

    /** 
     * HashMap created by reset().
     */
protected Map<K, V> confirmed;

    /** 
     * JUnit constructor.
     * 
     * @param testName the test name
     */
public AbstractMapTest(final String testName) {
        super(testName);
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * support the <code>put</code> and <code>putAll</code> operations
     * adding new mappings.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support put adding.
     */
public boolean isPutAddSupported() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * support the <code>put</code> and <code>putAll</code> operations
     * changing existing mappings.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support put changing.
     */
public boolean isPutChangeSupported() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * support the <code>setValue</code> operation on entrySet entries.
     * <p/>
     * Default implementation returns isPutChangeSupported().
     * Override if your collection class does not support setValue but does
     * support put changing.
     */
public boolean isSetValueSupported() {
        return isPutChangeSupported();
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * support the <code>remove</code> and <code>clear</code> operations.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support removal operations.
     */
public boolean isRemoveSupported() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * can cause structural modification on a get(). The example is LRUMap.
     * <p/>
     * Default implementation returns false.
     * Override if your map class structurally modifies on get.
     */
public boolean isGetStructuralModify() {
        return false;
    }

    /** 
     * Returns whether the sub map views of SortedMap are serializable.
     * If the class being tested is based around a TreeMap then you should
     * override and return false as TreeMap has a bug in deserialization.
     * 
     * @return false
     */
public boolean isSubMapViewsSerializable() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * supports null keys.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support null keys.
     */
public boolean isAllowNullKey() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * supports null values.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support null values.
     */
public boolean isAllowNullValue() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * supports duplicate values.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support duplicate values.
     */
public boolean isAllowDuplicateValues() {
        return true;
    }

    /** 
     * Returns true if the maps produced by
     * {@link #makeObject()} and {@link #makeFullMap()}
     * provide fail-fast behavior on their various iterators.
     * <p/>
     * Default implementation returns true.
     * Override if your collection class does not support fast failure.
     */
public boolean isFailFastExpected() {
        return true;
    }

    /** 
     * Returns the set of keys in the mappings used to test the map.  This
     * method must return an array with the same length as {@link
     * #getSampleValues()} and all array elements must be different. The
     * default implementation constructs a set of String keys, and includes a
     * single null key if {@link #isAllowNullKey()} returns <code>true</code>.
     */
@SuppressWarnings(value = "unchecked")
    public K[] getSampleKeys() {
        final Object[] result = new Object[]{ "blah" , "foo" , "bar" , "baz" , "tmp" , "gosh" , "golly" , "gee" , "hello" , "goodbye" , "we\'ll" , "see" , "you" , "all" , "again" , "key" , "key2" , (isAllowNullKey()) && (!(JDK12)) ? null : "nonnullkey" };
        return ((K[])(result));
    }

    @SuppressWarnings(value = "unchecked")
    public K[] getOtherKeys() {
        return ((K[])(getOtherNonNullStringElements()));
    }

    @SuppressWarnings(value = "unchecked")
    public V[] getOtherValues() {
        return ((V[])(getOtherNonNullStringElements()));
    }

    @SuppressWarnings(value = "unchecked")
    protected <E>List<E> getAsList(final Object[] o) {
        final ArrayList<E> result = new ArrayList<E>();
        for (final Object element : o) {
            result.add(((E)(element)));
        }
        return result;
    }

    /** 
     * Returns a list of string elements suitable for return by
     * {@link #getOtherKeys()} or {@link #getOtherValues}.
     * <p/>
     * <p>Override getOtherElements to return the results of this method if your
     * collection does not support heterogenous elements or the null element.
     * </p>
     */
public Object[] getOtherNonNullStringElements() {
        return new Object[]{ "For" , "then" , "despite" , "space" , "I" , "would" , "be" , "brought" , "From" , "limits" , "far" , "remote" , "where" , "thou" , "dost" , "stay" };
    }

    /** 
     * Returns the set of values in the mappings used to test the map.  This
     * method must return an array with the same length as
     * {@link #getSampleKeys()}.  The default implementation constructs a set of
     * String values and includes a single null value if
     * {@link #isAllowNullValue()} returns <code>true</code>, and includes
     * two values that are the same if {@link #isAllowDuplicateValues()} returns
     * <code>true</code>.
     */
@SuppressWarnings(value = "unchecked")
    public V[] getSampleValues() {
        final Object[] result = new Object[]{ "blahv" , "foov" , "barv" , "bazv" , "tmpv" , "goshv" , "gollyv" , "geev" , "hellov" , "goodbyev" , "we\'llv" , "seev" , "youv" , "allv" , "againv" , (isAllowNullValue()) && (!(JDK12)) ? null : "nonnullvalue" , "value" , isAllowDuplicateValues() ? "value" : "value2" };
        return ((V[])(result));
    }

    /** 
     * Returns a the set of values that can be used to replace the values
     * returned from {@link #getSampleValues()}.  This method must return an
     * array with the same length as {@link #getSampleValues()}.  The values
     * returned from this method should not be the same as those returned from
     * {@link #getSampleValues()}.  The default implementation constructs a
     * set of String values and includes a single null value if
     * {@link #isAllowNullValue()} returns <code>true</code>, and includes two values
     * that are the same if {@link #isAllowDuplicateValues()} returns
     * <code>true</code>.
     */
@SuppressWarnings(value = "unchecked")
    public V[] getNewSampleValues() {
        final Object[] result = new Object[]{ ((isAllowNullValue()) && (!(JDK12))) && (isAllowDuplicateValues()) ? null : "newnonnullvalue" , "newvalue" , isAllowDuplicateValues() ? "newvalue" : "newvalue2" , "newblahv" , "newfoov" , "newbarv" , "newbazv" , "newtmpv" , "newgoshv" , "newgollyv" , "newgeev" , "newhellov" , "newgoodbyev" , "newwe\'llv" , "newseev" , "newyouv" , "newallv" , "newagainv" };
        return ((V[])(result));
    }

    /** 
     * Helper method to add all the mappings described by
     * {@link #getSampleKeys()} and {@link #getSampleValues()}.
     */
public void addSampleMappings(final Map<? super K, ? super V> m) {
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            try {
                m.put(keys[i], values[i]);
            } catch (final NullPointerException exception) {
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),47,("NullPointerException only allowed to be thrown " + "if either the key or value is null."));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),48,(((keys[i]) == null) || ((values[i]) == null)));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),49,("NullPointerException on null key, but " + "isAllowNullKey is not overridden to return false."));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),50,(((keys[i]) == null) || (!(isAllowNullKey()))));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),51,("NullPointerException on null value, but " + "isAllowNullValue is not overridden to return false."));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),52,(((values[i]) == null) || (!(isAllowNullValue()))));
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),53,keys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),55,m,54,m.size());
    }

    /** 
     * Return a new, empty {@link Map} to be used for testing.
     * 
     * @return the map to be tested
     */
@Override
    public abstract Map<K, V> makeObject();

    /** 
     * Return a new, populated map.  The mappings in the map should match the
     * keys and values returned from {@link #getSampleKeys()} and
     * {@link #getSampleValues()}.  The default implementation uses makeEmptyMap()
     * and calls {@link #addSampleMappings} to add all the mappings to the
     * map.
     * 
     * @return the map to be tested
     */
public Map<K, V> makeFullMap() {
        final Map<K, V> m = makeObject();
        addSampleMappings(m);
        return m;
    }

    /** 
     * Override to return a map other than HashMap as the confirmed map.
     * 
     * @return a map that is known to be valid
     */
public Map<K, V> makeConfirmedMap() {
        return new HashMap<K, V>();
    }

    /** 
     * Creates a new Map Entry that is independent of the first and the map.
     */
public static <K, V>Map.Entry<K, V> cloneMapEntry(final Map.Entry<K, V> entry) {
        final HashMap<K, V> map = new HashMap<K, V>();
        map.put(entry.getKey(), entry.getValue());
        return map.entrySet().iterator().next();
    }

    /** 
     * Gets the compatability version, needed for package access.
     */
@Override
    public String getCompatibilityVersion() {
        return super.getCompatibilityVersion();
    }

    /** 
     * Test to ensure the test setup is working properly.  This method checks
     * to ensure that the getSampleKeys and getSampleValues methods are
     * returning results that look appropriate.  That is, they both return a
     * non-null array of equal length.  The keys array must not have any
     * duplicate values, and may only contain a (single) null key if
     * isNullKeySupported() returns true.  The values array must only have a null
     * value if useNullValue() is true and may only have duplicate values if
     * isAllowDuplicateValues() returns true.
     */
public void testSampleMappings() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testSampleMappings");
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        final Object[] newValues = getNewSampleValues();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),218,("failure in test: Must have keys returned from " + "getSampleKeys."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),219,keys);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),220,("failure in test: Must have values returned from " + "getSampleValues."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),221,values);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),222,("failure in test: not the same number of sample " + "keys and values."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),223,keys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),224,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),225,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),226,newValues.length);
        for (int i = 1 ; i < ((keys.length) - 1) ; i++) {
            for (int j = i + 1 ; j < (keys.length) ; j++) {
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),227,(((keys[i]) != null) || ((keys[j]) != null)));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),228,((((keys[i]) == null) || ((keys[j]) == null)) || ((!(keys[i].equals(keys[j]))) && (!(keys[j].equals(keys[i]))))));
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),229,("failure in test: found null key, but isNullKeySupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),230,(((keys[i]) != null) || (isAllowNullKey())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),231,("failure in test: found null value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),232,(((values[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),233,("failure in test: found null new value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),234,(((newValues[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),235,(((values[i]) != (newValues[i])) && (((values[i]) == null) || (!(values[i].equals(newValues[i]))))));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test to ensure the test setup is working properly.  This method checks
     * to ensure that the getSampleKeys and getSampleValues methods are
     * returning results that look appropriate.  That is, they both return a
     * non-null array of equal length.  The keys array must not have any
     * duplicate values, and may only contain a (single) null key if
     * isNullKeySupported() returns true.  The values array must only have a null
     * value if useNullValue() is true and may only have duplicate values if
     * isAllowDuplicateValues() returns true.
     */
public void testSampleMappings_literalMutation51() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testSampleMappings_literalMutation51");
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        final Object[] newValues = getNewSampleValues();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),218,("failure in test: Must have keys returned from " + "getSampleKeys."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),219,keys);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),220,("failure in test: Must have values returned from " + "getSampleValues."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),221,values);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),222,("failure in test: not the same number of sample " + "keys and values."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),223,keys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),224,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),225,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),226,newValues.length);
        for (int i = 0 ; i < ((keys.length) - 2) ; i++) {
            for (int j = i + 1 ; j < (keys.length) ; j++) {
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),227,(((keys[i]) != null) || ((keys[j]) != null)));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),228,((((keys[i]) == null) || ((keys[j]) == null)) || ((!(keys[i].equals(keys[j]))) && (!(keys[j].equals(keys[i]))))));
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),229,("failure in test: found null key, but isNullKeySupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),230,(((keys[i]) != null) || (isAllowNullKey())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),231,("failure in test: found null value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),232,(((values[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),233,("failure in test: found null new value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),234,(((newValues[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),235,(((values[i]) != (newValues[i])) && (((values[i]) == null) || (!(values[i].equals(newValues[i]))))));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test to ensure the test setup is working properly.  This method checks
     * to ensure that the getSampleKeys and getSampleValues methods are
     * returning results that look appropriate.  That is, they both return a
     * non-null array of equal length.  The keys array must not have any
     * duplicate values, and may only contain a (single) null key if
     * isNullKeySupported() returns true.  The values array must only have a null
     * value if useNullValue() is true and may only have duplicate values if
     * isAllowDuplicateValues() returns true.
     */
public void testSampleMappings_literalMutation52() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testSampleMappings_literalMutation52");
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        final Object[] newValues = getNewSampleValues();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),218,("failure in test: Must have keys returned from " + "getSampleKeys."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),219,keys);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),220,("failure in test: Must have values returned from " + "getSampleValues."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),221,values);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),222,("failure in test: not the same number of sample " + "keys and values."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),223,keys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),224,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),225,values.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),226,newValues.length);
        for (int i = 0 ; i < ((keys.length) - 1) ; i++) {
            for (int j = i + 2 ; j < (keys.length) ; j++) {
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),227,(((keys[i]) != null) || ((keys[j]) != null)));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),228,((((keys[i]) == null) || ((keys[j]) == null)) || ((!(keys[i].equals(keys[j]))) && (!(keys[j].equals(keys[i]))))));
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),229,("failure in test: found null key, but isNullKeySupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),230,(((keys[i]) != null) || (isAllowNullKey())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),231,("failure in test: found null value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),232,(((values[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),233,("failure in test: found null new value, but isNullValueSupported " + "is false."));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),234,(((newValues[i]) != null) || (isAllowNullValue())));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),235,(((values[i]) != (newValues[i])) && (((values[i]) == null) || (!(values[i].equals(newValues[i]))))));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test to ensure that makeEmptyMap and makeFull returns a new non-null
     * map with each invocation.
     */
public void testMakeMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMakeMap");
        final Map<K, V> em = makeObject();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),144,(em != null));
        final Map<K, V> em2 = makeObject();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),145,(em != null));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),146,("failure in test: makeEmptyMap must return a new map " + "with each invocation."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),147,(em != em2));
        final Map<K, V> fm = makeFullMap();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),148,(fm != null));
        final Map<K, V> fm2 = makeFullMap();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),149,(fm != null));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),150,("failure in test: makeFullMap must return a new map " + "with each invocation."));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),151,(fm != fm2));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_add100() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_add100");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty");
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_add98() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_add98");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_add99() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_add99");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_remove78() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_remove78");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_remove79() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_remove79");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_remove80() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_remove80");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.isEmpty()
     */
@Test(timeout = 1000)
    public void testMapIsEmpty_remove81() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapIsEmpty_remove81");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),173,getMap(),172,getMap().isEmpty());
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),175,getMap(),174,getMap().isEmpty());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize");
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_add151() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_add151");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_add152() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_add152");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_add153() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_add153");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_remove123() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_remove123");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_remove124() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_remove124");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_remove125() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_remove125");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.size()
     */
@Test(timeout = 1000)
    public void testMapSize_remove126() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapSize_remove126");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),211,getMap(),210,getMap().size());
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),212,("Map.size() should equal the number of entries " + "in the map"));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),213,getSampleKeys().length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),215,getMap(),214,getMap().size());
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add66() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add66");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add67() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add67");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add68() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add68");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add69() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add69");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add70() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add70");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add71() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add71");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add72() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add72");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add73() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add73");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_add74() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_add74");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove49() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove49");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        getMap().clear();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove50() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove50");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove51() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove51");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove52() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove52");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove53() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove53");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        verify();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove54() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove54");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove55() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove55");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getConfirmed().clear();
        verify();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests {@link Map#clear()}.  If the map {@link #isRemoveSupported()}
     * can add and remove elements}, then {@link Map#size()} and
     * {@link Map#isEmpty()} are used to ensure that map has no elements after
     * a call to clear.  If the map does not support adding and removing
     * elements, this method checks to ensure clear throws an
     * UnsupportedOperationException.
     */
@Test(timeout = 1000)
    public void testMapClear_remove56() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapClear_remove56");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().clear();
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        getMap().clear();
        getConfirmed().clear();
        resetFull();
        getMap().clear();
        getConfirmed().clear();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_add76() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_add76");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        verify();
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_add77() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_add77");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        resetFull();
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_add78() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_add78");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_remove57() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_remove57");
        final Object[] keys = getSampleKeys();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_remove58() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_remove58");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_remove59() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_remove59");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        verify();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsKey(Object) by verifying it returns false for all
     * sample keys on a map created using an empty map and returns true for
     * all sample keys returned on a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsKey_remove60() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsKey_remove60");
        final Object[] keys = getSampleKeys();
        resetEmpty();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),152,!(getMap().containsKey(key)));
        }
        resetFull();
        for (Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),153,(("Map must contain key for a mapping in the map. " + "Missing: ") + key));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),155,getMap(),154,getMap().containsKey(key));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_add79() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_add79");
        final Object[] values = getSampleValues();
        resetEmpty();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_add80() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_add80");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        verify();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_add81() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_add81");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        resetFull();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_add82() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_add82");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
public void testMapContainsValue() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = -1 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_remove61() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_remove61");
        final Object[] values = getSampleValues();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_remove62() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_remove62");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_remove63() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_remove63");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        verify();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.containsValue(Object) by verifying it returns false for all
     * sample values on an empty map and returns true for all sample values on
     * a full map.
     */
@Test(timeout = 1000)
    public void testMapContainsValue_remove64() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapContainsValue_remove64");
        final Object[] values = getSampleValues();
        resetEmpty();
        for (int i = 0 ; i < (values.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),156,!(getMap().containsValue(values[i])));
        }
        resetFull();
        for (final Object value : values) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),158,getMap(),157,getMap().containsValue(value));
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals");
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add84() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add84");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add85() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add85");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add86() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add86");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add87() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add87");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add88() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add88");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add89() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add89");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add90() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add90");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_add91() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_add91");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove65() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove65");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove66() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove66");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove67() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove67");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove68() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove68");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove69() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove69");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove70() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove70");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove71() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove71");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.equals(Object)
     */
@Test(timeout = 1000)
    public void testMapEquals_remove72() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEquals_remove72");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),160,getMap(),159,getMap().equals(confirmed));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),162,getMap(),161,getMap().equals(confirmed));
        verify();
        resetFull();
        final Iterator<K> iter = confirmed.keySet().iterator();
        iter.next();
        iter.remove();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),163,!(getMap().equals(confirmed)));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),164,!(getMap().equals(null)));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),165,!(getMap().equals(new java.lang.Object())));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_add92() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_add92");
        resetEmpty();
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_add93() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_add93");
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_add94() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_add94");
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        resetFull();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
public void testMapGet() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet");
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        resetFull();
        for (int i = -1 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_remove73() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_remove73");
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_remove74() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_remove74");
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.get(Object)
     */
@Test(timeout = 1000)
    public void testMapGet_remove75() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapGet_remove75");
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),166,((getMap().get(key)) == null));
        }
        verify();
        for (int i = 0 ; i < (keys.length) ; i++) {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),167,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),169,getMap(),168,getMap().get(keys[i]));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.hashCode()
     */
@Test(timeout = 1000)
    public void testMapHashCode() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapHashCode");
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),170,((getMap().hashCode()) == (confirmed.hashCode())));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),171,((getMap().hashCode()) == (confirmed.hashCode())));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.hashCode()
     */
@Test(timeout = 1000)
    public void testMapHashCode_add96() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapHashCode_add96");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),170,((getMap().hashCode()) == (confirmed.hashCode())));
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),171,((getMap().hashCode()) == (confirmed.hashCode())));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.hashCode()
     */
@Test(timeout = 1000)
    public void testMapHashCode_remove76() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapHashCode_remove76");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),170,((getMap().hashCode()) == (confirmed.hashCode())));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),171,((getMap().hashCode()) == (confirmed.hashCode())));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.hashCode()
     */
@Test(timeout = 1000)
    public void testMapHashCode_remove77() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapHashCode_remove77");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),170,((getMap().hashCode()) == (confirmed.hashCode())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),171,((getMap().hashCode()) == (confirmed.hashCode())));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString");
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_add155() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_add155");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_add156() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_add156");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        resetFull();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_add157() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_add157");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_remove127() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_remove127");
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_remove128() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_remove128");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_remove129() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_remove129");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.toString().  Since the format of the string returned by the
     * toString() method is not defined in the Map interface, there is no
     * common way to test the results of the toString() method.  Thereforce,
     * it is encouraged that Map implementations override this test with one
     * that checks the format matches any format defined in its API.  This
     * default implementation just verifies that the toString() method does
     * not return null.
     */
@Test(timeout = 1000)
    public void testMapToString_remove130() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapToString_remove130");
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),216,((getMap().toString()) != null));
        resetFull();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),217,((getMap().toString()) != null));
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Compare the current serialized form of the Map
     * against the canonical version in SVN.
     */
public void testEmptyMapCompatibility() throws Exception {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEmptyMapCompatibility");
        final Map<K, V> map = makeObject();
        if (((map instanceof java.io.Serializable) && (!(skipSerializedCanonicalTests()))) && (isTestSerialization())) {
            @SuppressWarnings(value = "unchecked")
            final Map<K, V> map2 = ((Map<K, V>)(readExternalFormFromDisk(getCanonicalEmptyCollectionName(map))));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),57,map2,56,map2.size());
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Compare the current serialized form of the Map
     * against the canonical version in SVN.
     */
public void testFullMapCompatibility() throws Exception {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testFullMapCompatibility");
        final Map<K, V> map = makeFullMap();
        if (((map instanceof java.io.Serializable) && (!(skipSerializedCanonicalTests()))) && (isTestSerialization())) {
            @SuppressWarnings(value = "unchecked")
            final Map<K, V> map2 = ((Map<K, V>)(readExternalFormFromDisk(getCanonicalFullCollectionName(map))));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),114,getSampleKeys().length);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),116,map2,115,map2.size());
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add101() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add101");
        resetEmpty();
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add102() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add102");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add103() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add103");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add104() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add104");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add105() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add105");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add106() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add106");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add107() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add107");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add108() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add108");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add109() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add109");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add110() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add110");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_add111() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_add111");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = -1 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation27() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation27");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 1 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation28() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation28");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[-1], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation29() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation29");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[-1]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation30() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation30");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[1], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation31() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation31");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[-1]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation32() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation32");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = -1;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation33() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation33");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[1], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
public void testMapPut_literalMutation34() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_literalMutation34");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[1]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove82() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove82");
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove83() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove83");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove84() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove84");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove85() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove85");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove86() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove86");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove87() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove87");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove88() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove88");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(Object, Object)
     */
@Test(timeout = 1000)
    public void testMapPut_remove89() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPut_remove89");
        resetEmpty();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        final V[] newValues = getNewSampleValues();
        if (isPutAddSupported()) {
            for (int i = 0 ; i < (keys.length) ; i++) {
                final Object o = getMap().put(keys[i], values[i]);
                getConfirmed().put(keys[i], values[i]);
                verify();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),183,(o == null));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),185,getMap(),184,getMap().containsKey(keys[i]));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),187,getMap(),186,getMap().containsValue(values[i]));
            }
            if (isPutChangeSupported()) {
                for (int i = 0 ; i < (keys.length) ; i++) {
                    final Object o = getMap().put(keys[i], newValues[i]);
                    getConfirmed().put(keys[i], newValues[i]);
                    verify();
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),188,values[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),189,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),191,getMap(),190,getMap().containsKey(keys[i]));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),193,getMap(),192,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),194,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], newValues[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
            }
        } else {
            if (isPutChangeSupported()) {
                resetEmpty();
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final IllegalArgumentException ex) {
                } catch (final UnsupportedOperationException ex) {
                }
                resetFull();
                int i = 0;
                for (final Iterator<K> it = getMap().keySet().iterator() ; (it.hasNext()) && (i < (newValues.length)) ; i++) {
                    final K key = it.next();
                    final V o = getMap().put(key, newValues[i]);
                    final V value = getConfirmed().put(key, newValues[i]);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),195,value);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),196,o);
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),198,getMap(),197,getMap().containsKey(key));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),200,getMap(),199,getMap().containsValue(newValues[i]));
                    if (!(isAllowDuplicateValues())) {
                        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),201,!(getMap().containsValue(values[i])));
                    } 
                }
            } else {
                try {
                    getMap().put(keys[0], values[0]);
                } catch (final UnsupportedOperationException ex) {
                }
            }
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullKey_add135() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_add135");
        resetFull();
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[0]);
            } else {
                try {
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullKey_add136() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_add136");
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[0]);
                getMap().put(null, values[0]);
            } else {
                try {
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullKey_add137() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_add137");
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[0]);
            } else {
                try {
                    getMap().put(null, values[0]);
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
public void testMapPutNullKey() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey");
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[1]);
            } else {
                try {
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
public void testMapPutNullKey_literalMutation44() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_literalMutation44");
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[0]);
            } else {
                try {
                    getMap().put(null, values[-1]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullKey_remove112() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_remove112");
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
                getMap().put(null, values[0]);
            } else {
                try {
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullKey_remove113() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullKey_remove113");
        resetFull();
        final V[] values = getSampleValues();
        if (isPutAddSupported()) {
            if (isAllowNullKey()) {
            } else {
                try {
                    getMap().put(null, values[0]);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullValue_add138() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_add138");
        resetFull();
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[0], null);
            } else {
                try {
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullValue_add139() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_add139");
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[0], null);
                getMap().put(keys[0], null);
            } else {
                try {
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullValue_add140() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_add140");
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[0], null);
            } else {
                try {
                    getMap().put(keys[0], null);
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
public void testMapPutNullValue() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue");
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[-1], null);
            } else {
                try {
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
public void testMapPutNullValue_literalMutation47() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_literalMutation47");
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[0], null);
            } else {
                try {
                    getMap().put(keys[-1], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullValue_remove114() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_remove114");
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
                getMap().put(keys[0], null);
            } else {
                try {
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.put(null, value)
     */
@Test(timeout = 1000)
    public void testMapPutNullValue_remove115() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutNullValue_remove115");
        resetFull();
        final K[] keys = getSampleKeys();
        if (isPutAddSupported()) {
            if (isAllowNullValue()) {
            } else {
                try {
                    getMap().put(keys[0], null);
                } catch (final NullPointerException ex) {
                } catch (final IllegalArgumentException ex) {
                }
            }
        } 
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add112() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add112");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add113() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add113");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add114() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add114");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add115() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add115");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add116() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add116");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add117() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add117");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add118() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add118");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add119() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add119");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add120() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add120");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add121() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add121");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add122() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add122");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add123() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add123");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add124() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add124");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add125() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add125");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add126() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add126");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add127() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add127");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add128() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add128");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add129() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add129");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add130() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add130");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add131() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add131");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add132() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add132");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add133() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add133");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_add134() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_add134");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll_literalMutation36() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_literalMutation36");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[-1], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll_literalMutation37() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_literalMutation37");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[1]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll_literalMutation38() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_literalMutation38");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[-1], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll_literalMutation39() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_literalMutation39");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[1]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
public void testMapPutAll_literalMutation40() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_literalMutation40");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 2 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove100() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove100");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove101() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove101");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove102() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove102");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove103() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove103");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove104() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove104");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove105() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove105");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove106() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove106");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove107() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove107");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove108() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove108");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove109() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove109");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove110() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove110");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove111() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove111");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove90() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove90");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove91() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove91");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove92() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove92");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove93() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove93");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove94() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove94");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove95() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove95");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove96() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove96");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove97() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove97");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove98() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove98");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        resetEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.putAll(map)
     */
@Test(timeout = 1000)
    public void testMapPutAll_remove99() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapPutAll_remove99");
        if (!(isPutAddSupported())) {
            if (!(isPutChangeSupported())) {
                final Map<K, V> temp = makeFullMap();
                resetEmpty();
                try {
                    getMap().putAll(temp);
                } catch (final UnsupportedOperationException ex) {
                }
            } 
            return ;
        } 
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),177,getMap(),176,getMap().size());
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),179,getMap(),178,getMap().size());
        resetFull();
        final int size = getMap().size();
        getMap().putAll(new HashMap<K, V>());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),180,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),182,getMap(),181,getMap().size());
        resetEmpty();
        Map<K, V> m2 = makeFullMap();
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        for (int i = 0 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        resetEmpty();
        m2 = makeConfirmedMap();
        getMap().put(keys[0], values[0]);
        getConfirmed().put(keys[0], values[0]);
        verify();
        for (int i = 1 ; i < (keys.length) ; i++) {
            m2.put(keys[i], values[i]);
        }
        getMap().putAll(m2);
        getConfirmed().putAll(m2);
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add141() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add141");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add142() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add142");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add143() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add143");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add144() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add144");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add145() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add145");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add146() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add146");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add147() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add147");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add148() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add148");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_add149() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_add149");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
public void testMapRemove() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 1 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove116() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove116");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove117() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove117");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove118() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove118");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove119() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove119");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove120() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove120");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove121() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove121");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        verify();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests Map.remove(Object)
     */
@Test(timeout = 1000)
    public void testMapRemove_remove122() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapRemove_remove122");
        if (!(isRemoveSupported())) {
            try {
                resetFull();
                getMap().remove(getMap().keySet().iterator().next());
            } catch (final UnsupportedOperationException ex) {
            }
            return ;
        } 
        resetEmpty();
        final Object[] keys = getSampleKeys();
        final Object[] values = getSampleValues();
        for (final Object key : keys) {
            final Object o = getMap().remove(key);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),202,(o == null));
        }
        resetFull();
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Object o = getMap().remove(keys[i]);
            getConfirmed().remove(keys[i]);
            verify();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),203,values[i]);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),204,o);
        }
        final Object[] other = getOtherKeys();
        resetFull();
        final int size = getMap().size();
        for (final Object element : other) {
            final Object o = getMap().remove(element);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),205,o);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),206,("map.remove for nonexistent key should not " + "shrink map"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),207,size);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),209,getMap(),208,getMap().size());
        }
        verify();
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_add159() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_add159");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_add160() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_add160");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_add161() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_add161");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_remove131() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_remove131");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_remove132() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_remove132");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_remove133() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_remove133");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testValuesClearChangesMap_remove134() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesClearChangesMap_remove134");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Collection<V> values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),236,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),237,((values.size()) > 0));
        values.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),238,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),239,((values.size()) == 0));
        resetFull();
        values = getMap().values();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),240,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),241,((values.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),242,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),243,((values.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_add56() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_add56");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_add57() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_add57");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_add58() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_add58");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_remove41() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_remove41");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_remove42() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_remove42");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_remove43() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_remove43");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testKeySetClearChangesMap_remove44() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetClearChangesMap_remove44");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<K> keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),117,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),118,((keySet.size()) > 0));
        keySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),119,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),120,((keySet.size()) == 0));
        resetFull();
        keySet = getMap().keySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),121,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),122,((keySet.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),123,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),124,((keySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_add38() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_add38");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_add39() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_add39");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_add40() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_add40");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_remove25() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_remove25");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_remove26() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_remove26");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_remove27() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_remove27");
        if (!(isRemoveSupported())) {
            return ;
        } 
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        getMap().clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet()} collection is backed by
     * the underlying map for clear().
     */
@Test(timeout = 1000)
    public void testEntrySetClearChangesMap_remove28() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetClearChangesMap_remove28");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),58,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),59,((entrySet.size()) > 0));
        entrySet.clear();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),60,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),61,((entrySet.size()) == 0));
        resetFull();
        entrySet = getMap().entrySet();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),62,((getMap().size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),63,((entrySet.size()) > 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),64,((getMap().size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),65,((entrySet.size()) == 0));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetContains1() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains1");
        resetFull();
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),67,entrySet,66,entrySet.contains(entry));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetContains1_remove29() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains1_remove29");
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),67,entrySet,66,entrySet.contains(entry));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetContains2() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains2");
        resetFull();
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final Map.Entry<K, V> test = AbstractMapTest.cloneMapEntry(entry);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),69,entrySet,68,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetContains2_remove30() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains2_remove30");
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final Map.Entry<K, V> test = AbstractMapTest.cloneMapEntry(entry);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),69,entrySet,68,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetContains3_add43() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3_add43");
        resetFull();
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetContains3_add44() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3_add44");
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    public void testEntrySetContains3() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3");
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    public void testEntrySetContains3_literalMutation13() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3_literalMutation13");
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("foo")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetContains3_remove31() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3_remove31");
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetContains3_remove32() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetContains3_remove32");
        resetFull();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final HashMap<K, V> temp = new HashMap<K, V>();
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),71,entrySet,70,entrySet.contains(test));
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetRemove1() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove1");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),75,entrySet,74,entrySet.remove(entry));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),77,getMap(),76,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),78,(size - 1));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),80,getMap(),79,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetRemove1_remove34() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove1_remove34");
        if (!(isRemoveSupported())) {
            return ;
        } 
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),75,entrySet,74,entrySet.remove(entry));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),77,getMap(),76,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),78,(size - 1));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),80,getMap(),79,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetRemove2() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove2");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final Map.Entry<K, V> test = AbstractMapTest.cloneMapEntry(entry);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),82,entrySet,81,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),84,getMap(),83,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),85,(size - 1));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),87,getMap(),86,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @Test(timeout = 1000)
    public void testEntrySetRemove2_remove35() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove2_remove35");
        if (!(isRemoveSupported())) {
            return ;
        } 
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final Map.Entry<K, V> test = AbstractMapTest.cloneMapEntry(entry);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),82,entrySet,81,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),84,getMap(),83,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),85,(size - 1));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),87,getMap(),86,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetRemove3_add49() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3_add49");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetRemove3_add50() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3_add50");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    public void testEntrySetRemove3() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    public void testEntrySetRemove3_literalMutation15() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3_literalMutation15");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("foo")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetRemove3_remove36() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3_remove36");
        if (!(isRemoveSupported())) {
            return ;
        } 
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        temp.put(entry.getKey(), ((V)("A VERY DIFFERENT VALUE")));
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    @SuppressWarnings(value = "unchecked")
    @Test(timeout = 1000)
    public void testEntrySetRemove3_remove37() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemove3_remove37");
        if (!(isRemoveSupported())) {
            return ;
        } 
        resetFull();
        final int size = getMap().size();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        final K key = entry.getKey();
        final HashMap<K, V> temp = new HashMap<K, V>();
        final Map.Entry<K, V> test = temp.entrySet().iterator().next();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),89,entrySet,88,entrySet.remove(test));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),91,getMap(),90,getMap().containsKey(key));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),92,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),94,getMap(),93,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
@Test(timeout = 1000)
    public void testValuesRemoveChangesMap_add168() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap_add168");
        resetFull();
        resetFull();
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 0 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 0;
                while ((values.contains(sampleValues[i])) && (j < 10000)) {
                    try {
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
@Test(timeout = 1000)
    public void testValuesRemoveChangesMap_add169() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap_add169");
        resetFull();
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 0 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 0;
                while ((values.contains(sampleValues[i])) && (j < 10000)) {
                    try {
                        values.remove(sampleValues[i]);
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
public void testValuesRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap");
        resetFull();
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 1 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 0;
                while ((values.contains(sampleValues[i])) && (j < 10000)) {
                    try {
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
public void testValuesRemoveChangesMap_literalMutation61() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap_literalMutation61");
        resetFull();
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 0 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 1;
                while ((values.contains(sampleValues[i])) && (j < 10000)) {
                    try {
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
public void testValuesRemoveChangesMap_literalMutation62() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap_literalMutation62");
        resetFull();
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 0 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 0;
                while ((values.contains(sampleValues[i])) && (j < 10001)) {
                    try {
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#values} collection is backed by
     * the underlying map by removing from the values collection
     * and testing if the value was removed from the map.
     * <p>
     * We should really test the "vice versa" case--that values removed
     * from the map are removed from the values collection--also,
     * but that's a more difficult test to construct (lacking a
     * "removeValue" method.)
     * </p>
     * <p>
     * See bug <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=9573">
     * 9573</a>.
     * </p>
     */
@Test(timeout = 1000)
    public void testValuesRemoveChangesMap_remove139() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveChangesMap_remove139");
        final V[] sampleValues = getSampleValues();
        final Collection<V> values = getMap().values();
        for (int i = 0 ; i < (sampleValues.length) ; i++) {
            if (map.containsValue(sampleValues[i])) {
                int j = 0;
                while ((values.contains(sampleValues[i])) && (j < 10000)) {
                    try {
                        values.remove(sampleValues[i]);
                    } catch (final UnsupportedOperationException e) {
                        return ;
                    }
                    j++;
                }
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),261,(j < 10000));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),262,!(getMap().containsValue(sampleValues[i])));
            } 
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests values.removeAll.
     */
@Test(timeout = 1000)
    public void testValuesRemoveAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveAll");
        resetFull();
        resetFull();
        final Collection<V> values = getMap().values();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        if (!(values.equals(sampleValuesAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),252,values,251,values.removeAll(java.util.Collections.<V>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),254,sampleValuesAsList,253,sampleValuesAsList.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),256,getMap(),255,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),258,values,257,values.removeAll(sampleValuesAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),260,getMap(),259,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests values.removeAll.
     */
@Test(timeout = 1000)
    public void testValuesRemoveAll_remove138() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRemoveAll_remove138");
        final Collection<V> values = getMap().values();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        if (!(values.equals(sampleValuesAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),252,values,251,values.removeAll(java.util.Collections.<V>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),254,sampleValuesAsList,253,sampleValuesAsList.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),256,getMap(),255,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),258,values,257,values.removeAll(sampleValuesAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),260,getMap(),259,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test values.retainAll.
     */
@Test(timeout = 1000)
    public void testValuesRetainAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRetainAll");
        resetFull();
        resetFull();
        final Collection<V> values = getMap().values();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        if (!(values.equals(sampleValuesAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),264,values,263,values.retainAll(sampleValuesAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),266,sampleValuesAsList,265,sampleValuesAsList.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),268,getMap(),267,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),270,values,269,values.retainAll(java.util.Collections.<V>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),272,getMap(),271,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test values.retainAll.
     */
@Test(timeout = 1000)
    public void testValuesRetainAll_remove140() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesRetainAll_remove140");
        final Collection<V> values = getMap().values();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        if (!(values.equals(sampleValuesAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),264,values,263,values.retainAll(sampleValuesAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),266,sampleValuesAsList,265,sampleValuesAsList.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),268,getMap(),267,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),270,values,269,values.retainAll(java.util.Collections.<V>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),272,getMap(),271,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_add162() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_add162");
        resetFull();
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_add163() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_add163");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_add164() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_add164");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_add165() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_add165");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_add166() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_add166");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap_literalMutation55() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_literalMutation55");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > -1;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap_literalMutation56() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_literalMutation56");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("foo");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap_literalMutation57() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_literalMutation57");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "foo" : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap_literalMutation58() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_literalMutation58");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "foo"));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    public void testValuesIteratorRemoveChangesMap_literalMutation59() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_literalMutation59");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("foo");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_remove135() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_remove135");
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append((expected ? "yet " : "no longer "));
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_remove136() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_remove136");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verifies that values.iterator.remove changes the underlying map.
     */
@SuppressWarnings(value = "boxing")
    @Test(timeout = 1000)
    public void testValuesIteratorRemoveChangesMap_remove137() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testValuesIteratorRemoveChangesMap_remove137");
        resetFull();
        final List<V> sampleValuesAsList = java.util.Arrays.asList(getSampleValues());
        final Map<V, java.lang.Integer> cardinality = org.apache.commons.collections4.CollectionUtils.getCardinalityMap(sampleValuesAsList);
        final Collection<V> values = getMap().values();
        for (final Iterator<V> iter = values.iterator() ; iter.hasNext() ; ) {
            final V value = iter.next();
            Integer count = cardinality.get(value);
            if (count == null) {
                return ;
            } 
            try {
                iter.remove();
                cardinality.put(value, --count);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            final boolean expected = count > 0;
            final StringBuilder msg = new StringBuilder("Value should ");
            msg.append("be present in the underlying map");
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),245,msg,244,msg.toString());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),246,expected);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),248,getMap(),247,getMap().containsValue(value));
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),250,getMap(),249,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} set is backed by
     * the underlying map by removing from the keySet set
     * and testing if the key was removed from the map.
     */
@Test(timeout = 1000)
    public void testKeySetRemoveChangesMap_add62() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveChangesMap_add62");
        resetFull();
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final Set<K> keys = getMap().keySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                keys.remove(sampleKeys[i]);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),135,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} set is backed by
     * the underlying map by removing from the keySet set
     * and testing if the key was removed from the map.
     */
@Test(timeout = 1000)
    public void testKeySetRemoveChangesMap_add63() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveChangesMap_add63");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final Set<K> keys = getMap().keySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                keys.remove(sampleKeys[i]);
                keys.remove(sampleKeys[i]);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),135,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} set is backed by
     * the underlying map by removing from the keySet set
     * and testing if the key was removed from the map.
     */
public void testKeySetRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveChangesMap");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final Set<K> keys = getMap().keySet();
        for (int i = 1 ; i < (sampleKeys.length) ; i++) {
            try {
                keys.remove(sampleKeys[i]);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),135,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#keySet} set is backed by
     * the underlying map by removing from the keySet set
     * and testing if the key was removed from the map.
     */
@Test(timeout = 1000)
    public void testKeySetRemoveChangesMap_remove47() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveChangesMap_remove47");
        final K[] sampleKeys = getSampleKeys();
        final Set<K> keys = getMap().keySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                keys.remove(sampleKeys[i]);
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),135,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test keySet.removeAll.
     */
@Test(timeout = 1000)
    public void testKeySetRemoveAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveAll");
        resetFull();
        resetFull();
        final Set<K> keys = getMap().keySet();
        final List<K> sampleKeysAsList = java.util.Arrays.asList(getSampleKeys());
        if (!(keys.equals(sampleKeysAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),128,keys,127,keys.removeAll(java.util.Collections.<K>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),129,sampleKeysAsList);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),130,keys);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),132,keys,131,keys.removeAll(sampleKeysAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),134,getMap(),133,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test keySet.removeAll.
     */
@Test(timeout = 1000)
    public void testKeySetRemoveAll_remove46() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRemoveAll_remove46");
        final Set<K> keys = getMap().keySet();
        final List<K> sampleKeysAsList = java.util.Arrays.asList(getSampleKeys());
        if (!(keys.equals(sampleKeysAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),128,keys,127,keys.removeAll(java.util.Collections.<K>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),129,sampleKeysAsList);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),130,keys);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),132,keys,131,keys.removeAll(sampleKeysAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),134,getMap(),133,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test keySet.retainAll.
     */
@Test(timeout = 1000)
    public void testKeySetRetainAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRetainAll");
        resetFull();
        resetFull();
        final Set<K> keys = getMap().keySet();
        final List<K> sampleKeysAsList = java.util.Arrays.asList(getSampleKeys());
        if (!(keys.equals(sampleKeysAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),137,keys,136,keys.retainAll(sampleKeysAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),138,sampleKeysAsList);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),139,keys);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),141,keys,140,keys.retainAll(java.util.Collections.<K>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),143,getMap(),142,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test keySet.retainAll.
     */
@Test(timeout = 1000)
    public void testKeySetRetainAll_remove48() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetRetainAll_remove48");
        final Set<K> keys = getMap().keySet();
        final List<K> sampleKeysAsList = java.util.Arrays.asList(getSampleKeys());
        if (!(keys.equals(sampleKeysAsList))) {
            return ;
        } 
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),137,keys,136,keys.retainAll(sampleKeysAsList));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),138,sampleKeysAsList);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),139,keys);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),141,keys,140,keys.retainAll(java.util.Collections.<K>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),143,getMap(),142,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that keySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testKeySetIteratorRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetIteratorRemoveChangesMap");
        resetFull();
        resetFull();
        for (final Iterator<K> iter = getMap().keySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next();
            try {
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),126,getMap(),125,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that keySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testKeySetIteratorRemoveChangesMap_add60() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetIteratorRemoveChangesMap_add60");
        resetFull();
        for (final Iterator<K> iter = getMap().keySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next();
            try {
                iter.remove();
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),126,getMap(),125,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that keySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testKeySetIteratorRemoveChangesMap_remove45() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testKeySetIteratorRemoveChangesMap_remove45");
        for (final Iterator<K> iter = getMap().keySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next();
            try {
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),126,getMap(),125,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet} set is backed by
     * the underlying map by removing from the entrySet set
     * and testing if the entry was removed from the map.
     */
@Test(timeout = 1000)
    public void testEntrySetRemoveChangesMap_add52() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveChangesMap_add52");
        resetFull();
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                entrySet.remove(new org.apache.commons.collections4.keyvalue.DefaultMapEntry<K, V>(sampleKeys[i] , sampleValues[i]));
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),104,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet} set is backed by
     * the underlying map by removing from the entrySet set
     * and testing if the entry was removed from the map.
     */
@Test(timeout = 1000)
    public void testEntrySetRemoveChangesMap_add53() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveChangesMap_add53");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                entrySet.remove(new org.apache.commons.collections4.keyvalue.DefaultMapEntry<K, V>(sampleKeys[i] , sampleValues[i]));
                entrySet.remove(new org.apache.commons.collections4.keyvalue.DefaultMapEntry<K, V>(sampleKeys[i] , sampleValues[i]));
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),104,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet} set is backed by
     * the underlying map by removing from the entrySet set
     * and testing if the entry was removed from the map.
     */
public void testEntrySetRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveChangesMap");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        for (int i = 1 ; i < (sampleKeys.length) ; i++) {
            try {
                entrySet.remove(new org.apache.commons.collections4.keyvalue.DefaultMapEntry<K, V>(sampleKeys[i] , sampleValues[i]));
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),104,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Tests that the {@link Map#entrySet} set is backed by
     * the underlying map by removing from the entrySet set
     * and testing if the entry was removed from the map.
     */
@Test(timeout = 1000)
    public void testEntrySetRemoveChangesMap_remove39() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveChangesMap_remove39");
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            try {
                entrySet.remove(new org.apache.commons.collections4.keyvalue.DefaultMapEntry<K, V>(sampleKeys[i] , sampleValues[i]));
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),104,!(getMap().containsKey(sampleKeys[i])));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.removeAll.
     */
@Test(timeout = 1000)
    public void testEntrySetRemoveAll_add51() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveAll_add51");
        resetFull();
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),96,entrySet,95,entrySet.removeAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),97,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),99,getMap(),98,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),101,entrySet,100,entrySet.removeAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),103,getMap(),102,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.removeAll.
     */
public void testEntrySetRemoveAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveAll");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = -1 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),96,entrySet,95,entrySet.removeAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),97,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),99,getMap(),98,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),101,entrySet,100,entrySet.removeAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),103,getMap(),102,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.removeAll.
     */
@Test(timeout = 1000)
    public void testEntrySetRemoveAll_remove38() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRemoveAll_remove38");
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),96,entrySet,95,entrySet.removeAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),97,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),99,getMap(),98,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),101,entrySet,100,entrySet.removeAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),103,getMap(),102,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.retainAll.
     */
@Test(timeout = 1000)
    public void testEntrySetRetainAll_add54() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRetainAll_add54");
        resetFull();
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),106,entrySet,105,entrySet.retainAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),107,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),109,getMap(),108,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),111,entrySet,110,entrySet.retainAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),113,getMap(),112,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.retainAll.
     */
public void testEntrySetRetainAll() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRetainAll");
        resetFull();
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = 1 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),106,entrySet,105,entrySet.retainAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),107,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),109,getMap(),108,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),111,entrySet,110,entrySet.retainAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),113,getMap(),112,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Test entrySet.retainAll.
     */
@Test(timeout = 1000)
    public void testEntrySetRetainAll_remove40() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetRetainAll_remove40");
        final K[] sampleKeys = getSampleKeys();
        final V[] sampleValues = getSampleValues();
        for (int i = 0 ; i < (sampleKeys.length) ; i++) {
            if (!(getMap().containsKey(sampleKeys[i]))) {
                return ;
            } 
            final V value = sampleValues[i];
            final V test = getMap().get(sampleKeys[i]);
            if ((value == test) || ((value != null) && (value.equals(test)))) {
                continue;
            } 
            return ;
        }
        final Set<java.util.Map.Entry<K, V>> entrySet = getMap().entrySet();
        final HashSet<java.util.Map.Entry<K, V>> comparisonSet = new HashSet<java.util.Map.Entry<K, V>>(entrySet);
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),106,entrySet,105,entrySet.retainAll(comparisonSet));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),107,sampleKeys.length);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),109,getMap(),108,getMap().size());
        try {
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),111,entrySet,110,entrySet.retainAll(java.util.Collections.<java.util.Map.Entry<K, V>>emptySet()));
        } catch (final UnsupportedOperationException e) {
            return ;
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),113,getMap(),112,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that entrySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testEntrySetIteratorRemoveChangesMap() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetIteratorRemoveChangesMap");
        resetFull();
        resetFull();
        for (final Iterator<java.util.Map.Entry<K, V>> iter = getMap().entrySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next().getKey();
            try {
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),73,getMap(),72,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that entrySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testEntrySetIteratorRemoveChangesMap_add46() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetIteratorRemoveChangesMap_add46");
        resetFull();
        for (final Iterator<java.util.Map.Entry<K, V>> iter = getMap().entrySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next().getKey();
            try {
                iter.remove();
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),73,getMap(),72,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Verify that entrySet.iterator.remove changes the underlying map.
     */
@Test(timeout = 1000)
    public void testEntrySetIteratorRemoveChangesMap_remove33() {
        fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testEntrySetIteratorRemoveChangesMap_remove33");
        for (final Iterator<java.util.Map.Entry<K, V>> iter = getMap().entrySet().iterator() ; iter.hasNext() ; ) {
            final K key = iter.next().getKey();
            try {
                iter.remove();
            } catch (final UnsupportedOperationException e) {
                return ;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),73,getMap(),72,getMap().containsKey(key));
        }
        fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
    }

    /** 
     * Utility methods to create an array of Map.Entry objects
     * out of the given key and value arrays.<P>
     * 
     * @param keys   the array of keys
     * @param values the array of values
     * @return an array of Map.Entry of those keys to those values
     */
@SuppressWarnings(value = "unchecked")
    private Map.Entry<K, V>[] makeEntryArray(final K[] keys, final V[] values) {
        final Map.Entry<K, V>[] result = new Map.Entry[keys.length];
        for (int i = 0 ; i < (keys.length) ; i++) {
            final Map<K, V> map = makeConfirmedMap();
            map.put(keys[i], values[i]);
            result[i] = map.entrySet().iterator().next();
        }
        return result;
    }

    /** 
     * Bulk test {@link Map#entrySet()}.  This method runs through all of
     * the tests in {@link AbstractSetTest}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the map and the other collection views are still valid.
     * 
     * @return a {@link AbstractSetTest} instance for testing the map's entry set
     */
public BulkTest bulkTestMapEntrySet() {
        return new TestMapEntrySet();
    }

    public class TestMapEntrySet extends AbstractSetTest<java.util.Map.Entry<K, V>> {
        public TestMapEntrySet() {
            super("MapEntrySet");
        }

        /** 
         * {@inheritDoc}
         */
@Override
        public Map.Entry<K, V>[] getFullElements() {
            return getFullNonNullElements();
        }

        /** 
         * {@inheritDoc}
         */
@Override
        public Map.Entry<K, V>[] getFullNonNullElements() {
            final K[] k = getSampleKeys();
            final V[] v = getSampleValues();
            return makeEntryArray(k, v);
        }

        @Override
        public Map.Entry<K, V>[] getOtherElements() {
            final K[] k = getOtherKeys();
            final V[] v = getOtherValues();
            return makeEntryArray(k, v);
        }

        @Override
        public Set<java.util.Map.Entry<K, V>> makeObject() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.makeObject().entrySet();
        }

        @Override
        public Set<java.util.Map.Entry<K, V>> makeFullCollection() {
            return makeFullMap().entrySet();
        }

        @Override
        public boolean isAddSupported() {
            return false;
        }

        @Override
        public boolean isRemoveSupported() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isRemoveSupported();
        }

        public boolean isGetStructuralModify() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isGetStructuralModify();
        }

        @Override
        public boolean isTestSerialization() {
            return false;
        }

        @Override
        public void resetFull() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetFull();
            setCollection(org.apache.commons.collections4.map.AbstractMapTest.this.getMap().entrySet());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().entrySet());
        }

        @Override
        public void resetEmpty() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetEmpty();
            setCollection(org.apache.commons.collections4.map.AbstractMapTest.this.getMap().entrySet());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().entrySet());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntry_add23() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntry_add23");
            resetFull();
            resetFull();
            final Iterator<java.util.Map.Entry<K, V>> it = getCollection().iterator();
            int count = 0;
            while (it.hasNext()) {
                final Map.Entry<K, V> entry = it.next();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),33,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),32,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry.getKey()));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),35,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),34,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(entry.getValue()));
                if (!(isGetStructuralModify())) {
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),37,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),36,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry.getKey()));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),39,entry,38,entry.getValue());
                } 
                count++;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),41,getCollection(),40,getCollection().size());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),42,count);
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntry() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntry");
            resetFull();
            final Iterator<java.util.Map.Entry<K, V>> it = getCollection().iterator();
            int count = 1;
            while (it.hasNext()) {
                final Map.Entry<K, V> entry = it.next();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),33,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),32,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry.getKey()));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),35,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),34,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(entry.getValue()));
                if (!(isGetStructuralModify())) {
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),37,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),36,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry.getKey()));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),39,entry,38,entry.getValue());
                } 
                count++;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),41,getCollection(),40,getCollection().size());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),42,count);
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntry_remove12() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntry_remove12");
            final Iterator<java.util.Map.Entry<K, V>> it = getCollection().iterator();
            int count = 0;
            while (it.hasNext()) {
                final Map.Entry<K, V> entry = it.next();
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),33,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),32,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry.getKey()));
                fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),35,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),34,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(entry.getValue()));
                if (!(isGetStructuralModify())) {
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),37,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),36,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry.getKey()));
                    fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),39,entry,38,entry.getValue());
                } 
                count++;
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),41,getCollection(),40,getCollection().size());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),42,count);
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add24() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add24");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add25() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add25");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add26() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add26");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add27() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add27");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add28() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add28");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add29() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add29");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add30() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add30");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add31() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add31");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add32() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add32");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add33() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add33");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add34() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add34");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_add35() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_add35");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue");
            final K key1 = getSampleKeys()[-1];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation3() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation3");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 0 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation4() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation4");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[1] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation5() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation5");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[0];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation6() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation6");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[1];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation7() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation7");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 2 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation8() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation8");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[1] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public void testMapEntrySetIteratorEntrySetValue_literalMutation9() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_literalMutation9");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[2];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove13() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove13");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove14() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove14");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove15() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove15");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove16() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove16");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove17() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove17");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove18() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove18");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove19() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove19");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove20() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove20");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove21() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove21");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove22() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove22");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            verify();
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetIteratorEntrySetValue_remove23() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetIteratorEntrySetValue_remove23");
            final K key1 = getSampleKeys()[0];
            final K key2 = (getSampleKeys().length) == 1 ? getSampleKeys()[0] : getSampleKeys()[1];
            final V newValue1 = getNewSampleValues()[0];
            final V newValue2 = (getNewSampleValues().length) == 1 ? getNewSampleValues()[0] : getNewSampleValues()[1];
            resetFull();
            Iterator<java.util.Map.Entry<K, V>> it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry1 = getEntry(it, key1);
            it = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getCollection().iterator();
            final Map.Entry<K, V> entry2 = getEntry(it, key2);
            Iterator<java.util.Map.Entry<K, V>> itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed1 = getEntry(itConfirmed, key1);
            itConfirmed = org.apache.commons.collections4.map.AbstractMapTest.TestMapEntrySet.this.getConfirmed().iterator();
            final Map.Entry<K, V> entryConfirmed2 = getEntry(itConfirmed, key2);
            if (!(isSetValueSupported())) {
                try {
                    entry1.setValue(newValue1);
                } catch (final UnsupportedOperationException ex) {
                }
                return ;
            } 
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),2,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),4,entry1,3,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),6,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),5,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),8,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),7,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),9,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),11,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),10,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry1.setValue(newValue1);
            entryConfirmed1.setValue(newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),12,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),14,entry1,13,entry1.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),16,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),15,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry1.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),18,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),17,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue1));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),19,newValue1);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),21,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),20,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry1.getKey()));
            verify();
            entry2.setValue(newValue2);
            entryConfirmed2.setValue(newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),22,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),24,entry2,23,entry2.getValue());
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),26,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),25,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsKey(entry2.getKey()));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),28,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),27,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().containsValue(newValue2));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),29,newValue2);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),31,org.apache.commons.collections4.map.AbstractMapTest.this.getMap(),30,org.apache.commons.collections4.map.AbstractMapTest.this.getMap().get(entry2.getKey()));
            verify();
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        public Map.Entry<K, V> getEntry(final Iterator<java.util.Map.Entry<K, V>> itConfirmed, final K key) {
            Map.Entry<K, V> entry = null;
            while (itConfirmed.hasNext()) {
                final Map.Entry<K, V> temp = itConfirmed.next();
                if ((temp.getKey()) == null) {
                    if (key == null) {
                        entry = temp;
                        break;
                    } 
                } else {
                    if (temp.getKey().equals(key)) {
                        entry = temp;
                        break;
                    } 
                }
            }
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),0,(("No matching entry in map for key \'" + key) + "\'"));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),1,entry);
            return entry;
        }

        @Test(timeout = 1000)
        public void testMapEntrySetRemoveNonMapEntry() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetRemoveNonMapEntry");
            if (!(isRemoveSupported())) {
                return ;
            } 
            resetFull();
            resetFull();
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),44,getCollection(),43,getCollection().remove(null));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),46,getCollection(),45,getCollection().remove(new java.lang.Object()));
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Test(timeout = 1000)
        public void testMapEntrySetRemoveNonMapEntry_remove24() {
            fr.inria.diversify.testamplification.logger.Logger.writeTestStart(Thread.currentThread(),this, "testMapEntrySetRemoveNonMapEntry_remove24");
            if (!(isRemoveSupported())) {
                return ;
            } 
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),44,getCollection(),43,getCollection().remove(null));
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),46,getCollection(),45,getCollection().remove(new java.lang.Object()));
            fr.inria.diversify.testamplification.logger.Logger.writeTestFinish(Thread.currentThread());
        }

        @Override
        public void verify() {
            super.verify();
            org.apache.commons.collections4.map.AbstractMapTest.this.verify();
        }
    }

    /** 
     * Bulk test {@link Map#keySet()}.  This method runs through all of
     * the tests in {@link AbstractSetTest}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the map and the other collection views are still valid.
     * 
     * @return a {@link AbstractSetTest} instance for testing the map's key set
     */
public BulkTest bulkTestMapKeySet() {
        return new TestMapKeySet();
    }

    public class TestMapKeySet extends AbstractSetTest<K> {
        public TestMapKeySet() {
            super("");
        }

        @Override
        public K[] getFullElements() {
            return getSampleKeys();
        }

        @Override
        public K[] getOtherElements() {
            return getOtherKeys();
        }

        @Override
        public Set<K> makeObject() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.makeObject().keySet();
        }

        @Override
        public Set<K> makeFullCollection() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.makeFullMap().keySet();
        }

        @Override
        public boolean isNullSupported() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isAllowNullKey();
        }

        @Override
        public boolean isAddSupported() {
            return false;
        }

        @Override
        public boolean isRemoveSupported() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isRemoveSupported();
        }

        @Override
        public boolean isTestSerialization() {
            return false;
        }

        @Override
        public void resetEmpty() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetEmpty();
            setCollection(org.apache.commons.collections4.map.AbstractMapTest.this.getMap().keySet());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapKeySet.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().keySet());
        }

        @Override
        public void resetFull() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetFull();
            setCollection(org.apache.commons.collections4.map.AbstractMapTest.this.getMap().keySet());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapKeySet.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().keySet());
        }

        @Override
        public void verify() {
            super.verify();
            org.apache.commons.collections4.map.AbstractMapTest.this.verify();
        }
    }

    /** 
     * Bulk test {@link Map#values()}.  This method runs through all of
     * the tests in {@link AbstractCollectionTest}.
     * After modification operations, {@link #verify()} is invoked to ensure
     * that the map and the other collection views are still valid.
     * 
     * @return a {@link AbstractCollectionTest} instance for testing the map's
     * values collection
     */
public BulkTest bulkTestMapValues() {
        return new TestMapValues();
    }

    public class TestMapValues extends AbstractCollectionTest<V> {
        public TestMapValues() {
            super("");
        }

        @Override
        public V[] getFullElements() {
            return getSampleValues();
        }

        @Override
        public V[] getOtherElements() {
            return getOtherValues();
        }

        @Override
        public Collection<V> makeObject() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.makeObject().values();
        }

        @Override
        public Collection<V> makeFullCollection() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.makeFullMap().values();
        }

        @Override
        public boolean isNullSupported() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isAllowNullKey();
        }

        @Override
        public boolean isAddSupported() {
            return false;
        }

        @Override
        public boolean isRemoveSupported() {
            return org.apache.commons.collections4.map.AbstractMapTest.this.isRemoveSupported();
        }

        @Override
        public boolean isTestSerialization() {
            return false;
        }

        @Override
        public boolean areEqualElementsDistinguishable() {
            return true;
        }

        @Override
        public Collection<V> makeConfirmedCollection() {
            return null;
        }

        @Override
        public Collection<V> makeConfirmedFullCollection() {
            return null;
        }

        @Override
        public void resetFull() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetFull();
            setCollection(map.values());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapValues.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().values());
        }

        @Override
        public void resetEmpty() {
            org.apache.commons.collections4.map.AbstractMapTest.this.resetEmpty();
            setCollection(map.values());
            org.apache.commons.collections4.map.AbstractMapTest.TestMapValues.this.setConfirmed(org.apache.commons.collections4.map.AbstractMapTest.this.getConfirmed().values());
        }

        @Override
        public void verify() {
            super.verify();
            org.apache.commons.collections4.map.AbstractMapTest.this.verify();
        }
    }

    /** 
     * Resets the {@link #map}, {@link #entrySet}, {@link #keySet},
     * {@link #values} and {@link #confirmed} fields to empty.
     */
public void resetEmpty() {
        this.map = makeObject();
        views();
        this.confirmed = makeConfirmedMap();
    }

    /** 
     * Resets the {@link #map}, {@link #entrySet}, {@link #keySet},
     * {@link #values} and {@link #confirmed} fields to full.
     */
public void resetFull() {
        this.map = makeFullMap();
        views();
        this.confirmed = makeConfirmedMap();
        final K[] k = getSampleKeys();
        final V[] v = getSampleValues();
        for (int i = 0 ; i < (k.length) ; i++) {
            confirmed.put(k[i], v[i]);
        }
    }

    /** 
     * Resets the collection view fields.
     */
private void views() {
        this.keySet = getMap().keySet();
        this.entrySet = getMap().entrySet();
    }

    /** 
     * Verifies that {@link #map} is still equal to {@link #confirmed}.
     * This method checks that the map is equal to the HashMap,
     * <I>and</I> that the map's collection views are still equal to
     * the HashMap's collection views.  An <Code>equals</Code> test
     * is done on the maps and their collection views; their size and
     * <Code>isEmpty</Code> results are compared; their hashCodes are
     * compared; and <Code>containsAll</Code> tests are run on the
     * collection views.
     */
public void verify() {
        verifyMap();
        verifyEntrySet();
        verifyKeySet();
        verifyValues();
    }

    public void verifyMap() {
        final int size = getConfirmed().size();
        final boolean empty = getConfirmed().isEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),311,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),313,getMap(),312,getMap().size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),314,empty);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),316,getMap(),315,getMap().isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),318,getConfirmed(),317,getConfirmed().hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),320,getMap(),319,getMap().hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),322,getMap(),321,getMap().equals(getConfirmed()));
    }

    public void verifyEntrySet() {
        final int size = getConfirmed().size();
        final boolean empty = getConfirmed().isEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),273,(((("entrySet should be same size as HashMap\'s" + "\nTest: ") + (entrySet)) + "\nReal: ") + (getConfirmed().entrySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),274,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),276,entrySet,275,entrySet.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),277,(((("entrySet should be empty if HashMap is" + "\nTest: ") + (entrySet)) + "\nReal: ") + (getConfirmed().entrySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),278,empty);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),280,entrySet,279,entrySet.isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),281,(((("entrySet should contain all HashMap\'s elements" + "\nTest: ") + (entrySet)) + "\nReal: ") + (getConfirmed().entrySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),283,entrySet,282,entrySet.containsAll(getConfirmed().entrySet()));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),284,(((("entrySet hashCodes should be the same" + "\nTest: ") + (entrySet)) + "\nReal: ") + (getConfirmed().entrySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),286,getConfirmed().entrySet(),285,getConfirmed().entrySet().hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),288,entrySet,287,entrySet.hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),290,getConfirmed(),289,getConfirmed().entrySet());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),291,entrySet);
    }

    public void verifyKeySet() {
        final int size = getConfirmed().size();
        final boolean empty = getConfirmed().isEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),292,(((("keySet should be same size as HashMap\'s" + "\nTest: ") + (keySet)) + "\nReal: ") + (getConfirmed().keySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),293,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),295,keySet,294,keySet.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),296,(((("keySet should be empty if HashMap is" + "\nTest: ") + (keySet)) + "\nReal: ") + (getConfirmed().keySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),297,empty);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),299,keySet,298,keySet.isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),300,(((("keySet should contain all HashMap\'s elements" + "\nTest: ") + (keySet)) + "\nReal: ") + (getConfirmed().keySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),302,keySet,301,keySet.containsAll(getConfirmed().keySet()));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),303,(((("keySet hashCodes should be the same" + "\nTest: ") + (keySet)) + "\nReal: ") + (getConfirmed().keySet())));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),305,getConfirmed().keySet(),304,getConfirmed().keySet().hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),307,keySet,306,keySet.hashCode());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),309,getConfirmed(),308,getConfirmed().keySet());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),310,keySet);
    }

    public void verifyValues() {
        final List<V> known = new ArrayList<V>(getConfirmed().values());
        values = getMap().values();
        final List<V> test = new ArrayList<V>(values);
        final int size = getConfirmed().size();
        final boolean empty = getConfirmed().isEmpty();
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),323,(((("values should be same size as HashMap\'s" + "\nTest: ") + test) + "\nReal: ") + known));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),324,size);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),326,values,325,values.size());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),327,(((("values should be empty if HashMap is" + "\nTest: ") + test) + "\nReal: ") + known));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),328,empty);
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),330,values,329,values.isEmpty());
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),331,(((("values should contain all HashMap\'s elements" + "\nTest: ") + test) + "\nReal: ") + known));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),333,test,332,test.containsAll(known));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),334,(((("values should contain all HashMap\'s elements" + "\nTest: ") + test) + "\nReal: ") + known));
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),336,known,335,known.containsAll(test));
        for (final V v : known) {
            final boolean removed = test.remove(v);
            fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),337,removed);
        }
        fr.inria.diversify.testamplification.logger.Logger.logAssertArgument(Thread.currentThread(),339,test,338,test.isEmpty());
    }

    /** 
     * Erases any leftover instance variables by setting them to null.
     */
@Override
    public void tearDown() throws Exception {
        map = null;
        keySet = null;
        entrySet = null;
        values = null;
        confirmed = null;
    }

    /** 
     * Get the map.
     * 
     * @return Map<K,V>
     */
public Map<K, V> getMap() {
        return map;
    }

    /** 
     * Get the confirmed.
     * 
     * @return Map<K,V>
     */
public Map<K, V> getConfirmed() {
        return confirmed;
    }
}

