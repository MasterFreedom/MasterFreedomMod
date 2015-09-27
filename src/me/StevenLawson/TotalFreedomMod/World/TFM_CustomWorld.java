/*    */ package me.StevenLawson.TotalFreedomMod.World;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public abstract class TFM_CustomWorld
/*    */ {
/*    */   private World world;
/*    */   
/*    */   protected abstract World generateWorld();
/*    */   
/*    */   public void sendToWorld(Player player)
/*    */   {
/*    */     try
/*    */     {
/* 17 */       player.teleport(getWorld().getSpawnLocation());
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 21 */       player.sendMessage(ex.getMessage());
/*    */     }
/*    */   }
/*    */   
/*    */   public final World getWorld()
/*    */     throws Exception
/*    */   {
/* 27 */     if ((world == null) || (!Bukkit.getWorlds().contains(world))) {
/* 29 */       world = generateWorld();
/*    */     }
/* 32 */     if (world == null) {
/* 34 */       throw new Exception("World not loaded.");
/*    */     }
/* 37 */     return world;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\World\TFM_CustomWorld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */