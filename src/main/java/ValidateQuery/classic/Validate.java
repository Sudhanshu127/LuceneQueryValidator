package ValidateQuery.classic;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Validate {
    public final Boolean valid;
    public String message = "";
    private static final QueryParser queryParser = new QueryParser("contents", new StandardAnalyzer());
    public static Validate validateQuery(String query){
        try{
            queryParser.parse(query);
        }
        catch (ParseException e){
            return new Validate(false, e.getMessage());
        }
        return new Validate(true);
    }

    private Validate(Boolean valid){
        this.valid = valid;
    }

    private Validate(Boolean valid, String message){
        this.valid = valid;
        this.message = message;
    }
}
