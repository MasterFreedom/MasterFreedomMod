/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import net.minecraft.server.v1_8_R2.EntityPlayer;
/*     */ import net.minecraft.server.v1_8_R2.MinecraftServer;
/*     */ import net.minecraft.server.v1_8_R2.PlayerList;
/*     */ import net.minecraft.server.v1_8_R2.PropertyManager;
/*     */ import net.minecraft.server.v1_8_R2.WhiteList;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
/*     */ import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
/*     */ import org.bukkit.event.player.PlayerLoginEvent;
/*     */ import org.bukkit.event.player.PlayerLoginEvent.Result;
/*     */ 
/*     */ public class TFM_ServerInterface
/*     */ {
/*     */   public static final String COMPILE_NMS_VERSION = "v1_8_R2";
/*  22 */   public static final Pattern USERNAME_REGEX = Pattern.compile("^[\\w\\d_]{3,20}$");
/*     */   
/*     */   public static void setOnlineMode(boolean mode)
/*     */   {
/*  26 */     PropertyManager manager = MinecraftServer.getServer().getPropertyManager();
/*  27 */     manager.setProperty("online-mode", Boolean.valueOf(mode));
/*  28 */     manager.savePropertiesFile();
/*     */   }
/*     */   
/*     */   public static int purgeWhitelist()
/*     */   {
/*  33 */     String[] whitelisted = MinecraftServer.getServer().getPlayerList().getWhitelisted();
/*  34 */     int size = whitelisted.length;
/*  35 */     for (EntityPlayer player : getServergetPlayerListplayers) {
/*  37 */       MinecraftServer.getServer().getPlayerList().getWhitelist().remove(player.getProfile());
/*     */     }
/*     */     try
/*     */     {
/*  42 */       MinecraftServer.getServer().getPlayerList().getWhitelist().save();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  46 */       TFM_Log.warning("Could not purge the whitelist!");
/*  47 */       TFM_Log.warning(ex);
/*     */     }
/*  49 */     return size;
/*     */   }
/*     */   
/*     */   public static boolean isWhitelisted()
/*     */   {
/*  54 */     return MinecraftServer.getServer().getPlayerList().getHasWhitelist();
/*     */   }
/*     */   
/*     */   public static List<?> getWhitelisted()
/*     */   {
/*  59 */     return Arrays.asList(MinecraftServer.getServer().getPlayerList().getWhitelisted());
/*     */   }
/*     */   
/*     */   public static String getVersion()
/*     */   {
/*  64 */     return MinecraftServer.getServer().getVersion();
/*     */   }
/*     */   
/*     */   public static void handlePlayerPreLogin(AsyncPlayerPreLoginEvent event)
/*     */   {
/*  69 */     String ip = event.getAddress().getHostAddress().trim();
/*  70 */     boolean isAdmin = TFM_AdminList.isSuperAdminSafe(null, ip);
/*  73 */     for (Player onlinePlayer : TotalFreedomMod.server.getOnlinePlayers()) {
/*  75 */       if (onlinePlayer.getName().equalsIgnoreCase(event.getName()))
/*     */       {
/*  80 */         if (!isAdmin)
/*     */         {
/*  81 */           event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Your username is already logged into this server.");
/*     */         }
/*     */         else
/*     */         {
/*  83 */           event.allow();
/*  84 */           TFM_Sync.playerKick(onlinePlayer, "An admin just logged in with the username you are using.");
/*     */         }
/*  86 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void handlePlayerLogin(PlayerLoginEvent event)
/*     */   {
/*  92 */     Server server = TotalFreedomMod.server;
/*  93 */     Player player = event.getPlayer();
/*  94 */     String username = player.getName();
/*  95 */     String ip = event.getAddress().getHostAddress().trim();
/*  96 */     UUID uuid = TFM_UuidManager.newPlayer(player, ip);
/*  99 */     if ((username.length() < 3) || (username.length() > 20))
/*     */     {
/* 101 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your username is an invalid length (must be between 3 and 20 characters long).");
/* 102 */       return;
/*     */     }
/* 106 */     if (!USERNAME_REGEX.matcher(username).find())
/*     */     {
/* 108 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your username contains invalid characters.");
/* 109 */       return;
/*     */     }
/* 113 */     if (TFM_ConfigEntry.FORCE_IP_ENABLED.getBoolean().booleanValue())
/*     */     {
/* 115 */       String hostname = event.getHostname().replace("FML", "");
/* 116 */       String connectAddress = TFM_ConfigEntry.SERVER_ADDRESS.getString();
/* 117 */       int connectPort = TotalFreedomMod.server.getPort();
/* 119 */       if ((!hostname.equalsIgnoreCase(connectAddress + ":" + connectPort)) && (!hostname.equalsIgnoreCase(connectAddress + ".:" + connectPort)))
/*     */       {
/* 121 */         int forceIpPort = TFM_ConfigEntry.FORCE_IP_PORT.getInteger().intValue();
/* 122 */         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, TFM_ConfigEntry.FORCE_IP_KICKMSG.getString().replace("%address%", TFM_ConfigEntry.SERVER_ADDRESS.getString() + (forceIpPort == 25565 ? "" : new StringBuilder().append(":").append(forceIpPort).toString())));
/*     */         
/* 125 */         return;
/*     */       }
/*     */     }
/* 132 */     boolean isAdmin = TFM_AdminList.isSuperAdminSafe(uuid, ip);
/* 135 */     if (isAdmin)
/*     */     {
/* 138 */       event.allow();
/*     */       
/* 140 */       int count = server.getOnlinePlayers().size();
/* 141 */       if (count >= server.getMaxPlayers()) {
/* 143 */         for (Player onlinePlayer : server.getOnlinePlayers())
/*     */         {
/* 145 */           if (!TFM_AdminList.isSuperAdmin(onlinePlayer))
/*     */           {
/* 147 */             onlinePlayer.kickPlayer("You have been kicked to free up room for an admin.");
/* 148 */             count--;
/*     */           }
/* 151 */           if (count < server.getMaxPlayers()) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/* 158 */       if (count >= server.getMaxPlayers())
/*     */       {
/* 160 */         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The server is full and a player could not be kicked, sorry!");
/* 161 */         return;
/*     */       }
/* 164 */       return;
/*     */     }
/* 169 */     if (server.getOnlinePlayers().size() >= server.getMaxPlayers())
/*     */     {
/* 171 */       event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Sorry, but this server is full.");
/* 172 */       return;
/*     */     }
/* 176 */     if (TFM_ConfigEntry.ADMIN_ONLY_MODE.getBoolean().booleanValue())
/*     */     {
/* 178 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is temporarily open to admins only.");
/* 179 */       return;
/*     */     }
/* 183 */     if (TotalFreedomMod.lockdownEnabled)
/*     */     {
/* 185 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is currently in lockdown mode.");
/* 186 */       return;
/*     */     }
/* 190 */     if (isWhitelisted()) {
/* 192 */       if (!getWhitelisted().contains(username.toLowerCase()))
/*     */       {
/* 194 */         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You are not whitelisted on this server.");
/* 195 */         return;
/*     */       }
/*     */     }
/* 200 */     if (TFM_BanManager.isUuidBanned(uuid))
/*     */     {
/* 202 */       TFM_Ban ban = TFM_BanManager.getByUuid(uuid);
/* 203 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ban.getKickMessage());
/* 204 */       return;
/*     */     }
/* 208 */     if (TFM_BanManager.isIpBanned(ip))
/*     */     {
/* 210 */       TFM_Ban ban = TFM_BanManager.getByIp(ip);
/* 211 */       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ban.getKickMessage());
/* 212 */       return;
/*     */     }
/* 216 */     for (String testIp : TFM_PermbanList.getPermbannedIps()) {
/* 218 */       if (TFM_Util.fuzzyIpMatch(testIp, ip, 4))
/*     */       {
/* 220 */         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Your IP address is permanently banned from this server.\n" + "Release procedures are available at\n" + ChatColor.GOLD + TFM_ConfigEntry.SERVER_PERMBAN_URL.getString());
/*     */         
/* 224 */         return;
/*     */       }
/*     */     }
/* 229 */     for (String testPlayer : TFM_PermbanList.getPermbannedPlayers()) {
/* 231 */       if (testPlayer.equalsIgnoreCase(username))
/*     */       {
/* 233 */         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Your username is permanently banned from this server.\n" + "Release procedures are available at\n" + ChatColor.GOLD + TFM_ConfigEntry.SERVER_PERMBAN_URL.getString());
/*     */         
/* 237 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_ServerInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */