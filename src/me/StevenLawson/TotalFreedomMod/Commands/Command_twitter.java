/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_TwitterHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Manage your twitter.", usage="/<command> <set [twitter] | info | enable | disable>")
/*     */ public class Command_twitter
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  18 */     if (!TFM_ConfigEntry.TWITTERBOT_ENABLED.getBoolean().booleanValue())
/*     */     {
/*  20 */       playerMsg("TwitterBot has been disabled in config.", ChatColor.RED);
/*  21 */       return true;
/*     */     }
/*  24 */     if (args.length < 1) {
/*  26 */       return false;
/*     */     }
/*  29 */     if ("set".equals(args[0]))
/*     */     {
/*  31 */       if (args.length != 2) {
/*  33 */         return false;
/*     */       }
/*  36 */       if (args[1].startsWith("@"))
/*     */       {
/*  38 */         playerMsg("Please do not prefix your twitter username with '@'");
/*  39 */         return true;
/*     */       }
/*  42 */       String reply = TFM_TwitterHandler.setTwitter(sender.getName(), args[1]);
/*  44 */       if ("ok".equals(reply))
/*     */       {
/*  46 */         playerMsg("Your twitter handle has been set to: " + ChatColor.AQUA + "@" + args[1] + ChatColor.GRAY + ".");
/*     */       }
/*  48 */       else if ("disabled".equals(reply))
/*     */       {
/*  50 */         playerMsg("TwitterBot has been temporarily disabled,, please wait until it get re-enabled", ChatColor.RED);
/*     */       }
/*  52 */       else if ("failed".equals(reply))
/*     */       {
/*  54 */         playerMsg("There was a problem querying the database, please let a developer know.", ChatColor.RED);
/*     */       }
/*  56 */       else if ("false".equals(reply))
/*     */       {
/*  58 */         playerMsg("There was a problem with the database, please let a developer know.", ChatColor.RED);
/*     */       }
/*  60 */       else if ("cannotauth".equals(reply))
/*     */       {
/*  62 */         playerMsg("The database password is incorrect, please let a developer know.", ChatColor.RED);
/*     */       }
/*     */       else
/*     */       {
/*  66 */         playerMsg("An unknown error occurred, please contact a developer", ChatColor.RED);
/*  67 */         playerMsg("Response code: " + reply);
/*     */       }
/*  69 */       return true;
/*     */     }
/*  72 */     if (args.length != 1) {
/*  74 */       return false;
/*     */     }
/*  77 */     if ("info".equals(args[0]))
/*     */     {
/*  79 */       String reply = TFM_TwitterHandler.getTwitter(sender.getName());
/*  80 */       playerMsg("-- Twitter Information --", ChatColor.BLUE);
/*  81 */       playerMsg("Using this feature, you can re-super yourself using twitter.");
/*  82 */       playerMsg("You can set your twitter handle using " + ChatColor.AQUA + "/twitter set [twittername]");
/*  83 */       playerMsg("Then, you can verify yourself by tweeting " + ChatColor.AQUA + "@TFUpdates #superme");
/*  84 */       if ("notfound".equals(reply)) {
/*  86 */         playerMsg("You currently have " + ChatColor.RED + "no" + ChatColor.BLUE + " Twitter handle set.", ChatColor.BLUE);
/*  88 */       } else if ("disabled".equals(reply)) {
/*  90 */         playerMsg("TwitterBot has been temporarily disabled, please wait until re-enabled", ChatColor.RED);
/*  92 */       } else if ("failed".equals(reply)) {
/*  94 */         playerMsg("There was a problem querying the database, please let a developer know.", ChatColor.RED);
/*  96 */       } else if ("false".equals(reply)) {
/*  98 */         playerMsg("There was a problem with the database, please let a developer know.", ChatColor.RED);
/* 100 */       } else if ("cannotauth".equals(reply)) {
/* 102 */         playerMsg("The database password is incorrect, please let a developer know.", ChatColor.RED);
/*     */       } else {
/* 106 */         playerMsg("Your current twitter handle: " + ChatColor.AQUA + "@" + reply + ChatColor.BLUE + ".", ChatColor.BLUE);
/*     */       }
/* 108 */       return true;
/*     */     }
/* 111 */     if (("enable".equals(args[0])) || ("disable".equals(args[0])))
/*     */     {
/* 113 */       if (!sender.getName().equalsIgnoreCase("DarthSalamon"))
/*     */       {
/* 115 */         sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 116 */         return true;
/*     */       }
/* 119 */       TFM_Util.adminAction(sender.getName(), ("enable".equals(args[0]) ? "Ena" : "Disa") + "bling Twitterbot", true);
/* 120 */       String reply = TFM_TwitterHandler.setEnabled(args[0] + "d");
/* 121 */       playerMsg("Reply: " + reply);
/* 122 */       return true;
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_twitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */