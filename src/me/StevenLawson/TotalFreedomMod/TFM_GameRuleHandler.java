/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.World;
/*     */ 
/*     */ public class TFM_GameRuleHandler
/*     */ {
/*  12 */   private static final EnumMap<TFM_GameRule, TFM_GameRule_Value> GAME_RULES = new EnumMap(TFM_GameRule.class);
/*     */   
/*     */   static
/*     */   {
/*  16 */     for (TFM_GameRule gameRule : TFM_GameRule.values()) {
/*  18 */       GAME_RULES.put(gameRule, gameRule.getDefaultValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private TFM_GameRuleHandler()
/*     */   {
/*  24 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void setGameRule(TFM_GameRule gameRule, boolean value)
/*     */   {
/*  29 */     setGameRule(gameRule, value, true);
/*     */   }
/*     */   
/*     */   public static void setGameRule(TFM_GameRule gameRule, boolean value, boolean doCommit)
/*     */   {
/*  34 */     GAME_RULES.put(gameRule, TFM_GameRule_Value.fromBoolean(value));
/*  35 */     if (doCommit) {
/*  37 */       commitGameRules();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void commitGameRules()
/*     */   {
/*  43 */     List<World> worlds = Bukkit.getWorlds();
/*  44 */     Iterator<Map.Entry<TFM_GameRule, TFM_GameRule_Value>> it = GAME_RULES.entrySet().iterator();
/*     */     Map.Entry<TFM_GameRule, TFM_GameRule_Value> gameRuleEntry;
/*     */     String gameRuleName;
/*     */     String gameRuleValue;
/*  45 */     while (it.hasNext())
/*     */     {
/*  47 */       gameRuleEntry = (Map.Entry)it.next();
/*  48 */       gameRuleName = ((TFM_GameRule)gameRuleEntry.getKey()).getGameRuleName();
/*  49 */       gameRuleValue = ((TFM_GameRule_Value)gameRuleEntry.getValue()).toString();
/*  50 */       for (World world : worlds)
/*     */       {
/*  52 */         world.setGameRuleValue(gameRuleName, gameRuleValue);
/*  53 */         if ((gameRuleEntry.getKey() == TFM_GameRule.DO_DAYLIGHT_CYCLE) && (!((TFM_GameRule_Value)gameRuleEntry.getValue()).toBoolean())) {
/*  55 */           TFM_Util.setWorldTime(world, 6000L);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum TFM_GameRule
/*     */   {
/*  63 */     DO_FIRE_TICK("doFireTick", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  MOB_GRIEFING("mobGriefing", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  KEEP_INVENTORY("keepInventory", TFM_GameRuleHandler.TFM_GameRule_Value.FALSE),  DO_MOB_SPAWNING("doMobSpawning", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  DO_MOB_LOOT("doMobLoot", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  DO_TILE_DROPS("doTileDrops", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  COMMAND_BLOCK_OUTPUT("commandBlockOutput", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  NATURAL_REGENERATION("naturalRegeneration", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE),  DO_DAYLIGHT_CYCLE("doDaylightCycle", TFM_GameRuleHandler.TFM_GameRule_Value.TRUE);
/*     */     
/*     */     private final String gameRuleName;
/*     */     private final TFM_GameRuleHandler.TFM_GameRule_Value defaultValue;
/*     */     
/*     */     private TFM_GameRule(String gameRuleName, TFM_GameRuleHandler.TFM_GameRule_Value defaultValue)
/*     */     {
/*  77 */       this.gameRuleName = gameRuleName;
/*  78 */       this.defaultValue = defaultValue;
/*     */     }
/*     */     
/*     */     public String getGameRuleName()
/*     */     {
/*  83 */       return gameRuleName;
/*     */     }
/*     */     
/*     */     public TFM_GameRuleHandler.TFM_GameRule_Value getDefaultValue()
/*     */     {
/*  88 */       return defaultValue;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum TFM_GameRule_Value
/*     */   {
/*  94 */     TRUE("true"),  FALSE("false");
/*     */     
/*     */     private final String value;
/*     */     
/*     */     private TFM_GameRule_Value(String value)
/*     */     {
/*  99 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 105 */       return value;
/*     */     }
/*     */     
/*     */     public boolean toBoolean()
/*     */     {
/* 110 */       return value.equals(TRUEvalue);
/*     */     }
/*     */     
/*     */     public static TFM_GameRule_Value fromBoolean(boolean in)
/*     */     {
/* 115 */       return in ? TRUE : FALSE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_GameRuleHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */