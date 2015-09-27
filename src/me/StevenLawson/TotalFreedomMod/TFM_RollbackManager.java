/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class TFM_RollbackManager
/*     */ {
/*  18 */   private static final Map<String, List<RollbackEntry>> PLAYER_HISTORY = new HashMap();
/*  19 */   private static final List<String> REMOVE_ROLLBACK_HISTORY = new ArrayList();
/*     */   
/*     */   private TFM_RollbackManager()
/*     */   {
/*  23 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void blockPlace(BlockPlaceEvent event)
/*     */   {
/*  28 */     storeEntry(event.getPlayer(), new RollbackEntry(event.getPlayer().getName(), event.getBlock(), EntryType.BLOCK_PLACE, null));
/*     */   }
/*     */   
/*     */   public static void blockBreak(BlockBreakEvent event)
/*     */   {
/*  33 */     storeEntry(event.getPlayer(), new RollbackEntry(event.getPlayer().getName(), event.getBlock(), EntryType.BLOCK_BREAK, null));
/*     */   }
/*     */   
/*     */   private static void storeEntry(Player player, RollbackEntry entry)
/*     */   {
/*  38 */     List<RollbackEntry> playerEntryList = getEntriesByPlayer(player.getName());
/*  40 */     if (playerEntryList != null) {
/*  42 */       playerEntryList.add(0, entry);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String findPlayer(String partial)
/*     */   {
/*  49 */     partial = partial.toLowerCase();
/*  51 */     for (String player : PLAYER_HISTORY.keySet()) {
/*  53 */       if (player.toLowerCase().equals(partial)) {
/*  55 */         return player;
/*     */       }
/*     */     }
/*  59 */     for (String player : PLAYER_HISTORY.keySet()) {
/*  61 */       if (player.toLowerCase().contains(partial)) {
/*  63 */         return player;
/*     */       }
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public static int purgeEntries()
/*     */   {
/*  72 */     Iterator<List<RollbackEntry>> it = PLAYER_HISTORY.values().iterator();
/*  73 */     while (it.hasNext())
/*     */     {
/*  75 */       List<RollbackEntry> playerEntryList = (List)it.next();
/*  76 */       if (playerEntryList != null) {
/*  78 */         playerEntryList.clear();
/*     */       }
/*     */     }
/*  81 */     return PLAYER_HISTORY.size();
/*     */   }
/*     */   
/*     */   public static int purgeEntries(String playerName)
/*     */   {
/*  86 */     List<RollbackEntry> playerEntryList = getEntriesByPlayer(playerName);
/*  88 */     if (playerEntryList == null) {
/*  90 */       return 0;
/*     */     }
/*  93 */     int count = playerEntryList.size();
/*  94 */     playerEntryList.clear();
/*  95 */     return count;
/*     */   }
/*     */   
/*     */   public static boolean canRollback(String playerName)
/*     */   {
/* 101 */     return (PLAYER_HISTORY.containsKey(playerName.toLowerCase())) && (!((List)PLAYER_HISTORY.get(playerName.toLowerCase())).isEmpty());
/*     */   }
/*     */   
/*     */   public static boolean canUndoRollback(String playerName)
/*     */   {
/* 106 */     return REMOVE_ROLLBACK_HISTORY.contains(playerName.toLowerCase());
/*     */   }
/*     */   
/*     */   public static int rollback(String playerName)
/*     */   {
/* 111 */     List<RollbackEntry> entries = getEntriesByPlayer(playerName);
/* 112 */     if (entries == null) {
/* 114 */       return 0;
/*     */     }
/* 117 */     int count = entries.size();
/* 118 */     for (RollbackEntry entry : entries) {
/* 120 */       if (entry != null) {
/* 122 */         entry.restore();
/*     */       }
/*     */     }
/* 126 */     if (!REMOVE_ROLLBACK_HISTORY.contains(playerName.toLowerCase())) {
/* 128 */       REMOVE_ROLLBACK_HISTORY.add(playerName.toLowerCase());
/*     */     }
/* 131 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 136 */         if (TFM_RollbackManager.REMOVE_ROLLBACK_HISTORY.contains(val$playerName.toLowerCase()))
/*     */         {
/* 138 */           TFM_RollbackManager.REMOVE_ROLLBACK_HISTORY.remove(val$playerName.toLowerCase());
/* 139 */           TFM_RollbackManager.purgeEntries(val$playerName);
/*     */         }
/*     */       }
/* 139 */     }.runTaskLater(TotalFreedomMod.plugin, 800L);
/*     */     
/* 143 */     return count;
/*     */   }
/*     */   
/*     */   public static int undoRollback(String playerName)
/*     */   {
/* 148 */     List<RollbackEntry> entries = getEntriesByPlayer(playerName);
/* 150 */     if (entries == null) {
/* 152 */       return 0;
/*     */     }
/* 155 */     int count = entries.size();
/*     */     
/* 157 */     ListIterator<RollbackEntry> it = entries.listIterator(count);
/* 158 */     while (it.hasPrevious())
/*     */     {
/* 160 */       RollbackEntry entry = (RollbackEntry)it.previous();
/* 161 */       if (entry != null) {
/* 163 */         entry.redo();
/*     */       }
/*     */     }
/* 167 */     if (REMOVE_ROLLBACK_HISTORY.contains(playerName.toLowerCase())) {
/* 169 */       REMOVE_ROLLBACK_HISTORY.remove(playerName.toLowerCase());
/*     */     }
/* 172 */     return count;
/*     */   }
/*     */   
/*     */   public static List<RollbackEntry> getEntriesAtLocation(Location location)
/*     */   {
/* 177 */     int testX = location.getBlockX();
/* 178 */     short testY = (short)location.getBlockY();
/* 179 */     int testZ = location.getBlockZ();
/* 180 */     String testWorldName = location.getWorld().getName();
/*     */     
/* 182 */     List<RollbackEntry> entries = new ArrayList();
/* 183 */     for (String playername : PLAYER_HISTORY.keySet()) {
/* 185 */       for (RollbackEntry entry : (List)PLAYER_HISTORY.get(playername.toLowerCase())) {
/* 187 */         if ((testX == x) && (testY == y) && (testZ == z) && (testWorldName.equals(worldName))) {
/* 189 */           entries.add(0, entry);
/*     */         }
/*     */       }
/*     */     }
/* 194 */     return entries;
/*     */   }
/*     */   
/*     */   private static List<RollbackEntry> getEntriesByPlayer(String playerName)
/*     */   {
/* 199 */     playerName = playerName.toLowerCase();
/* 200 */     List<RollbackEntry> playerEntryList = (List)PLAYER_HISTORY.get(playerName.toLowerCase());
/* 201 */     if (playerEntryList == null)
/*     */     {
/* 203 */       playerEntryList = new ArrayList();
/* 204 */       PLAYER_HISTORY.put(playerName.toLowerCase(), playerEntryList);
/*     */     }
/* 206 */     return playerEntryList;
/*     */   }
/*     */   
/*     */   public static enum EntryType
/*     */   {
/* 211 */     BLOCK_PLACE("placed"),  BLOCK_BREAK("broke");
/*     */     
/*     */     private final String action;
/*     */     
/*     */     private EntryType(String action)
/*     */     {
/* 217 */       this.action = action;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 223 */       return action;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RollbackEntry
/*     */   {
/*     */     public final String author;
/*     */     public final String worldName;
/*     */     public final int x;
/*     */     public final short y;
/*     */     public final int z;
/*     */     public final byte data;
/*     */     public final Material blockMaterial;
/*     */     private final boolean isBreak;
/*     */     
/*     */     private RollbackEntry(String author, Block block, TFM_RollbackManager.EntryType entryType)
/*     */     {
/* 241 */       Location location = block.getLocation();
/*     */       
/* 243 */       x = location.getBlockX();
/* 244 */       y = ((short)location.getBlockY());
/* 245 */       z = location.getBlockZ();
/* 246 */       worldName = location.getWorld().getName();
/* 247 */       this.author = author;
/* 249 */       if (entryType == TFM_RollbackManager.EntryType.BLOCK_BREAK)
/*     */       {
/* 251 */         blockMaterial = block.getType();
/* 252 */         data = TFM_DepreciationAggregator.getData_Block(block);
/* 253 */         isBreak = true;
/*     */       }
/*     */       else
/*     */       {
/* 257 */         blockMaterial = block.getType();
/* 258 */         data = TFM_DepreciationAggregator.getData_Block(block);
/* 259 */         isBreak = false;
/*     */       }
/*     */     }
/*     */     
/*     */     public Location getLocation()
/*     */     {
/*     */       try
/*     */       {
/* 267 */         return new Location(Bukkit.getWorld(worldName), x, y, z);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 271 */         TFM_Log.warning("Could not get location of rollback entry at (" + worldName + ":" + x + "," + y + "," + x + ")!");
/*     */       }
/* 273 */       return null;
/*     */     }
/*     */     
/*     */     public Material getMaterial()
/*     */     {
/* 278 */       return blockMaterial;
/*     */     }
/*     */     
/*     */     public TFM_RollbackManager.EntryType getType()
/*     */     {
/* 283 */       return isBreak ? TFM_RollbackManager.EntryType.BLOCK_BREAK : TFM_RollbackManager.EntryType.BLOCK_PLACE;
/*     */     }
/*     */     
/*     */     public void restore()
/*     */     {
/* 288 */       Block block = Bukkit.getWorld(worldName).getBlockAt(x, y, z);
/* 289 */       if (isBreak)
/*     */       {
/* 291 */         block.setType(getMaterial());
/* 292 */         TFM_DepreciationAggregator.setData_Block(block, data);
/*     */       }
/*     */       else
/*     */       {
/* 296 */         block.setType(Material.AIR);
/*     */       }
/*     */     }
/*     */     
/*     */     public void redo()
/*     */     {
/* 302 */       Block block = Bukkit.getWorld(worldName).getBlockAt(x, y, z);
/* 304 */       if (isBreak)
/*     */       {
/* 306 */         block.setType(Material.AIR);
/*     */       }
/*     */       else
/*     */       {
/* 310 */         block.setType(getMaterial());
/* 311 */         TFM_DepreciationAggregator.setData_Block(block, data);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_RollbackManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */