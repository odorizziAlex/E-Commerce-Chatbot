package com.company.Result;

import com.company.Tools.JSONHandler;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.company.utils.Config.*;

public class URLGenerator {

    /**
     * This url generator specifically works for the zalando.de website.
     */

    private JSONHandler jsonHandler = new JSONHandler();
    private StringBuilder urlZalando = new StringBuilder("https://en.zalando.de/");

    public String buildURL(ArrayList<String> components){
        if(!components.get(DEMAND_GENDER).equals(EMPTY_STRING)
                && !components.get(DEMAND_ITEM).equals(EMPTY_STRING)){
            urlZalando.append(jsonHandler.getGenderAndItemSnippet(components.get(DEMAND_GENDER), components.get(DEMAND_ITEM)));
        }
        if(!components.get(DEMAND_COLOR).equals(EMPTY_STRING)){
            urlZalando.append(jsonHandler.getColorSnippet(components.get(DEMAND_COLOR)));
        }
        if(Pattern.matches("[\\d]+", components.get(DEMAND_SIZE))){
            urlZalando.append(jsonHandler.getSizeSnippet("numeric")+components.get(DEMAND_SIZE));
        }else{
            urlZalando.append(jsonHandler.getSizeSnippet(components.get(DEMAND_SIZE)));
        }
        if(!components.get(DEMAND_PRICE).equals(EMPTY_STRING)){
            String price = components.get(DEMAND_PRICE).replaceAll("[^\\d]","");
            urlZalando.append(jsonHandler.getPriceSnippet("maxPrice")+price);
        }
        if(!components.get(DEMAND_FABRIC).equals(EMPTY_STRING)){
            urlZalando.append(jsonHandler.getFabricSnippet(components.get(DEMAND_FABRIC)));
        }
        return "\nHere are your results:\n"+urlZalando;
    }
}
