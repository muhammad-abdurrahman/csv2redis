package csv2redis;

/**
 *
 * @author Muhammad
 */
public class ArgumentHandler {
    private static final String helpStr = "\nOptions:\n"
            + "\t-i: Input file (path) [Mandatory]\n"
            + "\t-k: Key format (Format : {STRING}-%{COLOUMN_INDEX}) [Mandatory]\n"
            + "\t\tFormat of keys for RedisDB datasets. The coloumn index is a zero"
            + "\t\tbased index referencing a coloumn from the CSV coloumn headers.\n"
            + "\t\tThe value from the specified coloumn will be inserted into the\n"
            + "\t\tset's key for each dataset.\n"
            + "\t-o: Output file (Path) [Optional | Default is \"/output/out.json\"]\n"
            + "\t-r: Redis connection string [Optional | Default is \"localhost:6379\"]\n"
            + "\t\t Connection string must be provided in the format url:port.\n"
            + "\t-h: Help page [List of options]\n\n"
            + "Example usage:\njava -jar Csv2Redis.jar -i \"D://diameter_numbering_plan_V1.csv\" -k dnp-%3";
    
    static String getOptionValue(Option option, String[] args){
        if (option.equals(Option.H)){
            return helpStr;
        }
        
        if (!isValidArguments(args)){
            return null;
        }
        
        for (int i = 0; i < args.length; i+=2) {
            String formattedOption = String.valueOf(args[i].charAt(1));
            if(formattedOption.equalsIgnoreCase(option.name())){
                return args[++i];
            }
        }
        return null;
    }
    
    private static boolean isValidArguments(String[] args){
        if(!isValidArgumentLength(args)){
            return false;
        }
        
        // validate options
        for (int i = 0; i < args.length; i+=2) {
            boolean validOption = true;
            for (Option opt : Option.values()) {
                String formattedOption = String.valueOf(args[i].charAt(1));
                if(!opt.name().equalsIgnoreCase(formattedOption) && !validOption){
                    validOption = false;
                }
            }
            if (!validOption){
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean isValidArgumentLength(String[] args){
        return args.length % 2 == 0;
    }
}
