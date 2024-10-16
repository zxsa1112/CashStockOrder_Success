import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SarifToJson {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("SARIF 파일 경로를 입력하세요.");
            return;
        }

        String sarifFilePath = args[0];
        File sarifFile = new File(sarifFilePath);
        if (!sarifFile.exists()) {
            System.out.println("지정된 SARIF 파일이 존재하지 않습니다: " + sarifFilePath);
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode sarifData = objectMapper.readTree(sarifFile);

            JsonNode runs = sarifData.get("runs");
            if (runs != null && runs.isArray()) {
                for (JsonNode run : runs) {
                    JsonNode results = run.get("results");
                    if (results != null && results.isArray()) {
                        for (JsonNode result : results) {
                            String ruleId = result.has("ruleId") ? result.get("ruleId").asText() : "N/A";
                            String message = result.has("message") && result.get("message").has("text") 
                                ? result.get("message").get("text").asText() : "N/A";
                            
                            System.out.println("Rule ID: " + ruleId);
                            System.out.println("Message: " + message);
                        }
                    }
                }
            }

            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sarifData);
            System.out.println(jsonString);

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다: " + sarifFilePath);
        } catch (IOException e) {
            System.out.println("입출력 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
}