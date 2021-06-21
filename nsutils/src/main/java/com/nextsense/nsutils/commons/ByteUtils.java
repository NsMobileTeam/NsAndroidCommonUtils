package com.nextsense.nsutils.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressWarnings("unused")
public class ByteUtils {
    /**
     * Merge an array of byte arrays into a single array
     * @param arrays array of byte arrays
     * @return merged array
     */
    public static byte[] mergeArrays(byte[]... arrays) {
        byte[] composedArray = new byte[0];
        for (byte[] array : arrays) {
            byte[] combined = new byte[composedArray.length + array.length];
            System.arraycopy(composedArray, 0, combined, 0, composedArray.length);
            System.arraycopy(array, 0, combined, composedArray.length, array.length);
            composedArray = combined;
        }

        return composedArray;
    }

    /**
     * Hex string representing the values of the byte array
     * @param bytes array of bytes
     * @return string with hex values
     */
    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexMap = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexMap[v >>> 4];
            hexChars[j * 2 + 1] = hexMap[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * Any object to byte values
     * @param obj any object
     * @param <T> class of object
     * @return byte representation of the object
     * @throws Exception if conversion fails
     */
    public static <T> byte[] objectToBytes(T obj) throws Exception {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    /**
     * An object from a byte array
     * @param bytes bytes of previously converted object
     * @param <T> class of object
     * @return an object of type T
     * @throws Exception if conversion fails
     */
    @SuppressWarnings("unchecked")
    public static <T> T objectFromBytes(byte[] bytes) throws Exception {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return (T) o.readObject();
            }
        }
    }
}
