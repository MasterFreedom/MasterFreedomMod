/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH, blockHostConsole=true)
/*    */ @CommandParameters(description="Warns a player.", usage="/<command> <player> <reason>")
/*    */ public class Command_warn
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
/* 36 */         playerMsg(ChatColor.RED + "Please, don't try to warn yourself.");
/* 37 */         return true;
/*    */       }
/*    */     }
/* 41 */     if (TFM_AdminList.isSuperAdmin(player))
/*    */     {
/* 43 */       playerMsg(ChatColor.RED + "You can not warn admins");
/* 44 */       return true;
/*    */     }
/* 47 */     String warnReason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
/*    */     
/* 49 */     playerMsg(ChatColor.RED + "[WARNING] " + warnReason);
/* 50 */     playerMsg(ChatColor.GREEN + "You have successfully warned " + player.getName());
/*    */     
/* 52 */     TFM_PlayerData.getPlayerData(player).incrementWarnings();
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_warn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */