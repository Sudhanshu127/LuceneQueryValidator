package ValidateQuery.classic;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Validate {
    private final Boolean valid;
    private String message = "";
    private final QueryParser queryParser = new QueryParser("contents", new StandardAnalyzer());
    public Validate validateQuery(String query){
        try{
            queryParser.parse(query);
        }
        catch (ParseException e){
            return new Validate(false, e.getCause().toString());
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
