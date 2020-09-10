package com.feihong.jre8u20;

import com.feihong.jre8u20.util.Util;
import java.io.*;
import java.util.LinkedHashSet;

public class TestCase1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baous);

        LinkedHashSet set = new LinkedHashSet();
        set.add("aaa");

        oos.writeObject(set);
        oos.writeObject("bbb");
        oos.writeObject("ccc");
        oos.close();

        byte[] bytes = baous.toByteArray();
        //修改hashset的长度（元素个数）,由 1 修改为 3
        bytes[89] = 3;


        //调整 TC_ENDBLOCKDATA 标记的位置
        //97 = a
        for(int i = 0; i < bytes.length; i++){
            if(bytes[i] == 97 && bytes[i+1] == 97 && bytes[i+2] == 97){
                bytes = Util.deleteAt(bytes, i + 3);
                break;
            }
        }

        bytes = Util.addAtLast(bytes, (byte) 0x78);

        FileOutputStream fous = new FileOutputStream("case1.ser");
        fous.write(bytes);
        fous.close();

        //反序列化
        FileInputStream fis = new FileInputStream("case1.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        LinkedHashSet deserializedSet = (LinkedHashSet) ois.readObject();
        ois.close();

        for(Object obj : deserializedSet){
            System.out.println(obj);
        }

    }
}
