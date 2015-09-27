/*    */ package me.StevenLawson.TotalFreedomMod.World;
/*    */ 
/*    */ import java.io.File;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.World.Environment;
/*    */ import org.bukkit.WorldCreator;
/*    */ import org.bukkit.WorldType;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockFace;
/*    */ 
/*    */ public class TFM_Flatlands
/*    */   extends TFM_CustomWorld
/*    */ {
/* 20 */   private static final String GENERATION_PARAMETERS = TFM_ConfigEntry.FLATLANDS_GENERATE_PARAMS.getString();
/*    */   private static final String WORLD_NAME = "flatlands";
/*    */   
/*    */   protected World generateWorld()
/*    */   {
/* 30 */     if (!TFM_ConfigEntry.FLATLANDS_GENERATE.getBoolean().booleanValue()) {
/* 32 */       return null;
/*    */     }
/* 35 */     wipeFlatlandsIfFlagged();
/*    */     
/* 37 */     WorldCreator worldCreator = new WorldCreator("flatlands");
/* 38 */     worldCreator.generateStructures(false);
/* 39 */     worldCreator.type(WorldType.NORMAL);
/* 40 */     worldCreator.environment(World.Environment.NORMAL);
/* 41 */     worldCreator.generator(new CleanroomChunkGenerator(GENERATION_PARAMETERS));
/*    */     
/* 43 */     World world = Bukkit.getServer().createWorld(worldCreator);
/*    */     
/* 45 */     world.setSpawnFlags(false, false);
/* 46 */     world.setSpawnLocation(0, 50, 0);
/*    */     
/* 48 */     Block welcomeSignBlock = world.getBlockAt(0, 50, 0);
/* 49 */     welcomeSignBlock.setType(Material.SIGN_POST);
/* 50 */     org.bukkit.block.Sign welcomeSign = (org.bukkit.block.Sign)welcomeSignBlock.getState();
/*    */     
/* 52 */     org.bukkit.material.Sign signData = (org.bukkit.material.Sign)welcomeSign.getData();
/* 53 */     signData.setFacingDirection(BlockFace.NORTH);
/*    */     
/* 55 */     welcomeSign.setLine(0, ChatColor.GREEN + "Flatlands");
/* 56 */     welcomeSign.setLine(1, ChatColor.DARK_GRAY + "---");
/* 57 */     welcomeSign.setLine(2, ChatColor.YELLOW + "Spawn Point");
/* 58 */     welcomeSign.setLine(3, ChatColor.DARK_GRAY + "---");
/* 59 */     welcomeSign.update();
/*    */     
/* 61 */     TFM_GameRuleHandler.commitGameRules();
/*    */     
/* 63 */     return world;
/*    */   }
/*    */   
/*    */   public static void wipeFlatlandsIfFlagged()
/*    */   {
/* 68 */     boolean doFlatlandsWipe = false;
/*    */     try
/*    */     {
/* 71 */       doFlatlandsWipe = TFM_Util.getSavedFlag("do_wipe_flatlands");
/*    */     }
/*    */     catch (Exception ex) {}
/* 77 */     if (doFlatlandsWipe) {
/* 79 */       if (Bukkit.getServer().getWorld("flatlands") == null)
/*    */       {
/* 81 */         TFM_Log.info("Wiping flatlands.");
/* 82 */         TFM_Util.setSavedFlag("do_wipe_flatlands", false);
/* 83 */         FileUtils.deleteQuietly(new File("./flatlands"));
/*    */       }
/*    */       else
/*    */       {
/* 87 */         TFM_Log.severe("Can't wipe flatlands, it is already loaded.");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static TFM_Flatlands getInstance()
/*    */   {
/* 94 */     return TFM_FlatlandsHolder.INSTANCE;
/*    */   }
/*    */   
/*    */   private static class TFM_FlatlandsHolder
/*    */   {
/* 99 */     private static final TFM_Flatlands INSTANCE = new TFM_Flatlands(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\World\TFM_Flatlands.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */