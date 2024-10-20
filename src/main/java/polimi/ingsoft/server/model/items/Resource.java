package polimi.ingsoft.server.model.items;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import polimi.ingsoft.server.model.items.Item;

/**
 * Enumerations that lists all possible Resource values
 */
public enum Resource implements Item {
    LEAF( "LEAF"), WOLF("WOLF"), MUSHROOM("MUSHROOM"), BUTTERFLY("BUTTERFLY");
    private String value;

     @JsonCreator
     Resource(@JsonProperty("items") String value){
        this.value=value;
    }
}