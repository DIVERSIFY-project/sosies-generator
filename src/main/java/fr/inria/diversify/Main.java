package fr.inria.diversify;

import fr.inria.diversify.codeFragment.CodeFragmentList;
import fr.inria.diversify.codeFragment.TransformationParser;
import fr.inria.diversify.codeFragmentProcessor.StatementProcessor;
import fr.inria.diversify.replace.Diversify;
import fr.inria.diversify.replace.Transformation;
import fr.inria.diversify.runtest.CoverageReport;
import fr.inria.diversify.runtest.ICoverageReport;
import fr.inria.diversify.runtest.NullCoverageReport;
import fr.inria.diversify.statistic.StatisticCodeFragment;
import fr.inria.diversify.statistic.StatisticDiversification;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.json.JSONException;
import spoon.processing.ProcessingManager;
import spoon.reflect.Factory;
import spoon.support.DefaultCoreFactory;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;
import spoon.support.builder.SpoonBuildingManager;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main {
	private CodeFragmentList statements;

    public static void main(String[] args) throws Exception {
		Main app = new Main(args);
    }

	public Main(String[] args) throws Exception {
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( commandLineOption(), args);

        initSpoon(cmd.getOptionValue("src"));
        ICoverageReport rg = getCoverageReport(cmd.getOptionValue("jacoco"),cmd.getOptionValue("classes"));


//		computeStatistic(cmd.getOptionValue("out"));
//        System.out.println("number of statement: " + statements.size());
//        System.out.println("number of undiversify Statement: " + (new Util(statements)).numberOfNotDiversification());
//        System.out.println("number of diversification: " + (new Util(statements)).numberOfDiversification());

        int nbRun = Integer.parseInt(cmd.getOptionValue("nbRun"));
        Diversify d  = new Diversify(statements, rg, cmd.getOptionValue("project"), "output_diversify");

        d.run(nbRun);
        d.printResult(cmd.getOptionValue("out"));
//        computeDiversifyStat("result2/result/", cmd.getOptionValue("out"));
    }

    protected void initSpoon(String directory) {
        StandardEnvironment env = new StandardEnvironment();
		env.setComplianceLevel(6); //for jfreechart
		env.setVerbose(true);
		env.setDebug(true);

		DefaultCoreFactory f = new DefaultCoreFactory();
		Factory factory = new Factory(f, env);
		SpoonBuildingManager builder = new SpoonBuildingManager(factory);
        for(String dir : directory.split(System.getProperty("path.separator")))
			try {
				builder.addInputSource(new File(dir));
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

    protected void computeDiversifyStat(String dir, String fileName) throws IOException, JSONException {
        TransformationParser tf = new TransformationParser(statements);
        List<Transformation> list = tf.parseDir(dir);
        System.out.println("nb transformation: "+list.size());
        StatisticDiversification sd = new StatisticDiversification(list, statements);
        sd.writeStat(fileName);

    }

	protected void computeStatistic(String output) {
		StatisticCodeFragment stat = new StatisticCodeFragment(statements);
		 
		try {
			stat.writeStatistic(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    protected ICoverageReport getCoverageReport(String jacocoFile, String classes) throws IOException {
        ICoverageReport icr;

        if(jacocoFile != null)
            icr = new CoverageReport(classes,jacocoFile);
        else
            icr = new NullCoverageReport();

        icr.create();
        return  icr;
    }

    protected Options commandLineOption() {
        Options options = new Options();
        options.addOption("project", true, "sources directory");
        options.addOption("src", true, "sources directory");
        options.addOption("classes", true, "classes directory");
        options.addOption("nbRun", true, "number of run");
        options.addOption("jacoco", true, "jacoco file for test coverage");
        options.addOption("out", true, "prefix for output files");
        return  options;
    }
}
