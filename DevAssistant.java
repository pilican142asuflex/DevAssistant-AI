package devassistant;

public class DevAssistant {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: devassist [debug|testgen|commitmsg] <file>");
            return;
        }

        String command = args[0];
        switch (command) {
            case "debug":
                if (args.length < 2) {
                    System.out.println("Usage: devassist debug <JavaFile>");
                    return;
                }
                LogParser.analyze(args[1]);
                break;

            case "testgen":
                if (args.length < 2) {
                    System.out.println("Usage: devassist testgen <JavaFile>");
                    return;
                }
                TestGenerator.generateTests(args[1]);
                break;

            case "commitmsg":
                CommitMessageGen.generateFromGitDiff();
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
    }
}
