/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Spy on commands", usage="/<command>", aliases="commandspy")
/*    */ public class Command_cmdspy
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(sender_p);
/* 17 */     playerdata.setCommandSpy(!playerdata.cmdspyEnabled());
/* 18 */     playerMsg("CommandSpy " + (playerdata.cmdspyEnabled() ? "enabled." : "disabled."));
/*    */     
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cmdspy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */