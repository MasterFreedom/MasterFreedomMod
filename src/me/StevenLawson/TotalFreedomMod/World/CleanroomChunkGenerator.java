/*     */ package me.StevenLawson.TotalFreedomMod.World;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.generator.BlockPopulator;
/*     */ import org.bukkit.generator.ChunkGenerator;
/*     */ import org.bukkit.generator.ChunkGenerator.BiomeGrid;
/*     */ 
/*     */ public class CleanroomChunkGenerator
/*     */   extends ChunkGenerator
/*     */ {
/*  36 */   private static final Logger log = ;
/*     */   private short[] layer;
/*     */   private byte[] layerDataValues;
/*     */   
/*     */   public CleanroomChunkGenerator()
/*     */   {
/*  42 */     this("64,stone");
/*     */   }
/*     */   
/*     */   public CleanroomChunkGenerator(String id)
/*     */   {
/*  47 */     if (id != null)
/*     */     {
/*     */       try
/*     */       {
/*  51 */         int y = 0;
/*     */         
/*  53 */         layer = new short[''];
/*  54 */         layerDataValues = null;
/*  56 */         if ((id.length() > 0) && (id.charAt(0) == '.')) {
/*  58 */           id = id.substring(1);
/*     */         } else {
/*  62 */           layer[(y++)] = ((short)Material.BEDROCK.getId());
/*     */         }
/*  65 */         if (id.length() > 0)
/*     */         {
/*  67 */           String[] tokens = id.split("[,]");
/*  69 */           if (tokens.length % 2 != 0) {
/*  71 */             throw new Exception();
/*     */           }
/*  74 */           for (int i = 0; i < tokens.length; i += 2)
/*     */           {
/*  76 */             int height = Integer.parseInt(tokens[i]);
/*  77 */             if (height <= 0)
/*     */             {
/*  79 */               log.warning("[CleanroomGenerator] Invalid height '" + tokens[i] + "'. Using 64 instead.");
/*  80 */               height = 64;
/*     */             }
/*  83 */             String[] materialTokens = tokens[(i + 1)].split("[:]", 2);
/*  84 */             byte dataValue = 0;
/*  85 */             if (materialTokens.length == 2) {
/*     */               try
/*     */               {
/*  90 */                 dataValue = Byte.parseByte(materialTokens[1]);
/*     */               }
/*     */               catch (Exception e)
/*     */               {
/*  94 */                 log.warning("[CleanroomGenerator] Invalid Data Value '" + materialTokens[1] + "'. Defaulting to 0.");
/*  95 */                 dataValue = 0;
/*     */               }
/*     */             }
/*  98 */             Material mat = Material.matchMaterial(materialTokens[0]);
/*  99 */             if (mat == null)
/*     */             {
/*     */               try
/*     */               {
/* 104 */                 mat = Material.getMaterial(Integer.parseInt(materialTokens[0]));
/*     */               }
/*     */               catch (Exception e) {}
/* 111 */               if (mat == null)
/*     */               {
/* 113 */                 log.warning("[CleanroomGenerator] Invalid Block ID '" + materialTokens[0] + "'. Defaulting to stone.");
/* 114 */                 mat = Material.STONE;
/*     */               }
/*     */             }
/* 118 */             if (!mat.isBlock())
/*     */             {
/* 120 */               log.warning("[CleanroomGenerator] Error, '" + materialTokens[0] + "' is not a block. Defaulting to stone.");
/* 121 */               mat = Material.STONE;
/*     */             }
/* 124 */             if (y + height > layer.length)
/*     */             {
/* 126 */               short[] newLayer = new short[Math.max(y + height, layer.length * 2)];
/* 127 */               System.arraycopy(layer, 0, newLayer, 0, y);
/* 128 */               layer = newLayer;
/* 129 */               if (layerDataValues != null)
/*     */               {
/* 131 */                 byte[] newLayerDataValues = new byte[Math.max(y + height, layerDataValues.length * 2)];
/* 132 */                 System.arraycopy(layerDataValues, 0, newLayerDataValues, 0, y);
/* 133 */                 layerDataValues = newLayerDataValues;
/*     */               }
/*     */             }
/* 137 */             Arrays.fill(layer, y, y + height, (short)mat.getId());
/* 138 */             if (dataValue != 0)
/*     */             {
/* 140 */               if (layerDataValues == null) {
/* 142 */                 layerDataValues = new byte[layer.length];
/*     */               }
/* 144 */               Arrays.fill(layerDataValues, y, y + height, dataValue);
/*     */             }
/* 146 */             y += height;
/*     */           }
/*     */         }
/* 151 */         if (layer.length > y)
/*     */         {
/* 153 */           short[] newLayer = new short[y];
/* 154 */           System.arraycopy(layer, 0, newLayer, 0, y);
/* 155 */           layer = newLayer;
/*     */         }
/* 157 */         if ((layerDataValues != null) && (layerDataValues.length > y))
/*     */         {
/* 159 */           byte[] newLayerDataValues = new byte[y];
/* 160 */           System.arraycopy(layerDataValues, 0, newLayerDataValues, 0, y);
/* 161 */           layerDataValues = newLayerDataValues;
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 166 */         log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id + "'. using defaults '64,1': " + e.toString());
/* 167 */         e.printStackTrace();
/* 168 */         layerDataValues = null;
/* 169 */         layer = new short[65];
/* 170 */         layer[0] = ((short)Material.BEDROCK.getId());
/* 171 */         Arrays.fill(layer, 1, 65, (short)Material.STONE.getId());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 176 */       layerDataValues = null;
/* 177 */       layer = new short[65];
/* 178 */       layer[0] = ((short)Material.BEDROCK.getId());
/* 179 */       Arrays.fill(layer, 1, 65, (short)Material.STONE.getId());
/*     */     }
/*     */   }
/*     */   
/*     */   public short[][] generateExtBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes)
/*     */   {
/* 186 */     int maxHeight = world.getMaxHeight();
/* 187 */     if (layer.length > maxHeight)
/*     */     {
/* 189 */       log.warning("[CleanroomGenerator] Error, chunk height " + layer.length + " is greater than the world max height (" + maxHeight + "). Trimming to world max height.");
/* 190 */       short[] newLayer = new short[maxHeight];
/* 191 */       System.arraycopy(layer, 0, newLayer, 0, maxHeight);
/* 192 */       layer = newLayer;
/*     */     }
/* 194 */     short[][] result = new short[maxHeight / 16][];
/* 195 */     for (int i = 0; i < layer.length; i += 16)
/*     */     {
/* 197 */       result[(i >> 4)] = new short['က'];
/* 198 */       for (int y = 0; y < Math.min(16, layer.length - i); y++) {
/* 200 */         Arrays.fill(result[(i >> 4)], y * 16 * 16, (y + 1) * 16 * 16, layer[(i + y)]);
/*     */       }
/*     */     }
/* 204 */     return result;
/*     */   }
/*     */   
/*     */   public List<BlockPopulator> getDefaultPopulators(World world)
/*     */   {
/* 210 */     if (layerDataValues != null) {
/* 212 */       return Arrays.asList(new BlockPopulator[] { new CleanroomBlockPopulator(layerDataValues) });
/*     */     }
/* 217 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public Location getFixedSpawnLocation(World world, Random random)
/*     */   {
/* 224 */     if (!world.isChunkLoaded(0, 0)) {
/* 226 */       world.loadChunk(0, 0);
/*     */     }
/* 229 */     if ((world.getHighestBlockYAt(0, 0) <= 0) && (world.getBlockAt(0, 0, 0).getType() == Material.AIR)) {
/* 231 */       return new Location(world, 0.0D, 64.0D, 0.0D);
/*     */     }
/* 234 */     return new Location(world, 0.0D, world.getHighestBlockYAt(0, 0), 0.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\World\CleanroomChunkGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */