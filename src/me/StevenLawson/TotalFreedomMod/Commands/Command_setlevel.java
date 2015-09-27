/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Sets your expierence level.", usage="/<command> [level]")
/*    */ public class Command_setlevel
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 15 */     if (args.length != 1) {
/* 17 */       return false;
/*    */     }
/*    */     int new_level;
/*    */     try
/*    */     {
/* 24 */       new_level = Integer.parseInt(args[0]);
/* 26 */       if (new_level < 0) {
/* 28 */         new_level = 0;
/* 30 */       } else if (new_level > 50) {
/* 32 */         new_level = 50;
/*    */       }
/*    */     }
/*    */     catch (NumberFormatException ex)
/*    */     {
/* 37 */       playerMsg("Invalid level.", ChatColor.RED);
/* 38 */       return true;
/*    */     }
/* 41 */     sender_p.setLevel(new_level);
/*    */     
/* 43 */     playerMsg("You have been set to level " + Integer.toString(new_level), ChatColor.AQUA);
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_setlevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */