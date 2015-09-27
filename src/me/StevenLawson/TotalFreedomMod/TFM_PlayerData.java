/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Arrow;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class TFM_PlayerData
/*     */ {
/*  24 */   public static final Map<String, TFM_PlayerData> PLAYER_DATA = new HashMap();
/*     */   public static final long AUTO_PURGE = 6000L;
/*     */   private final Player player;
/*     */   private final String ip;
/*     */   private final UUID uuid;
/*     */   private BukkitTask unmuteTask;
/*     */   private BukkitTask unfreezeTask;
/*     */   private Location freezeLocation;
/*     */   
/*     */   public static boolean hasPlayerData(Player player)
/*     */   {
/*  29 */     return PLAYER_DATA.containsKey(TFM_Util.getIp(player));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static TFM_PlayerData getPlayerDataSync(Player player)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 2	me/StevenLawson/TotalFreedomMod/TFM_PlayerData:PLAYER_DATA	Ljava/util/Map;
/*     */     //   3: dup
/*     */     //   4: astore_1
/*     */     //   5: monitorenter
/*     */     //   6: aload_0
/*     */     //   7: invokestatic 5	me/StevenLawson/TotalFreedomMod/TFM_PlayerData:getPlayerData	(Lorg/bukkit/entity/Player;)Lme/StevenLawson/TotalFreedomMod/TFM_PlayerData;
/*     */     //   10: aload_1
/*     */     //   11: monitorexit
/*     */     //   12: areturn
/*     */     //   13: astore_2
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: aload_2
/*     */     //   17: athrow
/*     */     // Line number table:
/*     */     //   Java source line #34	-> byte code offset #0
/*     */     //   Java source line #36	-> byte code offset #6
/*     */     //   Java source line #37	-> byte code offset #13
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	18	0	player	Player
/*     */     //   4	11	1	Ljava/lang/Object;	Object
/*     */     //   13	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	12	13	finally
/*     */     //   13	16	13	finally
/*     */   }
/*     */   
/*     */   public static TFM_PlayerData getPlayerData(Player player)
/*     */   {
/*  42 */     String ip = TFM_Util.getIp(player);
/*     */     
/*  44 */     TFM_PlayerData data = (TFM_PlayerData)PLAYER_DATA.get(ip);
/*  46 */     if (data != null) {
/*  48 */       return data;
/*     */     }
/*  51 */     if (Bukkit.getOnlineMode()) {
/*  53 */       for (TFM_PlayerData dataTest : PLAYER_DATA.values()) {
/*  55 */         if (player.getName().equalsIgnoreCase(player.getName()))
/*     */         {
/*  57 */           data = dataTest;
/*  58 */           break;
/*     */         }
/*     */       }
/*     */     }
/*  63 */     if (data != null) {
/*  65 */       return data;
/*     */     }
/*  68 */     data = new TFM_PlayerData(player, TFM_UuidManager.getUniqueId(player), ip);
/*  69 */     PLAYER_DATA.put(ip, data);
/*     */     
/*  71 */     return data;
/*     */   }
/*     */   
/*  81 */   private boolean isHalted = false;
/*  82 */   private int messageCount = 0;
/*  83 */   private int totalBlockDestroy = 0;
/*  84 */   private int totalBlockPlace = 0;
/*  85 */   private int freecamDestroyCount = 0;
/*  86 */   private int freecamPlaceCount = 0;
/*  87 */   private boolean isCaged = false;
/*     */   private Location cagePosition;
/*  89 */   private List<TFM_BlockData> cageHistory = new ArrayList();
/*  90 */   private Material cageOuterMaterial = Material.GLASS;
/*  91 */   private Material cageInnerMatterial = Material.AIR;
/*  92 */   private boolean isOrbiting = false;
/*  93 */   private double orbitStrength = 10.0D;
/*  94 */   private boolean mobThrowerEnabled = false;
/*  95 */   private EntityType mobThrowerEntity = EntityType.PIG;
/*  96 */   private double mobThrowerSpeed = 4.0D;
/*  97 */   private List<LivingEntity> mobThrowerQueue = new ArrayList();
/*  98 */   private BukkitTask mp44ScheduleTask = null;
/*  99 */   private boolean mp44Armed = false;
/* 100 */   private boolean mp44Firing = false;
/* 101 */   private BukkitTask lockupScheduleTask = null;
/* 102 */   private String lastMessage = "";
/* 103 */   private boolean inAdminchat = false;
/* 104 */   private boolean allCommandsBlocked = false;
/* 105 */   private boolean verifiedSuperadminId = false;
/* 106 */   private String lastCommand = "";
/* 107 */   private boolean cmdspyEnabled = false;
/* 108 */   private String tag = null;
/* 109 */   private int warningCount = 0;
/*     */   
/*     */   private TFM_PlayerData(Player player, UUID uuid, String ip)
/*     */   {
/* 113 */     this.player = player;
/* 114 */     this.uuid = uuid;
/* 115 */     this.ip = ip;
/*     */   }
/*     */   
/*     */   public String getIpAddress()
/*     */   {
/* 120 */     return ip;
/*     */   }
/*     */   
/*     */   public UUID getUniqueId()
/*     */   {
/* 125 */     return uuid;
/*     */   }
/*     */   
/*     */   public boolean isOrbiting()
/*     */   {
/* 130 */     return isOrbiting;
/*     */   }
/*     */   
/*     */   public void startOrbiting(double strength)
/*     */   {
/* 135 */     isOrbiting = true;
/* 136 */     orbitStrength = strength;
/*     */   }
/*     */   
/*     */   public void stopOrbiting()
/*     */   {
/* 141 */     isOrbiting = false;
/*     */   }
/*     */   
/*     */   public double orbitStrength()
/*     */   {
/* 146 */     return orbitStrength;
/*     */   }
/*     */   
/*     */   public void setCaged(boolean state)
/*     */   {
/* 151 */     isCaged = state;
/*     */   }
/*     */   
/*     */   public void setCaged(boolean state, Location location, Material outer, Material inner)
/*     */   {
/* 156 */     isCaged = state;
/* 157 */     cagePosition = location;
/* 158 */     cageOuterMaterial = outer;
/* 159 */     cageInnerMatterial = inner;
/*     */   }
/*     */   
/*     */   public boolean isCaged()
/*     */   {
/* 164 */     return isCaged;
/*     */   }
/*     */   
/*     */   public Material getCageMaterial(CageLayer layer)
/*     */   {
/* 169 */     switch (layer)
/*     */     {
/*     */     case OUTER: 
/* 172 */       return cageOuterMaterial;
/*     */     case INNER: 
/* 174 */       return cageInnerMatterial;
/*     */     }
/* 176 */     return cageOuterMaterial;
/*     */   }
/*     */   
/*     */   public Location getCagePos()
/*     */   {
/* 182 */     return cagePosition;
/*     */   }
/*     */   
/*     */   public void clearHistory()
/*     */   {
/* 187 */     cageHistory.clear();
/*     */   }
/*     */   
/*     */   public void insertHistoryBlock(Location location, Material material)
/*     */   {
/* 192 */     cageHistory.add(new TFM_BlockData(location, material, null));
/*     */   }
/*     */   
/*     */   public void regenerateHistory()
/*     */   {
/* 197 */     for (TFM_BlockData blockdata : cageHistory) {
/* 199 */       location.getBlock().setType(material);
/*     */     }
/*     */   }
/*     */   
/*     */   public Location getFreezeLocation()
/*     */   {
/* 205 */     return freezeLocation;
/*     */   }
/*     */   
/*     */   public boolean isFrozen()
/*     */   {
/* 210 */     return unfreezeTask != null;
/*     */   }
/*     */   
/*     */   public void setFrozen(boolean freeze)
/*     */   {
/* 215 */     cancel(unfreezeTask);
/* 216 */     unfreezeTask = null;
/* 217 */     freezeLocation = null;
/* 219 */     if (player.getGameMode() != GameMode.CREATIVE) {
/* 221 */       TFM_Util.setFlying(player, false);
/*     */     }
/* 224 */     if (!freeze) {
/* 226 */       return;
/*     */     }
/* 229 */     freezeLocation = player.getLocation();
/* 230 */     TFM_Util.setFlying(player, true);
/*     */     
/* 232 */     unfreezeTask = new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 237 */         TFM_Util.adminAction("TotalFreedom", "Unfreezing " + player.getName(), false);
/* 238 */         setFrozen(false);
/*     */       }
/* 238 */     }.runTaskLater(TotalFreedomMod.plugin, 6000L);
/*     */   }
/*     */   
/*     */   public void resetMsgCount()
/*     */   {
/* 246 */     messageCount = 0;
/*     */   }
/*     */   
/*     */   public int incrementAndGetMsgCount()
/*     */   {
/* 251 */     return messageCount++;
/*     */   }
/*     */   
/*     */   public int incrementAndGetBlockDestroyCount()
/*     */   {
/* 256 */     return totalBlockDestroy++;
/*     */   }
/*     */   
/*     */   public void resetBlockDestroyCount()
/*     */   {
/* 261 */     totalBlockDestroy = 0;
/*     */   }
/*     */   
/*     */   public int incrementAndGetBlockPlaceCount()
/*     */   {
/* 266 */     return totalBlockPlace++;
/*     */   }
/*     */   
/*     */   public void resetBlockPlaceCount()
/*     */   {
/* 271 */     totalBlockPlace = 0;
/*     */   }
/*     */   
/*     */   public int incrementAndGetFreecamDestroyCount()
/*     */   {
/* 276 */     return freecamDestroyCount++;
/*     */   }
/*     */   
/*     */   public void resetFreecamDestroyCount()
/*     */   {
/* 281 */     freecamDestroyCount = 0;
/*     */   }
/*     */   
/*     */   public int incrementAndGetFreecamPlaceCount()
/*     */   {
/* 286 */     return freecamPlaceCount++;
/*     */   }
/*     */   
/*     */   public void resetFreecamPlaceCount()
/*     */   {
/* 291 */     freecamPlaceCount = 0;
/*     */   }
/*     */   
/*     */   public void enableMobThrower(EntityType mobThrowerCreature, double mobThrowerSpeed)
/*     */   {
/* 296 */     mobThrowerEnabled = true;
/* 297 */     mobThrowerEntity = mobThrowerCreature;
/* 298 */     this.mobThrowerSpeed = mobThrowerSpeed;
/*     */   }
/*     */   
/*     */   public void disableMobThrower()
/*     */   {
/* 303 */     mobThrowerEnabled = false;
/*     */   }
/*     */   
/*     */   public EntityType mobThrowerCreature()
/*     */   {
/* 308 */     return mobThrowerEntity;
/*     */   }
/*     */   
/*     */   public double mobThrowerSpeed()
/*     */   {
/* 313 */     return mobThrowerSpeed;
/*     */   }
/*     */   
/*     */   public boolean mobThrowerEnabled()
/*     */   {
/* 318 */     return mobThrowerEnabled;
/*     */   }
/*     */   
/*     */   public void enqueueMob(LivingEntity mob)
/*     */   {
/* 323 */     mobThrowerQueue.add(mob);
/* 324 */     if (mobThrowerQueue.size() > 4)
/*     */     {
/* 326 */       LivingEntity oldmob = (LivingEntity)mobThrowerQueue.remove(0);
/* 327 */       if (oldmob != null) {
/* 329 */         oldmob.damage(500.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void startArrowShooter(TotalFreedomMod plugin)
/*     */   {
/* 336 */     stopArrowShooter();
/* 337 */     mp44ScheduleTask = new ArrowShooter(player, null).runTaskTimer(plugin, 1L, 1L);
/* 338 */     mp44Firing = true;
/*     */   }
/*     */   
/*     */   public void stopArrowShooter()
/*     */   {
/* 343 */     if (mp44ScheduleTask != null)
/*     */     {
/* 345 */       mp44ScheduleTask.cancel();
/* 346 */       mp44ScheduleTask = null;
/*     */     }
/* 348 */     mp44Firing = false;
/*     */   }
/*     */   
/*     */   public void armMP44()
/*     */   {
/* 353 */     mp44Armed = true;
/* 354 */     stopArrowShooter();
/*     */   }
/*     */   
/*     */   public void disarmMP44()
/*     */   {
/* 359 */     mp44Armed = false;
/* 360 */     stopArrowShooter();
/*     */   }
/*     */   
/*     */   public boolean isMP44Armed()
/*     */   {
/* 365 */     return mp44Armed;
/*     */   }
/*     */   
/*     */   public boolean toggleMP44Firing()
/*     */   {
/* 370 */     mp44Firing = (!mp44Firing);
/* 371 */     return mp44Firing;
/*     */   }
/*     */   
/*     */   public boolean isMuted()
/*     */   {
/* 376 */     return unmuteTask != null;
/*     */   }
/*     */   
/*     */   public void setMuted(boolean muted)
/*     */   {
/* 381 */     cancel(unmuteTask);
/* 382 */     unmuteTask = null;
/* 384 */     if (!muted) {
/* 386 */       return;
/*     */     }
/* 389 */     unmuteTask = new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 394 */         TFM_Util.adminAction("TotalFreedom", "Unmuting " + player.getName(), false);
/* 395 */         setMuted(false);
/*     */       }
/* 395 */     }.runTaskLater(TotalFreedomMod.plugin, 6000L);
/*     */   }
/*     */   
/*     */   public boolean isHalted()
/*     */   {
/* 402 */     return isHalted;
/*     */   }
/*     */   
/*     */   public void setHalted(boolean halted)
/*     */   {
/* 407 */     isHalted = halted;
/* 409 */     if (halted)
/*     */     {
/* 411 */       player.setOp(false);
/* 412 */       player.setGameMode(GameMode.SURVIVAL);
/* 413 */       TFM_Util.setFlying(player, false);
/* 414 */       TFM_EssentialsBridge.setNickname(player.getName(), player.getName());
/* 415 */       player.closeInventory();
/* 416 */       player.setTotalExperience(0);
/*     */       
/* 418 */       stopOrbiting();
/* 419 */       setFrozen(true);
/* 420 */       setMuted(true);
/*     */       
/* 422 */       player.sendMessage(ChatColor.GRAY + "You have been halted, don't move!");
/*     */     }
/*     */     else
/*     */     {
/* 426 */       player.setOp(true);
/* 427 */       player.setGameMode(GameMode.CREATIVE);
/* 428 */       setFrozen(false);
/* 429 */       setMuted(false);
/*     */       
/* 431 */       player.sendMessage(ChatColor.GRAY + "You are no longer halted.");
/*     */     }
/*     */   }
/*     */   
/*     */   public BukkitTask getLockupScheduleID()
/*     */   {
/* 438 */     return lockupScheduleTask;
/*     */   }
/*     */   
/*     */   public void setLockupScheduleID(BukkitTask id)
/*     */   {
/* 443 */     lockupScheduleTask = id;
/*     */   }
/*     */   
/*     */   public void setLastMessage(String message)
/*     */   {
/* 448 */     lastMessage = message;
/*     */   }
/*     */   
/*     */   public String getLastMessage()
/*     */   {
/* 453 */     return lastMessage;
/*     */   }
/*     */   
/*     */   public void setAdminChat(boolean inAdminchat)
/*     */   {
/* 458 */     this.inAdminchat = inAdminchat;
/*     */   }
/*     */   
/*     */   public boolean inAdminChat()
/*     */   {
/* 463 */     return inAdminchat;
/*     */   }
/*     */   
/*     */   public boolean allCommandsBlocked()
/*     */   {
/* 468 */     return allCommandsBlocked;
/*     */   }
/*     */   
/*     */   public void setCommandsBlocked(boolean commandsBlocked)
/*     */   {
/* 473 */     allCommandsBlocked = commandsBlocked;
/*     */   }
/*     */   
/*     */   public boolean isSuperadminIdVerified()
/*     */   {
/* 480 */     return verifiedSuperadminId;
/*     */   }
/*     */   
/*     */   public void setSuperadminIdVerified(boolean verifiedSuperadminId)
/*     */   {
/* 487 */     this.verifiedSuperadminId = verifiedSuperadminId;
/*     */   }
/*     */   
/*     */   public String getLastCommand()
/*     */   {
/* 492 */     return lastCommand;
/*     */   }
/*     */   
/*     */   public void setLastCommand(String lastCommand)
/*     */   {
/* 497 */     this.lastCommand = lastCommand;
/*     */   }
/*     */   
/*     */   public void setCommandSpy(boolean enabled)
/*     */   {
/* 502 */     cmdspyEnabled = enabled;
/*     */   }
/*     */   
/*     */   public boolean cmdspyEnabled()
/*     */   {
/* 507 */     return cmdspyEnabled;
/*     */   }
/*     */   
/*     */   public void setTag(String tag)
/*     */   {
/* 512 */     if (tag == null) {
/* 514 */       this.tag = null;
/*     */     } else {
/* 518 */       this.tag = (TFM_Util.colorize(tag) + ChatColor.WHITE);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getTag()
/*     */   {
/* 524 */     return tag;
/*     */   }
/*     */   
/*     */   public int getWarningCount()
/*     */   {
/* 529 */     return warningCount;
/*     */   }
/*     */   
/*     */   public void incrementWarnings()
/*     */   {
/* 534 */     warningCount += 1;
/* 536 */     if (warningCount % 2 == 0)
/*     */     {
/* 538 */       player.getWorld().strikeLightning(player.getLocation());
/* 539 */       TFM_Util.playerMsg(player, ChatColor.RED + "You have been warned at least twice now, make sure to read the rules at " + TFM_ConfigEntry.SERVER_BAN_URL.getString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void cancel(BukkitTask task)
/*     */   {
/* 545 */     if (task == null) {
/* 547 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 552 */       task.cancel();
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */   public static enum CageLayer
/*     */   {
/* 561 */     INNER,  OUTER;
/*     */     
/*     */     private CageLayer() {}
/*     */   }
/*     */   
/*     */   private class TFM_BlockData
/*     */   {
/*     */     public Material material;
/*     */     public Location location;
/*     */     
/*     */     private TFM_BlockData(Location location, Material material)
/*     */     {
/* 571 */       this.location = location;
/* 572 */       this.material = material;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ArrowShooter
/*     */     extends BukkitRunnable
/*     */   {
/*     */     private Player player;
/*     */     
/*     */     private ArrowShooter(Player player)
/*     */     {
/* 582 */       this.player = player;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 588 */       Arrow shot = (Arrow)player.launchProjectile(Arrow.class);
/* 589 */       shot.setVelocity(shot.getVelocity().multiply(2.0D));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_PlayerData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */