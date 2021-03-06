package fr.inria.diversify.processor.main;


import fr.inria.diversify.processor.ProcessorUtil;
import fr.inria.diversify.runner.InputProgram;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtMethod;
//import spoon.reflect.visitor.QueryVisitor;
import spoon.reflect.visitor.filter.TypeFilter;

;


/**
 * User: Simon
 * Date: 06/01/14
 * Time: 10:04
 */
public class MethodCallInstrumenter extends AbstractLoggingInstrumenter<CtMethod> {

    public MethodCallInstrumenter(InputProgram inputProgram) {
        super(inputProgram);
    }


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {

        return !candidate.isImplicit()
                && candidate.getBody() != null
                && candidate.getBody().getStatements().size() != 0
                && hasCall(candidate)
                && !containsGoto(candidate);
    }

    @Override
    public void process(CtMethod candidate) {
        CtTry ctTry = tryFinallyBody(candidate);
        int methodId = ProcessorUtil.methodId(candidate);

        String snippet = getLogger() + ".methodIn(Thread.currentThread(),\"" + methodId + "\")";
        CtCodeSnippetStatement beginStmt = getFactory().Code().createCodeSnippetStatement(snippet);

        ctTry.getBody().insertBegin(beginStmt);

        snippet = getLogger() + ".methodOut(Thread.currentThread(),\"" + methodId + "\")";
        CtCodeSnippetStatement endStmt = getFactory().Code().createCodeSnippetStatement(snippet);

        ctTry.getFinalizer().addStatement(endStmt);

    }

    protected boolean hasCall(CtMethod method) {
        /*QueryVisitor query = new QueryVisitor(new TypeFilter(CtInvocation.class));
        method.accept(query);

        for(Object o : query.getResult()) {*/
        for(Object o : method.getElements(new TypeFilter(CtInvocation.class))) {
            CtInvocation target = (CtInvocation) o;
            if(target.getExecutable() != null && target.getExecutable().getDeclaration() != null)
                if (inputProgram.getAllElement(CtMethod.class).contains(target.getExecutable().getDeclaration())) {
                    return true;
                }
        }
        return false;
    }
}
