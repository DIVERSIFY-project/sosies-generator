package fr.inria.diversify.diversification;

import fr.inria.diversify.statistic.SinglePointSessionResults;
import fr.inria.diversify.transformation.Transformation;
import fr.inria.diversify.transformation.ast.ASTTransformation;
import fr.inria.diversify.transformation.ast.exception.ApplyTransformationException;
import fr.inria.diversify.transformation.ast.exception.BuildTransplantException;
import fr.inria.diversify.util.Log;
import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Simon on 23/10/2014.
 */
public class UniqueSosieGenerator extends AbstractDiversify {
    List<Transformation> allTransformation;
    private Collection<Transformation> transformation;

    public UniqueSosieGenerator(InputConfiguration inputConfiguration, String projectDir, String srcDir) {
        this.sourceDir = srcDir;
        this.projectDir = projectDir;
        transformations = new ArrayList<>();
        this.inputConfiguration = inputConfiguration;
        sessionResults = new SinglePointSessionResults();
    }

    public UniqueSosieGenerator(List<Transformation> allTransformation) {
        this.allTransformation = allTransformation;
    }

    @Override
    public void run(int n) throws Exception {
//        Random random = new Random();
        while (transformations.size() < n && !allTransformation.isEmpty()) {
//            Transformation t = allTransformation.get(random.nextInt(allTransformation.size()));
//            allTransformation.remove(t);
            run(allTransformation.remove(0));
        }
    }

    protected void run(Transformation trans) throws Exception {
        Log.info("trial {}", trial);
        Log.debug("output dir: " + tmpDir + "/" + sourceDir);
        writeTransformation(tmpDir + "/transplant.json", (ASTTransformation) trans);

        try {
            trans.apply(tmpDir + "/" + sourceDir);

            try {
                int status = runTest(tmpDir);

                trans.setStatus(status);
                trans.setFailures(builder.getTestFail());
                if (status == 0) {
                    transformations.add(trans);
                    copySosieProgram();
                }
                // error during runTest
            } catch (Exception e) {
                trans.setStatus(-2);
                Log.debug("compile error during diversification", e);
            }

            trial++;
            trans.restore(tmpDir + "/" + sourceDir);

            ((SinglePointSessionResults) sessionResults).addRunResults(trans);
            Log.debug("run after restore: " + tmpDir + "/" + sourceDir);
        } catch (ApplyTransformationException e) {
            tryRestore(trans, e);
        } catch (BuildTransplantException e) {}
    }

    protected void writeTransformation(String fileName, ASTTransformation transformation) throws IOException, JSONException {
        FileWriter out = new FileWriter(fileName);

        out.write(transformation.toJSONObject().toString());

        out.close();
    }

    public void setTransformation(Collection<Transformation> transformation) {
        allTransformation = new ArrayList<>(transformation);
    }
}
