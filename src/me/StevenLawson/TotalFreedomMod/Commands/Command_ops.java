/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Set;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Manager operators", usage="/<command> <count | purge>")
/*    */ public class Command_ops
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equals("count"))
/*    */     {
/* 24 */       int totalOps = server.getOperators().size();
/* 25 */       int onlineOps = 0;
/* 27 */       for (Player player : server.getOnlinePlayers()) {
/* 29 */         if (player.isOp()) {
/* 31 */           onlineOps++;
/*    */         }
/*    */       }
/* 35 */       playerMsg("Online OPs: " + onlineOps);
/* 36 */       playerMsg("Offline OPs: " + (totalOps - onlineOps));
/* 37 */       playerMsg("Total OPs: " + totalOps);
/*    */       
/* 39 */       return true;
/*    */     }
/* 42 */     if (args[0].equals("purge"))
/*    */     {
/* 44 */       if (!TFM_AdminList.isSuperAdmin(sender))
/*    */       {
/* 46 */         playerMsg(TFM_Command.MSG_NO_PERMS);
/* 47 */         return true;
/*    */       }
/* 50 */       TFM_Util.adminAction(sender.getName(), "Purging all operators", true);
/* 52 */       for (OfflinePlayer player : server.getOperators())
/*    */       {
/* 54 */         player.setOp(false);
/* 55 */         if (player.isOnline()) {
/* 57 */           playerMsg(player.getPlayer(), TFM_Command.YOU_ARE_NOT_OP);
/*    */         }
/*    */       }
/* 60 */       return true;
/*    */     }
/* 63 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_ops.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */