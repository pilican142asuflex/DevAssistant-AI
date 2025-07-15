package devassistant;
public class SampleService {
    public int multiply(int a, int b) {
        return a * b;
    }

    public String greet(String name) {
        return name == null ? "Hello, Guest" : "Hello, " + name;
    }
}
