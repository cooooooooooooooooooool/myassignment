package com.jm;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class TokenGenerator {

    public static String generateToken() {

        Random rnd = new Random(System.nanoTime());

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 3; i++) {
            int randNum = (int) rnd.nextInt(126);
            if (randNum < 33)
                randNum += 33;
            System.out.println("randNum : " + randNum);
            buf.append((char) randNum);
        }

        return buf.toString();
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> map = new HashMap<String, Integer>();
            for (int i = 0; i < 1000; i++) {
                String token = TokenGenerator.generateToken();
                Thread.sleep(10);
                map.put(token, map.get(token) != null ? map.get(token) + 1 : 0);
            }

            int count = 0;
            Iterator<String> keys = map.keySet().iterator();
            while (keys.hasNext()) {

                String key = keys.next();
                System.out.println("token : " + new String(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(key.getBytes("UTF-8")))));
                int value = map.get(key);
                if (value > 0) {
                    ++count;
                    System.out.println("key : " + key.getBytes("UTF-8") + ", value : " + map.get(key));
                }
            }

            System.out.println("count: " + count);
        } catch (InterruptedException e) {
        } catch (UnsupportedEncodingException e) {
        }
    }
}
