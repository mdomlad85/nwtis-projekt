/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Zeus
 */
public class SerializatorHelper {

    public static Serializable[] loadFile(String filename) {
        Serializable[] arr = null;
        try {
            File file = new File(filename);
            if (file.exists() && file.isFile()) {
                FileInputStream fis = new FileInputStream(filename);
                try (ObjectInputStream in = new ObjectInputStream(fis)) {
                    arr = (Serializable[]) in.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        return arr;
    }

    public static void saveFile(Serializable[] arr, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(arr);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
