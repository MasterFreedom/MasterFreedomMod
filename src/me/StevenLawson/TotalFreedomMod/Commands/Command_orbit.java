/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="POW!!! Right in the kisser! One of these days Alice, straight to the Moon!", usage="/<command> <target> [<<power> | stop>]")
/*    */ public class Command_orbit
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     if (args.length == 0) {
/* 22 */       return false;
/*    */     }
/* 25 */     Player player = getPlayer(args[0]);
/* 27 */     if (player == null)
/*    */     {
/* 29 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/* 30 */       return true;
/*    */     }
/* 33 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*    */     
/* 35 */     double strength = 10.0D;
/* 37 */     if (args.length >= 2)
/*    */     {
/* 39 */       if (args[1].equals("stop"))
/*    */       {
/* 41 */         playerMsg("Stopped orbiting " + player.getName());
/* 42 */         playerdata.stopOrbiting();
/* 43 */         return true;
/*    */       }
/*    */       try
/*    */       {
/* 48 */         strength = Math.max(1.0D, Math.min(150.0D, Double.parseDouble(args[1])));
/*    */       }
/*    */       catch (NumberFormatException ex)
/*    */       {
/* 52 */         playerMsg(ex.getMessage(), ChatColor.RED);
/* 53 */         return true;
/*    */       }
/*    */     }
/* 57 */     player.setGameMode(GameMode.SURVIVAL);
/* 58 */     playerdata.startOrbiting(strength);
/*    */     
/* 60 */     player.setVelocity(new Vector(0.0D, strength, 0.0D));
/* 61 */     TFM_Util.adminAction(sender.getName(), "Orbiting " + player.getName(), false);
/*    */     
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_orbit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */