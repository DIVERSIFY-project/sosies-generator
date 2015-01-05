package fr.inria.diversify.sosie.compare;

import fr.inria.diversify.sosie.compare.stackElement.StackTraceCall;
import fr.inria.diversify.sosie.compare.stackTraceOperation.StackTrace;

import java.util.*;

/**
 * Created by Simon on 17/04/14.
 */
public class ComparePartialStackTrace  extends  AbstractCompareStackTrace {

    public ComparePartialStackTrace(StackTrace st1, StackTrace st2) {
       super(st1,st2);
    }


    public List<String> findDiff() {
        List<String> diffs = new LinkedList<>();
        boolean st1Lower = false, st2Lower = false;

        if(sosieStackTrace.getStartLogging().isEmpty()) {
            return diffs;
        }
        sosieStackTrace.next();
        StackTraceCall sosieTop = sosieStackTrace.getTop();
        while(originalStackTrace.hasNext()
                && !sosieStackTrace.getStartLogging().contains(originalStackTrace.getTop().getId())
                && !sosieTop.equals(originalStackTrace.getTop())) {
            originalStackTrace.next();
        }
        if(originalStackTrace.hasNext()) {
            sosieStackTrace.previous();
            originalStackTrace.previous();
            sosieStackTrace.getVariable().putAll(originalStackTrace.getVariable());
            originalStackTrace.copy(sosieStackTrace);
        }

        while(originalStackTrace.hasNext() && sosieStackTrace.hasNext()) {
            if (!(originalStackTrace.nextIsVar() || sosieStackTrace.nextIsVar())) {
                if (!st1Lower) {
                    originalStackTrace.next();
                }
                if (!st2Lower) {
                    sosieStackTrace.next();
                }
            } else {
                if(originalStackTrace.nextIsVar()) {
                    nextVar(originalStackTrace, sosieStackTrace.getStartLogging());
//                    originalStackTrace.next();
                }
                if(sosieStackTrace.nextIsVar()) {
                    nextVar(sosieStackTrace, sosieStackTrace.getStartLogging());
//                    sosieStackTrace.next();
                }
            }

            StackTraceCall top1 = originalStackTrace.getTop();
            StackTraceCall top2 = sosieStackTrace.getTop();
            int deep1 = originalStackTrace.getDeep();
            int deep2 = sosieStackTrace.getDeep();
            maxPop = Math.min(deep1,deep2);

            if(!st1Lower && deep1 < deep2) {
                st1Lower = true;
            }
            if(!st2Lower &&  deep1 > deep2) {
                st1Lower = true;
            }
            if(st1Lower || st2Lower) {
//                Log.info("stack trace diff: st1 size: {}, st2 size: {},\nst1 top: {}, st2 top: {}",deep1,deep2,top1,top2);
                testReport.addDiffMethodCall(top1);
                testReport.addDiffMethodCall(top2);
            }

            boolean sameTop = top1.equals(top2);
            if(st1Lower && st2Lower || !sameTop) {
                testReport.addDiffMethodCall(top1);
                testReport.addDiffMethodCall(top2);
//                Log.info("stack trace diff: st1 size: {}, st2 size: {},\nst1 top: {}, st2 top: {}",deep1,deep2,top1,top2);
                findNewSyncro(20, 2, originalStackTrace, sosieStackTrace);

                if(originalStackTrace.getDeep() == sosieStackTrace.getDeep()) {
                    st1Lower = false; st2Lower = false;
                }
            }
            if(sameTop && !(st1Lower && st2Lower)){ //same stack trace
                testReport.addSameMethodCall(top1);
            }

            if(st1Lower == st2Lower && (originalStackTrace.getVariablesValueChange() || sosieStackTrace.getVariablesValueChange())) {
                Set<String> vd = varDiff(originalStackTrace, sosieStackTrace);
                if (!vd.isEmpty()) {
                    diffs.addAll(vd);
                }
            }
        }


//        while(originalStackTrace.hasNext() && sosieStackTrace.hasNext()) {
//            if (originalStackTrace.nextIsVar() || sosieStackTrace.nextIsVar()) {
//                if(originalStackTrace.nextIsVar()) {
////                    nextVar(originalStackTrace, sosieStackTrace.getStartLogging());
//                    originalStackTrace.next();
//                }
//                if(sosieStackTrace.nextIsVar()) {
////                    nextVar(sosieStackTrace, sosieStackTrace.getStartLogging());
//                    sosieStackTrace.next();
//                }
//            } else {
//                sosieStackTrace.next();
//                originalStackTrace.next();
//            }
//
//            StackTraceCall top1 = originalStackTrace.getTop();
//            StackTraceCall top2 = sosieStackTrace.getTop();
//
//            boolean sameTop = top1.equals(top2);
//
//            if(sameTop) {
//                testReport.addSameMethodCall(top1);
//                if(originalStackTrace.getVariablesValueChange() || sosieStackTrace.getVariablesValueChange()) {
//                    Set<VariableDiff> vd = varDiff(originalStackTrace, sosieStackTrace);
//                    if (!vd.isEmpty()) {
//                        diffs.addAll(vd);
//                    }
//                }
//            } else {
//                testReport.addDiffMethodCall(top1);
//                testReport.addDiffMethodCall(top2);
////                Log.info("stack trace diff: st1 size: {}, st2 size: {},\nst1 top: {}, st2 top: {}",deep1,deep2,top1,top2);
//                diffs.add(findNewSyncro(20, 2, originalStackTrace, sosieStackTrace));
//
//            }
//        }
        return diffs;
    }
}
