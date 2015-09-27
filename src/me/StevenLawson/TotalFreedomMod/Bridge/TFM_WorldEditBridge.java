/*     */ package me.StevenLawson.TotalFreedomMod.Bridge;
/*     */ 
/*     */ import com.sk89q.worldedit.LocalSession;
/*     */ import com.sk89q.worldedit.bukkit.BukkitPlayer;
/*     */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ 
/*     */ public class TFM_WorldEditBridge
/*     */ {
/*  13 */   private static WorldEditPlugin worldEditPlugin = null;
/*     */   
/*     */   private TFM_WorldEditBridge()
/*     */   {
/*  17 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   private static WorldEditPlugin getWorldEditPlugin()
/*     */   {
/*  22 */     if (worldEditPlugin == null) {
/*     */       try
/*     */       {
/*  26 */         Plugin we = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
/*  27 */         if (we != null) {
/*  29 */           if ((we instanceof WorldEditPlugin)) {
/*  31 */             worldEditPlugin = (WorldEditPlugin)we;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/*  37 */         TFM_Log.severe(ex);
/*     */       }
/*     */     }
/*  40 */     return worldEditPlugin;
/*     */   }
/*     */   
/*     */   private static LocalSession getPlayerSession(Player player)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       WorldEditPlugin wep = getWorldEditPlugin();
/*  48 */       if (wep != null) {
/*  50 */         return wep.getSession(player);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  55 */       TFM_Log.severe(ex);
/*     */     }
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   private static BukkitPlayer getBukkitPlayer(Player player)
/*     */   {
/*     */     try
/*     */     {
/*  64 */       WorldEditPlugin wep = getWorldEditPlugin();
/*  65 */       if (wep != null) {
/*  67 */         return wep.wrapPlayer(player);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  72 */       TFM_Log.severe(ex);
/*     */     }
/*  74 */     return null;
/*     */   }
/*     */   
/*     */   public static void undo(Player player, int count)
/*     */   {
/*     */     try
/*     */     {
/*  81 */       LocalSession session = getPlayerSession(player);
/*  82 */       if (session != null)
/*     */       {
/*  84 */         BukkitPlayer bukkitPlayer = getBukkitPlayer(player);
/*  85 */         if (bukkitPlayer != null) {
/*  87 */           for (int i = 0; i < count; i++) {
/*  89 */             session.undo(session.getBlockBag(bukkitPlayer), bukkitPlayer);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  96 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setLimit(Player player, int limit)
/*     */   {
/*     */     try
/*     */     {
/* 104 */       LocalSession session = getPlayerSession(player);
/* 105 */       if (session != null) {
/* 107 */         session.setBlockChangeLimit(limit);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 112 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Bridge\TFM_WorldEditBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */