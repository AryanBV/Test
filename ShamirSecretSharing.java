package EXAM;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecretSharing {

    // Function to decode a value based on its base
    public static long decodeValue(String value, int base) {
        return Long.parseLong(value, base);  // Parse the string value with the specified base
    }

    // Function to perform Lagrange interpolation
    public static double lagrangeInterpolation(Map<Integer, Long> points, int k) {
        double result = 0.0;

        for (Map.Entry<Integer, Long> entry : points.entrySet()) {
            int xi = entry.getKey();
            long yi = entry.getValue();
            double term = yi;

            for (Map.Entry<Integer, Long> entry2 : points.entrySet()) {
                int xj = entry2.getKey();
                if (xi != xj) {
                    term *= (0 - xj);  // Evaluate at x = 0 for the constant term
                    term /= (xi - xj);
                }
            }
            result += term;
        }

        return result;
    }

    public static void main(String[] args) {
        // Sample JSON input as string (replace this with actual JSON input)
        String jsonString = "{ \"keys\": { \"n\": 4, \"k\": 3 }, \"1\": { \"base\": \"10\", \"value\": \"4\" }, \"2\": { \"base\": \"2\", \"value\": \"111\" }, \"3\": { \"base\": \"10\", \"value\": \"12\" }, \"6\": { \"base\": \"4\", \"value\": \"213\" } }";
        
        // Parse JSON
        JSONObject jsonObj = new JSONObject(jsonString);
        JSONObject keys = jsonObj.getJSONObject("keys");
        int n = keys.getInt("n");  // Number of roots provided
        int k = keys.getInt("k");  // Minimum number of roots needed

        // Map to store (x, y) points
        Map<Integer, Long> points = new HashMap<>();

        for (String key : jsonObj.keySet()) {
            if (!key.equals("keys")) {
                JSONObject root = jsonObj.getJSONObject(key);
                int base = root.getInt("base");
                String value = root.getString("value");

                int x = Integer.parseInt(key);  // x is the key of the JSON object
                long y = decodeValue(value, base);  // Decode y value

                points.put(x, y);
            }
        }

        // Apply Lagrange interpolation to find the constant term 'c'
        double constantTerm = lagrangeInterpolation(points, k);

        // Print the constant term
        System.out.println("Constant term (c) is: " + constantTerm);
    }
}
