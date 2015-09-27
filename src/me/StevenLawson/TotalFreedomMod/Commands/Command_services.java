/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ServiceChecker;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ServiceChecker.ServiceStatus;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows the status of all Mojang services", usage="/<command>")
/*    */ public class Command_services
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 18 */     playerMsg("Mojang Services" + ChatColor.WHITE + ":", ChatColor.BLUE);
/* 20 */     for (TFM_ServiceChecker.ServiceStatus service : TFM_ServiceChecker.getAllStatuses()) {
/* 22 */       playerMsg(service.getFormattedStatus());
/*    */     }
/* 24 */     playerMsg("Version" + ChatColor.WHITE + ": " + TFM_ServiceChecker.getVersion(), ChatColor.DARK_PURPLE);
/* 25 */     playerMsg("Last Check" + ChatColor.WHITE + ": " + TFM_ServiceChecker.getLastCheck(), ChatColor.DARK_PURPLE);
/*    */     
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_services.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */