package fr.inria.diversify;

import fr.inria.diversify.codeFragment.CodeFragmentList;
import fr.inria.diversify.codeFragmentProcessor.StatementProcessor;
import fr.inria.diversify.sosie.Sosie;
import fr.inria.diversify.statistic.StatisticDiversification;
import fr.inria.diversify.statistic.Util;
import fr.inria.diversify.transformation.*;
import fr.inria.diversify.coverage.CoverageReport;
import fr.inria.diversify.coverage.ICoverageReport;
import fr.inria.diversify.coverage.NullCoverageReport;
import fr.inria.diversify.statistic.StatisticCodeFragment;

import fr.inria.diversify.transformation.query.AbstractTransformationQuery;
import fr.inria.diversify.transformation.query.TransformationQuery;
import fr.inria.diversify.transformation.query.TransformationQueryT;
import fr.inria.diversify.util.DiversifyProperties;
import org.json.JSONArray;
import org.json.JSONException;
import spoon.processing.ProcessingManager;
import spoon.reflect.Factory;
import spoon.support.DefaultCoreFactory;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;
import spoon.support.builder.SpoonBuildingManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private CodeFragmentList statements;

    public static void main(String[] args) throws Exception {
        new Main(args);
    }

    public Main(String[] args) throws Exception {
        new DiversifyProperties(args[0]);

//        new ProjectDependency("junit:junit:4.12-SNAPSHOT",  DiversifyProperties.getProperty("project")+"/pom.xml");

        initSpoon();

        System.out.println("number of statement: " + statements.size());

        if(DiversifyProperties.getProperty("sosie").equals("true"))
            runSosie();
        else
            runDiversification();

        if (DiversifyProperties.getProperty("stat").equals("true"))
            computeStatistic();
    }

    protected void runSosie() throws Exception {
        Sosie d = new Sosie(initTransformationQuery(), DiversifyProperties.getProperty("project"));
        d.setSourceDirectory(DiversifyProperties.getProperty("src"));

        int t = Integer.parseInt(DiversifyProperties.getProperty("timeOut"));
        d.setTimeOut(t);

        d.setTmpDirectory(DiversifyProperties.getProperty("output_diversify"));

        int n = Integer.parseInt(DiversifyProperties.getProperty("nbRun"));
        d.run(n);

    }

    protected void runDiversification() throws Exception {
        Diversify d = new Diversify(initTransformationQuery(), DiversifyProperties.getProperty("project"));

        d.setSourceDirectory(DiversifyProperties.getProperty("src"));

        if (DiversifyProperties.getProperty("clojure").equals("true"))
            d.setClojureTest(true);

        int t = Integer.parseInt(DiversifyProperties.getProperty("timeOut"));
        d.setTimeOut(t);

        //TODO refactor
        if(DiversifyProperties.getProperty("nbRun").equals("all")) {
            Util util = new Util(statements);
            if(DiversifyProperties.getProperty("transformation.type").equals("replace"))
                d.run(util.getAllReplace());
            if(DiversifyProperties.getProperty("transformation.type").equals("add"))
                d.run(util.getAllAdd());
            if(DiversifyProperties.getProperty("transformation.type").equals("delete"))
                d.run(util.getAllDelete());
        }
        else {
            int n = Integer.parseInt(DiversifyProperties.getProperty("nbRun"));
            d.run(n);
        }

        d.printResult(DiversifyProperties.getProperty("result"));
    }

    protected AbstractTransformationQuery initTransformationQuery() throws IOException, JSONException {
        ICoverageReport rg = initCoverageReport();

        AbstractTransformationQuery atq;
        String transformation = DiversifyProperties.getProperty("transformation.directory");
        if (transformation != null) {
            TransformationParser tf = new TransformationParser(statements);
            List<Transformation> list = tf.parseDir(transformation);
            atq = new TransformationQueryT(list, statements);
        } else {
            atq = new TransformationQuery(rg, statements);
        }
        atq.setType(DiversifyProperties.getProperty("transformation.type"));
        int n = Integer.parseInt(DiversifyProperties.getProperty("transformation.size"));
        atq.setNbTransformation(n);

        return atq;
    }


    protected void initSpoon() {
        String srcDirectory = DiversifyProperties.getProperty("project") + "/" + DiversifyProperties.getProperty("src");

        StandardEnvironment env = new StandardEnvironment();
        int javaVersion = Integer.parseInt(DiversifyProperties.getProperty("javaVersion"));
        env.setComplianceLevel(javaVersion);
        env.setVerbose(true);
        env.setDebug(true);

        DefaultCoreFactory f = new DefaultCoreFactory();
        Factory factory = new Factory(f, env);
        SpoonBuildingManager builder = new SpoonBuildingManager(factory);

        for (String dir : srcDirectory.split(System.getProperty("path.separator")))
            try {
                builder.addInputSource(new File(dir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            System.out.println("sourcepath " + factory.getEnvironment().getSourcePath());
            System.out.println(Thread.currentThread().getContextClassLoader().getClass());

            builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProcessingManager pm = new QueueProcessingManager(factory);
        StatementProcessor processor = new StatementProcessor();
        pm.addProcessor(processor);
        pm.process();

        statements = processor.getStatements();
    }

    protected ICoverageReport initCoverageReport() throws IOException {
        String project = DiversifyProperties.getProperty("project");
        String classes = DiversifyProperties.getProperty("classes");
        String jacocoFile = DiversifyProperties.getProperty("jacoco");
        ICoverageReport icr;

        if (jacocoFile != null)
            icr = new CoverageReport(project + "/" + classes, jacocoFile);
        else
            icr = new NullCoverageReport();

        icr.create();
        return icr;
    }

    protected void computeStatistic() throws IOException, JSONException {
        String out = DiversifyProperties.getProperty("result");
        computeCodeFragmentStatistic(out);

        String transDir = DiversifyProperties.getProperty("transformation.directory");
        if (transDir != null) {
            computeDiversifyStat(transDir, out);
        }
        computeOtherStat();
    }

    protected void computeDiversifyStat(String transDir, String fileName) throws IOException, JSONException {
        TransformationParser tf = new TransformationParser(statements);
        List<Transformation> transformations = tf.parseDir(transDir);
        System.out.println("nb transformation: " + transformations.size());

        writeTransformation(fileName + "_allTransformation.json", transformations);
        writeGoodTransformation(fileName + "_goodTransformation.json", transformations);

        StatisticDiversification sd = new StatisticDiversification(transformations, statements);
        sd.writeStat(fileName);
        computeOtherStat();
    }

    protected void computeOtherStat() {
        Util stat = new Util(statements);
        System.out.println("number of possible code fragment replace: " + stat.numberOfDiversification());
        System.out.println("number of not possible code fragment replace/add: " + stat.numberOfNotDiversification());
        System.out.println("number of possible code fragment add: " + stat.getAllAdd().size());
        System.out.println("number of possible code fragment delete: " + stat.getAllDelete().size());
    }

    protected void computeCodeFragmentStatistic(String output) {
        StatisticCodeFragment stat = new StatisticCodeFragment(statements);
        try {
            stat.writeStatistic(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeGoodTransformation(String FileName, List<Transformation> transformations) throws IOException, JSONException {
        List<Transformation> goodTransformation = new ArrayList<Transformation>();
        for (Transformation transformation : transformations) {
            if (transformation.numberOfFailure() == 0) {
                goodTransformation.add(transformation);
            }
        }
        writeTransformation(FileName, goodTransformation);
    }

    protected void writeTransformation(String FileName, List<Transformation> transformations) throws IOException, JSONException {

        BufferedWriter out = new BufferedWriter(new FileWriter(FileName));
        JSONArray obj = new JSONArray();
        for (Transformation transformation : transformations) {
            try {
                obj.put(transformation.toJSONObject());
            } catch (Exception e) {
            }
        }
        out.write(obj.toString());
        out.newLine();
        out.close();
    }

}
