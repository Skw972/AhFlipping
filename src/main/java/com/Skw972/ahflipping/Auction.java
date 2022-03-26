package com.Skw972.ahflipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Auction {
    String name;
    String uuid;
    int price;
    String rarity;

    public static Auction newAh(String name1, String uuid1, int price1, String rarity1) {
        Auction auction = new Auction();
        auction.name = name1;
        auction.uuid = uuid1;
        auction.price = price1;
        auction.rarity = rarity1;
        return auction;
    }

    public static void sortList(ArrayList<Auction> list) {
        Collections.sort(list, new Comparator<Auction>() {
            @Override
            public int compare(Auction o1, Auction o2) {
                int price1 = o1.price;
                int price2 = o2.price;
                return price1 - price2;
            }
        });
    }

    public static boolean listContain(ArrayList<Auction> list, Auction auction) {
        for(Auction ah: list) {
            if(ah.uuid.equalsIgnoreCase(auction.uuid)) {
                return true;
            }
        }
        return false;
    }
}
