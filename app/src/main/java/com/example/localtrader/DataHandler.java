package com.example.localtrader;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

public class DataHandler {

    public static ArrayList<AllItemsViewEntry> jsonNodeToAllItemsViewEntryArrayList(JsonNode jsonNode) {
        ArrayList<AllItemsViewEntry> out = new ArrayList<>();
        for (JsonNode currJn : jsonNode) {
            AllItemsViewEntry currAive = new AllItemsViewEntry(
                    currJn.get("title").textValue(),
                    currJn.get("price").textValue(),
                    currJn.get("descr").textValue(),
                    currJn.get("uuid").textValue()
            );
            out.add(currAive);
        }
        return out;
    }

    public static ItemDetailsDataHolder jsonNodeToItemDetailsDataHolder(JsonNode jn) {
        return new ItemDetailsDataHolder(
                jn.get("title").textValue(),
                jn.get("seller").textValue(),
                jn.get("price").textValue(),
                jn.get("descr").textValue(),
                jn.get("contact").textValue(),
                jn.get("uuid").textValue()
        );
    }

    public static JwtHolder jwtToString(JsonNode jn) {
        return new JwtHolder(jn.get("token").textValue());
    }
}
