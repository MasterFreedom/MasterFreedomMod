/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_WorldEditBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Sets everyone's Worldedit block modification limit to 500.", usage="/<command>")
/*    */ public class Command_setl
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_Util.adminAction(sender.getName(), "Setting everyone's Worldedit block modification limit to 2500.", true);
/* 17 */     for (Player player : server.getOnlinePlayers()) {
/* 19 */       TFM_WorldEditBridge.setLimit(player, 2500);
/*    */     }
/* 21 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_setl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */