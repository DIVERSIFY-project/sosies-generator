package fr.inria.diversify.sosie.logger.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.*;


import java.util.*;

/**
 * User: Simon
 * Date: 25/02/14
 * Time: 11:32
 */
public class ErrorLoggingInstrumenter extends AbstractProcessor<CtStatement> {
    protected static Map<CtExecutable,Integer> count = new HashMap<CtExecutable, Integer>();
    protected static Map<String,String> idMap = new HashMap<String, String>();

    @Override
    public boolean isToBeProcessed(CtStatement candidate) {
        if(candidate.getParent(CtCase.class) != null)
            return false;

        return
                CtIf.class.isAssignableFrom(candidate.getClass())
                        || CtTry.class.isAssignableFrom(candidate.getClass())
                        || CtThrow.class.isAssignableFrom(candidate.getClass())
                ;
    }

//    public boolean hasStaticParent(CtElement el) {
//        if (el instanceof CtModifiable) {
//            if (((CtModifiable) el).getModifiers().contains(ModifierKind.STATIC)) {
//                return true;
//            }
//        }
//
//        if (el.getParent() != null) {
//            return hasStaticParent(el.getParent());
//        }
//
//        return false;
//    }

    public void process(CtStatement statement) {
        if (CtThrow.class.isAssignableFrom(statement.getClass()))
            instruThrow((CtThrow) statement);
        if (CtTry.class.isAssignableFrom(statement.getClass()))
            instruCatch((CtTry)statement);
    }



    protected void instruThrow(CtThrow throwStmt) {
        String className = getClass(throwStmt).getQualifiedName();
        String methodName;
        if(getMethod(throwStmt) == null)
            methodName = "field";

        else
            methodName = getMethod(throwStmt).getSignature();
        String snippet = "{\nfr.inria.diversify.sosie.logger.LogWriter.writeException(Thread.currentThread(),\"" +
                 className + "\",\"" + methodName + "\"," +
                throwStmt.getThrownExpression() + ");\n";
        SourcePosition sp = throwStmt.getPosition();
        CompilationUnit compileUnit = sp.getCompilationUnit();
        int index = compileUnit.beginOfLineIndex(sp.getSourceStart());
        compileUnit.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));

        snippet = "\n}\n";

        index = compileUnit.nextLineIndex(sp.getSourceEnd());
        compileUnit.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
    }


    protected void instruCatch(CtTry tryStmt) {
        String className = getClass(tryStmt).getQualifiedName();
        String methodName;
        if(getMethod(tryStmt) == null)
            methodName = "field";
        else
            methodName = getMethod(tryStmt).getSignature();

        List<CtCatch> catchList = tryStmt.getCatchers();
        for (CtCatch catchStmt : catchList) {
            if(getMethod(tryStmt) != null) {
                String snippet = "fr.inria.diversify.sosie.logger.LogWriter.writeCatch(Thread.currentThread(),\"" +
                        className + "\",\"" + methodName + "\"," +
                        catchStmt.getParameter().getSimpleName() + ");\n";

                CtBlock<?> catchBlock = catchStmt.getBody();
                if(!catchBlock.getStatements().isEmpty()) {
                    CtStatement statement = catchBlock.getStatements().get(0);
                    SourcePosition sp = statement.getPosition();
                    CompilationUnit compileUnit = sp.getCompilationUnit();
                    int index = compileUnit.beginOfLineIndex(sp.getSourceStart());
                    compileUnit.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
                }
            }
        }
    }

//    protected static String idFor(String string) {
//        if(!idMap.containsKey(string))
//            idMap.put(string,idMap.size()+"");
//
//        return idMap.get(string);
//    }
//
//    public static void writeIdFile(String dir) throws IOException {
//        File file = new File(dir+"/log");
//        file.mkdirs();
//        FileWriter fw = new FileWriter(file.getAbsoluteFile()+"/id");
//
//        for(String s : idMap.keySet())
//            fw.write(idMap.get(s)+ " " +s+"\n");
//
//        fw.close();
//    }

    protected int getCount(CtStatement stmt) {
        CtExecutable parent = stmt.getParent(CtExecutable.class);
        if(count.containsKey(parent))
            count.put(parent,count.get(parent) + 1);
        else
            count.put(parent,0);
        return count.get(parent);
    }

    protected CtSimpleType<?> getClass(CtStatement stmt) {
        return stmt.getParent(CtSimpleType.class);
    }

    protected CtExecutable<?> getMethod(CtStatement stmt) {
        CtExecutable<?> ret = stmt.getParent(CtMethod.class);
        if (ret == null)
            ret = stmt.getParent(CtConstructor.class);
        return ret;
    }

}