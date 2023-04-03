package org.jem.lichess.lichessbot.utilities;

public class Random {

    public static long longBetween(long min, long max) {
        long difference = max - min;
        Double random = Math.random();
        random = Math.floor(random * difference);
        random = random + min;
        return random.longValue();
    }

}
