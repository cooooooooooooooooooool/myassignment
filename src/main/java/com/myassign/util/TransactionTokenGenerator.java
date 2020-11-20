package com.myassign.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TransactionTokenGenerator {

    public static String generateToken() {

        Random rnd = new Random(System.nanoTime());

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            int randNum = rnd.nextInt(126);
            if (randNum < 33)
                randNum += 33;
            buf.append((char) randNum);
        }

        log.info("generated token : " + buf);
        return buf.toString();
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < 1000; i++) {
                String token = TransactionTokenGenerator.generateToken();
                Thread.sleep(10);
                map.put(token, map.get(token) != null ? map.get(token) + 1 : 0);
            }

            int count = 0;
            Iterator<String> keys = map.keySet().iterator();
            while (keys.hasNext()) {

                String token = keys.next();
                log.info("token : " + new String(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(token.getBytes("UTF-8")))));
                int value = map.get(token);
                if (value > 0) {
                    ++count;
                    log.info("key : " + token.getBytes("UTF-8") + ", value : " + map.get(token));
                }
            }

            System.out.println("count: " + count);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (UnsupportedEncodingException e) {
        }
    }
}
