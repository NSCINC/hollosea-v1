import java.io.File;
import java.io.IOException;

public class CCompiler {
    private static final String COMPILE_COMMAND = "gcc"; // Compilation command
    private static final String OUTPUT_NAME = "output_program"; // Final program name
    private static final String TERMINAL_COMMAND = "abc"; // Fictional command to run the program

    // Method to compile all .c files in the current directory
    public static void compileAllCFiles() {
        StringBuilder compileCmd = new StringBuilder(COMPILE_COMMAND + " -o " + OUTPUT_NAME + " ");

        // Get the current directory
        File currentDir = new File(".");

        // List all files in the current directory
        File[] files = currentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                // If the file ends with .c, add it to the compile command
                if (file.isFile() && file.getName().endsWith(".c")) {
                    compileCmd.append(file.getName()).append(" ");
                }
            }
        }

        // Execute the compilation command
        System.out.println("Compiling with command: " + compileCmd);
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd.toString().trim());
            int exitCode = compileProcess.waitFor();
            if (exitCode != 0) {
                throw new IOException("Compilation failed.");
            }
            System.out.println("Compilation successful!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Method to run the compiled program using the terminal "abc"
    public static void runProgramWithABC() {
        String runCmd = TERMINAL_COMMAND + " ./" + OUTPUT_NAME;
        
        // Execute the program
        System.out.println("Executing the program with terminal '" + TERMINAL_COMMAND + "': " + runCmd);
        try {
            Process runProcess = Runtime.getRuntime().exec(runCmd);
            int exitCode = runProcess.waitFor();
            if (exitCode != 0) {
                throw new IOException("Execution failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        compileAllCFiles();  // Compile all .c files
        runProgramWithABC(); // Run the program with the fictional terminal command "abc"
    }
}
