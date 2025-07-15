package devassistant;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.*;
import java.util.List;

public class TestGenerator {
    public static void generateTests(String filePath) {
        System.out.println("[TESTGEN] Analyzing file: " + filePath);

        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            JavaParser parser = new JavaParser();
            CompilationUnit cu = parser.parse(in).getResult()
                .orElseThrow(() -> new RuntimeException("Parse error"));

            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
            if (methods.isEmpty()) {
                System.out.println("No methods found in file.");
                return;
            }

            String className = getClassName(filePath);
            String testClassName = className + "Test";
            StringBuilder testClassContent = new StringBuilder();

            testClassContent.append("package devassistant.generated;\n\n");
testClassContent.append("import devassistant.SampleService;\n");  // ✅ Import the class, not the package
testClassContent.append("import org.junit.jupiter.api.*;\n");
testClassContent.append("import static org.junit.jupiter.api.Assertions.*;\n\n");
testClassContent.append("public class ").append(testClassName).append(" {\n\n");
testClassContent.append("    private final SampleService service = new SampleService();\n\n");




            for (MethodDeclaration method : methods) {
                String methodName = method.getNameAsString();
                String methodCode = method.toString();

                System.out.println("\n[Generating Test for: " + methodName + "]");

                String prompt = """
                        Given this Java method:
                                %s
                        Write 2–3 JUnit 5 test methods ONLY.
                            - Do NOT include import statements.
                            - Do NOT include a class declaration.
                            - ONLY return test methods with proper indentation.
                            - Just write method-level @Test methods using static imports like assertEquals(...), not Assertions.assertEquals(...).
                            - Do not include outer class or import statements.

                        The output should look like:

                        @Test
                        public void testXYZ() {
                                        ...
                        }

                        @Test
                        public void testABC() {
                            ...
                        }
                        """.formatted(methodCode);

                String rawResponse = GPTClient.ask(prompt);
                String testCode = GPTClient.cleanCodeBlock(rawResponse);

                System.out.println("[Cleaned Test Code]\n" + testCode);  // optional debug

                // Indent the generated lines correctly
                for (String line : testCode.split("\n")) {
                    testClassContent.append("    ").append(line).append("\n");
                }

                testClassContent.append("\n");
            }

            testClassContent.append("}");

            // Ensure output directories exist
            File outputFile = new File("src/test/java/devassistant/generated/" + testClassName + ".java");
            outputFile.getParentFile().mkdirs();

            // Write to file
            FileWriter writer = new FileWriter(outputFile);
            writer.write(testClassContent.toString());
            writer.close();

            System.out.println("\n✅ Test file written to: " + outputFile.getPath());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error during test generation: " + e.getMessage());
        }
    }

    private static String getClassName(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        return name.replace(".java", "");
    }
}
