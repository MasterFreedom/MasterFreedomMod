/*     */ package me.StevenLawson.TotalFreedomMod.Listener;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Heartbeat;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_ProtectedArea;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockBurnEvent;
/*     */ import org.bukkit.event.block.BlockFromToEvent;
/*     */ import org.bukkit.event.block.BlockIgniteEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ public class TFM_BlockListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onBlockBurn(BlockBurnEvent event)
/*     */   {
/*  31 */     if (!TFM_ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean().booleanValue()) {
/*  33 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onBlockIgnite(BlockIgniteEvent event)
/*     */   {
/*  40 */     if (!TFM_ConfigEntry.ALLOW_FIRE_PLACE.getBoolean().booleanValue()) {
/*  42 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onBlockBreak(BlockBreakEvent event)
/*     */   {
/*  49 */     Player player = event.getPlayer();
/*  50 */     Location location = event.getBlock().getLocation();
/*  52 */     if (TFM_ConfigEntry.NUKE_MONITOR_ENABLED.getBoolean().booleanValue())
/*     */     {
/*  54 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*     */       
/*  56 */       Location playerLocation = player.getLocation();
/*     */       
/*  58 */       double nukeMonitorRange = TFM_ConfigEntry.NUKE_MONITOR_RANGE.getDouble().doubleValue();
/*     */       
/*  60 */       boolean outOfRange = false;
/*  61 */       if (!playerLocation.getWorld().equals(location.getWorld())) {
/*  63 */         outOfRange = true;
/*  65 */       } else if (playerLocation.distanceSquared(location) > nukeMonitorRange * nukeMonitorRange) {
/*  67 */         outOfRange = true;
/*     */       }
/*  70 */       if (outOfRange) {
/*  72 */         if (playerdata.incrementAndGetFreecamDestroyCount() > TFM_ConfigEntry.FREECAM_TRIGGER_COUNT.getInteger().intValue())
/*     */         {
/*  74 */           TFM_Util.bcastMsg(player.getName() + " has been flagged for possible freecam nuking.", ChatColor.RED);
/*  75 */           TFM_Util.autoEject(player, "Freecam (extended range) block breaking is not permitted on this server.");
/*     */           
/*  77 */           playerdata.resetFreecamDestroyCount();
/*     */           
/*  79 */           event.setCancelled(true);
/*  80 */           return;
/*     */         }
/*     */       }
/*  84 */       Long lastRan = TFM_Heartbeat.getLastRan();
/*  85 */       if ((lastRan != null) && (lastRan.longValue() + 5000L >= System.currentTimeMillis())) {
/*  91 */         if (playerdata.incrementAndGetBlockDestroyCount() > TFM_ConfigEntry.NUKE_MONITOR_COUNT_BREAK.getInteger().intValue())
/*     */         {
/*  93 */           TFM_Util.bcastMsg(player.getName() + " is breaking blocks too fast!", ChatColor.RED);
/*  94 */           TFM_Util.autoEject(player, "You are breaking blocks too fast. Nukers are not permitted on this server.");
/*     */           
/*  96 */           playerdata.resetBlockDestroyCount();
/*     */           
/*  98 */           event.setCancelled(true);
/*  99 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 104 */     if (TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) {
/* 106 */       if (!TFM_AdminList.isSuperAdmin(player)) {
/* 108 */         if (TFM_ProtectedArea.isInProtectedArea(location)) {
/* 110 */           event.setCancelled(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onBlockPlace(BlockPlaceEvent event)
/*     */   {
/* 119 */     Player player = event.getPlayer();
/* 120 */     Location blockLocation = event.getBlock().getLocation();
/* 122 */     if (TFM_ConfigEntry.NUKE_MONITOR_ENABLED.getBoolean().booleanValue())
/*     */     {
/* 124 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*     */       
/* 126 */       Location playerLocation = player.getLocation();
/*     */       
/* 128 */       double nukeMonitorRange = TFM_ConfigEntry.NUKE_MONITOR_RANGE.getDouble().doubleValue();
/*     */       
/* 130 */       boolean outOfRange = false;
/* 131 */       if (!playerLocation.getWorld().equals(blockLocation.getWorld())) {
/* 133 */         outOfRange = true;
/* 135 */       } else if (playerLocation.distanceSquared(blockLocation) > nukeMonitorRange * nukeMonitorRange) {
/* 137 */         outOfRange = true;
/*     */       }
/* 140 */       if (outOfRange) {
/* 142 */         if (playerdata.incrementAndGetFreecamPlaceCount() > TFM_ConfigEntry.FREECAM_TRIGGER_COUNT.getInteger().intValue())
/*     */         {
/* 144 */           TFM_Util.bcastMsg(player.getName() + " has been flagged for possible freecam building.", ChatColor.RED);
/* 145 */           TFM_Util.autoEject(player, "Freecam (extended range) block building is not permitted on this server.");
/*     */           
/* 147 */           playerdata.resetFreecamPlaceCount();
/*     */           
/* 149 */           event.setCancelled(true);
/* 150 */           return;
/*     */         }
/*     */       }
/* 154 */       Long lastRan = TFM_Heartbeat.getLastRan();
/* 155 */       if ((lastRan != null) && (lastRan.longValue() + 5000L >= System.currentTimeMillis())) {
/* 161 */         if (playerdata.incrementAndGetBlockPlaceCount() > TFM_ConfigEntry.NUKE_MONITOR_COUNT_PLACE.getInteger().intValue())
/*     */         {
/* 163 */           TFM_Util.bcastMsg(player.getName() + " is placing blocks too fast!", ChatColor.RED);
/* 164 */           TFM_Util.autoEject(player, "You are placing blocks too fast.");
/*     */           
/* 166 */           playerdata.resetBlockPlaceCount();
/*     */           
/* 168 */           event.setCancelled(true);
/* 169 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 174 */     if (TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) {
/* 176 */       if (!TFM_AdminList.isSuperAdmin(player)) {
/* 178 */         if (TFM_ProtectedArea.isInProtectedArea(blockLocation))
/*     */         {
/* 180 */           event.setCancelled(true);
/* 181 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 186 */     switch (event.getBlockPlaced().getType())
/*     */     {
/*     */     case LAVA: 
/*     */     case STATIONARY_LAVA: 
/* 191 */       if (TFM_ConfigEntry.ALLOW_LAVA_PLACE.getBoolean().booleanValue())
/*     */       {
/* 193 */         TFM_Log.info(String.format("%s placed lava @ %s", new Object[] { player.getName(), TFM_Util.formatLocation(event.getBlock().getLocation()) }));
/*     */         
/* 195 */         player.getInventory().clear(player.getInventory().getHeldItemSlot());
/*     */       }
/*     */       else
/*     */       {
/* 199 */         player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/* 200 */         player.sendMessage(ChatColor.GRAY + "Lava placement is currently disabled.");
/*     */         
/* 202 */         event.setCancelled(true);
/*     */       }
/* 204 */       break;
/*     */     case WATER: 
/*     */     case STATIONARY_WATER: 
/* 209 */       if (TFM_ConfigEntry.ALLOW_WATER_PLACE.getBoolean().booleanValue())
/*     */       {
/* 211 */         TFM_Log.info(String.format("%s placed water @ %s", new Object[] { player.getName(), TFM_Util.formatLocation(event.getBlock().getLocation()) }));
/*     */         
/* 213 */         player.getInventory().clear(player.getInventory().getHeldItemSlot());
/*     */       }
/*     */       else
/*     */       {
/* 217 */         player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/* 218 */         player.sendMessage(ChatColor.GRAY + "Water placement is currently disabled.");
/*     */         
/* 220 */         event.setCancelled(true);
/*     */       }
/* 222 */       break;
/*     */     case FIRE: 
/* 226 */       if (TFM_ConfigEntry.ALLOW_FIRE_PLACE.getBoolean().booleanValue())
/*     */       {
/* 228 */         TFM_Log.info(String.format("%s placed fire @ %s", new Object[] { player.getName(), TFM_Util.formatLocation(event.getBlock().getLocation()) }));
/*     */         
/* 230 */         player.getInventory().clear(player.getInventory().getHeldItemSlot());
/*     */       }
/*     */       else
/*     */       {
/* 234 */         player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/* 235 */         player.sendMessage(ChatColor.GRAY + "Fire placement is currently disabled.");
/*     */         
/* 237 */         event.setCancelled(true);
/*     */       }
/* 239 */       break;
/*     */     case TNT: 
/* 243 */       if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())
/*     */       {
/* 245 */         TFM_Log.info(String.format("%s placed TNT @ %s", new Object[] { player.getName(), TFM_Util.formatLocation(event.getBlock().getLocation()) }));
/*     */         
/* 247 */         player.getInventory().clear(player.getInventory().getHeldItemSlot());
/*     */       }
/*     */       else
/*     */       {
/* 251 */         player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
/*     */         
/* 253 */         player.sendMessage(ChatColor.GRAY + "TNT is currently disabled.");
/* 254 */         event.setCancelled(true);
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
/*     */   public void onRollbackBlockBreak(BlockBreakEvent event)
/*     */   {
/* 264 */     if (!TFM_AdminList.isSuperAdmin(event.getPlayer())) {
/* 266 */       TFM_RollbackManager.blockBreak(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
/*     */   public void onRollbackBlockPlace(BlockPlaceEvent event)
/*     */   {
/* 273 */     if (!TFM_AdminList.isSuperAdmin(event.getPlayer())) {
/* 275 */       TFM_RollbackManager.blockPlace(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onBlockFromTo(BlockFromToEvent event)
/*     */   {
/* 282 */     if (!TFM_ConfigEntry.ALLOW_FLUID_SPREAD.getBoolean().booleanValue()) {
/* 284 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Listener\TFM_BlockListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */