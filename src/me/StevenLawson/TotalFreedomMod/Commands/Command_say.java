/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Broadcasts the given message as the console, includes sender name.", usage="/<command> <message>")
/*    */ public class Command_say
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length == 0) {
/* 19 */       return false;
/*    */     }
/* 22 */     String message = StringUtils.join(args, " ");
/* 24 */     if ((senderIsConsole) && (TFM_Util.isFromHostConsole(sender.getName()))) {
/* 26 */       if (message.equalsIgnoreCase("WARNING: Server is restarting, you will be kicked"))
/*    */       {
/* 28 */         TFM_Util.bcastMsg("Server is going offline.", ChatColor.GRAY);
/* 30 */         for (Player player : server.getOnlinePlayers()) {
/* 32 */           player.kickPlayer("Server is going offline, come back in about 20 seconds.");
/*    */         }
/* 35 */         server.shutdown();
/*    */         
/* 37 */         return true;
/*    */       }
/*    */     }
/* 41 */     TFM_Util.bcastMsg(String.format("[Server:%s] %s", new Object[] { sender.getName(), message }), ChatColor.LIGHT_PURPLE);
/*    */     
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_say.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */