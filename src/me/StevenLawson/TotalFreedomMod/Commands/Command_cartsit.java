/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Minecart;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Sit in nearest minecart. If target is in a minecart already, they will disembark.", usage="/<command> [partialname]")
/*    */ public class Command_cartsit
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     Player targetPlayer = sender_p;
/* 18 */     if (args.length == 1)
/*    */     {
/* 21 */       targetPlayer = getPlayer(args[0]);
/* 23 */       if (targetPlayer == null)
/*    */       {
/* 25 */         sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 26 */         return true;
/*    */       }
/*    */     }
/* 30 */     if (senderIsConsole)
/*    */     {
/* 32 */       if (targetPlayer == null)
/*    */       {
/* 34 */         sender.sendMessage("When used from the console, you must define a target player: /cartsit <player>");
/* 35 */         return true;
/*    */       }
/*    */     }
/* 38 */     else if ((targetPlayer != sender_p) && (!TFM_AdminList.isSuperAdmin(sender)))
/*    */     {
/* 40 */       sender.sendMessage("Only superadmins can select another player as a /cartsit target.");
/* 41 */       return true;
/*    */     }
/* 44 */     if (targetPlayer.isInsideVehicle())
/*    */     {
/* 46 */       targetPlayer.getVehicle().eject();
/*    */     }
/*    */     else
/*    */     {
/* 50 */       Minecart nearest_cart = null;
/* 51 */       for (Minecart cart : targetPlayer.getWorld().getEntitiesByClass(Minecart.class)) {
/* 53 */         if (cart.isEmpty()) {
/* 55 */           if (nearest_cart == null) {
/* 57 */             nearest_cart = cart;
/* 61 */           } else if (cart.getLocation().distanceSquared(targetPlayer.getLocation()) < nearest_cart.getLocation().distanceSquared(targetPlayer.getLocation())) {
/* 63 */             nearest_cart = cart;
/*    */           }
/*    */         }
/*    */       }
/* 69 */       if (nearest_cart != null) {
/* 71 */         nearest_cart.setPassenger(targetPlayer);
/*    */       } else {
/* 75 */         sender.sendMessage("There are no empty minecarts in the target world.");
/*    */       }
/*    */     }
/* 79 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cartsit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */