/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Use admin commands on someone by hash. Use mode 'list' to get a player's hash. Other modes are kick, nameban, ipban, ban, op, deop, ci, fr, smite.", usage="/<command> [list | [<kick | nameban | ipban | ban | op | deop | ci | fr | smite> <targethash>] ]")
/*     */ public class Command_gadmin
/*     */   extends TFM_Command
/*     */ {
/*     */   private static enum GadminMode
/*     */   {
/*  21 */     LIST("list"),  KICK("kick"),  NAMEBAN("nameban"),  IPBAN("ipban"),  BAN("ban"),  OP("op"),  DEOP("deop"),  CI("ci"),  FR("fr"),  SMITE("smite");
/*     */     
/*     */     private final String modeName;
/*     */     
/*     */     private GadminMode(String command)
/*     */     {
/*  35 */       modeName = command;
/*     */     }
/*     */     
/*     */     public String getModeName()
/*     */     {
/*  40 */       return modeName;
/*     */     }
/*     */     
/*     */     public static GadminMode findMode(String needle)
/*     */     {
/*  45 */       for (GadminMode mode : ) {
/*  47 */         if (needle.equalsIgnoreCase(mode.getModeName())) {
/*  49 */           return mode;
/*     */         }
/*     */       }
/*  52 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  59 */     if (args.length == 0) {
/*  61 */       return false;
/*     */     }
/*  64 */     GadminMode mode = GadminMode.findMode(args[0].toLowerCase());
/*  65 */     if (mode == null)
/*     */     {
/*  67 */       playerMsg("Invalid mode: " + args[0], ChatColor.RED);
/*  68 */       return true;
/*     */     }
/*  71 */     Iterator<? extends Player> it = server.getOnlinePlayers().iterator();
/*  73 */     if (mode == GadminMode.LIST)
/*     */     {
/*  75 */       playerMsg("[ Real Name ] : [ Display Name ] - Hash:");
/*  76 */       while (it.hasNext())
/*     */       {
/*  78 */         Player player = (Player)it.next();
/*  79 */         String hash = TFM_UuidManager.getUniqueId(player).toString().substring(0, 4);
/*  80 */         sender.sendMessage(ChatColor.GRAY + String.format("[ %s ] : [ %s ] - %s", new Object[] { player.getName(), ChatColor.stripColor(player.getDisplayName()), hash }));
/*     */       }
/*  85 */       return true;
/*     */     }
/*  88 */     if (args.length < 2) {
/*  90 */       return false;
/*     */     }
/*  93 */     Player target = null;
/*  94 */     while ((it.hasNext()) && (target == null))
/*     */     {
/*  96 */       Player player = (Player)it.next();
/*  97 */       String hash = TFM_UuidManager.getUniqueId(player).toString().substring(0, 4);
/*  99 */       if (hash.equalsIgnoreCase(args[1])) {
/* 101 */         target = player;
/*     */       }
/*     */     }
/* 105 */     if (target == null)
/*     */     {
/* 107 */       playerMsg("Invalid player hash: " + args[1], ChatColor.RED);
/* 108 */       return true;
/*     */     }
/* 111 */     switch (mode)
/*     */     {
/*     */     case KICK: 
/* 115 */       TFM_Util.adminAction(sender.getName(), String.format("Kicking: %s.", new Object[] { target.getName() }), false);
/* 116 */       target.kickPlayer("Kicked by Administrator");
/*     */       
/* 118 */       break;
/*     */     case NAMEBAN: 
/* 122 */       TFM_BanManager.addUuidBan(target);
/*     */       
/* 124 */       TFM_Util.adminAction(sender.getName(), String.format("Banning Name: %s.", new Object[] { target.getName() }), true);
/* 125 */       target.kickPlayer("Username banned by Administrator.");
/*     */       
/* 127 */       break;
/*     */     case IPBAN: 
/* 131 */       String ip = target.getAddress().getAddress().getHostAddress();
/* 132 */       String[] ip_parts = ip.split("\\.");
/* 133 */       if (ip_parts.length == 4) {
/* 135 */         ip = String.format("%s.%s.*.*", new Object[] { ip_parts[0], ip_parts[1] });
/*     */       }
/* 137 */       TFM_Util.adminAction(sender.getName(), String.format("Banning IP: %s.", new Object[] { ip }), true);
/* 138 */       TFM_BanManager.addIpBan(target);
/*     */       
/* 140 */       target.kickPlayer("IP address banned by Administrator.");
/*     */       
/* 142 */       break;
/*     */     case BAN: 
/* 146 */       String ip = target.getAddress().getAddress().getHostAddress();
/* 147 */       String[] ip_parts = ip.split("\\.");
/* 148 */       if (ip_parts.length == 4) {
/* 150 */         ip = String.format("%s.%s.*.*", new Object[] { ip_parts[0], ip_parts[1] });
/*     */       }
/* 152 */       TFM_Util.adminAction(sender.getName(), String.format("Banning Name: %s, IP: %s.", new Object[] { target.getName(), ip }), true);
/*     */       
/* 154 */       TFM_BanManager.addUuidBan(target);
/* 155 */       TFM_BanManager.addIpBan(target);
/*     */       
/* 157 */       target.kickPlayer("IP and username banned by Administrator.");
/*     */       
/* 159 */       break;
/*     */     case OP: 
/* 163 */       TFM_Util.adminAction(sender.getName(), String.format("Opping %s.", new Object[] { target.getName() }), false);
/* 164 */       target.setOp(false);
/* 165 */       target.sendMessage(TFM_Command.YOU_ARE_OP);
/*     */       
/* 167 */       break;
/*     */     case DEOP: 
/* 171 */       TFM_Util.adminAction(sender.getName(), String.format("Deopping %s.", new Object[] { target.getName() }), false);
/* 172 */       target.setOp(false);
/* 173 */       target.sendMessage(TFM_Command.YOU_ARE_NOT_OP);
/*     */       
/* 175 */       break;
/*     */     case CI: 
/* 179 */       target.getInventory().clear();
/*     */       
/* 181 */       break;
/*     */     case FR: 
/* 185 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(target);
/* 186 */       playerdata.setFrozen(!playerdata.isFrozen());
/*     */       
/* 188 */       playerMsg(target.getName() + " has been " + (playerdata.isFrozen() ? "frozen" : "unfrozen") + ".");
/* 189 */       target.sendMessage(ChatColor.AQUA + "You have been " + (playerdata.isFrozen() ? "frozen" : "unfrozen") + ".");
/*     */       
/* 191 */       break;
/*     */     case SMITE: 
/* 195 */       Command_smite.smite(target);
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_gadmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */