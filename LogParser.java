package devassistant;

public class LogParser {
    public static void analyze(String filePath) {
        System.out.println("[DEBUG] Simulating log analysis for: " + filePath);

        String simulatedLog = """
            java.lang.NullPointerException
                at MyService.getUser(MyService.java:42)
                at Main.main(Main.java:10)
            """;

        System.out.println("[LOG] Detected log:\n" + simulatedLog);

        String prompt = """
            Analyze this Java error log and suggest the root cause and fix:

            Log:
            """ + simulatedLog;

        String reply = GPTClient.ask(prompt);
        System.out.println("\n[GPT-4 Response]");
        System.out.println(reply);
    }
}
