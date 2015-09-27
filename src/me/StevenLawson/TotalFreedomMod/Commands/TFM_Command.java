/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public abstract class TFM_Command
/*     */ {
/*  17 */   public static final String MSG_NO_PERMS = ChatColor.YELLOW + "You do not have permission to use this command.";
/*  18 */   public static final String YOU_ARE_OP = ChatColor.YELLOW + "You are now op!";
/*  19 */   public static final String YOU_ARE_NOT_OP = ChatColor.YELLOW + "You are no longer op!";
/*     */   public static final String NOT_FROM_CONSOLE = "This command may not be used from the console.";
/*  21 */   public static final String PLAYER_NOT_FOUND = ChatColor.GRAY + "Player not found!";
/*     */   protected TotalFreedomMod plugin;
/*     */   protected Server server;
/*     */   private CommandSender commandSender;
/*     */   private Class<?> commandClass;
/*     */   
/*     */   public abstract boolean run(CommandSender paramCommandSender, Player paramPlayer, Command paramCommand, String paramString, String[] paramArrayOfString, boolean paramBoolean);
/*     */   
/*     */   public void setup(TotalFreedomMod plugin, CommandSender commandSender, Class<?> commandClass)
/*     */   {
/*  35 */     this.plugin = plugin;
/*  36 */     server = plugin.getServer();
/*  37 */     this.commandSender = commandSender;
/*  38 */     this.commandClass = commandClass;
/*     */   }
/*     */   
/*     */   public void playerMsg(CommandSender sender, String message, ChatColor color)
/*     */   {
/*  43 */     if (sender == null) {
/*  45 */       return;
/*     */     }
/*  47 */     sender.sendMessage(color + message);
/*     */   }
/*     */   
/*     */   public void playerMsg(String message, ChatColor color)
/*     */   {
/*  52 */     playerMsg(commandSender, message, color);
/*     */   }
/*     */   
/*     */   public void playerMsg(CommandSender sender, String message)
/*     */   {
/*  57 */     playerMsg(sender, message, ChatColor.GRAY);
/*     */   }
/*     */   
/*     */   public void playerMsg(String message)
/*     */   {
/*  62 */     playerMsg(commandSender, message);
/*     */   }
/*     */   
/*     */   public boolean senderHasPermission()
/*     */   {
/*  67 */     CommandPermissions permissions = (CommandPermissions)commandClass.getAnnotation(CommandPermissions.class);
/*  69 */     if (permissions == null)
/*     */     {
/*  71 */       TFM_Log.warning(commandClass.getName() + " is missing permissions annotation.");
/*  72 */       return true;
/*     */     }
/*  75 */     boolean isSuper = TFM_AdminList.isSuperAdmin(commandSender);
/*  76 */     boolean isSenior = false;
/*  78 */     if (isSuper) {
/*  80 */       isSenior = TFM_AdminList.isSeniorAdmin(commandSender);
/*     */     }
/*  83 */     AdminLevel level = permissions.level();
/*  84 */     SourceType source = permissions.source();
/*  85 */     boolean blockHostConsole = permissions.blockHostConsole();
/*  87 */     if (!(commandSender instanceof Player))
/*     */     {
/*  89 */       if (source == SourceType.ONLY_IN_GAME) {
/*  91 */         return false;
/*     */       }
/*  94 */       if ((level == AdminLevel.SENIOR) && (!isSenior)) {
/*  96 */         return false;
/*     */       }
/*  99 */       if ((blockHostConsole) && (TFM_Util.isFromHostConsole(commandSender.getName()))) {
/* 101 */         return false;
/*     */       }
/* 104 */       return true;
/*     */     }
/* 107 */     Player senderPlayer = (Player)commandSender;
/* 109 */     if (source == SourceType.ONLY_CONSOLE) {
/* 111 */       return false;
/*     */     }
/* 114 */     if (level == AdminLevel.SENIOR)
/*     */     {
/* 116 */       if (!isSenior) {
/* 118 */         return false;
/*     */       }
/* 121 */       if (!TFM_PlayerData.getPlayerData(senderPlayer).isSuperadminIdVerified()) {
/* 123 */         return false;
/*     */       }
/* 126 */       return true;
/*     */     }
/* 129 */     if ((level == AdminLevel.SUPER) && (!isSuper)) {
/* 131 */       return false;
/*     */     }
/* 134 */     if ((level == AdminLevel.OP) && (!senderPlayer.isOp())) {
/* 136 */       return false;
/*     */     }
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   public Player getPlayer(String partialName)
/*     */   {
/* 144 */     return getPlayer(partialName, false);
/*     */   }
/*     */   
/*     */   public Player getPlayer(String partialName, boolean exact)
/*     */   {
/* 149 */     if ((partialName == null) || (partialName.isEmpty())) {
/* 151 */       return null;
/*     */     }
/* 154 */     Collection<? extends Player> players = server.getOnlinePlayers();
/* 157 */     for (Player player : players) {
/* 159 */       if (partialName.equalsIgnoreCase(player.getName())) {
/* 161 */         return player;
/*     */       }
/*     */     }
/* 165 */     if (exact) {
/* 167 */       return null;
/*     */     }
/* 171 */     for (Player player : players) {
/* 173 */       if (player.getName().toLowerCase().contains(partialName.toLowerCase())) {
/* 175 */         return player;
/*     */       }
/*     */     }
/* 180 */     for (Player player : players) {
/* 182 */       if (player.getDisplayName().toLowerCase().contains(partialName.toLowerCase())) {
/* 184 */         return player;
/*     */       }
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\TFM_Command.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */