/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_CONSOLE)
/*    */ @CommandParameters(description="Telnet command - Send a chat message with chat formatting over telnet.", usage="/<command> <message...>")
/*    */ public class Command_csay
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length > 0) {
/* 18 */       TFM_Util.bcastMsg(String.format("§7[CONSOLE]§f<§c%s§f> %s", new Object[] { sender.getName(), StringUtils.join(args, " ") }));
/*    */     }
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_csay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */