/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Push people away from you.", usage="/<command> [radius] [strength]")
/*    */ public class Command_expel
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     double radius = 20.0D;
/* 21 */     double strength = 5.0D;
/* 23 */     if (args.length >= 1) {
/*    */       try
/*    */       {
/* 27 */         radius = Math.max(1.0D, Math.min(100.0D, Double.parseDouble(args[0])));
/*    */       }
/*    */       catch (NumberFormatException ex) {}
/*    */     }
/* 34 */     if (args.length >= 2) {
/*    */       try
/*    */       {
/* 38 */         strength = Math.max(0.0D, Math.min(50.0D, Double.parseDouble(args[1])));
/*    */       }
/*    */       catch (NumberFormatException ex) {}
/*    */     }
/* 45 */     List<String> pushedPlayers = new ArrayList();
/*    */     
/* 47 */     Vector senderPos = sender_p.getLocation().toVector();
/* 48 */     List<Player> players = sender_p.getWorld().getPlayers();
/* 49 */     for (Player player : players) {
/* 51 */       if (!player.equals(sender_p))
/*    */       {
/* 56 */         Location targetPos = player.getLocation();
/* 57 */         Vector targetPosVec = targetPos.toVector();
/*    */         
/* 59 */         boolean inRange = false;
/*    */         try
/*    */         {
/* 62 */           inRange = targetPosVec.distanceSquared(senderPos) < radius * radius;
/*    */         }
/*    */         catch (IllegalArgumentException ex) {}
/* 68 */         if (inRange)
/*    */         {
/* 70 */           player.getWorld().createExplosion(targetPos, 0.0F, false);
/* 71 */           TFM_Util.setFlying(player, false);
/* 72 */           player.setVelocity(targetPosVec.subtract(senderPos).normalize().multiply(strength));
/* 73 */           pushedPlayers.add(player.getName());
/*    */         }
/*    */       }
/*    */     }
/* 77 */     if (pushedPlayers.isEmpty()) {
/* 79 */       playerMsg("No players pushed.");
/*    */     } else {
/* 83 */       playerMsg("Pushed " + pushedPlayers.size() + " players: " + StringUtils.join(pushedPlayers, ", "));
/*    */     }
/* 86 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_expel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */