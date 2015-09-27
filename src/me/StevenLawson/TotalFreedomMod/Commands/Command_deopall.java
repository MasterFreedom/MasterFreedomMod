/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Deop everyone on the server.", usage="/<command>")
/*    */ public class Command_deopall
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 15 */     TFM_Util.adminAction(sender.getName(), "De-opping all players on the server", true);
/* 17 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 19 */       player.setOp(false);
/* 20 */       player.sendMessage(TFM_Command.YOU_ARE_NOT_OP);
/*    */     }
/* 23 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_deopall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */