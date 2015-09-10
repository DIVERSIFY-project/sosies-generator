package fr.inria.diversify.processor.main;

import fr.inria.diversify.diversification.InputProgram;
import fr.inria.diversify.transformation.Transformation;
import fr.inria.diversify.transformation.ast.ASTTransformation;
import spoon.reflect.code.*;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;


/**
 * User: Simon
 * Date: 10/07/15
 * Time: 11:01
 */
public class TransformationUsedProcessor extends AbstractLoggingInstrumenter<CtStatement> {
    protected ASTTransformation transformation;

    public TransformationUsedProcessor(InputProgram inputProgram, Transformation transformation) {
        super(inputProgram);
        this.transformation = (ASTTransformation) transformation;
    }

    @Override
    public boolean isToBeProcessed(CtStatement candidate) {
        return !transformation.getName().equals("delete")
        && transformation.getCopyTransplant().toString().equals(candidate.toString());
    }

    public void process(CtStatement stmtTrans) {
        String transformationName = transformation.getName();

        if(transformationName.equals("add")) {
            CtIf ctIf = (CtIf) stmtTrans;
            pprocess(((CtBlock) ctIf.getThenStatement()).getLastStatement());
        } else {
            pprocess(stmtTrans);
        }


    }

    protected void pprocess(CtStatement stmtTrans) {
        int count = 0;
        for(Object object : Query.getElements(stmtTrans, new TypeFilter(CtIf.class))) {
            CtIf ctIf = (CtIf) object;
//            int branchId = idBranch(methodId);
            CtStatement stmt = ctIf.getThenStatement();
            if (!(stmt instanceof CtBlock)) {
                CtBlock block = getFactory().Core().createBlock();
                block.setParent(stmt.getParent());
                block.addStatement(stmt);
                ctIf.setThenStatement(block);
            }
            addBlockSnippet(ctIf.getThenStatement(), "t" + count);
            count++;
            if (ctIf.getElseStatement() != null) {
                stmt = ctIf.getElseStatement();
                if (!(stmt instanceof CtBlock)) {
                    CtBlock block = getFactory().Core().createBlock();
                    block.setParent(stmt.getParent());
                    block.addStatement(stmt);
                    ctIf.setElseStatement(block);
                }
                addBlockSnippet(ctIf.getElseStatement(), "e" + count);
                count++;
            }
        }

        for(Object object : Query.getElements(stmtTrans, new TypeFilter(CtLoop.class))) {
            CtLoop ctLoop = (CtLoop) object;
            CtStatement stmt = ctLoop.getBody();
            if (!(stmt instanceof CtBlock)) {
                CtBlock block = getFactory().Core().createBlock();
                if (stmt != null) {
                    block.setParent(stmt.getParent());
                    block.addStatement(stmt);
                } else {
                    block.setParent(ctLoop);
                }
                ctLoop.setBody(block);
            }
            addBlockSnippet(ctLoop.getBody(), "l" + count);
            count++;
        }

        for(Object object : Query.getElements(stmtTrans, new TypeFilter(CtCatch.class))) {
            CtCatch ctCatch = (CtCatch) object;
            CtStatement stmt = ctCatch.getBody();
            if (!(stmt instanceof CtBlock)) {
                CtBlock block = getFactory().Core().createBlock();
                block.setParent(stmt.getParent());
                block.addStatement(stmt);
                ctCatch.setBody(block);
            }

            addBlockSnippet(ctCatch.getBody(), "c" + count);
            count++;
        }

        if(count == 0) {
            stmtTrans.insertBefore(getFactory().Code().createCodeSnippetStatement(getLogger() + ".logTransformation(Thread.currentThread(),\"b\");"));
        }
    }

    protected void addBlockSnippet(CtStatement ctBlock, String branchId) {
        CtCodeSnippetStatement snippet = getFactory().Code().createCodeSnippetStatement(getLogger() + ".logTransformation(Thread.currentThread(),\""+branchId+"\")");
        ((CtBlock) ctBlock).insertBegin(snippet);
    }

//    protected int idBranch(int methodId) {
//        if(!blockIds.containsKey(methodId)) {
//            blockIds.put(methodId, 0);
//        }
//        blockIds.put(methodId, blockIds.get(methodId) + 1);
//        return blockIds.get(methodId);
//    }
}
