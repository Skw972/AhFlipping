package com.Skw972.ahflipping;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GetAH {

    public static String[] reflist = {"Very ", "Highly ", "Extremely ", "Not So ", "Thicc ", "Even More ", "◆ ", "\\[[^\\]]*\\] ", " ✦", "⚚ ", " ✪", "✪", "Stiff ", "Lucky ", "Jerry's ", "Dirty ", "Fabled ", "Suspicious ", "Gilded ", "Warped ", "Withered ", "Bulky ", "Stellar ", "Heated ", "Ambered ", "Fruitful ", "Magnetic ", "Fleet ", "Mithraic ", "Auspicious ", "Refined ", "Headstrong ", "Precise ", "Spiritual ", "Moil ", "Blessed ", "Toil ", "Bountiful ", "Candied ", "Submerged ", "Reinforced ", "Cubic ", "Warped ", "Undead ", "Ridiculous ", "Necrotic ", "Spiked ", "Jaded ", "Loving ", "Absolutely ", "Perfect ", "Renowned ", "Giant ", "Empowered ", "Ancient ", "Sweet ", "Silky ", "Bloody ", "Shaded ", "Gentle ", "Odd ", "Fast ", "Fair ", "Epic ", "Sharp ", "Heroic ", "Spicy ", "Legendary ", "Deadly ", "Fine ", "Grand ", "Hasty ", "Neat ", "Rapid ", "Unreal ", "Awkward ", "Rich ", "Clean ", "Fierce ", "Heavy ", "Light ", "Mythic ", "Pure ", "Smart ", "Titanic ", "Wise ", "Bizarre ", "Itchy ", "Ominous ", "Pleasant ", "Pretty ", "Shiny ", "Simple ", "Strange ", "Vivid ", "Godly ", "Demonic ", "Forceful ", "Hurtful ", "Keen ", "Strong ", "Superior ", "Unpleasant ", "Zealous ", "Salty ", "Treacherous "};

    public static ArrayList<String> reforges = new ArrayList<String>(Arrays.asList(reflist));

    static HashMap<String, ArrayList<Auction>> map = new HashMap<String, ArrayList<Auction>>();


    public static String deleteReforge(String name) {
        String okay = name;
        for(String ref: reforges) {
            okay = okay.replaceAll(ref, "");
        }
        return okay;
    }

    public static String changeInt(int value) {
        if(1000 <= value && value < 1000000) {
            double zaok = (double) value/1000;
            double licz = (Math.floor(100*zaok))/100;
            return licz +"K";
        }
        if(1000000 <= value && value < 1000000000) {
            double zaok = (double) value/1000000;
            double licz = (Math.floor(100*zaok))/100;
            return licz +"M";
        }
        if(1000000000 <= value) {
            double zaok = (double) value/1000000000;
            double licz = (Math.floor(100*zaok))/100;
            return licz +"B";
        }
        return Integer.toString(value);
    }

    public static ChatComponentText createMessage(String uuid, String name, int price, int price2, int profit) {
        ChatComponentText text = new ChatComponentText(MessageFormat.format("{0}{1}Name: {2}{1}{8} {3}{1}{9} {4}{1}-> {3}{1}{10} {5}{1}({6}Profit: {7}{1}{11}{5}{1})", EnumChatFormatting.GRAY, EnumChatFormatting.BOLD, EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, name, changeInt(price), changeInt(price2), changeInt(profit)));
        // {8} - nazwa {9} - cena {10} - druga cena {11} - profit
        ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + uuid);
        ChatStyle style = new ChatStyle();
        style = style.setChatClickEvent(click);
        text.setChatStyle(style);
        return text;
    }


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JsonObject readJsonFromUrl(String url) throws IOException {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JsonParser().parse(jsonText).getAsJsonObject();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void getauctions() throws IOException {
        final String linkjson = "https://api.hypixel.net/skyblock/auctions?page=";
        JsonObject json1 = readJsonFromUrl(linkjson + "0");
        int totalpages = json1.get("totalPages").getAsInt();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(15);
        if(map.isEmpty()) {
            System.out.println("Map is empty! Wait until it becomes full!");
            for (int i = 0; i < totalpages; i++) {
                final int finalI = i;
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        JsonObject json = null;
                        try {
                            json = readJsonFromUrl(linkjson + finalI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        long unixTime = System.currentTimeMillis();
                        assert json != null;
                        for (Object o : json.get("auctions").getAsJsonArray()) {
                            JsonObject ob = new JsonParser().parse(o.toString()).getAsJsonObject();
                            if (ob.get("bin").getAsBoolean() && !ob.get("item_lore").getAsString().contains("Furniture") && !ob.get("item_lore").getAsString().contains("Cake Soul") && !ob.get("item_name").getAsString().contains("Skin") && !ob.get("item_name").getAsString().contains("Enchanted Book") && !ob.get("claimed").getAsBoolean() && ob.get("start").getAsLong() + 60000 > unixTime && !ob.get("item_name").getAsString().contains("Beach Ball") && !ob.get("item_name").getAsString().contains("Potatoes")) {
                                String name = ob.get("item_name").getAsString();
                                String uuid = ob.get("uuid").getAsString();
                                int price = ob.get("starting_bid").getAsInt();
                                String rarity = ob.get("tier").getAsString();
                                Auction auction = Auction.newAh(name, uuid, price, rarity);
                                if (ob.get("item_lore").getAsString().contains("pet menu!")) {
                                    if (ob.get("tier").getAsString().equalsIgnoreCase("LEGENDARY") || ob.get("tier").getAsString().equalsIgnoreCase("EPIC") || ob.get("tier").getAsString().equalsIgnoreCase("MYTHIC")) {
                                        if (!map.containsKey(deleteReforge(auction.name) + auction.rarity)) {
                                            map.put(deleteReforge(auction.name) + auction.rarity, new ArrayList<Auction>());
                                        }
                                        map.get(deleteReforge(auction.name) + auction.rarity).add(auction);
                                        ArrayList<Auction> ok = map.get(deleteReforge(auction.name) + auction.rarity);
                                        Auction.sortList(ok);
                                        map.put(deleteReforge(auction.name) + auction.rarity, ok);
                                    }
                                } else {
                                    if (!map.containsKey((deleteReforge(auction.name) + auction.rarity))) {
                                        map.put(deleteReforge(auction.name) + auction.rarity, new ArrayList<Auction>());
                                    }
                                    map.get(deleteReforge(auction.name) + auction.rarity).add(auction);
                                    ArrayList<Auction> ok = map.get(deleteReforge(auction.name) + auction.rarity);
                                    Auction.sortList(ok);
                                    map.put(deleteReforge(auction.name) + auction.rarity, ok);
                                }
                            }
                        }
                    }
                });
            }
            while(true) {
                if(executor.getActiveCount() == 0) {
                    break;
                }
            }
            System.out.println("So he made it full. Check it!");
        }
        if(!map.isEmpty()){
            for(int i = 0; i < totalpages; i++) {
                final int finalI = i;
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        JsonObject json = null;
                        try {
                            json = readJsonFromUrl(linkjson+ finalI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert json != null;
                        for(Object o : json.get("auctions").getAsJsonArray()){
                            JsonObject ob = new JsonParser().parse(o.toString()).getAsJsonObject();
                            if(ob.get("bin").getAsBoolean() && !ob.get("item_lore").getAsString().contains("Furniture") && !ob.get("item_lore").getAsString().contains("Cake Soul") && !ob.get("item_name").getAsString().contains("Skin") && !ob.get("item_name").getAsString().contains("Enchanted Book") && !ob.get("claimed").getAsBoolean() && !ob.get("item_name").getAsString().contains("Beach Ball") && !ob.get("item_name").getAsString().contains("Potatoes")) {
                                String name = ob.get("item_name").getAsString();
                                String uuid = ob.get("uuid").getAsString();
                                int price = ob.get("starting_bid").getAsInt();
                                String rarity = ob.get("tier").getAsString();
                                Auction auction = Auction.newAh(name, uuid, price, rarity);
                                if (ob.get("item_lore").getAsString().contains("pet menu!")) {
                                    if (ob.get("tier").getAsString().equalsIgnoreCase("LEGENDARY") || ob.get("tier").getAsString().equalsIgnoreCase("EPIC") || ob.get("tier").getAsString().equalsIgnoreCase("MYTHIC")) {
                                        if (!Auction.listContain(map.get(deleteReforge(auction.name)+rarity), auction)) {
                                            ChatComponentText mess = createMessage(uuid, name, price, map.get(deleteReforge(name)+rarity).get(0).price, (map.get(deleteReforge(name)+rarity).get(0).price-price));
                                            if(map.get(deleteReforge(name)+rarity).get(0).price-price >= 100000) {
                                                try {
                                                    Minecraft.getMinecraft().thePlayer.addChatMessage(mess);
                                                } catch (NullPointerException e) {
                                                    System.out.println(mess);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (!Auction.listContain(map.get(deleteReforge(auction.name)+rarity), auction)) {
                                        ChatComponentText mess = createMessage(uuid, name, price, map.get(deleteReforge(name)+rarity).get(0).price, (map.get(deleteReforge(name)+rarity).get(0).price-price));
                                        if(map.get(deleteReforge(name)+rarity).get(0).price-price >= 100000) {
                                            try {
                                                Minecraft.getMinecraft().thePlayer.addChatMessage(mess);
                                            } catch (NullPointerException e) {
                                                System.out.println(mess);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        awaitTerminationAfterShutdown(executor);
    }
}
