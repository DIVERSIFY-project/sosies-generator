package fr.inria.diversify.sosie.compare;


import fr.inria.diversify.codeFragment.CodeFragment;
import fr.inria.diversify.sosie.pointSequence.ConditionalPoint;
import fr.inria.diversify.sosie.pointSequence.Point;
import fr.inria.diversify.sosie.pointSequence.PointSequence;
import fr.inria.diversify.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Simon
 * Date: 7/23/13
 * Time: 4:17 PM
 */
public class CompareSingleLogSequence {
    //set of variables whose value changes at each execution
    protected  Set<VariableDiff> diffVar;
    protected PointSequence original;
    protected PointSequence sosie;
    protected CodeFragment startPoint;


    public CompareSingleLogSequence(PointSequence original, PointSequence sosie, CodeFragment startPoint) {
        this.original = original;
        this.sosie = sosie;
        this.diffVar = new HashSet<VariableDiff>();
        this.startPoint = startPoint;
    }


    public int[][] findDivergence(int syncroRange) {
        if(startPoint == null || true)
            return findDivergence(syncroRange, 0,0);
        else {
//            Log.debug("{} {} {}",original.size(), findDiversificationIndex(original), original.size() == findDiversificationIndex(original));
//            Log.debug("{} {} {}",sosie.size(), findDiversificationIndex(sosie), sosie.size() == findDiversificationIndex(sosie));
            return findDivergence(syncroRange, findDiversificationIndex(original),findDiversificationIndex(sosie));
        }
    }


    /**
     * search if original and sosie (two traces) are the same trace at the call level (module the syncro range).
     * @param syncroRange
     * @param startOriginal
     * @param startSosie
     * @return the local divergence. null if original and sosie are not the same trace
     */
    protected int[][] findDivergence(int syncroRange, int startOriginal, int startSosie) {
        if(findDivergence(syncroRange, startSosie, startOriginal, sosie, original) != null)
            return findDivergence(syncroRange, startOriginal, startSosie, original, sosie);
        return null;
    }

    /**
     * search if two traces are the same trace at the call level (module the syncro range).

     * @return the local divergence. null if original and sosie are not the same trace
     */
    protected int[][] findDivergence(int syncroRange, int start1, int start2, PointSequence ps1, PointSequence ps2) {
        int bound = Math.min(ps1.size(), ps2.size());
        int[][] divergence = new int[bound][2];
        int i = 0;
        divergence[i][0] = start1;
        divergence[i][1] = start2;

        while(start1 < bound - 1 && start2 < bound - 1) {
            i++;
            start1++;
            start2++;
            Point oPoint = ps1.get(start1);
            Point sPoint = ps2.get(start2);
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

    /**
     * search in original and sosie (two traces) the divergence variable. a exception is thrown if the two traces are not the same at the call level
     *
     * @param syncroRange
     * @return the set of divergence variables
     */
    public Set<VariableDiff> findDivergenceVar(int syncroRange) {
        int startOriginal = -1;
        int startSosie = -1;
        int bound = Math.min(original.size(), sosie.size());

        Set<VariableDiff> var = new HashSet<VariableDiff>();
        while(startOriginal < bound - 1 && startSosie < bound - 1) {
            startOriginal++;
            startSosie++;
            ConditionalPoint oPoint = original.get(startOriginal);
            ConditionalPoint sPoint = sosie.get(startSosie);
            if(oPoint.sameLogPoint(sPoint) && !oPoint.sameValue(sPoint)) {
                for(VariableDiff dVar : oPoint.getDifVar(sPoint))
                    if(!containsExcludeVar(dVar)) {
                        dVar.setPositionInOrignal(startOriginal);
                        dVar.setPositionInSosie(startSosie);
                        var.add(dVar);
                    }
            }
            else {
                int newSyncho[] = findSyncro(syncroRange, startOriginal,startSosie);
                if(newSyncho == null)
                    new Exception("call trace "+original.getName()+ " and "+sosie.getName()+" no syncro");
                else {
                    startOriginal = newSyncho[0];
                    startSosie = newSyncho[1];
                }
            }
        }
        return var;
    }

    protected int findDiversificationIndex(PointSequence sequence) {

        for (int i = 0; i < sequence.size(); i++)
            if(sequence.get(i).containsInto(startPoint)) {
                if(i == 0)

                 Log.info("{} {}",sequence.get(i).getClassName(),startPoint.getSourceClass().getQualifiedName() );
                return i;
            }

        return sequence.size();
    }


    protected int[] findSyncro(int syncroRange, int iOriginal, int iSosie) {
        if(iSosie < iOriginal)
            return findSyncroP(syncroRange, iOriginal, iSosie);
            else
        return findSyncroP(syncroRange,iSosie, iOriginal);
    }

    protected int[] findSyncroP(int syncroRange, int iOriginal, int iSosie){
        for(int i = iOriginal; (i < syncroRange + iOriginal) && (i < original.size()); i++) {
            for(int j = iSosie; (j < syncroRange + iSosie) && (j < sosie.size()); j++) {
                Point oPoint = original.get(i);
                Point sPoint = sosie.get(j);
                if(oPoint.sameLogPoint(sPoint))
                    return new int[]{i,j};
            }
        }
        return null;
    }

    protected boolean containsExcludeVar(VariableDiff var) {
        for (VariableDiff excludeVar : diffVar)
            if (excludeVar.stringForExcludeFile().equals(var.stringForExcludeFile()))
                return true;
        return false;
    }

    public void setDiffVar(Collection<VariableDiff> set) {
        diffVar.addAll(set);
    }
}