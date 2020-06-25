package ValidateQuery.classic;


import java.util.HashSet;
import java.util.Set;

public class Mapping{
    public boolean checkFields = false;
    public Set<String> fields = new HashSet<>();

    public Mapping(){
    }

    public boolean checkField(String field){
        return fields.contains(field);
    }

    public Mapping(Mapping mapping){
        checkFields = mapping.checkFields;
        fields = mapping.fields;
    }

}