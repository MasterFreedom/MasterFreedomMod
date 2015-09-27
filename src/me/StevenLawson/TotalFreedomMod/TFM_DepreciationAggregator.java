/*    */ package me.StevenLawson.TotalFreedomMod;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.material.Lever;
/*    */ import org.bukkit.material.MaterialData;
/*    */ 
/*    */ public class TFM_DepreciationAggregator
/*    */ {
/*    */   public static Block getTargetBlock(LivingEntity entity, HashSet<Byte> transparent, int maxDistance)
/*    */   {
/* 17 */     return entity.getTargetBlock(transparent, maxDistance);
/*    */   }
/*    */   
/*    */   public static OfflinePlayer getOfflinePlayer(Server server, String name)
/*    */   {
/* 22 */     return server.getOfflinePlayer(name);
/*    */   }
/*    */   
/*    */   public static Material getMaterial(int id)
/*    */   {
/* 27 */     return Material.getMaterial(id);
/*    */   }
/*    */   
/*    */   public static byte getData_MaterialData(MaterialData md)
/*    */   {
/* 32 */     return md.getData();
/*    */   }
/*    */   
/*    */   public static void setData_MaterialData(MaterialData md, byte data)
/*    */   {
/* 37 */     md.setData(data);
/*    */   }
/*    */   
/*    */   public static byte getData_Block(Block block)
/*    */   {
/* 42 */     return block.getData();
/*    */   }
/*    */   
/*    */   public static void setData_Block(Block block, byte data)
/*    */   {
/* 47 */     block.setData(data);
/*    */   }
/*    */   
/*    */   public static Lever makeLeverWithData(byte data)
/*    */   {
/* 52 */     return new Lever(Material.LEVER, data);
/*    */   }
/*    */   
/*    */   public static int getTypeId_Block(Block block)
/*    */   {
/* 57 */     return block.getTypeId();
/*    */   }
/*    */   
/*    */   public static String getName_EntityType(EntityType et)
/*    */   {
/* 62 */     return et.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_DepreciationAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */