package com.agiledge.keocometemployee.utilities;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Pateel on 1/20/2016.
 */
public class OtherFunctions {
    public static String[] JSONArrayToStringArray(JSONArray array	)
    {
        String[] ret= new String[array.length()];
        try {
            for (int i=0;i<array.length();i++){

                ret[i]= (array.get(i).toString());


            }
            return ret;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }
}
