/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.Command_logs;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class TFM_AdminList
/*     */ {
/*  35 */   private static int cleanThreshold = 168;
/*     */   private static final Set<String> superIps;
/*     */   private static final Set<String> seniorConsoleNames;
/*     */   private static final Set<UUID> seniorUUIDs;
/*     */   private static final Set<UUID> telnetUUIDs;
/*     */   private static final Set<UUID> superUUIDs;
/*     */   private static final Map<UUID, TFM_Admin> adminList;
/*     */   
/*     */   static
/*     */   {
/*  39 */     adminList = new HashMap();
/*  40 */     superUUIDs = new HashSet();
/*  41 */     telnetUUIDs = new HashSet();
/*  42 */     seniorUUIDs = new HashSet();
/*  43 */     seniorConsoleNames = new HashSet();
/*  44 */     superIps = new HashSet();
/*     */   }
/*     */   
/*  46 */   public static final Function<Player, Boolean> SUPERADMIN_SERVICE = new Function()
/*     */   {
/*     */     public Boolean apply(Player f)
/*     */     {
/*  52 */       return Boolean.valueOf(TFM_AdminList.isSuperAdmin(f));
/*     */     }
/*     */   };
/*     */   
/*     */   private TFM_AdminList()
/*     */   {
/*  59 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static Set<UUID> getSuperUUIDs()
/*     */   {
/*  64 */     return Collections.unmodifiableSet(superUUIDs);
/*     */   }
/*     */   
/*     */   public static Set<UUID> getTelnetUUIDs()
/*     */   {
/*  69 */     return Collections.unmodifiableSet(telnetUUIDs);
/*     */   }
/*     */   
/*     */   public static Set<UUID> getSeniorUUIDs()
/*     */   {
/*  74 */     return Collections.unmodifiableSet(seniorUUIDs);
/*     */   }
/*     */   
/*     */   public static Set<String> getSeniorConsoleNames()
/*     */   {
/*  79 */     return Collections.unmodifiableSet(seniorConsoleNames);
/*     */   }
/*     */   
/*     */   public static Set<String> getSuperadminIps()
/*     */   {
/*  84 */     return Collections.unmodifiableSet(superIps);
/*     */   }
/*     */   
/*     */   public static Set<TFM_Admin> getAllAdmins()
/*     */   {
/*  89 */     return Sets.newHashSet(adminList.values());
/*     */   }
/*     */   
/*     */   public static Set<String> getSuperNames()
/*     */   {
/*  94 */     Set<String> names = new HashSet();
/*  96 */     for (TFM_Admin admin : adminList.values()) {
/*  98 */       if (admin.isActivated()) {
/* 103 */         names.add(admin.getLastLoginName());
/*     */       }
/*     */     }
/* 106 */     return Collections.unmodifiableSet(names);
/*     */   }
/*     */   
/*     */   public static Set<String> getLowercaseSuperNames()
/*     */   {
/* 111 */     Set<String> names = new HashSet();
/* 113 */     for (TFM_Admin admin : adminList.values()) {
/* 115 */       if (admin.isActivated()) {
/* 120 */         names.add(admin.getLastLoginName().toLowerCase());
/*     */       }
/*     */     }
/* 123 */     return Collections.unmodifiableSet(names);
/*     */   }
/*     */   
/*     */   public static void setUuid(TFM_Admin admin, UUID oldUuid, UUID newUuid)
/*     */   {
/* 128 */     if (!adminList.containsKey(oldUuid))
/*     */     {
/* 130 */       TFM_Log.warning("Could not set new UUID for admin " + admin.getLastLoginName() + ", admin is not loaded!");
/* 131 */       return;
/*     */     }
/* 134 */     if (oldUuid.equals(newUuid))
/*     */     {
/* 136 */       TFM_Log.warning("could not set new UUID for admin " + admin.getLastLoginName() + ", UUIDs match.");
/* 137 */       return;
/*     */     }
/* 141 */     TFM_Admin newAdmin = new TFM_Admin(newUuid, admin.getLastLoginName(), admin.getLastLogin(), admin.getCustomLoginMessage(), admin.isTelnetAdmin(), admin.isSeniorAdmin(), admin.isActivated());
/*     */     
/* 149 */     newAdmin.addIps(admin.getIps());
/* 150 */     adminList.put(newUuid, newAdmin);
/* 151 */     save(newAdmin);
/*     */     
/* 154 */     adminList.remove(oldUuid);
/* 155 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "superadmin.yml", true);
/* 156 */     config.load();
/* 157 */     config.set("admins." + oldUuid.toString(), null);
/* 158 */     config.save();
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/* 163 */     adminList.clear();
/*     */     
/* 165 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "superadmin.yml", true);
/* 166 */     config.load();
/*     */     
/* 168 */     cleanThreshold = config.getInt("clean_threshold_hours", cleanThreshold);
/* 171 */     if (config.isConfigurationSection("superadmins")) {
/* 173 */       parseOldConfig(config);
/*     */     }
/* 176 */     if (!config.isConfigurationSection("admins"))
/*     */     {
/* 178 */       TFM_Log.warning("Missing admins section in superadmin.yml.");
/* 179 */       return;
/*     */     }
/* 182 */     ConfigurationSection section = config.getConfigurationSection("admins");
/* 184 */     for (String uuidString : section.getKeys(false)) {
/* 186 */       if (!TFM_Util.isUniqueId(uuidString))
/*     */       {
/* 188 */         TFM_Log.warning("Invalid Unique ID: " + uuidString + " in superadmin.yml, ignoring");
/*     */       }
/*     */       else
/*     */       {
/* 192 */         UUID uuid = UUID.fromString(uuidString);
/*     */         
/* 194 */         TFM_Admin superadmin = new TFM_Admin(uuid, section.getConfigurationSection(uuidString));
/* 195 */         adminList.put(uuid, superadmin);
/*     */       }
/*     */     }
/* 198 */     updateIndexLists();
/*     */     
/* 200 */     TFM_Log.info("Loaded " + adminList.size() + " admins (" + superUUIDs.size() + " active) and " + superIps.size() + " IPs.");
/*     */   }
/*     */   
/*     */   public static void updateIndexLists()
/*     */   {
/* 205 */     superUUIDs.clear();
/* 206 */     telnetUUIDs.clear();
/* 207 */     seniorUUIDs.clear();
/* 208 */     seniorConsoleNames.clear();
/* 209 */     superIps.clear();
/* 211 */     for (TFM_Admin admin : adminList.values()) {
/* 213 */       if (admin.isActivated())
/*     */       {
/* 218 */         UUID uuid = admin.getUniqueId();
/*     */         
/* 220 */         superUUIDs.add(uuid);
/* 222 */         for (String ip : admin.getIps()) {
/* 224 */           superIps.add(ip);
/*     */         }
/* 227 */         if (admin.isTelnetAdmin()) {
/* 229 */           telnetUUIDs.add(uuid);
/*     */         }
/* 232 */         if (admin.isSeniorAdmin())
/*     */         {
/* 234 */           seniorUUIDs.add(uuid);
/*     */           
/* 236 */           seniorConsoleNames.add(admin.getLastLoginName());
/* 237 */           for (String alias : admin.getConsoleAliases()) {
/* 239 */             seniorConsoleNames.add(alias.toLowerCase());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 244 */     TFM_AdminWorld.getInstance().wipeAccessCache();
/*     */   }
/*     */   
/*     */   private static void parseOldConfig(TFM_Config config)
/*     */   {
/* 249 */     TFM_Log.info("Old superadmin configuration found, parsing...");
/*     */     
/* 251 */     ConfigurationSection section = config.getConfigurationSection("superadmins");
/*     */     
/* 253 */     int counter = 0;
/* 254 */     int errors = 0;
/* 256 */     for (String admin : config.getConfigurationSection("superadmins").getKeys(false))
/*     */     {
/* 258 */       UUID uuid = TFM_UuidManager.getUniqueId(admin);
/* 260 */       if (uuid == null)
/*     */       {
/* 262 */         errors++;
/* 263 */         TFM_Log.warning("Could not convert admin " + admin + ", UUID could not be found!");
/*     */       }
/*     */       else
/*     */       {
/* 267 */         config.set("admins." + uuid + ".last_login_name", uuid);
/* 268 */         config.set("admins." + uuid + ".is_activated", Boolean.valueOf(section.getBoolean(admin + ".is_activated")));
/* 269 */         config.set("admins." + uuid + ".is_telnet_admin", Boolean.valueOf(section.getBoolean(admin + ".is_telnet_admin")));
/* 270 */         config.set("admins." + uuid + ".is_senior_admin", Boolean.valueOf(section.getBoolean(admin + ".is_senior_admin")));
/* 271 */         config.set("admins." + uuid + ".last_login", section.getString(admin + ".last_login"));
/* 272 */         config.set("admins." + uuid + ".custom_login_message", section.getString(admin + ".custom_login_message"));
/* 273 */         config.set("admins." + uuid + ".console_aliases", section.getStringList(admin + ".console_aliases"));
/* 274 */         config.set("admins." + uuid + ".ips", section.getStringList(admin + ".ips"));
/*     */         
/* 276 */         counter++;
/*     */       }
/*     */     }
/* 279 */     config.set("superadmins", null);
/* 280 */     config.save();
/*     */     
/* 282 */     TFM_Log.info("Done! " + counter + " admins parsed, " + errors + " errors");
/*     */   }
/*     */   
/*     */   public static void saveAll()
/*     */   {
/* 287 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "superadmin.yml", true);
/* 288 */     config.load();
/*     */     
/* 290 */     config.set("clean_threshold_hours", Integer.valueOf(cleanThreshold));
/*     */     
/* 292 */     Iterator<Map.Entry<UUID, TFM_Admin>> it = adminList.entrySet().iterator();
/* 293 */     while (it.hasNext())
/*     */     {
/* 295 */       Map.Entry<UUID, TFM_Admin> pair = (Map.Entry)it.next();
/*     */       
/* 297 */       UUID uuid = (UUID)pair.getKey();
/* 298 */       TFM_Admin superadmin = (TFM_Admin)pair.getValue();
/*     */       
/* 300 */       config.set("admins." + uuid + ".last_login_name", superadmin.getLastLoginName());
/* 301 */       config.set("admins." + uuid + ".is_activated", Boolean.valueOf(superadmin.isActivated()));
/* 302 */       config.set("admins." + uuid + ".is_telnet_admin", Boolean.valueOf(superadmin.isTelnetAdmin()));
/* 303 */       config.set("admins." + uuid + ".is_senior_admin", Boolean.valueOf(superadmin.isSeniorAdmin()));
/* 304 */       config.set("admins." + uuid + ".last_login", TFM_Util.dateToString(superadmin.getLastLogin()));
/* 305 */       config.set("admins." + uuid + ".custom_login_message", superadmin.getCustomLoginMessage());
/* 306 */       config.set("admins." + uuid + ".console_aliases", TFM_Util.removeDuplicates(superadmin.getConsoleAliases()));
/* 307 */       config.set("admins." + uuid + ".ips", TFM_Util.removeDuplicates(superadmin.getIps()));
/*     */     }
/* 310 */     config.save();
/*     */   }
/*     */   
/*     */   public static void save(TFM_Admin admin)
/*     */   {
/* 315 */     if (!adminList.containsValue(admin))
/*     */     {
/* 317 */       TFM_Log.warning("Could not save admin " + admin.getLastLoginName() + ", admin is not loaded!");
/* 318 */       return;
/*     */     }
/* 321 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "superadmin.yml", true);
/* 322 */     config.load();
/*     */     
/* 324 */     UUID uuid = admin.getUniqueId();
/*     */     
/* 326 */     config.set("admins." + uuid + ".last_login_name", admin.getLastLoginName());
/* 327 */     config.set("admins." + uuid + ".is_activated", Boolean.valueOf(admin.isActivated()));
/* 328 */     config.set("admins." + uuid + ".is_telnet_admin", Boolean.valueOf(admin.isTelnetAdmin()));
/* 329 */     config.set("admins." + uuid + ".is_senior_admin", Boolean.valueOf(admin.isSeniorAdmin()));
/* 330 */     config.set("admins." + uuid + ".last_login", TFM_Util.dateToString(admin.getLastLogin()));
/* 331 */     config.set("admins." + uuid + ".custom_login_message", admin.getCustomLoginMessage());
/* 332 */     config.set("admins." + uuid + ".console_aliases", TFM_Util.removeDuplicates(admin.getConsoleAliases()));
/* 333 */     config.set("admins." + uuid + ".ips", TFM_Util.removeDuplicates(admin.getIps()));
/*     */     
/* 335 */     config.save();
/*     */   }
/*     */   
/*     */   public static TFM_Admin getEntry(Player player)
/*     */   {
/* 340 */     return getEntry(TFM_UuidManager.getUniqueId(player));
/*     */   }
/*     */   
/*     */   public static TFM_Admin getEntry(UUID uuid)
/*     */   {
/* 345 */     return (TFM_Admin)adminList.get(uuid);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static TFM_Admin getEntry(String name)
/*     */   {
/* 351 */     for (UUID uuid : adminList.keySet()) {
/* 353 */       if (((TFM_Admin)adminList.get(uuid)).getLastLoginName().equalsIgnoreCase(name)) {
/* 355 */         return (TFM_Admin)adminList.get(uuid);
/*     */       }
/*     */     }
/* 358 */     return null;
/*     */   }
/*     */   
/*     */   public static TFM_Admin getEntryByIp(String ip)
/*     */   {
/* 363 */     return getEntryByIp(ip, false);
/*     */   }
/*     */   
/*     */   public static TFM_Admin getEntryByIp(String needleIp, boolean fuzzy)
/*     */   {
/* 368 */     Iterator<Map.Entry<UUID, TFM_Admin>> it = adminList.entrySet().iterator();
/* 369 */     while (it.hasNext())
/*     */     {
/* 371 */       Map.Entry<UUID, TFM_Admin> pair = (Map.Entry)it.next();
/* 372 */       TFM_Admin superadmin = (TFM_Admin)pair.getValue();
/* 374 */       if (fuzzy) {
/* 376 */         for (String haystackIp : superadmin.getIps()) {
/* 378 */           if (TFM_Util.fuzzyIpMatch(needleIp, haystackIp, 3)) {
/* 380 */             return superadmin;
/*     */           }
/*     */         }
/* 386 */       } else if (superadmin.getIps().contains(needleIp)) {
/* 388 */         return superadmin;
/*     */       }
/*     */     }
/* 392 */     return null;
/*     */   }
/*     */   
/*     */   public static void updateLastLogin(Player player)
/*     */   {
/* 397 */     TFM_Admin admin = getEntry(player);
/* 398 */     if (admin == null) {
/* 400 */       return;
/*     */     }
/* 402 */     admin.setLastLogin(new Date());
/* 403 */     admin.setLastLoginName(player.getName());
/* 404 */     saveAll();
/*     */   }
/*     */   
/*     */   public static boolean isSuperAdminSafe(UUID uuid, String ip)
/*     */   {
/* 409 */     if ((TotalFreedomMod.server.getOnlineMode()) && (uuid != null)) {
/* 411 */       return getSuperUUIDs().contains(uuid);
/*     */     }
/* 414 */     TFM_Admin admin = getEntryByIp(ip);
/* 415 */     return (admin != null) && (admin.isActivated());
/*     */   }
/*     */   
/*     */   public static synchronized boolean isSuperAdminSync(CommandSender sender)
/*     */   {
/* 420 */     return isSuperAdmin(sender);
/*     */   }
/*     */   
/*     */   public static boolean isSuperAdmin(CommandSender sender)
/*     */   {
/* 425 */     if (!(sender instanceof Player)) {
/* 427 */       return true;
/*     */     }
/* 430 */     Player player = (Player)sender;
/* 432 */     if (superIps.contains(TFM_Util.getIp(player))) {
/* 434 */       return true;
/*     */     }
/* 437 */     if ((Bukkit.getOnlineMode()) && (superUUIDs.contains(TFM_UuidManager.getUniqueId(player)))) {
/* 439 */       return true;
/*     */     }
/* 442 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isTelnetAdmin(CommandSender sender, boolean verifySuperadmin)
/*     */   {
/* 447 */     if (verifySuperadmin) {
/* 449 */       if (!isSuperAdmin(sender)) {
/* 451 */         return false;
/*     */       }
/*     */     }
/* 455 */     if (!(sender instanceof Player)) {
/* 457 */       return true;
/*     */     }
/* 460 */     TFM_Admin entry = getEntry((Player)sender);
/* 461 */     if (entry != null) {
/* 463 */       return entry.isTelnetAdmin();
/*     */     }
/* 466 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isSeniorAdmin(CommandSender sender)
/*     */   {
/* 471 */     return isSeniorAdmin(sender, false);
/*     */   }
/*     */   
/*     */   public static boolean isSeniorAdmin(CommandSender sender, boolean verifySuperadmin)
/*     */   {
/* 476 */     if (verifySuperadmin) {
/* 478 */       if (!isSuperAdmin(sender)) {
/* 480 */         return false;
/*     */       }
/*     */     }
/* 484 */     if (!(sender instanceof Player)) {
/* 486 */       return (seniorConsoleNames.contains(sender.getName())) || ((TFM_MainConfig.getBoolean(TFM_ConfigEntry.CONSOLE_IS_SENIOR).booleanValue()) && (sender.getName().equals("CONSOLE")));
/*     */     }
/* 490 */     TFM_Admin entry = getEntry((Player)sender);
/* 491 */     if (entry != null) {
/* 493 */       return entry.isSeniorAdmin();
/*     */     }
/* 496 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isIdentityMatched(Player player)
/*     */   {
/* 501 */     if (!isSuperAdmin(player)) {
/* 503 */       return false;
/*     */     }
/* 506 */     if (Bukkit.getOnlineMode()) {
/* 508 */       return true;
/*     */     }
/* 511 */     TFM_Admin entry = getEntry(player);
/* 512 */     if (entry == null) {
/* 514 */       return false;
/*     */     }
/* 517 */     return entry.getUniqueId().equals(TFM_UuidManager.getUniqueId(player));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static boolean checkPartialSuperadminIp(String ip, String name)
/*     */   {
/* 523 */     ip = ip.trim();
/* 525 */     if (superIps.contains(ip)) {
/* 527 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 532 */       String matchIp = null;
/* 533 */       for (String testIp : superIps) {
/* 535 */         if (TFM_Util.fuzzyIpMatch(ip, testIp, 3))
/*     */         {
/* 537 */           matchIp = testIp;
/* 538 */           break;
/*     */         }
/*     */       }
/* 542 */       if (matchIp != null)
/*     */       {
/* 544 */         TFM_Admin entry = getEntryByIp(matchIp);
/* 546 */         if (entry == null) {
/* 548 */           return true;
/*     */         }
/* 551 */         if (entry.getLastLoginName().equalsIgnoreCase(name))
/*     */         {
/* 553 */           if (!entry.getIps().contains(ip)) {
/* 555 */             entry.addIp(ip);
/*     */           }
/* 557 */           saveAll();
/*     */         }
/* 559 */         return true;
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 565 */       TFM_Log.severe(ex);
/*     */     }
/* 568 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAdminImpostor(Player player)
/*     */   {
/* 573 */     if (superUUIDs.contains(TFM_UuidManager.getUniqueId(player))) {
/* 575 */       return !isSuperAdmin(player);
/*     */     }
/* 578 */     return false;
/*     */   }
/*     */   
/*     */   public static void addSuperadmin(OfflinePlayer player)
/*     */   {
/* 583 */     UUID uuid = TFM_UuidManager.getUniqueId(player);
/* 584 */     String ip = TFM_Util.getIp(player);
/* 585 */     boolean canSuperIp = !TFM_MainConfig.getList(TFM_ConfigEntry.NOADMIN_IPS).contains(ip);
/* 587 */     if (adminList.containsKey(uuid))
/*     */     {
/* 589 */       TFM_Admin superadmin = (TFM_Admin)adminList.get(uuid);
/* 590 */       superadmin.setActivated(true);
/* 592 */       if (player.isOnline())
/*     */       {
/* 594 */         superadmin.setLastLogin(new Date());
/* 596 */         if ((ip != null) && (canSuperIp)) {
/* 598 */           superadmin.addIp(ip);
/*     */         }
/*     */       }
/* 602 */       saveAll();
/* 603 */       updateIndexLists();
/* 604 */       return;
/*     */     }
/* 607 */     if (ip == null)
/*     */     {
/* 609 */       TFM_Log.severe("Could not add superadmin: " + TFM_Util.formatPlayer(player));
/* 610 */       TFM_Log.severe("Could not retrieve IP!");
/* 611 */       return;
/*     */     }
/* 614 */     if (!canSuperIp)
/*     */     {
/* 616 */       TFM_Log.warning("Could not add superadmin: " + TFM_Util.formatPlayer(player));
/* 617 */       TFM_Log.warning("IP " + ip + " may not be supered.");
/* 618 */       return;
/*     */     }
/* 621 */     TFM_Admin superadmin = new TFM_Admin(uuid, player.getName(), new Date(), "", false, false, true);
/*     */     
/* 629 */     superadmin.addIp(ip);
/*     */     
/* 631 */     adminList.put(uuid, superadmin);
/*     */     
/* 633 */     saveAll();
/* 634 */     updateIndexLists();
/*     */   }
/*     */   
/*     */   public static void removeSuperadmin(OfflinePlayer player)
/*     */   {
/* 639 */     UUID uuid = TFM_UuidManager.getUniqueId(player);
/* 641 */     if (!adminList.containsKey(uuid))
/*     */     {
/* 643 */       TFM_Log.warning("Could not remove admin: " + TFM_Util.formatPlayer(player));
/* 644 */       TFM_Log.warning("Player is not an admin!");
/* 645 */       return;
/*     */     }
/* 648 */     TFM_Admin superadmin = (TFM_Admin)adminList.get(uuid);
/* 649 */     superadmin.setActivated(false);
/* 650 */     Command_logs.deactivateSuperadmin(superadmin);
/*     */     
/* 652 */     saveAll();
/* 653 */     updateIndexLists();
/*     */   }
/*     */   
/*     */   public static void cleanSuperadminList(boolean verbose)
/*     */   {
/* 658 */     Iterator<Map.Entry<UUID, TFM_Admin>> it = adminList.entrySet().iterator();
/* 659 */     while (it.hasNext())
/*     */     {
/* 661 */       Map.Entry<UUID, TFM_Admin> pair = (Map.Entry)it.next();
/* 662 */       TFM_Admin superadmin = (TFM_Admin)pair.getValue();
/* 664 */       if ((superadmin.isActivated()) && (!superadmin.isSeniorAdmin()))
/*     */       {
/* 669 */         Date lastLogin = superadmin.getLastLogin();
/* 670 */         long lastLoginHours = TimeUnit.HOURS.convert(new Date().getTime() - lastLogin.getTime(), TimeUnit.MILLISECONDS);
/* 672 */         if (lastLoginHours > cleanThreshold)
/*     */         {
/* 674 */           if (verbose) {
/* 676 */             TFM_Util.adminAction("TotalFreedomMod", "Deactivating superadmin " + superadmin.getLastLoginName() + ", inactive for " + lastLoginHours + " hours.", true);
/*     */           }
/* 679 */           superadmin.setActivated(false);
/* 680 */           Command_logs.deactivateSuperadmin(superadmin);
/* 681 */           TFM_TwitterHandler.delTwitter(superadmin.getLastLoginName());
/*     */         }
/*     */       }
/*     */     }
/* 685 */     saveAll();
/* 686 */     updateIndexLists();
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_AdminList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */