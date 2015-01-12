package fr.inria.diversify.persistence.json.output;

import fr.inria.diversify.transformation.Transformation;
import fr.inria.diversify.transformation.ast.ASTDelete;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marodrig on 08/01/2015.
 */
public class JsonASTDeleteSectionOutput extends JsonASTSectionOutput {
    /**
     * Puts the transformation data into the JSON Object.
     * @param object Objecto to put data
     * @param transformation Transformation to obtain data from
     * @param isEmptyObject Indicate if the JSON object is empty
     */
    protected void put(JSONObject object, Transformation transformation, boolean isEmptyObject) throws JSONException {
        if ( isEmptyObject ) super.put(object, transformation, isEmptyObject);
        if (transformation instanceof ASTDelete) {
            ASTDelete d = (ASTDelete) transformation;
            object.put("transplantationPoint", codeFragmentToJSON(d.getTransplantationPoint()));
        }
    }
}
