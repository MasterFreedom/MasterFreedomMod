/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.io.File;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE)
/*    */ @CommandParameters(description="Removes essentials playerdata", usage="/<command>")
/*    */ public class Command_wipeuserdata
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (!server.getPluginManager().isPluginEnabled("Essentials"))
/*    */     {
/* 18 */       playerMsg("Essentials is not enabled on this server");
/* 19 */       return true;
/*    */     }
/* 22 */     TFM_Util.adminAction(sender.getName(), "Wiping Essentials playerdata", true);
/*    */     
/* 24 */     TFM_Util.deleteFolder(new File(server.getPluginManager().getPlugin("Essentials").getDataFolder(), "userdata"));
/*    */     
/* 26 */     playerMsg("All playerdata deleted.");
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_wipeuserdata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */