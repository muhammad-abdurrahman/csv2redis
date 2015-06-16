package csv2redis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Muhammad
 */
public class RedisConnection {

    public static Jedis RedisConnection(String redisUrl, int redisPort) {
        try {
            Jedis jedis = new Jedis(redisUrl, redisPort);
            return jedis;
        } catch (Exception e) {
            System.out.println("Error: unable to load driver class!" + e);
            logger("The Following Exception was triggered during the connection, probably unable to load driver class: " + e);
            System.exit(1);
            return null;
        }

    }

    private static void logger(String Message) {
        try {
            String data = Message;
            File file = new File("./log/" + getOnlyCurrentDate() + "_log.txt");
            //if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            //true = append file
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            FileWriter fileWritter = new FileWriter(file.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.newLine();
            bufferWritter.write(dateFormat.format(cal.getTime()) + ": " + data);

            bufferWritter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getOnlyCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return currentDate;
    }
}
