package ValidateQuery.classic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateTest {

    @Test
    @DisplayName("Testing validateQuery")
    public void validateQuery() {
        Map<String, Boolean> queries = new HashMap<>();

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(Objects.requireNonNull(getClass().getClassLoader().getResource("queries.txt")).getFile()));
            JSONArray jsonArray= (JSONArray) obj;
            for (JSONObject jsonObject : (Iterable<JSONObject>) jsonArray) {
                JSONArray array = (JSONArray) jsonObject.get("criterias");
                System.out.println(array);
                if(array == null){
                    continue;
                }
                JSONObject jsonObject1 = (JSONObject) array.get(0);
                queries.put((String) jsonObject1.get("textQuery"), true);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        int i=1;
        for (Map.Entry<String, Boolean> query : queries.entrySet()){
            System.out.println(query.getKey());
            Validate result = Validate.validateQuery(query.getKey());
            assertEquals(query.getValue(), result.valid, "Failed at query " + i + ":- " + query.getKey());
            i++;
        }
    }
}