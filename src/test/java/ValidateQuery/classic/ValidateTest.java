package ValidateQuery.classic;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void validateQuery() {
        String query1 = "name:jack AND country:India";
        String query2 = "name:jack and (clothes:jeans";

        Validate result1 = Validate.validateQuery(query1);
        Validate result2 = Validate.validateQuery(query2);

        assertEquals(true, result1.valid);
        assertEquals(false, result2.valid);
    }
}