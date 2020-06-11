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

        ElasticSearchQuery.makeConnection();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(Objects.requireNonNull(getClass().getClassLoader().getResource("queries.txt")).getFile()));
            JSONArray jsonArray= (JSONArray) obj;
            for (JSONObject jsonObject : (Iterable<JSONObject>) jsonArray) {

                JSONArray array = (JSONArray) jsonObject.get("criterias");

                if(array == null){
                    continue;
                }
                JSONObject jsonObject1 = (JSONObject) array.get(0);

                String query = (String) jsonObject1.get("textQuery");
                if(query.contains("NEAR/")){
                    query = query.replaceAll("NEAR\\/\\d*", "AND");
                }

                queries.put(query, ElasticSearchQuery.validQuery(query));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------------------------Starting Validation -------------------------------------------------------------------------------------");
        System.out.println("Validating on " + queries.size() + " cases\n\n");

        int counter = 0;
        int i = 1;
        for (Map.Entry<String, Boolean> query : queries.entrySet()){
            System.out.println("Query number " + i);
            System.out.println(query.getKey() + "\n");
            Validate result = Validate.validateQuery(query.getKey());
            if(!query.getValue()){
                System.out.println("Query is invalid");
                System.out.println(result.message);
                counter++;
            }
            else
            {
                System.out.println("Query is valid");
            }
            System.out.println("\n\n");

            assertEquals(query.getValue(), result.valid, "Query Result mismatch. Query :- " + query.getKey());
            i++;
        }


        System.out.println("Invalid queries found are " + counter + " out of " + queries.size());
    }
}