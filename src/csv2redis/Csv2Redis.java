package csv2redis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 *
 * @author Muhammad
 */
public class Csv2Redis {

    private static String redisConnStr = "";
    private static String inputFile = "";
    private static String keyFormat = "";
    private static String prefix = "";
    private static int suffixColoumnIndex = -1;
    private static String outputFile = "";
    private static final String argumentErrorStr = "Error: You have entered an incorrect argument.\n"
            + "Please check and try again. For help, run the program with the -h option";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        processArguments(args);
        outputDataEntityToRedis(DataEntity.getDataEntityFromCSV(inputFile));
//        outputDataEntityToJsonFile(DataEntity.getDataEntityFromCSV(inputFile));
    }

    private static void processArguments(String[] args) {
        if (args.length < 1) {
            System.out.println(argumentErrorStr);
            System.exit(0);
        }
        if (String.valueOf(args[0].charAt(1)).equalsIgnoreCase(Option.H.name())) {
            // if -h is specified then display help message and exit program
            System.out.println(ArgumentHandler.getOptionValue(Option.H, args));
            System.exit(0);
        }

        if (ArgumentHandler.getOptionValue(Option.I, args) == null) {
            inputFile = null; // used to enforce option as mandatory
        } else {
            inputFile = ArgumentHandler.getOptionValue(Option.I, args);
        }

        if (ArgumentHandler.getOptionValue(Option.K, args) == null) {
            keyFormat = null; // used to enforce option as mandatory
        } else {
            keyFormat = ArgumentHandler.getOptionValue(Option.K, args);
            String str[] = keyFormat.split("-%");
            try {
                prefix = str[0];
                suffixColoumnIndex = Integer.parseInt(str[1]);
            } catch (Exception ex) {
                System.out.println(argumentErrorStr);
                System.exit(0);
            }

        }

        redisConnStr = ArgumentHandler.getOptionValue(Option.R, args) != null
                ? ArgumentHandler.getOptionValue(Option.R, args) : "localhost:6379";

        outputFile = ArgumentHandler.getOptionValue(Option.O, args) != null
                ? ArgumentHandler.getOptionValue(Option.O, args) : "/output/out.json";

        if (inputFile == null || keyFormat == null) {
            // terminate program if -i option is not supplied
            System.out.println(argumentErrorStr);
            System.exit(0);
        }
    }

    private static void outputDataEntityToRedis(DataEntity dataEntity) {
        Jedis conn = null;

        try {
            String[] str = redisConnStr.split(":");
            try {
                String redisUrl = str[0];
                int redisPort = Integer.parseInt(str[1]);
                conn = RedisConnection.RedisConnection(redisUrl, redisPort);
                System.out.println("Connection to RedisDB server was established successfully.");
            } catch (Exception ex){
                System.out.println("Connection to RedisDB server failed.\nExiting program...");
                System.exit(0);
            }

            Transaction t = conn.multi();

            //store data in redis list
            for (ArrayList<LinkedHashMap<String, String>> documents : dataEntity.getDocuments()) {
                System.out.println("Inserting data into RedisDB...");
                for (LinkedHashMap<String, String> document : documents) {
                    t.lpush(prefix + "-%" + document.get(dataEntity.getKeys().get(suffixColoumnIndex)),
                            DataEntity.getJsonFormattedStringOfDocument(document));
                }
                t.exec();
                System.out.println("Insertion of data completed successfully!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger("The Following Exception was triggered during the insertion of data into redis: " + exx);
        }
    }

    private static void outputDataEntityToJsonFile(DataEntity dataEntity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
