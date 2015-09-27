/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.World.TFM_Flatlands;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Goto the flatlands.", usage="/<command>")
/*    */ public class Command_flatlands
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (TFM_ConfigEntry.FLATLANDS_GENERATE.getBoolean().booleanValue()) {
/* 18 */       TFM_Flatlands.getInstance().sendToWorld(sender_p);
/*    */     } else {
/* 22 */       playerMsg("Flatlands is currently disabled.");
/*    */     }
/* 24 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_flatlands.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */