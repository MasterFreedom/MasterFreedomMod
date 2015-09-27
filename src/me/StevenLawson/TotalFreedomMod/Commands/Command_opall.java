/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Op everyone on the server, optionally change everyone's gamemode at the same time.", usage="/<command> [-c | -s]")
/*    */ public class Command_opall
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_Util.adminAction(sender.getName(), "Opping all players on the server", false);
/*    */     
/* 18 */     boolean doSetGamemode = false;
/* 19 */     GameMode targetGamemode = GameMode.CREATIVE;
/* 20 */     if (args.length != 0) {
/* 22 */       if (args[0].equals("-c"))
/*    */       {
/* 24 */         doSetGamemode = true;
/* 25 */         targetGamemode = GameMode.CREATIVE;
/*    */       }
/* 27 */       else if (args[0].equals("-s"))
/*    */       {
/* 29 */         doSetGamemode = true;
/* 30 */         targetGamemode = GameMode.SURVIVAL;
/*    */       }
/*    */     }
/* 34 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 36 */       player.setOp(true);
/* 37 */       player.sendMessage(TFM_Command.YOU_ARE_OP);
/* 39 */       if (doSetGamemode) {
/* 41 */         player.setGameMode(targetGamemode);
/*    */       }
/*    */     }
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_opall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */