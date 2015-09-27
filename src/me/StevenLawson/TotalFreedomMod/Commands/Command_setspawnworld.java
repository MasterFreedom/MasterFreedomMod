/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ProtectedArea;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Set world spawnpoint.", usage="/<command>")
/*    */ public class Command_setspawnworld
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 18 */     Location pos = sender_p.getLocation();
/* 19 */     sender_p.getWorld().setSpawnLocation(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
/*    */     
/* 21 */     playerMsg("Spawn location for this world set to: " + TFM_Util.formatLocation(sender_p.getWorld().getSpawnLocation()));
/* 23 */     if ((TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) && (TFM_ConfigEntry.PROTECTAREA_SPAWNPOINTS.getBoolean().booleanValue())) {
/* 25 */       TFM_ProtectedArea.addProtectedArea("spawn_" + sender_p.getWorld().getName(), pos, TFM_ConfigEntry.PROTECTAREA_RADIUS.getDouble().doubleValue());
/*    */     }
/* 28 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_setspawnworld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */