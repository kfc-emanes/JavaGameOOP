package com.ror.gamemodel;

import java.util.*;

public class WorldManager {
    private final LinkedHashMap<String, Queue<Entity>> worlds = new LinkedHashMap<>();
    private Iterator<String> worldIterator;
    private String currentWorld;
    private boolean finalBossUnlocked = false;

    public WorldManager() {
        loadWorlds();
        worldIterator = worlds.keySet().iterator();

        // Ensure there's at least one world before calling next()
        if (worldIterator.hasNext()) {
            currentWorld = worldIterator.next(); // Start with first world
        } else {
            currentWorld = null;
        }
    }

    private void loadWorlds() {
        // 🌬️ Aetheria
        Queue<Entity> aetheria = new LinkedList<>();
        aetheria.add(new SkySerpent());
        aetheria.add(new SkySerpent());
        aetheria.add(new GeneralZephra());
        worlds.put("Aetheria", aetheria);

        // 🔥 Ignara
        Queue<Entity> ignara = new LinkedList<>();
        ignara.add(new MoltenImp());
        ignara.add(new MoltenImp());
        ignara.add(new GeneralVulkrag());
        worlds.put("Ignara", ignara);

        // 🌑 Noxterra
        Queue<Entity> noxterra = new LinkedList<>();
        noxterra.add(new ShadowCreeper());
        noxterra.add(new ShadowCreeper());
        noxterra.add(new ShadowWarlord()); // corrected name
        worlds.put("Noxtperra", noxterra);
    }

    public Entity getNextEnemy() {
        if (currentWorld == null) return null;

        Queue<Entity> enemies = worlds.get(currentWorld);

        // If the current world still has enemies
        if (enemies != null && !enemies.isEmpty()) {
            return enemies.poll();
        }

        // If current world cleared, move to the next
        if (worldIterator.hasNext()) {
            currentWorld = worldIterator.next();
            System.out.println("\n🌍 Entering " + currentWorld + "!");
            return getNextEnemy();
        }

        // All worlds cleared → spawn final boss
        if (!finalBossUnlocked) {
            finalBossUnlocked = true;
            currentWorld = "Final Battle";
            System.out.println("\n⚡ The Final Battle Begins! ⚡");
            return new Vorthnar(); // corrected class name
        }

        // Game finished
        return null;
    }

    public String getCurrentWorld() {
        return currentWorld;
    }

    public boolean isFinalBossUnlocked() {
        return finalBossUnlocked;
    }
}
