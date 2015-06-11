package csv2redis;

/**
 *
 * @author Muhammad
 */
public class Csv2Redis {

    private static String redisConnStr = "";
    private static String inputFile = "";
    private static String outputFile = "";
    private static final String argumentErrorStr = "Error: You have entered an incorrect argument.\n"
            + "Please check and try again. For help, run the program with the -h option";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        handleArguments(args);
        
    }

    private static void handleArguments(String[] args) {
        if (args.length < 1) {
            System.out.println(argumentErrorStr);
            System.exit(0);
        }
        if (String.valueOf(args[0].charAt(1)).equalsIgnoreCase(Option.H.name())) {
            System.out.println(ArgumentHandler.getOptionValue(Option.H, args));
            System.exit(0);
        }
        if (ArgumentHandler.getOptionValue(Option.I, args) == null) {
            inputFile = null;
        } else {
            inputFile = ArgumentHandler.getOptionValue(Option.I, args);
        }

        redisConnStr = ArgumentHandler.getOptionValue(Option.R, args) != null
                ? ArgumentHandler.getOptionValue(Option.R, args) : "localhost";

        outputFile = ArgumentHandler.getOptionValue(Option.O, args) != null
                ? ArgumentHandler.getOptionValue(Option.O, args) : "/output/out.json";
        
        if(inputFile == null){
            System.out.println(argumentErrorStr);
            System.exit(0);
        }
    }

}
