/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="You'll never even see it coming.", usage="/<command>")
/*    */ public class Command_fuckoff
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 15 */     if (args.length < 1) {
/* 17 */       return false;
/*    */     }
/* 20 */     boolean fuckoff_enabled = false;
/* 21 */     double fuckoff_range = 25.0D;
/* 23 */     if (args[0].equalsIgnoreCase("on"))
/*    */     {
/* 25 */       fuckoff_enabled = true;
/* 27 */       if (args.length >= 2) {
/*    */         try
/*    */         {
/* 31 */           fuckoff_range = Math.max(5.0D, Math.min(100.0D, Double.parseDouble(args[1])));
/*    */         }
/*    */         catch (NumberFormatException ex) {}
/*    */       }
/*    */     }
/* 39 */     if (TotalFreedomMod.fuckoffEnabledFor.containsKey(sender_p)) {
/* 41 */       TotalFreedomMod.fuckoffEnabledFor.remove(sender_p);
/*    */     }
/* 44 */     if (fuckoff_enabled) {
/* 46 */       TotalFreedomMod.fuckoffEnabledFor.put(sender_p, new Double(fuckoff_range));
/*    */     }
/* 49 */     playerMsg("Fuckoff " + (fuckoff_enabled ? "enabled. Range: " + fuckoff_range + "." : "disabled."));
/*    */     
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_fuckoff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */