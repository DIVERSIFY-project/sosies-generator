package fr.inria.diversify.transformation.ast;

import fr.inria.diversify.codeFragment.CodeFragment;
import fr.inria.diversify.transformation.ITransformation;
import fr.inria.diversify.util.Log;
import org.apache.commons.io.FileUtils;
import spoon.processing.Environment;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.declaration.CtSimpleType;
import spoon.support.JavaOutputProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Simon
 * Date: 7/11/13
 * Time: 4:15 PM
 */
public abstract class ASTTransformation implements ITransformation {
    protected CodeFragment position;
    protected Integer failures;
    protected List<ASTTransformation> parents;

    public ASTTransformation() {
        parents = new ArrayList<ASTTransformation>();
    }

    public void apply(String srcDir) throws Exception {
        addSourceCode();

        printJavaFile(srcDir);
        removeSourceCode();
    }

    protected abstract void addSourceCode() throws Exception;

    public void restore(String srcDir) throws Exception {
        restore(srcDir, getOriginalClass(position));

    }

    protected void printJavaFile(String repository) throws IOException {
        CtSimpleType<?> type = getOriginalClass(position);
        Environment env = type.getFactory().getEnvironment();

        JavaOutputProcessor processor = new JavaOutputProcessor(new File(repository));
        env.useSourceCodeFragments(true);
        processor.setFactory(type.getFactory());

        processor.createJavaFile(type);
        Log.debug("copy file: " + repository + " " + type.getQualifiedName());
    }

    protected void removeSourceCode() {
        CtSimpleType<?> type = getOriginalClass(position);
        CompilationUnit compileUnit = type.getPosition().getCompilationUnit();
        compileUnit.getSourceCodeFraments().clear();
    }

    protected void restore(String srcDir,CtSimpleType<?> originalClass) throws Exception {
        String fileToCopy = originalClass.getPosition().getFile().toString();
        String destination = srcDir+ "/"+originalClass.getQualifiedName().replace('.', '/') + ".java";
        Log.debug("restore file: " + fileToCopy + " -> " + destination);
        FileUtils.copyFile(originalClass.getPosition().getFile(), new File(destination));
    }

    public CtSimpleType<?> getOriginalClass(CodeFragment cf) {
        return cf.getCompilationUnit().getMainType();
    }

//    public void addCodeFragmentToTransform(CodeFragment cf) {
//        transform.add(cf);
//    }

//    public void setCodeFragmentToTransform(CodeFragment cf) {
//        position = cf;
//    }

    public void addParent(ASTTransformation p) {
        parents.add(p);
    }

    public CodeFragment getPosition() {
        return position;
    }

    public void setJUnitResult(Integer result) {
        failures = result;
    }

    public int numberOfFailure() {
        return failures;
    }

    public abstract ASTReplace toReplace() throws Exception;
    public abstract ASTAdd toAdd() throws Exception;
    public abstract ASTDelete toDelete() throws Exception;

    public void setPosition(CodeFragment position) {
        this.position = position;
    }

//    public abstract void add(ASTTransformation replace);

//    public String positionString() {
//        return transform.positionString() + "\n";
//    }
}