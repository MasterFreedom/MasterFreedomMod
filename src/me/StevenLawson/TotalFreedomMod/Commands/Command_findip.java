/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Player;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows all IPs registered to a player", usage="/<command> <player>")
/*    */ public class Command_findip
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length != 1) {
/* 18 */       return false;
/*    */     }
/* 21 */     Player player = getPlayer(args[0]);
/* 23 */     if (player == null)
/*    */     {
/* 26 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 27 */       return true;
/*    */     }
/* 30 */     playerMsg("Player IPs: " + StringUtils.join(TFM_PlayerList.getEntry(player).getIps(), ", "));
/*    */     
/* 32 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_findip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */