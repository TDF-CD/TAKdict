import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvMapper {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println(
                    "Argument(s) missing. Required args: filePath, term, sourceColumn, destinationColumn. " +
                            "Optional 5th arg: delimiter"
            );
            System.exit(1);
        }

        String filePath = args[0]; // "src/main/resources/APP6B_to_CoT.csv"
        String term = args[1]; // "SUG-IRSR--"

        int sourceColumn = 0;
        int destinationColumn = 0;
        try {
            sourceColumn = Integer.parseInt(args[2]); // 0
        } catch (NumberFormatException e) {
            System.err.println("Argument \"" + args[2] + "\" must be an integer.");
            System.exit(1);
        }
        try {
           destinationColumn  = Integer.parseInt(args[3]); // 3
        } catch (NumberFormatException e) {
            System.err.println("Argument \"" + args[3] + "\" must be an integer.");
            System.exit(1);
        }

        String delimiter;
        try {
            delimiter = args[4];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            delimiter = ";";
        }

        findInCsv(filePath, term, sourceColumn, destinationColumn, delimiter);
    }

    public static void findInCsv(String filePath, String term, int sourceColumn, int destinationColumn, String delimiter) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                if (values[sourceColumn].equals(term)) {
                    System.out.println(values[destinationColumn]);
                };
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
