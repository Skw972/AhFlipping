package com.Skw972.ahflipping;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.IOException;

@Mod(modid = AhFlipping.MODID, version = AhFlipping.VERSION)
public class AhFlipping
{
    public static final String MODID = "ahflipping";
    public static final String VERSION = "1.8.9-0.0.3";

    static Thread dothings;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        dothings = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetAH.getauctions();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    long now = System.currentTimeMillis();
                    long update = 0;
                    try {
                        update = GetAH.readJsonFromUrl("https://api.hypixel.net/skyblock/auctions?page=0").get("lastUpdated").getAsLong();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (now + 1 > update + 60000) {
                        System.out.println("Did or not!");
                        try {
                            GetAH.getauctions();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Did!");
                    }
                }
            }
        });
        dothings.start();
    }
}
