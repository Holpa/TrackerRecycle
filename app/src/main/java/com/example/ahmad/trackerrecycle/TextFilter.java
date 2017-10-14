package com.example.ahmad.trackerrecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ahmad on 10/13/2017.
 */

public class TextFilter {
    public TextFilter(){}
    private static final Pattern TAG_REGEX_tag = Pattern.compile("<Title_E>(.+?)</Title_E>");
    public ArrayList<String> getUserNames(String userNames)
    {
        ArrayList<String> userList = new ArrayList<>();
        String userNamesArr[];
        userNames = userNames.replaceAll("[^a-zA-Z0-9  //W %]", "");
        userNamesArr = userNames.split("%%");
        for(String i : userNamesArr ){
            if(i.equals("") || i.equals("%"))
            {
                i.toString();
            }else
            {
                userList.add(i);
            }
        }

        return userList;
    }

    public List<String> getPostOrders(String s) {
        List<String> listFinal = new ArrayList<>();
        Pattern TAG_REGEX_Title_E = Pattern.compile("<Title_E>(.+?)</Title_E>");
        Pattern TAG_REGEX_Instructions_E = Pattern.compile("<Instructions_E>(.+?)</Instructions_E>");
        listFinal.addAll(getTagsValue(s,TAG_REGEX_Title_E));
        listFinal.addAll(getTagsValue(s,TAG_REGEX_Instructions_E));
        return listFinal;
    }

    private List<String> getTagsValue(String s, Pattern pattern){
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }
}
