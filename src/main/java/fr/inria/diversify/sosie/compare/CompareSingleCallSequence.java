package fr.inria.diversify.sosie.compare;

import fr.inria.diversify.codeFragment.CodeFragment;
import fr.inria.diversify.sosie.pointSequence.Point;
import fr.inria.diversify.sosie.pointSequence.PointSequence;
import fr.inria.diversify.util.Log;

import java.util.Arrays;

/**
 * User: Simon
 * Date: 13/01/14
 * Time: 10:05
 */
public class CompareSingleCallSequence {
    protected PointSequence original;
    protected PointSequence sosie;
    protected CodeFragment startPoint;


    public CompareSingleCallSequence(PointSequence original, PointSequence sosie, CodeFragment startPoint) {
        this.original = original;
        this.sosie = sosie;
        this.startPoint = startPoint;
    }

    public int[][] findDivergence(int syncroRange) {
        if(startPoint == null || true)
            return findDivergence(syncroRange, 0,0);
        else {
            return findDivergence(syncroRange, findDiversificationIndex(original),findDiversificationIndex(sosie));
        }
    }

    /**
     * search if original and sosie (two traces) are the same trace at the call level (module the syncro range).
     * @param syncroRange
     * @param startOriginal
     * @param startSosie
     * @return the local conditionalDivergence. null if original and sosie are not the same trace
     */
    protected int[][] findDivergence(int syncroRange, int startOriginal, int startSosie) {
        if(findDivergence(syncroRange, startSosie, startOriginal, sosie, original) != null)
            return findDivergence(syncroRange, startOriginal, startSosie, original, sosie);
        return null;
    }

    /**
     * search if two traces are the same trace at the call level (module the syncro range).

     * @return the local conditionalDivergence. null if original and sosie are not the same trace
     */
    protected int[][] findDivergence(int syncroRange, int start1, int start2, PointSequence ps1, PointSequence ps2) {
        int bound = Math.min(ps1.callSize(), ps2.callSize());
        if(bound == 0)
            return null;
        int[][] divergence = new int[bound][2];
        int i = 0;
        divergence[i][0] = start1;
        divergence[i][1] = start2;

        while(start1 < bound - 1 && start2 < bound - 1) {
            i++;
            start1++;
            start2++;
            Point oPoint = ps1.getCallPoint(start1);
            Point sPoint = ps2.getCallPoint(start2);
            if(!oPoint.sameLogPoint(sPoint)) {
                int newSyncho[] = findSyncro(syncroRange, start1,start2);
                if(newSyncho == null)
                    return null;
                else {
                    start1 = newSyncho[0];
                    start2 = newSyncho[1];
                }
            }
            divergence[i][0] = start1;
            divergence[i][1] = start2;
//            i++;
        }
        return Arrays.copyOf(divergence, i);
    }

    protected int findDiversificationIndex(PointSequence sequence) {

        for (int i = 0; i < sequence.callSize(); i++)
            if(sequence.getCallPoint(i).containsInto(startPoint)) {
                if(i == 0)

                    Log.info("{} {}", sequence.getCallPoint(i).getClassName(), startPoint.getSourceClass().getQualifiedName());
                return i;
            }

        return sequence.callSize();
    }

    protected int[] findSyncro(int syncroRange, int iOriginal, int iSosie) {
        if(iSosie < iOriginal)
            return findSyncroP(syncroRange, iOriginal, iSosie);
        else
            return findSyncroP(syncroRange,iSosie, iOriginal);
    }

    protected int[] findSyncroP(int syncroRange, int iOriginal, int iSosie){
        for(int i = iOriginal; (i < syncroRange + iOriginal) && (i < original.callSize()); i++) {
            for(int j = iSosie; (j < syncroRange + iSosie) && (j < sosie.callSize()); j++) {
                Point oPoint = original.getCallPoint(i);
                Point sPoint = sosie.getCallPoint(j);
                if(oPoint.sameLogPoint(sPoint))
                    return new int[]{i,j};
            }
        }
        return null;
    }
}