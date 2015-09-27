/*     */ package me.StevenLawson.TotalFreedomMod.Listener;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.Command_landmine.TFM_LandmineData;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Heartbeat;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Jumppads;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Jumppads.JumpPadMode;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Player;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData.CageLayer;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerRank;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager.RollbackEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Sync;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util.TFM_EntityWiper;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.TNTPrimed;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.LeavesDecayEvent;
/*     */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*     */ import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
/*     */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerKickEvent;
/*     */ import org.bukkit.event.player.PlayerLoginEvent;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class TFM_PlayerListener
/*     */   implements Listener
/*     */ {
/*  64 */   public static final List<String> BLOCKED_MUTED_CMDS = Arrays.asList(StringUtils.split("say,me,msg,m,tell,r,reply,mail,email", ","));
/*     */   public static final int MSG_PER_HEARTBEAT = 10;
/*     */   public static final int DEFAULT_PORT = 25565;
/*     */   public static final int MAX_XY_COORD = 30000000;
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onPlayerInteract(PlayerInteractEvent event)
/*     */   {
/*  72 */     Player player = event.getPlayer();
/*  73 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  75 */     switch (event.getAction())
/*     */     {
/*     */     case RIGHT_CLICK_AIR: 
/*     */     case RIGHT_CLICK_BLOCK: 
/*  80 */       switch (event.getMaterial())
/*     */       {
/*     */       case WATER_BUCKET: 
/*  84 */         if (!TFM_ConfigEntry.ALLOW_WATER_PLACE.getBoolean().booleanValue())
/*     */         {
/*  89 */           player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/*  90 */           player.sendMessage(ChatColor.GRAY + "Water buckets are currently disabled.");
/*  91 */           event.setCancelled(true);
/*     */         }
/*  92 */         break;
/*     */       case LAVA_BUCKET: 
/*  97 */         if (!TFM_ConfigEntry.ALLOW_LAVA_PLACE.getBoolean().booleanValue())
/*     */         {
/* 102 */           player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/* 103 */           player.sendMessage(ChatColor.GRAY + "Lava buckets are currently disabled.");
/* 104 */           event.setCancelled(true);
/*     */         }
/* 105 */         break;
/*     */       case EXPLOSIVE_MINECART: 
/* 110 */         if (!TFM_ConfigEntry.ALLOW_TNT_MINECARTS.getBoolean().booleanValue())
/*     */         {
/* 115 */           player.getInventory().clear(player.getInventory().getHeldItemSlot());
/* 116 */           player.sendMessage(ChatColor.GRAY + "TNT minecarts are currently disabled.");
/* 117 */           event.setCancelled(true);
/*     */         }
/*     */         break;
/*     */       }
/* 121 */       break;
/*     */     case LEFT_CLICK_AIR: 
/*     */     case LEFT_CLICK_BLOCK: 
/* 127 */       switch (event.getMaterial())
/*     */       {
/*     */       case STICK: 
/* 131 */         if (TFM_AdminList.isSuperAdmin(player))
/*     */         {
/* 136 */           event.setCancelled(true);
/*     */           
/* 138 */           Location location = TFM_DepreciationAggregator.getTargetBlock(player, null, 5).getLocation();
/* 139 */           List<TFM_RollbackManager.RollbackEntry> entries = TFM_RollbackManager.getEntriesAtLocation(location);
/* 141 */           if (entries.isEmpty())
/*     */           {
/* 143 */             TFM_Util.playerMsg(player, "No block edits at that location.");
/*     */           }
/*     */           else
/*     */           {
/* 147 */             TFM_Util.playerMsg(player, "Block edits at (" + ChatColor.WHITE + "x" + location.getBlockX() + ", y" + location.getBlockY() + ", z" + location.getBlockZ() + ChatColor.BLUE + ")" + ChatColor.WHITE + ":", ChatColor.BLUE);
/* 152 */             for (TFM_RollbackManager.RollbackEntry entry : entries) {
/* 154 */               TFM_Util.playerMsg(player, " - " + ChatColor.BLUE + author + " " + entry.getType() + " " + StringUtils.capitalize(entry.getMaterial().toString().toLowerCase()) + (data == 0 ? "" : new StringBuilder().append(":").append(data).toString()));
/*     */             }
/*     */           }
/*     */         }
/* 158 */         break;
/*     */       case BONE: 
/* 163 */         if (playerdata.mobThrowerEnabled())
/*     */         {
/* 168 */           Location player_pos = player.getLocation();
/* 169 */           Vector direction = player_pos.getDirection().normalize();
/*     */           
/* 171 */           LivingEntity rezzed_mob = (LivingEntity)player.getWorld().spawnEntity(player_pos.add(direction.multiply(2.0D)), playerdata.mobThrowerCreature());
/* 172 */           rezzed_mob.setVelocity(direction.multiply(playerdata.mobThrowerSpeed()));
/* 173 */           playerdata.enqueueMob(rezzed_mob);
/*     */           
/* 175 */           event.setCancelled(true);
/*     */         }
/* 176 */         break;
/*     */       case SULPHUR: 
/* 181 */         if (playerdata.isMP44Armed())
/*     */         {
/* 186 */           event.setCancelled(true);
/* 188 */           if (playerdata.toggleMP44Firing()) {
/* 190 */             playerdata.startArrowShooter(TotalFreedomMod.plugin);
/*     */           } else {
/* 194 */             playerdata.stopArrowShooter();
/*     */           }
/*     */         }
/* 196 */         break;
/*     */       case BLAZE_ROD: 
/* 201 */         if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue()) {
/* 206 */           if (TFM_AdminList.isSeniorAdmin(player, true))
/*     */           {
/* 211 */             event.setCancelled(true);
/*     */             Block targetBlock;
/*     */             Block targetBlock;
/* 214 */             if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
/* 216 */               targetBlock = TFM_DepreciationAggregator.getTargetBlock(player, null, 120);
/*     */             } else {
/* 220 */               targetBlock = event.getClickedBlock();
/*     */             }
/* 223 */             if (targetBlock == null)
/*     */             {
/* 225 */               player.sendMessage("Can't resolve target block.");
/*     */             }
/*     */             else
/*     */             {
/* 229 */               player.getWorld().createExplosion(targetBlock.getLocation(), 4.0F, true);
/* 230 */               player.getWorld().strikeLightning(targetBlock.getLocation());
/*     */             }
/*     */           }
/*     */         }
/* 232 */         break;
/*     */       case CARROT: 
/* 237 */         if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue()) {
/* 242 */           if (TFM_AdminList.isSeniorAdmin(player, true))
/*     */           {
/* 247 */             Location location = player.getLocation().clone();
/*     */             
/* 249 */             Vector playerPostion = location.toVector().add(new Vector(0.0D, 1.65D, 0.0D));
/* 250 */             Vector playerDirection = location.getDirection().normalize();
/*     */             
/* 252 */             double distance = 150.0D;
/* 253 */             Block targetBlock = TFM_DepreciationAggregator.getTargetBlock(player, null, Math.round((float)distance));
/* 254 */             if (targetBlock != null) {
/* 256 */               distance = location.distance(targetBlock.getLocation());
/*     */             }
/* 259 */             final List<Block> affected = new ArrayList();
/*     */             
/* 261 */             Block lastBlock = null;
/* 262 */             for (double offset = 0.0D; offset <= distance; offset += distance / 25.0D)
/*     */             {
/* 264 */               Block block = playerPostion.clone().add(playerDirection.clone().multiply(offset)).toLocation(player.getWorld()).getBlock();
/* 266 */               if (!block.equals(lastBlock))
/*     */               {
/* 268 */                 if (!block.isEmpty()) {
/*     */                   break;
/*     */                 }
/* 270 */                 affected.add(block);
/* 271 */                 block.setType(Material.TNT);
/*     */               }
/* 279 */               lastBlock = block;
/*     */             }
/* 282 */             new BukkitRunnable()
/*     */             {
/*     */               public void run()
/*     */               {
/* 287 */                 for (Block tntBlock : affected)
/*     */                 {
/* 289 */                   TNTPrimed tnt = (TNTPrimed)tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
/* 290 */                   tnt.setFuseTicks(5);
/* 291 */                   tntBlock.setType(Material.AIR);
/*     */                 }
/*     */               }
/* 291 */             }.runTaskLater(TotalFreedomMod.plugin, 30L);
/*     */             
/* 296 */             event.setCancelled(true);
/*     */           }
/*     */         }
/* 297 */         break;
/*     */       case RAW_FISH: 
/* 302 */         int RADIUS_HIT = 5;
/* 303 */         int STRENGTH = 4;
/* 306 */         if (TFM_DepreciationAggregator.getData_MaterialData(event.getItem().getData()) == 2)
/*     */         {
/* 308 */           if ((TFM_AdminList.isSeniorAdmin(player, true)) || (TFM_AdminList.isTelnetAdmin(player, true)))
/*     */           {
/* 310 */             boolean didHit = false;
/*     */             
/* 312 */             Location playerLoc = player.getLocation();
/* 313 */             Vector playerLocVec = playerLoc.toVector();
/*     */             
/* 315 */             List<Player> players = player.getWorld().getPlayers();
/* 316 */             for (Player target : players) {
/* 318 */               if (target != player)
/*     */               {
/* 323 */                 Location targetPos = target.getLocation();
/* 324 */                 Vector targetPosVec = targetPos.toVector();
/*     */                 try
/*     */                 {
/* 328 */                   if (targetPosVec.distanceSquared(playerLocVec) < 25.0D)
/*     */                   {
/* 330 */                     TFM_Util.setFlying(player, false);
/* 331 */                     target.setVelocity(targetPosVec.subtract(playerLocVec).normalize().multiply(4));
/* 332 */                     didHit = true;
/*     */                   }
/*     */                 }
/*     */                 catch (IllegalArgumentException ex) {}
/*     */               }
/*     */             }
/* 340 */             if (didHit)
/*     */             {
/* 342 */               Sound[] sounds = Sound.values();
/* 343 */               for (Sound sound : sounds) {
/* 345 */                 if (sound.toString().contains("HIT")) {
/* 347 */                   playerLoc.getWorld().playSound(randomOffset(playerLoc, 5.0D), sound, 100.0F, randomDoubleRange(0.5D, 2.0D).floatValue());
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 354 */             StringBuilder msg = new StringBuilder();
/* 355 */             char[] chars = (player.getName() + " is a clown.").toCharArray();
/* 356 */             for (char c : chars) {
/* 358 */               msg.append(TFM_Util.randomChatColor()).append(c);
/*     */             }
/* 360 */             TFM_Util.bcastMsg(msg.toString());
/*     */             
/* 362 */             player.getInventory().getItemInHand().setType(Material.POTATO_ITEM);
/*     */           }
/* 365 */           event.setCancelled(true);
/*     */         }
/*     */         break;
/*     */       }
/* 366 */       break;
/*     */     }
/*     */   }
/*     */   
/* 374 */   private static final Random RANDOM = new Random();
/*     */   
/*     */   private static Location randomOffset(Location a, double magnitude)
/*     */   {
/* 378 */     return a.clone().add(randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude, randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude, randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude);
/*     */   }
/*     */   
/*     */   private static Double randomDoubleRange(double min, double max)
/*     */   {
/* 383 */     return Double.valueOf(min + RANDOM.nextDouble() * (max - min + 1.0D));
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerTeleport(PlayerTeleportEvent event)
/*     */   {
/* 389 */     Player player = event.getPlayer();
/* 390 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 392 */     if ((Math.abs(event.getTo().getX()) >= 3.0E7D) || (Math.abs(event.getTo().getZ()) >= 3.0E7D)) {
/* 394 */       event.setCancelled(true);
/*     */     }
/* 397 */     if ((!TFM_AdminList.isSuperAdmin(player)) && (playerdata.isFrozen()))
/*     */     {
/* 399 */       TFM_Util.setFlying(player, true);
/* 400 */       event.setTo(playerdata.getFreezeLocation());
/* 401 */       return;
/*     */     }
/* 404 */     TFM_AdminWorld.getInstance().validateMovement(event);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerMove(PlayerMoveEvent event)
/*     */   {
/* 410 */     Location from = event.getFrom();
/* 411 */     Location to = event.getTo();
/*     */     try
/*     */     {
/* 414 */       if ((from.getWorld() == to.getWorld()) && (from.distanceSquared(to) < 1.0E-8D)) {
/* 417 */         return;
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException ex) {}
/* 424 */     if (!TFM_AdminWorld.getInstance().validateMovement(event)) {
/* 426 */       return;
/*     */     }
/* 429 */     Player player = event.getPlayer();
/* 430 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 432 */     for (Map.Entry<Player, Double> fuckoff : TotalFreedomMod.fuckoffEnabledFor.entrySet())
/*     */     {
/* 434 */       Player fuckoffPlayer = (Player)fuckoff.getKey();
/* 436 */       if ((!fuckoffPlayer.equals(player)) && (fuckoffPlayer.isOnline()))
/*     */       {
/* 441 */         double fuckoffRange = ((Double)fuckoff.getValue()).doubleValue();
/*     */         
/* 443 */         Location playerLocation = player.getLocation();
/* 444 */         Location fuckoffLocation = fuckoffPlayer.getLocation();
/*     */         double distanceSquared;
/*     */         try
/*     */         {
/* 449 */           distanceSquared = playerLocation.distanceSquared(fuckoffLocation);
/*     */         }
/*     */         catch (IllegalArgumentException ex) {}
/* 453 */         continue;
/* 456 */         if (distanceSquared < fuckoffRange * fuckoffRange)
/*     */         {
/* 458 */           event.setTo(fuckoffLocation.clone().add(playerLocation.subtract(fuckoffLocation).toVector().normalize().multiply(fuckoffRange * 1.1D)));
/* 459 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 464 */     if ((!TFM_AdminList.isSuperAdmin(player)) && (playerdata.isFrozen()))
/*     */     {
/* 466 */       TFM_Util.setFlying(player, true);
/* 467 */       event.setTo(playerdata.getFreezeLocation());
/*     */     }
/* 470 */     if (playerdata.isCaged())
/*     */     {
/* 472 */       Location targetPos = player.getLocation().add(0.0D, 1.0D, 0.0D);
/*     */       boolean outOfCage;
/*     */       boolean outOfCage;
/* 475 */       if (!targetPos.getWorld().equals(playerdata.getCagePos().getWorld())) {
/* 477 */         outOfCage = true;
/*     */       } else {
/* 481 */         outOfCage = targetPos.distanceSquared(playerdata.getCagePos()) > 6.25D;
/*     */       }
/* 484 */       if (outOfCage)
/*     */       {
/* 486 */         playerdata.setCaged(true, targetPos, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.OUTER), playerdata.getCageMaterial(TFM_PlayerData.CageLayer.INNER));
/* 487 */         playerdata.regenerateHistory();
/* 488 */         playerdata.clearHistory();
/* 489 */         TFM_Util.buildHistory(targetPos, 2, playerdata);
/* 490 */         TFM_Util.generateHollowCube(targetPos, 2, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.OUTER));
/* 491 */         TFM_Util.generateCube(targetPos, 1, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.INNER));
/*     */       }
/*     */     }
/* 495 */     if (playerdata.isOrbiting()) {
/* 497 */       if (player.getVelocity().length() < playerdata.orbitStrength() * 0.6666666666666666D) {
/* 499 */         player.setVelocity(new Vector(0.0D, playerdata.orbitStrength(), 0.0D));
/*     */       }
/*     */     }
/* 503 */     if (TFM_Jumppads.getMode().isOn()) {
/* 505 */       TFM_Jumppads.PlayerMoveEvent(event);
/*     */     }
/* 508 */     if ((!TFM_ConfigEntry.LANDMINES_ENABLED.getBoolean().booleanValue()) || (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())) {
/* 510 */       return;
/*     */     }
/* 513 */     Iterator<Command_landmine.TFM_LandmineData> landmines = Command_landmine.TFM_LandmineData.landmines.iterator();
/* 514 */     while (landmines.hasNext())
/*     */     {
/* 516 */       Command_landmine.TFM_LandmineData landmine = (Command_landmine.TFM_LandmineData)landmines.next();
/*     */       
/* 518 */       Location location = location;
/* 519 */       if (location.getBlock().getType() != Material.TNT)
/*     */       {
/* 521 */         landmines.remove();
/*     */       }
/*     */       else
/*     */       {
/* 525 */         if (player.equals(player)) {
/*     */           break;
/*     */         }
/* 530 */         if (player.getWorld().equals(location.getWorld()))
/*     */         {
/* 535 */           if (player.getLocation().distanceSquared(location) > radius * radius) {
/*     */             break;
/*     */           }
/* 540 */           location.getBlock().setType(Material.AIR);
/*     */           
/* 542 */           TNTPrimed tnt1 = (TNTPrimed)location.getWorld().spawn(location, TNTPrimed.class);
/* 543 */           tnt1.setFuseTicks(40);
/* 544 */           tnt1.setPassenger(player);
/* 545 */           tnt1.setVelocity(new Vector(0.0D, 2.0D, 0.0D));
/*     */           
/* 547 */           TNTPrimed tnt2 = (TNTPrimed)location.getWorld().spawn(player.getLocation(), TNTPrimed.class);
/* 548 */           tnt2.setFuseTicks(1);
/*     */           
/* 550 */           player.setGameMode(GameMode.SURVIVAL);
/* 551 */           landmines.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onLeavesDecay(LeavesDecayEvent event)
/*     */   {
/* 558 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerChat(AsyncPlayerChatEvent event)
/*     */   {
/*     */     try
/*     */     {
/* 566 */       Player player = event.getPlayer();
/* 567 */       String message = event.getMessage().trim();
/*     */       
/* 569 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerDataSync(player);
/*     */       
/* 572 */       Long lastRan = TFM_Heartbeat.getLastRan();
/* 573 */       if ((lastRan != null) && (lastRan.longValue() + 5000L >= System.currentTimeMillis())) {
/* 579 */         if (playerdata.incrementAndGetMsgCount() > 10)
/*     */         {
/* 581 */           TFM_Sync.bcastMsg(player.getName() + " was automatically kicked for spamming chat.", ChatColor.RED);
/* 582 */           TFM_Sync.autoEject(player, "Kicked for spamming chat.");
/*     */           
/* 584 */           playerdata.resetMsgCount();
/*     */           
/* 586 */           event.setCancelled(true);
/* 587 */           return;
/*     */         }
/*     */       }
/* 592 */       if (playerdata.getLastMessage().equalsIgnoreCase(message))
/*     */       {
/* 594 */         TFM_Sync.playerMsg(player, "Please do not repeat messages.");
/* 595 */         event.setCancelled(true);
/* 596 */         return;
/*     */       }
/* 599 */       playerdata.setLastMessage(message);
/* 602 */       if (playerdata.isMuted())
/*     */       {
/* 604 */         if (!TFM_AdminList.isSuperAdminSync(player))
/*     */         {
/* 606 */           TFM_Sync.playerMsg(player, ChatColor.RED + "You are muted, STFU! - You will be unmuted in 5 minutes.");
/* 607 */           event.setCancelled(true);
/* 608 */           return;
/*     */         }
/* 611 */         playerdata.setMuted(false);
/*     */       }
/* 615 */       message = ChatColor.stripColor(message);
/* 618 */       if (message.length() > 100)
/*     */       {
/* 620 */         message = message.substring(0, 100);
/* 621 */         TFM_Sync.playerMsg(player, "Message was shortened because it was too long to send.");
/*     */       }
/* 625 */       if (message.length() >= 6)
/*     */       {
/* 627 */         int caps = 0;
/* 628 */         for (char c : message.toCharArray()) {
/* 630 */           if (Character.isUpperCase(c)) {
/* 632 */             caps++;
/*     */           }
/*     */         }
/* 635 */         if (caps / message.length() > 0.65D) {
/* 637 */           message = message.toLowerCase();
/*     */         }
/*     */       }
/* 642 */       if (playerdata.inAdminChat())
/*     */       {
/* 644 */         TFM_Sync.adminChatMessage(player, message, false);
/* 645 */         event.setCancelled(true);
/* 646 */         return;
/*     */       }
/* 650 */       event.setMessage(message);
/* 653 */       if (playerdata.getTag() != null) {
/* 655 */         event.setFormat("<" + playerdata.getTag().replaceAll("%", "%%") + " %1$s> %2$s");
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 660 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
/*     */   {
/* 667 */     String command = event.getMessage();
/* 668 */     Player player = event.getPlayer();
/*     */     
/* 670 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 671 */     playerdata.setLastCommand(command);
/* 673 */     if (playerdata.incrementAndGetMsgCount() > 10)
/*     */     {
/* 675 */       TFM_Util.bcastMsg(player.getName() + " was automatically kicked for spamming commands.", ChatColor.RED);
/* 676 */       TFM_Util.autoEject(player, "Kicked for spamming commands.");
/*     */       
/* 678 */       playerdata.resetMsgCount();
/*     */       
/* 680 */       TFM_Util.TFM_EntityWiper.wipeEntities(true, true);
/*     */       
/* 682 */       event.setCancelled(true);
/* 683 */       return;
/*     */     }
/* 686 */     if (playerdata.allCommandsBlocked())
/*     */     {
/* 688 */       TFM_Util.playerMsg(player, "Your commands have been blocked by an admin.", ChatColor.RED);
/* 689 */       event.setCancelled(true);
/* 690 */       return;
/*     */     }
/* 694 */     if (playerdata.isMuted()) {
/* 696 */       if (!TFM_AdminList.isSuperAdmin(player)) {
/* 698 */         for (String commandName : BLOCKED_MUTED_CMDS) {
/* 700 */           if (Pattern.compile("^/" + commandName.toLowerCase() + " ").matcher(command.toLowerCase()).find())
/*     */           {
/* 702 */             player.sendMessage(ChatColor.RED + "That command is blocked while you are muted.");
/* 703 */             event.setCancelled(true);
/* 704 */             return;
/*     */           }
/*     */         }
/*     */       } else {
/* 710 */         playerdata.setMuted(false);
/*     */       }
/*     */     }
/* 714 */     if (TFM_ConfigEntry.ENABLE_PREPROCESS_LOG.getBoolean().booleanValue()) {
/* 716 */       TFM_Log.info(String.format("[PREPROCESS_COMMAND] %s(%s): %s", new Object[] { player.getName(), ChatColor.stripColor(player.getDisplayName()), command }), Boolean.valueOf(true));
/*     */     }
/* 720 */     if (TFM_CommandBlocker.isCommandBlocked(command, player, true)) {
/* 723 */       event.setCancelled(true);
/*     */     }
/* 726 */     if (!TFM_AdminList.isSuperAdmin(player)) {
/* 728 */       for (Player pl : Bukkit.getOnlinePlayers()) {
/* 730 */         if ((TFM_AdminList.isSuperAdmin(pl)) && (TFM_PlayerData.getPlayerData(pl).cmdspyEnabled())) {
/* 732 */           TFM_Util.playerMsg(pl, player.getName() + ": " + command);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerDropItem(PlayerDropItemEvent event)
/*     */   {
/* 741 */     if (TFM_ConfigEntry.AUTO_ENTITY_WIPE.getBoolean().booleanValue()) {
/* 743 */       if (event.getPlayer().getWorld().getEntities().size() > 750) {
/* 745 */         event.setCancelled(true);
/*     */       } else {
/* 749 */         event.getItemDrop().remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerKick(PlayerKickEvent event)
/*     */   {
/* 757 */     playerLeave(event.getPlayer());
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerQuit(PlayerQuitEvent event)
/*     */   {
/* 763 */     playerLeave(event.getPlayer());
/*     */   }
/*     */   
/*     */   private void playerLeave(Player player)
/*     */   {
/* 768 */     if (TotalFreedomMod.fuckoffEnabledFor.containsKey(player)) {
/* 770 */       TotalFreedomMod.fuckoffEnabledFor.remove(player);
/*     */     }
/* 773 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*     */     
/* 775 */     playerdata.disarmMP44();
/* 777 */     if (playerdata.isCaged())
/*     */     {
/* 779 */       playerdata.regenerateHistory();
/* 780 */       playerdata.clearHistory();
/*     */     }
/* 783 */     TFM_PlayerList.removeEntry(player);
/* 784 */     TFM_Log.info("[EXIT] " + player.getName() + " left the game.", Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onPlayerJoin(PlayerJoinEvent event)
/*     */   {
/* 790 */     final Player player = event.getPlayer();
/* 791 */     String ip = TFM_Util.getIp(player);
/*     */     
/* 793 */     TFM_Log.info("[JOIN] " + TFM_Util.formatPlayer(player) + " joined the game with IP address: " + ip, Boolean.valueOf(true));
/* 795 */     if ((Math.abs(player.getLocation().getX()) >= 3.0E7D) || (Math.abs(player.getLocation().getZ()) >= 3.0E7D)) {
/* 797 */       player.teleport(player.getWorld().getSpawnLocation());
/*     */     }
/*     */     TFM_Player playerEntry;
/* 800 */     if (TFM_PlayerList.existsEntry(player))
/*     */     {
/* 802 */       TFM_Player playerEntry = TFM_PlayerList.getEntry(player);
/* 803 */       playerEntry.setLastLoginUnix(TFM_Util.getUnixTime());
/* 804 */       playerEntry.setLastLoginName(player.getName());
/* 805 */       playerEntry.addIp(ip);
/* 806 */       playerEntry.save();
/*     */     }
/*     */     else
/*     */     {
/* 810 */       playerEntry = TFM_PlayerList.getEntry(player);
/* 811 */       TFM_Log.info("Added new player: " + TFM_Util.formatPlayer(player));
/*     */     }
/* 815 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 816 */     playerdata.setSuperadminIdVerified(false);
/* 818 */     if (TFM_AdminList.isSuperAdmin(player))
/*     */     {
/* 820 */       for (String storedIp : playerEntry.getIps())
/*     */       {
/* 822 */         TFM_BanManager.unbanIp(storedIp);
/* 823 */         TFM_BanManager.unbanIp(TFM_Util.getFuzzyIp(storedIp));
/*     */       }
/* 826 */       TFM_BanManager.unbanUuid(TFM_UuidManager.getUniqueId(player));
/*     */       
/* 828 */       player.setOp(true);
/* 831 */       if (!TFM_AdminList.isIdentityMatched(player))
/*     */       {
/* 833 */         playerdata.setSuperadminIdVerified(false);
/* 834 */         TFM_Util.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
/*     */       }
/*     */       else
/*     */       {
/* 838 */         playerdata.setSuperadminIdVerified(true);
/* 839 */         TFM_AdminList.updateLastLogin(player);
/*     */       }
/*     */     }
/* 844 */     if (TFM_AdminList.isAdminImpostor(player))
/*     */     {
/* 846 */       TFM_Util.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor and has been frozen!", ChatColor.RED);
/* 847 */       TFM_Util.bcastMsg(ChatColor.AQUA + player.getName() + " is " + TFM_PlayerRank.getLoginMessage(player));
/* 848 */       player.getInventory().clear();
/* 849 */       player.setOp(false);
/* 850 */       player.setGameMode(GameMode.SURVIVAL);
/* 851 */       TFM_PlayerData.getPlayerData(player).setFrozen(true);
/*     */     }
/* 853 */     else if ((TFM_AdminList.isSuperAdmin(player)) || (TFM_Util.DEVELOPERS.contains(player.getName())))
/*     */     {
/* 855 */       TFM_Util.bcastMsg(ChatColor.AQUA + player.getName() + " is " + TFM_PlayerRank.getLoginMessage(player));
/*     */     }
/* 859 */     String name = player.getName();
/* 860 */     if (TFM_Util.DEVELOPERS.contains(player.getName()))
/*     */     {
/* 862 */       name = ChatColor.DARK_PURPLE + name;
/* 863 */       TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
/*     */     }
/* 865 */     else if (TFM_AdminList.isSuperAdmin(player))
/*     */     {
/* 867 */       if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(name))
/*     */       {
/* 869 */         name = ChatColor.BLUE + name;
/* 870 */         TFM_PlayerData.getPlayerData(player).setTag("&8[&9Owner&8]");
/*     */       }
/* 872 */       else if (TFM_AdminList.isSeniorAdmin(player))
/*     */       {
/* 874 */         name = ChatColor.LIGHT_PURPLE + name;
/* 875 */         TFM_PlayerData.getPlayerData(player).setTag("&8[&dSenior Admin&8]");
/*     */       }
/* 877 */       else if (TFM_AdminList.isTelnetAdmin(player, true))
/*     */       {
/* 879 */         name = ChatColor.DARK_GREEN + name;
/* 880 */         TFM_PlayerData.getPlayerData(player).setTag("&8[&2Telnet Admin&8]");
/*     */       }
/*     */       else
/*     */       {
/* 884 */         name = ChatColor.AQUA + name;
/* 885 */         TFM_PlayerData.getPlayerData(player).setTag("&8[&BSuper Admin&8]");
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 891 */       player.setPlayerListName(StringUtils.substring(name, 0, 16));
/*     */     }
/*     */     catch (IllegalArgumentException ex) {}
/* 897 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 902 */         if (TFM_ConfigEntry.ADMIN_ONLY_MODE.getBoolean().booleanValue()) {
/* 904 */           player.sendMessage(ChatColor.RED + "Server is currently closed to non-superadmins.");
/*     */         }
/* 907 */         if (TotalFreedomMod.lockdownEnabled) {
/* 909 */           TFM_Util.playerMsg(player, "Warning: Server is currenty in lockdown-mode, new players will not be able to join!", ChatColor.RED);
/*     */         }
/*     */       }
/* 909 */     }.runTaskLater(TotalFreedomMod.plugin, 20L);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event)
/*     */   {
/* 918 */     TFM_ServerInterface.handlePlayerPreLogin(event);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerLogin(PlayerLoginEvent event)
/*     */   {
/* 924 */     TFM_ServerInterface.handlePlayerLogin(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Listener\TFM_PlayerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */