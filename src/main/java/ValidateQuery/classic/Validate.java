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
            String message = e.getMessage();
            if(e.getMessage().equals("Syntax Error")){

                String eol = " , ";
                StringBuilder expected = new StringBuilder();
                for (int i = 0; i < e.expectedTokenSequences.length; i++) {
                    for (int j = 0; j < e.expectedTokenSequences[i].length; j++) {
                        expected.append(e.tokenImage[e.expectedTokenSequences[i][j]]);
                    }
                    expected.append(eol);
                }

                message += " at line " + e.currentToken.endLine + " column " + e.currentToken.endColumn + " after " + e.currentToken.image + "\n";
                message += "Try using :- " + expected.toString();
            }
            else if(e.getMessage().equals("Lexical Error")){
                message += " at line " + e.currentToken.endLine + " column " + e.currentToken.endColumn + " after " + e.currentToken.image + "\n";
            }

            return new Validate(false, message);
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
