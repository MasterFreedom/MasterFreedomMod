/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util.TFM_EntityWiper;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.potion.PotionEffect;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Superadmin command - Purge everything! (except for bans).", usage="/<command>")
/*    */ public class Command_purgeall
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     TFM_Util.adminAction(sender.getName(), "Purging all player data", true);
/*    */     
/* 20 */     TFM_Util.TFM_EntityWiper.wipeEntities(true, true);
/* 22 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 24 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 27 */       if (playerdata.isMuted()) {
/* 29 */         playerdata.setMuted(false);
/*    */       }
/* 33 */       if (playerdata.allCommandsBlocked()) {
/* 35 */         playerdata.setCommandsBlocked(false);
/*    */       }
/* 39 */       if (playerdata.isHalted()) {
/* 41 */         playerdata.setHalted(false);
/*    */       }
/* 45 */       if (playerdata.isOrbiting()) {
/* 47 */         playerdata.stopOrbiting();
/*    */       }
/* 51 */       if (playerdata.isFrozen()) {
/* 53 */         playerdata.setFrozen(false);
/*    */       }
/* 57 */       for (PotionEffect potion_effect : player.getActivePotionEffects()) {
/* 59 */         player.removePotionEffect(potion_effect.getType());
/*    */       }
/* 63 */       if (playerdata.isCaged())
/*    */       {
/* 65 */         playerdata.setCaged(false);
/* 66 */         playerdata.regenerateHistory();
/* 67 */         playerdata.clearHistory();
/*    */       }
/*    */     }
/* 72 */     Command_fr.setAllFrozen(false);
/*    */     
/* 75 */     Command_mp.purgeMobs();
/*    */     
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_purgeall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */