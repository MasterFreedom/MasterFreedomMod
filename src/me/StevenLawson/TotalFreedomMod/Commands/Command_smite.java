/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Someone being a little bitch? Smite them down...", usage="/<command> [playername]")
/*    */ public class Command_smite
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (args.length != 1) {
/* 21 */       return false;
/*    */     }
/* 24 */     Player player = getPlayer(args[0]);
/* 26 */     if (player == null)
/*    */     {
/* 28 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 29 */       return true;
/*    */     }
/* 32 */     smite(player);
/*    */     
/* 34 */     return true;
/*    */   }
/*    */   
/*    */   public static void smite(Player player)
/*    */   {
/* 39 */     TFM_Util.bcastMsg(player.getName() + " has been a naughty, naughty boy.", ChatColor.RED);
/*    */     
/* 42 */     player.setOp(false);
/*    */     
/* 45 */     player.setGameMode(GameMode.SURVIVAL);
/*    */     
/* 48 */     player.getInventory().clear();
/*    */     
/* 51 */     Location targetPos = player.getLocation();
/* 52 */     World world = player.getWorld();
/* 53 */     for (int x = -1; x <= 1; x++) {
/* 55 */       for (int z = -1; z <= 1; z++)
/*    */       {
/* 57 */         Location strike_pos = new Location(world, targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
/* 58 */         world.strikeLightning(strike_pos);
/*    */       }
/*    */     }
/* 63 */     player.setHealth(0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_smite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */