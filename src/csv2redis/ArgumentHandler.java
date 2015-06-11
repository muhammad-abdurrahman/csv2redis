package csv2redis;

/**
 *
 * @author Muhammad
 */
public class ArgumentHandler {
    private static final String helpStr = "Options:\n"
            + "\t-I: Input file (path) [Mandatory]\n"
            + "\t-O: Output file (Path) [Optional | Default is \"/output/out.json\"]\n"
            + "\t-R: Redis connection string [Optional | Default is \"localhost\"]\n"
            + "\t-H: Help page [List of options]";
    
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
