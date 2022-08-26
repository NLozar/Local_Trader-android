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

    public static LoginRequestStatus resToLoginReqStatus(JsonNode jn) {
        if (jn.get("bad creds").asBoolean()) {
            return new LoginRequestStatus(true);
        } else {
            return new LoginRequestStatus(jn.get("token").textValue());
        }
    }

    public static boolean isUsernameTaken(JsonNode jn) {
        return jn.get("username taken").asBoolean();
    }

    public static PostRequestStatus jsonNodeToPostReqStatus(JsonNode jn) {
        PostRequestStatus prs = new PostRequestStatus(false);
        prs.setBadJwt(jn.get("bad jwt").asBoolean());
        return prs;
    }

    public static ProfileEditRequestStatus jsonNodeToPers(JsonNode jn) {
        ProfileEditRequestStatus pers = new ProfileEditRequestStatus(false);
        pers.setUsernameTaken(jn.get("username taken").asBoolean());
        pers.setWrongPassword(jn.get("wrong password").asBoolean());
        return pers;
    }
}
