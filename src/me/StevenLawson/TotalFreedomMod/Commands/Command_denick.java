/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Essentials Interface Command - Remove the nickname of all players on the server.", usage="/<command>")
/*    */ public class Command_denick
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_Util.adminAction(sender.getName(), "Removing all nicknames", false);
/* 18 */     for (Player player : server.getOnlinePlayers()) {
/* 20 */       TFM_EssentialsBridge.setNickname(player.getName(), null);
/*    */     }
/* 23 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_denick.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */