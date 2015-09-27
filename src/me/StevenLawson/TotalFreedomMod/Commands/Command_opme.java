/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Automatically ops user.", usage="/<command>")
/*    */ public class Command_opme
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 15 */     TFM_Util.adminAction(sender.getName(), "Opping " + sender.getName(), false);
/* 16 */     sender.setOp(true);
/* 17 */     sender.sendMessage(TFM_Command.YOU_ARE_OP);
/*    */     
/* 19 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_opme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */