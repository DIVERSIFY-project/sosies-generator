package fr.inria.diversify.util;

import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nharrand on 21/11/16.
 */
public class VarFinder {

    public static String createVarName(List<CtVariable> vars) {
        boolean found = false;
        String varName = "";
        while(!found) {
            varName = RandomLiteralFactory.createString();
            found = true;
            for(CtVariable var: vars) {
                if(var.getSimpleName().equals(varName)) found = false;
            }
        }
        return varName;
    }

    //Pre: El is an instruction inside an executable.
    public static List<CtVariable> getAccessibleVars(CtElement el) {
        Set<CtVariable> res = new HashSet<>();

        CtExecutable elExecutable = el.getParent(CtExecutable.class);
        res.addAll(elExecutable.getParameters());

        CtClass elClass = el.getParent(CtClass.class);
        res.addAll(elClass.getFields());

        res.addAll(getAccessibleVarsFromBlock(el));
        List<CtVariable> lres = new ArrayList<>(res);
        Collections.shuffle(lres);
        return lres;
    }

    public static Set<CtVariable> getAccessibleVarsFromBlock(CtElement el) {
        Set<CtVariable> res = new HashSet<>();
        CtBlock elBlock = el.getParent(CtBlock.class);
        if(elBlock != null) {
            for(CtStatement statement : elBlock.getStatements()) {
                if(el.equals(statement)) {
                    break;
                }
                if(statement instanceof CtLocalVariable) {
                    res.add(((CtLocalVariable) statement));
                }
            }
            res.addAll(getAccessibleVarsFromBlock(elBlock));
        }
        return res;
    }

    public static Set<CtClass> getAccessibleClasses(CtElement el) {
        Set<CtClass> res = new HashSet<>();
        CtClass elClass = el.getParent(CtClass.class);
        res.add(elClass);

        for(CtType t : elClass.getFactory().Class().getAll()) {
            if(t instanceof CtClass) res.add((CtClass) t);
        }
        return res;
    }

    public static Set<CtMethod> getAccessibleMethods(CtElement el) {
        Set<CtMethod> res = new HashSet<>();
        CtClass elClass = el.getParent(CtClass.class);
        CtPackage elPackage = elClass.getPackage();
        for(CtClass c : getAccessibleClasses(el)) {
            Set<CtMethod> methods = c.getAllMethods();
            res.addAll(
                    methods.stream()
                    .filter(
                            m -> (m.getModifiers().contains(ModifierKind.PUBLIC)
                                    || (c.getPackage().equals(elPackage) && m.getModifiers().contains(ModifierKind.PROTECTED))
                                    || c.equals(elClass)
                            )
                    )
                    .collect(Collectors.toSet())
            );
        }
        res.remove(el.getParent(CtMethod.class));
        return res;
    }

    public static CtVariable getTypedVar(CtType t, List<CtVariable> vars) {
        Collections.shuffle(vars);

        for(CtVariable var : vars) {
            if (var.getType().getActualClass() == t.getActualClass()) return var;
        }
        return null;
    }

    public static CtVariable getTypedVar(CtTypeReference t, List<CtVariable> vars) {
        Collections.shuffle(vars);
        for(CtVariable var : vars) {
            //if (var.getType().getActualClass() == t.getActualClass()) return var;
            if (var.getType().equals(t))
                return var;
        }
        return null;
    }

    public static CtVariableReference createRef(CtVariable v) {
        Factory f = v.getFactory();
        CtVariableReference ref;
        if(v instanceof CtLocalVariable) {
            CtLocalVariableReference l = f.Code().createLocalVariableReference((CtLocalVariable)v);
            ref = l;
        } else if(v instanceof CtField) {
            CtFieldReference field = f.Field().createReference((CtField) v);
            ref = field;
        } else if(v instanceof CtParameter) {
            CtParameterReference p = f.Executable().createParameterReference((CtParameter) v);
            ref = p;
        } else {
            System.out.println("Ref creator failed");
            ref = null;
        }
        return ref;
    }
    public static boolean fillParameter(List<CtExpression> paramFillList, CtExecutable exe, List<CtVariable> vars) {
        return fillParameter(paramFillList, exe, vars, false);
    }

    public static boolean fillParameter(List<CtExpression> paramFillList, CtExecutable exe, List<CtVariable> vars, boolean staticCall) {
        Factory f = exe.getFactory();
        List<CtParameter> ps = exe.getParameters();
        for(CtParameter param : ps) {
            CtVariableReference varRef;
            CtVariable var = getTypedVar(param.getReference().getType(), vars);
            if(var != null) {
                if(staticCall) {
                    paramFillList.add(f.Code().createCodeSnippetExpression(var.getSimpleName()));
                } else {
                    varRef = createRef(var);
                    paramFillList.add(f.Code().createVariableRead(varRef, false));
                }
            } else {
                if(param.getType().isPrimitive()) {
                    CtExpression expression;
                    if((param.getType().getActualClass() == byte.class)
                            || (param.getType().getActualClass() == short.class)) {
                        expression = f.Code().createCodeSnippetExpression("(" + param.getType().getActualClass().toString() + ")" + RandomLiteralFactory.randomValue(param.getType()));
                    } else {
                        expression = RandomLiteralFactory.randomValue(param.getType());
                    }

                    paramFillList.add(expression);
                } else {
                    /*if(param.getType().getDeclaration() instanceof CtEnum) {
                        CtExpression expression;
                        List<CtEnumValue> vals = ((CtEnum) param.getType().getDeclaration()).getEnumValues();
                        Collections.shuffle(vals);
                        expression = f.Core().clone(vals.get(0).getAssignment());
                        paramFillList.add(expression);
                    } else */if(param.getType().getDeclaration() instanceof CtClass) {
                        CtClass cla = (CtClass) param.getType().getDeclaration();
                        Collection<CtConstructor> constructors = cla.getConstructors();
                        List<CtExpression> constParamFillList;
                        boolean constructable = false;
                        for (CtConstructor c : constructors) {
                            constParamFillList = new LinkedList<CtExpression>();
                            if(fillParameter(constParamFillList,c,vars)) {
                                CtExpression[] array = constParamFillList.toArray(new CtExpression[constParamFillList.size()]);
                                CtConstructorCall call = f.Code().createConstructorCall(param.getType(),array);
                                paramFillList.add(call);
                                constructable = true;
                                break;
                            }
                        }
                        if(!constructable) return false;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
