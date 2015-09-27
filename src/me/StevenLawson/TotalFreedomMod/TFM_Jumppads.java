/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class TFM_Jumppads
/*     */ {
/*  21 */   public static final Material BLOCK_ID = Material.WOOL;
/*  22 */   public static final double DAMPING_COEFFICIENT = 0.8D;
/*  23 */   public static final Map<Player, Boolean> PUSH_MAP = new HashMap();
/*  24 */   private static JumpPadMode mode = JumpPadMode.MADGEEK;
/*  25 */   private static double strength = 0.4D;
/*     */   
/*     */   public static void PlayerMoveEvent(PlayerMoveEvent event)
/*     */   {
/*  30 */     if (mode == JumpPadMode.OFF) {
/*  32 */       return;
/*     */     }
/*  35 */     Player player = event.getPlayer();
/*  36 */     Block block = event.getTo().getBlock();
/*  37 */     Vector velocity = player.getVelocity().clone();
/*  39 */     if (mode == JumpPadMode.MADGEEK)
/*     */     {
/*  41 */       Boolean canPush = (Boolean)PUSH_MAP.get(player);
/*  42 */       if (canPush == null) {
/*  44 */         canPush = Boolean.valueOf(true);
/*     */       }
/*  46 */       if (block.getRelative(0, -1, 0).getType() == BLOCK_ID)
/*     */       {
/*  48 */         if (canPush.booleanValue()) {
/*  50 */           velocity.multiply(strength + 0.85D).multiply(-1.0D);
/*     */         }
/*  52 */         canPush = Boolean.valueOf(false);
/*     */       }
/*     */       else
/*     */       {
/*  56 */         canPush = Boolean.valueOf(true);
/*     */       }
/*  58 */       PUSH_MAP.put(player, canPush);
/*     */     }
/*     */     else
/*     */     {
/*  62 */       if (block.getRelative(0, -1, 0).getType() == BLOCK_ID) {
/*  64 */         velocity.add(new Vector(0.0D, strength, 0.0D));
/*     */       }
/*  67 */       if (mode == JumpPadMode.NORMAL_AND_SIDEWAYS)
/*     */       {
/*  69 */         if (block.getRelative(1, 0, 0).getType() == BLOCK_ID) {
/*  71 */           velocity.add(new Vector(-DAMPING_COEFFICIENT * strength, 0.0D, 0.0D));
/*     */         }
/*  74 */         if (block.getRelative(-1, 0, 0).getType() == BLOCK_ID) {
/*  76 */           velocity.add(new Vector(DAMPING_COEFFICIENT * strength, 0.0D, 0.0D));
/*     */         }
/*  79 */         if (block.getRelative(0, 0, 1).getType() == BLOCK_ID) {
/*  81 */           velocity.add(new Vector(0.0D, 0.0D, -DAMPING_COEFFICIENT * strength));
/*     */         }
/*  84 */         if (block.getRelative(0, 0, -1).getType() == BLOCK_ID) {
/*  86 */           velocity.add(new Vector(0.0D, 0.0D, DAMPING_COEFFICIENT * strength));
/*     */         }
/*     */       }
/*     */     }
/*  91 */     if (!player.getVelocity().equals(velocity))
/*     */     {
/*  93 */       player.setFallDistance(0.0F);
/*  94 */       player.setVelocity(velocity);
/*     */     }
/*     */   }
/*     */   
/*     */   public static JumpPadMode getMode()
/*     */   {
/* 100 */     return mode;
/*     */   }
/*     */   
/*     */   public static void setMode(JumpPadMode mode)
/*     */   {
/* 105 */     mode = mode;
/*     */   }
/*     */   
/*     */   public static double getStrength()
/*     */   {
/* 110 */     return strength;
/*     */   }
/*     */   
/*     */   public static void setStrength(double strength)
/*     */   {
/* 115 */     strength = strength;
/*     */   }
/*     */   
/*     */   public static enum JumpPadMode
/*     */   {
/* 120 */     OFF(false),  NORMAL(true),  NORMAL_AND_SIDEWAYS(true),  MADGEEK(true);
/*     */     
/*     */     private boolean on;
/*     */     
/*     */     private JumpPadMode(boolean on)
/*     */     {
/* 125 */       this.on = on;
/*     */     }
/*     */     
/*     */     public boolean isOn()
/*     */     {
/* 130 */       return on;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Jumppads.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */