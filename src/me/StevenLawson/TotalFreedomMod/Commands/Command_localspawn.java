/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Teleport to the spawn point for the current world.", usage="/<command>", aliases="worldspawn,gotospawn")
/*    */ public class Command_localspawn
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 14 */     sender_p.teleport(sender_p.getWorld().getSpawnLocation());
/* 15 */     playerMsg("Teleported to spawnpoint for world \"" + sender_p.getWorld().getName() + "\".");
/* 16 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_localspawn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */