/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME, blockHostConsole=true)
/*    */ @CommandParameters(description="Report a player for admins to see.", usage="/<command> <player> <reason>")
/*    */ public class Command_report
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (args.length < 2) {
/* 21 */       return false;
/*    */     }
/* 24 */     Player player = getPlayer(args[0]);
/* 26 */     if (player == null)
/*    */     {
/* 28 */       playerMsg(PLAYER_NOT_FOUND);
/* 29 */       return true;
/*    */     }
/* 32 */     if ((sender instanceof Player)) {
/* 34 */       if (player.equals(sender_p))
/*    */       {
/* 36 */         playerMsg(ChatColor.RED + "Please, don't try to report yourself.");
/* 37 */         return true;
/*    */       }
/*    */     }
/* 41 */     if (TFM_AdminList.isSuperAdmin(player))
/*    */     {
/* 43 */       playerMsg(ChatColor.RED + "You can not report an admin.");
/* 44 */       return true;
/*    */     }
/* 47 */     String report = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
/* 48 */     TFM_Util.reportAction(sender_p, player, report);
/*    */     
/* 50 */     playerMsg(ChatColor.GREEN + "Thank you, your report has been successfully logged.");
/*    */     
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_report.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */