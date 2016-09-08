import util.Hadoop.FileCombiner;
import util.Hadoop.PearsonAndPartialCorrelation.ComputePartialCorrelation;
import util.Hadoop.PearsonAndPartialCorrelation.ComputePearsonCorrelation;

/**
 * Created by FireAwayH on 05/09/2016.
 */
public class Run {
    public static void main(String[] args){
        if(args.length < 3){
            System.out.println("Please specify command");
            System.exit(0);
        }

        try {
            String command = args[0];
            String input = args[1];
            String output = args[2];

            switch (command) {
                case "combine":
                    FileCombiner.combine(input, output);
                    break;
                case "compute":
                    String saveToDB = "no";
                    if(args.length == 4){
                        saveToDB = args[3];
                    }
                    ComputePartialCorrelation.compute(input, output, saveToDB);
                    break;
                case "pearson":
                    ComputePearsonCorrelation.compute(input, output);
                    break;
                default:
                    System.out.println("Commands: combine, compute, pearson");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
