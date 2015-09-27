/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_TwitterHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Manage superadmins.", usage="/<command> <list | clean | clearme [ip] | <add | delete | info> <username>>")
/*     */ public class Command_saconfig
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*     */     SAConfigMode mode;
/*     */     try
/*     */     {
/*  27 */       mode = SAConfigMode.findMode(args, sender, senderIsConsole);
/*     */     }
/*     */     catch (PermissionsException ex)
/*     */     {
/*  31 */       playerMsg(ex.getMessage());
/*  32 */       return true;
/*     */     }
/*     */     catch (FormatException ex)
/*     */     {
/*  36 */       playerMsg(ex.getMessage());
/*  37 */       return false;
/*     */     }
/*  40 */     switch (mode)
/*     */     {
/*     */     case LIST: 
/*  44 */       playerMsg("Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "), ChatColor.GOLD);
/*     */       
/*  46 */       break;
/*     */     case CLEAN: 
/*  50 */       TFM_Util.adminAction(sender.getName(), "Cleaning superadmin list", true);
/*  51 */       TFM_AdminList.cleanSuperadminList(true);
/*  52 */       playerMsg("Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "), ChatColor.YELLOW);
/*     */       
/*  54 */       break;
/*     */     case CLEARME: 
/*  58 */       TFM_Admin admin = TFM_AdminList.getEntry(sender_p);
/*  60 */       if (admin == null)
/*     */       {
/*  62 */         playerMsg("Could not find your admin entry! Please notify a developer.", ChatColor.RED);
/*  63 */         return true;
/*     */       }
/*  66 */       String ip = TFM_Util.getIp(sender_p);
/*  68 */       if (args.length == 1)
/*     */       {
/*  70 */         TFM_Util.adminAction(sender.getName(), "Cleaning my supered IPs", true);
/*     */         
/*  72 */         int counter = admin.getIps().size() - 1;
/*  73 */         admin.clearIPs();
/*  74 */         admin.addIp(ip);
/*     */         
/*  76 */         TFM_AdminList.saveAll();
/*     */         
/*  78 */         playerMsg(counter + " IPs removed.");
/*  79 */         playerMsg((String)admin.getIps().get(0) + " is now your only IP address");
/*     */       }
/*  83 */       else if (!admin.getIps().contains(args[1]))
/*     */       {
/*  85 */         playerMsg("That IP is not registered to you.");
/*     */       }
/*  87 */       else if (ip.equals(args[1]))
/*     */       {
/*  89 */         playerMsg("You cannot remove your current IP.");
/*     */       }
/*     */       else
/*     */       {
/*  93 */         TFM_Util.adminAction(sender.getName(), "Removing a supered IP", true);
/*     */         
/*  95 */         admin.removeIp(args[1]);
/*     */         
/*  97 */         TFM_AdminList.saveAll();
/*     */         
/*  99 */         playerMsg("Removed IP " + args[1]);
/* 100 */         playerMsg("Current IPs: " + StringUtils.join(admin.getIps(), ", "));
/*     */       }
/* 104 */       break;
/*     */     case INFO: 
/* 108 */       TFM_Admin superadmin = TFM_AdminList.getEntry(args[1].toLowerCase());
/* 110 */       if (superadmin == null)
/*     */       {
/* 112 */         Player player = getPlayer(args[1]);
/* 113 */         if (player != null) {
/* 115 */           superadmin = TFM_AdminList.getEntry(player.getName().toLowerCase());
/*     */         }
/*     */       }
/* 119 */       if (superadmin == null) {
/* 121 */         playerMsg("Superadmin not found: " + args[1]);
/*     */       } else {
/* 125 */         playerMsg(superadmin.toString());
/*     */       }
/* 128 */       break;
/*     */     case ADD: 
/* 132 */       OfflinePlayer player = getPlayer(args[1], true);
/* 134 */       if (player == null)
/*     */       {
/* 136 */         TFM_Admin superadmin = TFM_AdminList.getEntry(args[1]);
/* 138 */         if (superadmin == null)
/*     */         {
/* 140 */           playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 141 */           return true;
/*     */         }
/* 144 */         player = TFM_DepreciationAggregator.getOfflinePlayer(server, superadmin.getLastLoginName());
/*     */       }
/* 147 */       TFM_Util.adminAction(sender.getName(), "Adding " + player.getName() + " to the superadmin list", true);
/* 148 */       TFM_AdminList.addSuperadmin(player);
/* 150 */       if (player.isOnline())
/*     */       {
/* 152 */         TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player.getPlayer());
/* 154 */         if (playerdata.isFrozen())
/*     */         {
/* 156 */           playerdata.setFrozen(false);
/* 157 */           playerMsg(player.getPlayer(), "You have been unfrozen.");
/*     */         }
/*     */       }
/* 159 */       break;
/*     */     case DELETE: 
/* 165 */       String targetName = args[1];
/*     */       
/* 167 */       Player player = getPlayer(targetName, true);
/* 169 */       if (player != null) {
/* 171 */         targetName = player.getName();
/*     */       }
/* 174 */       if (!TFM_AdminList.getLowercaseSuperNames().contains(targetName.toLowerCase()))
/*     */       {
/* 176 */         playerMsg("Superadmin not found: " + targetName);
/* 177 */         return true;
/*     */       }
/* 180 */       TFM_Util.adminAction(sender.getName(), "Removing " + targetName + " from the superadmin list", true);
/* 181 */       TFM_AdminList.removeSuperadmin(TFM_DepreciationAggregator.getOfflinePlayer(server, targetName));
/* 184 */       if (TFM_ConfigEntry.TWITTERBOT_ENABLED.getBoolean().booleanValue()) {
/* 186 */         TFM_TwitterHandler.delTwitterVerbose(targetName, sender);
/*     */       }
/*     */       break;
/*     */     }
/* 193 */     return true;
/*     */   }
/*     */   
/*     */   private static enum SAConfigMode
/*     */   {
/* 198 */     LIST("list", AdminLevel.OP, SourceType.BOTH, 1, 1),  CLEAN("clean", AdminLevel.SENIOR, SourceType.BOTH, 1, 1),  CLEARME("clearme", AdminLevel.SUPER, SourceType.ONLY_IN_GAME, 1, 2),  INFO("info", AdminLevel.SUPER, SourceType.BOTH, 2, 2),  ADD("add", AdminLevel.SUPER, SourceType.ONLY_CONSOLE, 2, 2),  DELETE("delete", AdminLevel.SENIOR, SourceType.ONLY_CONSOLE, 2, 2);
/*     */     
/*     */     private final String modeName;
/*     */     private final AdminLevel adminLevel;
/*     */     private final SourceType sourceType;
/*     */     private final int minArgs;
/*     */     private final int maxArgs;
/*     */     
/*     */     private SAConfigMode(String modeName, AdminLevel adminLevel, SourceType sourceType, int minArgs, int maxArgs)
/*     */     {
/* 212 */       this.modeName = modeName;
/* 213 */       this.adminLevel = adminLevel;
/* 214 */       this.sourceType = sourceType;
/* 215 */       this.minArgs = minArgs;
/* 216 */       this.maxArgs = maxArgs;
/*     */     }
/*     */     
/*     */     private static SAConfigMode findMode(String[] args, CommandSender sender, boolean senderIsConsole)
/*     */       throws Command_saconfig.PermissionsException, Command_saconfig.FormatException
/*     */     {
/* 221 */       if (args.length == 0) {
/* 223 */         throw new Command_saconfig.FormatException("Invalid number of arguments.");
/*     */       }
/* 226 */       boolean isSuperAdmin = TFM_AdminList.isSuperAdmin(sender);
/* 227 */       boolean isSeniorAdmin = isSuperAdmin ? TFM_AdminList.isSeniorAdmin(sender, false) : false;
/* 229 */       for (SAConfigMode mode : values()) {
/* 231 */         if (modeName.equalsIgnoreCase(args[0]))
/*     */         {
/* 233 */           if (adminLevel == AdminLevel.SUPER)
/*     */           {
/* 235 */             if (!isSuperAdmin) {
/* 237 */               throw new Command_saconfig.PermissionsException(TFM_Command.MSG_NO_PERMS);
/*     */             }
/*     */           }
/* 240 */           else if (adminLevel == AdminLevel.SENIOR) {
/* 242 */             if (!isSeniorAdmin) {
/* 244 */               throw new Command_saconfig.PermissionsException(TFM_Command.MSG_NO_PERMS);
/*     */             }
/*     */           }
/* 248 */           if (sourceType == SourceType.ONLY_IN_GAME)
/*     */           {
/* 250 */             if (senderIsConsole) {
/* 252 */               throw new Command_saconfig.PermissionsException("This command may only be used in-game.");
/*     */             }
/*     */           }
/* 255 */           else if (sourceType == SourceType.ONLY_CONSOLE) {
/* 257 */             if (!senderIsConsole) {
/* 259 */               throw new Command_saconfig.PermissionsException("This command may only be used from the console.");
/*     */             }
/*     */           }
/* 263 */           if ((args.length >= minArgs) && (args.length <= maxArgs)) {
/* 265 */             return mode;
/*     */           }
/* 269 */           throw new Command_saconfig.FormatException("Invalid number of arguments for mode: " + modeName);
/*     */         }
/*     */       }
/* 274 */       throw new Command_saconfig.FormatException("Invalid mode.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermissionsException
/*     */     extends Exception
/*     */   {
/*     */     public PermissionsException(String message)
/*     */     {
/* 282 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FormatException
/*     */     extends Exception
/*     */   {
/*     */     public FormatException(String message)
/*     */     {
/* 290 */       super();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_saconfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */