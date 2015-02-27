package edu.csusb.cse.roomquest;

/**
 * Created by Michael on 2/18/2015.
 */
public class SearchMap {
    Map map;
    public SearchMap(Map map) {
        this.map = map;
    }
     static class SearchResults {
         Result[] results;
         static class Result {
             Room room;
             int matchStrength;
        }
    }
}
//SearchMap sm = new SearchMap;
//SearchMap.SearchResults sr = SearchMap.SearchResults();