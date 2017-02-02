package com.floorcorn.tickettoride;

/**
 * Created by mgard on 2/1/2017.
 */

public class Serializer {

    private Gson gson;

    public Serializer(){

    }

    /**
    * This method converts an object into a String using the Gson library.
    *
    * @param obj The object to be serialized
    *
    * @return   The String representation of the input object.
     */
    public String serialize(Object obj){
        return gson.toJson(obj);
    }

    /**
     * This method converts a String into a corresponding Results object.
     *
     * @param results The String representing the results
     * @return      a Results object representing the input String
     */
    public Results deserializeResults(String results){
        Results resultsDeserialized = gson.fromJson(json, Results.class);
        return resultsDeserialized;
    }


}
