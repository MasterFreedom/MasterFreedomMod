/*      */ package me.StevenLawson.TotalFreedomMod;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.URL;
/*      */ import java.nio.channels.Channels;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
/*      */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.GameMode;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.OfflinePlayer;
/*      */ import org.bukkit.SkullType;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.Skull;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.entity.Boat;
/*      */ import org.bukkit.entity.Creature;
/*      */ import org.bukkit.entity.EnderCrystal;
/*      */ import org.bukkit.entity.EnderSignal;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.EntityType;
/*      */ import org.bukkit.entity.ExperienceOrb;
/*      */ import org.bukkit.entity.Explosive;
/*      */ import org.bukkit.entity.FallingBlock;
/*      */ import org.bukkit.entity.Firework;
/*      */ import org.bukkit.entity.Item;
/*      */ import org.bukkit.entity.Minecart;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.entity.Projectile;
/*      */ import org.bukkit.inventory.PlayerInventory;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ import org.bukkit.util.FileUtil;
/*      */ 
/*      */ public class TFM_Util
/*      */ {
/*   67 */   private static final Map<String, Integer> ejectTracker = new HashMap();
/*   68 */   public static final Map<String, EntityType> mobtypes = new HashMap();
/*   70 */   public static final List<String> DEVELOPERS = Arrays.asList(new String[] { "Madgeek1450", "Prozza", "DarthSalmon", "AcidicCyanide", "Wild1145", "WickedGamingUK" });
/*   71 */   private static final Random RANDOM = new Random();
/*   72 */   public static String DATE_STORAGE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
/*   73 */   public static final Map<String, ChatColor> CHAT_COLOR_NAMES = new HashMap();
/*   74 */   public static final List<ChatColor> CHAT_COLOR_POOL = Arrays.asList(new ChatColor[] { ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW });
/*      */   
/*      */   static
/*      */   {
/*   90 */     for (EntityType type : EntityType.values()) {
/*      */       try
/*      */       {
/*   94 */         if (TFM_DepreciationAggregator.getName_EntityType(type) != null) {
/*   96 */           if (Creature.class.isAssignableFrom(type.getEntityClass())) {
/*   98 */             mobtypes.put(TFM_DepreciationAggregator.getName_EntityType(type).toLowerCase(), type);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {}
/*      */     }
/*  107 */     for (ChatColor chatColor : CHAT_COLOR_POOL) {
/*  109 */       CHAT_COLOR_NAMES.put(chatColor.name().toLowerCase().replace("_", ""), chatColor);
/*      */     }
/*      */   }
/*      */   
/*      */   private TFM_Util()
/*      */   {
/*  115 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   public static void bcastMsg(String message, ChatColor color)
/*      */   {
/*  120 */     TFM_Log.info(message, Boolean.valueOf(true));
/*  122 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  124 */       player.sendMessage((color == null ? "" : color) + message);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void bcastMsg(String message)
/*      */   {
/*  130 */     bcastMsg(message, null);
/*      */   }
/*      */   
/*      */   public static void playerMsg(CommandSender sender, String message, ChatColor color)
/*      */   {
/*  136 */     sender.sendMessage(color + message);
/*      */   }
/*      */   
/*      */   public static void playerMsg(CommandSender sender, String message)
/*      */   {
/*  142 */     playerMsg(sender, message, ChatColor.GRAY);
/*      */   }
/*      */   
/*      */   public static void setFlying(Player player, boolean flying)
/*      */   {
/*  147 */     player.setAllowFlight(true);
/*  148 */     player.setFlying(flying);
/*      */   }
/*      */   
/*      */   public static void adminAction(String adminName, String action, boolean isRed)
/*      */   {
/*  153 */     bcastMsg(adminName + " - " + action, isRed ? ChatColor.RED : ChatColor.AQUA);
/*      */   }
/*      */   
/*      */   public static String getIp(OfflinePlayer player)
/*      */   {
/*  158 */     if (player.isOnline()) {
/*  160 */       return player.getPlayer().getAddress().getAddress().getHostAddress().trim();
/*      */     }
/*  163 */     TFM_Player entry = TFM_PlayerList.getEntry(TFM_UuidManager.getUniqueId(player));
/*      */     
/*  165 */     return entry == null ? null : (String)entry.getIps().get(0);
/*      */   }
/*      */   
/*      */   public static boolean isUniqueId(String uuid)
/*      */   {
/*      */     try
/*      */     {
/*  172 */       UUID.fromString(uuid);
/*  173 */       return true;
/*      */     }
/*      */     catch (IllegalArgumentException ex) {}
/*  177 */     return false;
/*      */   }
/*      */   
/*      */   public static String formatLocation(Location location)
/*      */   {
/*  183 */     return String.format("%s: (%d, %d, %d)", new Object[] { location.getWorld().getName(), Long.valueOf(Math.round(location.getX())), Long.valueOf(Math.round(location.getY())), Long.valueOf(Math.round(location.getZ())) });
/*      */   }
/*      */   
/*      */   public static String formatPlayer(OfflinePlayer player)
/*      */   {
/*  192 */     return player.getName() + " (" + TFM_UuidManager.getUniqueId(player) + ")";
/*      */   }
/*      */   
/*      */   public static String toEscapedString(String ip)
/*      */   {
/*  209 */     return ip.trim().replaceAll("\\.", "_");
/*      */   }
/*      */   
/*      */   public static String fromEscapedString(String escapedIp)
/*      */   {
/*  226 */     return escapedIp.trim().replaceAll("_", "\\.");
/*      */   }
/*      */   
/*      */   public static void gotoWorld(Player player, String targetWorld)
/*      */   {
/*  231 */     if (player == null) {
/*  233 */       return;
/*      */     }
/*  236 */     if (player.getWorld().getName().equalsIgnoreCase(targetWorld))
/*      */     {
/*  238 */       playerMsg(player, "Going to main world.", ChatColor.GRAY);
/*  239 */       player.teleport(((World)Bukkit.getWorlds().get(0)).getSpawnLocation());
/*  240 */       return;
/*      */     }
/*  243 */     for (World world : Bukkit.getWorlds()) {
/*  245 */       if (world.getName().equalsIgnoreCase(targetWorld))
/*      */       {
/*  247 */         playerMsg(player, "Going to world: " + targetWorld, ChatColor.GRAY);
/*  248 */         player.teleport(world.getSpawnLocation());
/*  249 */         return;
/*      */       }
/*      */     }
/*  253 */     playerMsg(player, "World " + targetWorld + " not found.", ChatColor.GRAY);
/*      */   }
/*      */   
/*      */   public static String decolorize(String string)
/*      */   {
/*  258 */     return string.replaceAll("\\u00A7(?=[0-9a-fk-or])", "&");
/*      */   }
/*      */   
/*      */   public static void buildHistory(Location location, int length, TFM_PlayerData playerdata)
/*      */   {
/*  263 */     Block center = location.getBlock();
/*  264 */     for (int xOffset = -length; xOffset <= length; xOffset++) {
/*  266 */       for (int yOffset = -length; yOffset <= length; yOffset++) {
/*  268 */         for (int zOffset = -length; zOffset <= length; zOffset++)
/*      */         {
/*  270 */           Block block = center.getRelative(xOffset, yOffset, zOffset);
/*  271 */           playerdata.insertHistoryBlock(block.getLocation(), block.getType());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateCube(Location location, int length, Material material)
/*      */   {
/*  279 */     Block center = location.getBlock();
/*  280 */     for (int xOffset = -length; xOffset <= length; xOffset++) {
/*  282 */       for (int yOffset = -length; yOffset <= length; yOffset++) {
/*  284 */         for (int zOffset = -length; zOffset <= length; zOffset++)
/*      */         {
/*  286 */           Block block = center.getRelative(xOffset, yOffset, zOffset);
/*  287 */           if (block.getType() != material) {
/*  289 */             block.setType(material);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateHollowCube(Location location, int length, Material material)
/*      */   {
/*  298 */     Block center = location.getBlock();
/*  299 */     for (int xOffset = -length; xOffset <= length; xOffset++) {
/*  301 */       for (int yOffset = -length; yOffset <= length; yOffset++) {
/*  303 */         for (int zOffset = -length; zOffset <= length; zOffset++) {
/*  306 */           if ((Math.abs(xOffset) == length) || (Math.abs(yOffset) == length) || (Math.abs(zOffset) == length))
/*      */           {
/*  311 */             Block block = center.getRelative(xOffset, yOffset, zOffset);
/*  313 */             if (material != Material.SKULL)
/*      */             {
/*  316 */               if ((material != Material.GLASS) && (xOffset == 0) && (yOffset == 2) && (zOffset == 0)) {
/*  318 */                 block.setType(Material.GLOWSTONE);
/*      */               } else {
/*  322 */                 block.setType(material);
/*      */               }
/*      */             }
/*  326 */             else if ((Math.abs(xOffset) == length) && (Math.abs(yOffset) == length) && (Math.abs(zOffset) == length))
/*      */             {
/*  328 */               block.setType(Material.GLOWSTONE);
/*      */             }
/*      */             else
/*      */             {
/*  332 */               block.setType(Material.SKULL);
/*  333 */               Skull skull = (Skull)block.getState();
/*  334 */               skull.setSkullType(SkullType.PLAYER);
/*  335 */               skull.setOwner("Prozza");
/*  336 */               skull.update();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setWorldTime(World world, long ticks)
/*      */   {
/*  345 */     long time = world.getTime();
/*  346 */     time -= time % 24000L;
/*  347 */     world.setTime(time + 24000L + ticks);
/*      */   }
/*      */   
/*      */   public static void createDefaultConfiguration(String configFileName)
/*      */   {
/*  352 */     File targetFile = new File(TotalFreedomMod.plugin.getDataFolder(), configFileName);
/*  354 */     if (targetFile.exists()) {
/*  356 */       return;
/*      */     }
/*  359 */     TFM_Log.info("Installing default configuration file template: " + targetFile.getPath());
/*      */     try
/*      */     {
/*  363 */       InputStream configFileStream = TotalFreedomMod.plugin.getResource(configFileName);
/*  364 */       FileUtils.copyInputStreamToFile(configFileStream, targetFile);
/*  365 */       configFileStream.close();
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  369 */       TFM_Log.severe(ex);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean deleteFolder(File file)
/*      */   {
/*  375 */     if ((file.exists()) && (file.isDirectory())) {
/*  377 */       return FileUtils.deleteQuietly(file);
/*      */     }
/*  379 */     return false;
/*      */   }
/*      */   
/*      */   public static void deleteCoreDumps()
/*      */   {
/*  384 */     File[] coreDumps = new File(".").listFiles(new FileFilter()
/*      */     {
/*      */       public boolean accept(File file)
/*      */       {
/*  389 */         return file.getName().startsWith("java.core");
/*      */       }
/*      */     });
/*  393 */     for (File dump : coreDumps)
/*      */     {
/*  395 */       TFM_Log.info("Removing core dump file: " + dump.getName());
/*  396 */       dump.delete();
/*      */     }
/*      */   }
/*      */   
/*      */   public static EntityType getEntityType(String mobname)
/*      */     throws Exception
/*      */   {
/*  402 */     mobname = mobname.toLowerCase().trim();
/*  404 */     if (!mobtypes.containsKey(mobname)) {
/*  406 */       throw new Exception();
/*      */     }
/*  409 */     return (EntityType)mobtypes.get(mobname);
/*      */   }
/*      */   
/*      */   public static void copy(InputStream in, File file)
/*      */     throws IOException
/*      */   {
/*  421 */     if (!file.exists()) {
/*  423 */       file.getParentFile().mkdirs();
/*      */     }
/*  426 */     OutputStream out = new FileOutputStream(file);
/*  427 */     byte[] buf = new byte['Ѐ'];
/*      */     int len;
/*  429 */     while ((len = in.read(buf)) > 0) {
/*  431 */       out.write(buf, 0, len);
/*      */     }
/*  433 */     out.close();
/*  434 */     in.close();
/*      */   }
/*      */   
/*      */   public static File getPluginFile(Plugin plugin, String name)
/*      */   {
/*  446 */     return new File(plugin.getDataFolder(), name);
/*      */   }
/*      */   
/*      */   public static void autoEject(Player player, String kickMessage)
/*      */   {
/*  451 */     EjectMethod method = EjectMethod.STRIKE_ONE;
/*  452 */     String ip = getIp(player);
/*  454 */     if (!ejectTracker.containsKey(ip)) {
/*  456 */       ejectTracker.put(ip, Integer.valueOf(0));
/*      */     }
/*  459 */     int kicks = ((Integer)ejectTracker.get(ip)).intValue();
/*  460 */     kicks++;
/*      */     
/*  462 */     ejectTracker.put(ip, Integer.valueOf(kicks));
/*  464 */     if (kicks <= 1) {
/*  466 */       method = EjectMethod.STRIKE_ONE;
/*  468 */     } else if (kicks == 2) {
/*  470 */       method = EjectMethod.STRIKE_TWO;
/*  472 */     } else if (kicks >= 3) {
/*  474 */       method = EjectMethod.STRIKE_THREE;
/*      */     }
/*  477 */     TFM_Log.info("AutoEject -> name: " + player.getName() + " - player ip: " + ip + " - method: " + method.toString());
/*      */     
/*  479 */     player.setOp(false);
/*  480 */     player.setGameMode(GameMode.SURVIVAL);
/*  481 */     player.getInventory().clear();
/*  483 */     switch (method)
/*      */     {
/*      */     case STRIKE_ONE: 
/*  487 */       Calendar cal = new GregorianCalendar();
/*  488 */       cal.add(12, 1);
/*  489 */       Date expires = cal.getTime();
/*      */       
/*  491 */       bcastMsg(ChatColor.RED + player.getName() + " has been banned for 1 minute.");
/*      */       
/*  493 */       TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName(), "AutoEject", expires, kickMessage));
/*  494 */       TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), "AutoEject", expires, kickMessage));
/*  495 */       player.kickPlayer(kickMessage);
/*      */       
/*  497 */       break;
/*      */     case STRIKE_TWO: 
/*  501 */       Calendar c = new GregorianCalendar();
/*  502 */       c.add(12, 3);
/*  503 */       Date expires = c.getTime();
/*      */       
/*  505 */       bcastMsg(ChatColor.RED + player.getName() + " has been banned for 3 minutes.");
/*      */       
/*  507 */       TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName(), "AutoEject", expires, kickMessage));
/*  508 */       TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), "AutoEject", expires, kickMessage));
/*  509 */       player.kickPlayer(kickMessage);
/*  510 */       break;
/*      */     case STRIKE_THREE: 
/*  514 */       String[] ipAddressParts = ip.split("\\.");
/*      */       
/*  516 */       TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName(), "AutoEject", null, kickMessage));
/*  517 */       TFM_BanManager.addIpBan(new TFM_Ban(ipAddressParts[0] + "." + ipAddressParts[1] + ".*.*", player.getName(), "AutoEject", null, kickMessage));
/*  518 */       TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), "AutoEject", null, kickMessage));
/*      */       
/*  520 */       bcastMsg(ChatColor.RED + player.getName() + " has been banned.");
/*      */       
/*  522 */       player.kickPlayer(kickMessage);
/*  523 */       break;
/*      */     }
/*      */   }
/*      */   
/*      */   public static Date parseDateOffset(String time)
/*      */   {
/*  530 */     Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
/*      */     
/*  538 */     Matcher m = timePattern.matcher(time);
/*  539 */     int years = 0;
/*  540 */     int months = 0;
/*  541 */     int weeks = 0;
/*  542 */     int days = 0;
/*  543 */     int hours = 0;
/*  544 */     int minutes = 0;
/*  545 */     int seconds = 0;
/*  546 */     boolean found = false;
/*  547 */     while (m.find()) {
/*  549 */       if ((m.group() != null) && (!m.group().isEmpty()))
/*      */       {
/*  553 */         for (int i = 0; i < m.groupCount(); i++) {
/*  555 */           if ((m.group(i) != null) && (!m.group(i).isEmpty()))
/*      */           {
/*  557 */             found = true;
/*  558 */             break;
/*      */           }
/*      */         }
/*  561 */         if (found)
/*      */         {
/*  563 */           if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
/*  565 */             years = Integer.parseInt(m.group(1));
/*      */           }
/*  567 */           if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
/*  569 */             months = Integer.parseInt(m.group(2));
/*      */           }
/*  571 */           if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
/*  573 */             weeks = Integer.parseInt(m.group(3));
/*      */           }
/*  575 */           if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
/*  577 */             days = Integer.parseInt(m.group(4));
/*      */           }
/*  579 */           if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
/*  581 */             hours = Integer.parseInt(m.group(5));
/*      */           }
/*  583 */           if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
/*  585 */             minutes = Integer.parseInt(m.group(6));
/*      */           }
/*  587 */           if ((m.group(7) != null) && (!m.group(7).isEmpty())) {
/*  589 */             seconds = Integer.parseInt(m.group(7));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  594 */     if (!found) {
/*  596 */       return null;
/*      */     }
/*  599 */     Calendar c = new GregorianCalendar();
/*  601 */     if (years > 0) {
/*  603 */       c.add(1, years);
/*      */     }
/*  605 */     if (months > 0) {
/*  607 */       c.add(2, months);
/*      */     }
/*  609 */     if (weeks > 0) {
/*  611 */       c.add(3, weeks);
/*      */     }
/*  613 */     if (days > 0) {
/*  615 */       c.add(5, days);
/*      */     }
/*  617 */     if (hours > 0) {
/*  619 */       c.add(11, hours);
/*      */     }
/*  621 */     if (minutes > 0) {
/*  623 */       c.add(12, minutes);
/*      */     }
/*  625 */     if (seconds > 0) {
/*  627 */       c.add(13, seconds);
/*      */     }
/*  630 */     return c.getTime();
/*      */   }
/*      */   
/*      */   public static String playerListToNames(Set<OfflinePlayer> players)
/*      */   {
/*  635 */     List<String> names = new ArrayList();
/*  636 */     for (OfflinePlayer player : players) {
/*  638 */       names.add(player.getName());
/*      */     }
/*  640 */     return StringUtils.join(names, ", ");
/*      */   }
/*      */   
/*      */   public static Map<String, Boolean> getSavedFlags()
/*      */   {
/*  646 */     Map<String, Boolean> flags = null;
/*      */     
/*  648 */     File input = new File(TotalFreedomMod.plugin.getDataFolder(), "savedflags.dat");
/*  649 */     if (input.exists()) {
/*      */       try
/*      */       {
/*  653 */         FileInputStream fis = new FileInputStream(input);
/*  654 */         ObjectInputStream ois = new ObjectInputStream(fis);
/*  655 */         flags = (HashMap)ois.readObject();
/*  656 */         ois.close();
/*  657 */         fis.close();
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  661 */         TFM_Log.severe(ex);
/*      */       }
/*      */     }
/*  665 */     return flags;
/*      */   }
/*      */   
/*      */   public static boolean getSavedFlag(String flag)
/*      */     throws Exception
/*      */   {
/*  670 */     Boolean flagValue = null;
/*      */     
/*  672 */     Map<String, Boolean> flags = getSavedFlags();
/*  674 */     if (flags != null) {
/*  676 */       if (flags.containsKey(flag)) {
/*  678 */         flagValue = (Boolean)flags.get(flag);
/*      */       }
/*      */     }
/*  682 */     if (flagValue != null) {
/*  684 */       return flagValue.booleanValue();
/*      */     }
/*  688 */     throw new Exception();
/*      */   }
/*      */   
/*      */   public static void setSavedFlag(String flag, boolean value)
/*      */   {
/*  694 */     Map<String, Boolean> flags = getSavedFlags();
/*  696 */     if (flags == null) {
/*  698 */       flags = new HashMap();
/*      */     }
/*  701 */     flags.put(flag, Boolean.valueOf(value));
/*      */     try
/*      */     {
/*  705 */       FileOutputStream fos = new FileOutputStream(new File(TotalFreedomMod.plugin.getDataFolder(), "savedflags.dat"));
/*  706 */       ObjectOutputStream oos = new ObjectOutputStream(fos);
/*  707 */       oos.writeObject(flags);
/*  708 */       oos.close();
/*  709 */       fos.close();
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  713 */       TFM_Log.severe(ex);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void createBackups(String file)
/*      */   {
/*  719 */     createBackups(file, false);
/*      */   }
/*      */   
/*      */   public static void createBackups(String file, boolean onlyWeekly)
/*      */   {
/*  724 */     String save = file.split("\\.")[0];
/*  725 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "backup/backup.yml", false);
/*  726 */     config.load();
/*  729 */     if (!config.isInt(save + ".weekly"))
/*      */     {
/*  731 */       performBackup(file, "weekly");
/*  732 */       config.set(save + ".weekly", Long.valueOf(getUnixTime()));
/*      */     }
/*      */     else
/*      */     {
/*  736 */       int lastBackupWeekly = config.getInt(save + ".weekly");
/*  738 */       if (lastBackupWeekly + 604800 < getUnixTime())
/*      */       {
/*  740 */         performBackup(file, "weekly");
/*  741 */         config.set(save + ".weekly", Long.valueOf(getUnixTime()));
/*      */       }
/*      */     }
/*  745 */     if (onlyWeekly)
/*      */     {
/*  747 */       config.save();
/*  748 */       return;
/*      */     }
/*  752 */     if (!config.isInt(save + ".daily"))
/*      */     {
/*  754 */       performBackup(file, "daily");
/*  755 */       config.set(save + ".daily", Long.valueOf(getUnixTime()));
/*      */     }
/*      */     else
/*      */     {
/*  759 */       int lastBackupDaily = config.getInt(save + ".daily");
/*  761 */       if (lastBackupDaily + 86400 < getUnixTime())
/*      */       {
/*  763 */         performBackup(file, "daily");
/*  764 */         config.set(save + ".daily", Long.valueOf(getUnixTime()));
/*      */       }
/*      */     }
/*  768 */     config.save();
/*      */   }
/*      */   
/*      */   private static void performBackup(String file, String type)
/*      */   {
/*  773 */     TFM_Log.info("Backing up " + file + " to " + file + "." + type + ".bak");
/*  774 */     File backupFolder = new File(TotalFreedomMod.plugin.getDataFolder(), "backup");
/*  776 */     if (!backupFolder.exists()) {
/*  778 */       backupFolder.mkdirs();
/*      */     }
/*  781 */     File oldYaml = new File(TotalFreedomMod.plugin.getDataFolder(), file);
/*  782 */     File newYaml = new File(backupFolder, file + "." + type + ".bak");
/*  783 */     FileUtil.copy(oldYaml, newYaml);
/*      */   }
/*      */   
/*      */   public static String dateToString(Date date)
/*      */   {
/*  788 */     return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).format(date);
/*      */   }
/*      */   
/*      */   public static Date stringToDate(String dateString)
/*      */   {
/*      */     try
/*      */     {
/*  795 */       return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).parse(dateString);
/*      */     }
/*      */     catch (ParseException pex) {}
/*  799 */     return new Date(0L);
/*      */   }
/*      */   
/*      */   public static boolean isFromHostConsole(String senderName)
/*      */   {
/*  806 */     return TFM_ConfigEntry.HOST_SENDER_NAMES.getList().contains(senderName.toLowerCase());
/*      */   }
/*      */   
/*      */   public static List<String> removeDuplicates(List<String> oldList)
/*      */   {
/*  811 */     List<String> newList = new ArrayList();
/*  812 */     for (String entry : oldList) {
/*  814 */       if (!newList.contains(entry)) {
/*  816 */         newList.add(entry);
/*      */       }
/*      */     }
/*  819 */     return newList;
/*      */   }
/*      */   
/*      */   public static boolean fuzzyIpMatch(String a, String b, int octets)
/*      */   {
/*  824 */     boolean match = true;
/*      */     
/*  826 */     String[] aParts = a.split("\\.");
/*  827 */     String[] bParts = b.split("\\.");
/*  829 */     if ((aParts.length != 4) || (bParts.length != 4)) {
/*  831 */       return false;
/*      */     }
/*  834 */     if (octets > 4) {
/*  836 */       octets = 4;
/*  838 */     } else if (octets < 1) {
/*  840 */       octets = 1;
/*      */     }
/*  843 */     for (int i = 0; (i < octets) && (i < 4); i++) {
/*  845 */       if ((!aParts[i].equals("*")) && (!bParts[i].equals("*"))) {
/*  850 */         if (!aParts[i].equals(bParts[i]))
/*      */         {
/*  852 */           match = false;
/*  853 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  857 */     return match;
/*      */   }
/*      */   
/*      */   public static String getFuzzyIp(String ip)
/*      */   {
/*  862 */     String[] ipParts = ip.split("\\.");
/*  863 */     if (ipParts.length == 4) {
/*  865 */       return String.format("%s.%s.*.*", new Object[] { ipParts[0], ipParts[1] });
/*      */     }
/*  868 */     return ip;
/*      */   }
/*      */   
/*      */   public static int replaceBlocks(Location center, Material fromMaterial, Material toMaterial, int radius)
/*      */   {
/*  873 */     int affected = 0;
/*      */     
/*  875 */     Block centerBlock = center.getBlock();
/*  876 */     for (int xOffset = -radius; xOffset <= radius; xOffset++) {
/*  878 */       for (int yOffset = -radius; yOffset <= radius; yOffset++) {
/*  880 */         for (int zOffset = -radius; zOffset <= radius; zOffset++)
/*      */         {
/*  882 */           Block block = centerBlock.getRelative(xOffset, yOffset, zOffset);
/*  884 */           if (block.getType().equals(fromMaterial)) {
/*  886 */             if (block.getLocation().distanceSquared(center) < radius * radius)
/*      */             {
/*  888 */               block.setType(toMaterial);
/*  889 */               affected++;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  896 */     return affected;
/*      */   }
/*      */   
/*      */   public static void downloadFile(String url, File output)
/*      */     throws Exception
/*      */   {
/*  901 */     downloadFile(url, output, false);
/*      */   }
/*      */   
/*      */   public static void downloadFile(String url, File output, boolean verbose)
/*      */     throws Exception
/*      */   {
/*  906 */     URL website = new URL(url);
/*  907 */     ReadableByteChannel rbc = Channels.newChannel(website.openStream());
/*  908 */     FileOutputStream fos = new FileOutputStream(output);
/*  909 */     fos.getChannel().transferFrom(rbc, 0L, 16777216L);
/*  910 */     fos.close();
/*  912 */     if (verbose) {
/*  914 */       TFM_Log.info("Downloaded " + url + " to " + output.toString() + ".");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void adminChatMessage(CommandSender sender, String message, boolean senderIsConsole)
/*      */   {
/*  920 */     String name = sender.getName() + " " + TFM_PlayerRank.fromSender(sender).getPrefix() + ChatColor.WHITE;
/*  921 */     TFM_Log.info("[ADMIN] " + name + ": " + message);
/*  923 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  925 */       if (TFM_AdminList.isSuperAdmin(player)) {
/*  927 */         player.sendMessage("[" + ChatColor.AQUA + "ADMIN" + ChatColor.WHITE + "] " + ChatColor.DARK_RED + name + ": " + ChatColor.AQUA + message);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static <T> T getField(Object from, String name)
/*      */   {
/*  936 */     Class<?> checkClass = from.getClass();
/*      */     do
/*      */     {
/*      */       try
/*      */       {
/*  941 */         Field field = checkClass.getDeclaredField(name);
/*  942 */         field.setAccessible(true);
/*  943 */         return (T)field.get(from);
/*      */       }
/*      */       catch (NoSuchFieldException ex) {}catch (IllegalAccessException ex) {}
/*  954 */     } while ((checkClass.getSuperclass() != Object.class) && ((checkClass = checkClass.getSuperclass()) != null));
/*  956 */     return null;
/*      */   }
/*      */   
/*      */   public static ChatColor randomChatColor()
/*      */   {
/*  961 */     return (ChatColor)CHAT_COLOR_POOL.get(RANDOM.nextInt(CHAT_COLOR_POOL.size()));
/*      */   }
/*      */   
/*      */   public static String colorize(String string)
/*      */   {
/*  966 */     return ChatColor.translateAlternateColorCodes('&', string);
/*      */   }
/*      */   
/*      */   public static long getUnixTime()
/*      */   {
/*  971 */     return System.currentTimeMillis() / 1000L;
/*      */   }
/*      */   
/*      */   public static Date getUnixDate(long unix)
/*      */   {
/*  976 */     return new Date(unix * 1000L);
/*      */   }
/*      */   
/*      */   public static long getUnixTime(Date date)
/*      */   {
/*  981 */     if (date == null) {
/*  983 */       return 0L;
/*      */     }
/*  986 */     return date.getTime() / 1000L;
/*      */   }
/*      */   
/*      */   public static String getNmsVersion()
/*      */   {
/*  991 */     String packageName = Bukkit.getServer().getClass().getPackage().getName();
/*  992 */     return packageName.substring(packageName.lastIndexOf('.') + 1);
/*      */   }
/*      */   
/*      */   public static void reportAction(Player reporter, Player reported, String report)
/*      */   {
/*  998 */     for (Player player : ) {
/* 1000 */       if (TFM_AdminList.isSuperAdmin(player)) {
/* 1002 */         playerMsg(player, ChatColor.RED + "[REPORTS] " + ChatColor.GOLD + reporter.getName() + " has reported " + reported.getName() + " for " + report);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class TFM_EntityWiper
/*      */   {
/* 1009 */     private static final List<Class<? extends Entity>> WIPEABLES = new ArrayList();
/*      */     
/*      */     static
/*      */     {
/* 1013 */       WIPEABLES.add(EnderCrystal.class);
/* 1014 */       WIPEABLES.add(EnderSignal.class);
/* 1015 */       WIPEABLES.add(ExperienceOrb.class);
/* 1016 */       WIPEABLES.add(Projectile.class);
/* 1017 */       WIPEABLES.add(FallingBlock.class);
/* 1018 */       WIPEABLES.add(Firework.class);
/* 1019 */       WIPEABLES.add(Item.class);
/*      */     }
/*      */     
/*      */     private TFM_EntityWiper()
/*      */     {
/* 1024 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     private static boolean canWipe(Entity entity, boolean wipeExplosives, boolean wipeVehicles)
/*      */     {
/* 1029 */       if (wipeExplosives) {
/* 1031 */         if (Explosive.class.isAssignableFrom(entity.getClass())) {
/* 1033 */           return true;
/*      */         }
/*      */       }
/* 1037 */       if (wipeVehicles)
/*      */       {
/* 1039 */         if (Boat.class.isAssignableFrom(entity.getClass())) {
/* 1041 */           return true;
/*      */         }
/* 1043 */         if (Minecart.class.isAssignableFrom(entity.getClass())) {
/* 1045 */           return true;
/*      */         }
/*      */       }
/* 1049 */       Iterator<Class<? extends Entity>> it = WIPEABLES.iterator();
/* 1050 */       while (it.hasNext()) {
/* 1052 */         if (((Class)it.next()).isAssignableFrom(entity.getClass())) {
/* 1054 */           return true;
/*      */         }
/*      */       }
/* 1058 */       return false;
/*      */     }
/*      */     
/*      */     public static int wipeEntities(boolean wipeExplosives, boolean wipeVehicles)
/*      */     {
/* 1063 */       int removed = 0;
/*      */       
/* 1065 */       Iterator<World> worlds = Bukkit.getWorlds().iterator();
/* 1066 */       while (worlds.hasNext())
/*      */       {
/* 1068 */         Iterator<Entity> entities = ((World)worlds.next()).getEntities().iterator();
/* 1069 */         while (entities.hasNext())
/*      */         {
/* 1071 */           Entity entity = (Entity)entities.next();
/* 1072 */           if (canWipe(entity, wipeExplosives, wipeVehicles))
/*      */           {
/* 1074 */             entity.remove();
/* 1075 */             removed++;
/*      */           }
/*      */         }
/*      */       }
/* 1080 */       return removed;
/*      */     }
/*      */   }
/*      */   
/*      */   public static enum EjectMethod
/*      */   {
/* 1086 */     STRIKE_ONE,  STRIKE_TWO,  STRIKE_THREE;
/*      */     
/*      */     private EjectMethod() {}
/*      */   }
/*      */   
/*      */   public static class MethodTimer
/*      */   {
/*      */     private long lastStart;
/* 1092 */     private long total = 0L;
/*      */     
/*      */     public void start()
/*      */     {
/* 1100 */       lastStart = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     public void update()
/*      */     {
/* 1105 */       total += System.currentTimeMillis() - lastStart;
/*      */     }
/*      */     
/*      */     public long getTotal()
/*      */     {
/* 1110 */       return total;
/*      */     }
/*      */     
/*      */     public void printTotalToLog(String timerName)
/*      */     {
/* 1115 */       TFM_Log.info("DEBUG: " + timerName + " used " + getTotal() + " ms.");
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */