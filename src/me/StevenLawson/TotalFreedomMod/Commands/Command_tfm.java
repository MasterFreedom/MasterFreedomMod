/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PermbanList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows information about TotalFreedomMod or reloads it", usage="/<command> [reload]")
/*    */ public class Command_tfm
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 27 */     if (args.length == 1)
/*    */     {
/* 29 */       if (!args[0].equals("reload")) {
/* 31 */         return false;
/*    */       }
/* 34 */       if (!TFM_AdminList.isSuperAdmin(sender))
/*    */       {
/* 36 */         playerMsg(TFM_Command.MSG_NO_PERMS);
/* 37 */         return true;
/*    */       }
/* 40 */       TFM_MainConfig.load();
/* 41 */       TFM_AdminList.load();
/* 42 */       TFM_PermbanList.load();
/* 43 */       TFM_PlayerList.load();
/* 44 */       TFM_BanManager.load();
/* 45 */       TFM_CommandBlocker.load();
/*    */       
/* 47 */       String message = String.format("%s v%s.%s reloaded.", new Object[] { TotalFreedomMod.pluginName, TotalFreedomMod.pluginVersion, TotalFreedomMod.buildNumber });
/*    */       
/* 52 */       playerMsg(message);
/* 53 */       TFM_Log.info(message);
/* 54 */       return true;
/*    */     }
/* 57 */     playerMsg("TotalFreedomMod for 'Total Freedom', the original all-op server.", ChatColor.GOLD);
/* 58 */     playerMsg(String.format("Version " + ChatColor.BLUE + "%s.%s" + ChatColor.GOLD + ", built " + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by " + ChatColor.BLUE + "%s" + ChatColor.GOLD + ".", new Object[] { TotalFreedomMod.pluginVersion, TotalFreedomMod.buildNumber, TotalFreedomMod.buildDate, TotalFreedomMod.buildCreator }), ChatColor.GOLD);
/*    */     
/* 66 */     playerMsg("Running on " + TFM_ConfigEntry.SERVER_NAME.getString() + ".", ChatColor.GOLD);
/* 67 */     playerMsg("Created by Madgeek1450 and Prozza.", ChatColor.GOLD);
/* 68 */     playerMsg("Visit " + ChatColor.AQUA + "http://totalfreedom.me/" + ChatColor.GREEN + " for more information.", ChatColor.GREEN);
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tfm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */