package cn.whatisee.dynamic.test;

import javassist.*;

import java.lang.reflect.Method;

public class JavassistCalls extends TimeCalls
{
    /** Parameter type array for call with no parameters. */
    private static final CtClass[] NO_ARGS = {};
    
    /** Parameter type array for call with single <code>int</code> value. */
    private static final CtClass[] INT_ARGS = { CtClass.intType };
    
    /**
     * Create access class for getting and setting a bean-style property value.
     * This creates a class that implements a get/set interface, with the actual
     * implementations of the get and set methods simply calling the supplied
     * target class methods, returning the bytecode array. As written, this only
     * works with <code>int</code> property values, but could easily be modified
     * for any other type.
     *
     * @param tclas target class (may inherit get and set methods, or implement
     * directly)
     * @param gmeth get method (must take nothing, return <code>int</code>)
     * @param smeth set method (must take <code>int</code>, return void)
     * @param cname name for constructed access class
     * @return bytecode of class implementing the object interface
     * @throws Exception on error creating class
     */
     
    protected byte[] createAccess(Class tclas, Method gmeth, Method smeth,
        String cname) throws Exception {
        
        // build generator for the new class
        String tname = tclas.getName();
        ClassPool pool = ClassPool.getDefault();
        CtClass clas = pool.makeClass(cname);
        CtClass target = pool.get(tname);
        
        // add target object field to class
        CtField field = new CtField(target, "m_target", clas);
        clas.addField(field);
        
        // add public default constructor method to class
        CtConstructor cons = new CtConstructor(NO_ARGS, clas);
        cons.setBody(";");
        clas.addConstructor(cons);
        
        // add public <code>setTarget</code> method
        CtMethod meth = new CtMethod(CtClass.voidType, "setTarget",
            new CtClass[] { pool.get("java.lang.Object") }, clas);
        meth.setBody("m_target = (" + tclas.getName() + ")$1;");
        clas.addMethod(meth);
        
        // add public <code>getValue</code> method
        meth = new CtMethod(CtClass.intType, "getValue", NO_ARGS, clas);
        meth.setBody("return m_target." + gmeth.getName() + "();");
        clas.addMethod(meth);
        
        // add public <code>setValue</code> method
        meth = new CtMethod(CtClass.voidType, "setValue", INT_ARGS, clas);
        meth.setBody("m_target." + smeth.getName() + "($1);");
        clas.addMethod(meth);
        
        // return binary representation of completed class
        clas.addInterface(pool.get("IAccess"));
        return clas.toBytecode();
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            BCELCalls inst = new BCELCalls();
            int lcnt = Integer.parseInt(args[0]);
            inst.run("value1", lcnt);
            inst.run("value2", lcnt);
        } else {
            System.out.println("Usage: JavassistCalls loop-count");
        }
    }
}