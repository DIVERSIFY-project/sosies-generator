package fr.inria.diversify.persistence.json.input;

import fr.inria.diversify.diversification.InputProgram;
import fr.inria.diversify.it.SosieGeneratorIntegrationTests;
import fr.inria.diversify.transformation.SingleTransformation;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marodrig on 14/01/2015.
 */
public class JsonSosiesInput_IT extends SosieGeneratorIntegrationTests{

    @Test
    public void testRead() throws IOException {
        //Gets the input program described in common-collections.properties
        //file in the <getDataDir()>/input_configurations
        InputProgram inputProgram = getInputProgram("common-collections-small-test");
        //Loads wiht the new System
        String transfPath = inputProgram.getPreviousTransformationsPath();
        JsonSosiesInput newIn = new JsonSosiesInput(
                "C:\\MarcelStuff\\data\\DIVERSE\\sosies-pools\\collections_4_0_small_test_new.json", inputProgram);
        Collection<SingleTransformation> newLoad = newIn.read();
        assertEquals(0, newIn.getErrors().size()); //TODO:Add later a more robust questions
        int i = 0;
        for ( SingleTransformation t : newLoad ) {
            System.out.println(i + ":" + t.getIndex());
        }
        assertEquals(13, newLoad.size());
    }
}
