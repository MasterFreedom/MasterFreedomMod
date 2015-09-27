/*    */ package me.StevenLawson.TotalFreedomMod.World;
/*    */ 
/*    */ import java.util.Random;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.generator.BlockPopulator;
/*    */ 
/*    */ public class CleanroomBlockPopulator
/*    */   extends BlockPopulator
/*    */ {
/*    */   byte[] layerDataValues;
/*    */   
/*    */   protected CleanroomBlockPopulator(byte[] layerDataValues)
/*    */   {
/* 33 */     this.layerDataValues = layerDataValues;
/*    */   }
/*    */   
/*    */   public void populate(World world, Random random, Chunk chunk)
/*    */   {
/* 39 */     if (layerDataValues != null)
/*    */     {
/* 41 */       int x = chunk.getX() << 4;
/* 42 */       int z = chunk.getZ() << 4;
/* 44 */       for (int y = 0; y < layerDataValues.length; y++)
/*    */       {
/* 46 */         byte dataValue = layerDataValues[y];
/* 47 */         if (dataValue != 0) {
/* 51 */           for (int xx = 0; xx < 16; xx++) {
/* 53 */             for (int zz = 0; zz < 16; zz++) {
/* 55 */               world.getBlockAt(x + xx, y, z + zz).setData(dataValue);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\World\CleanroomBlockPopulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */