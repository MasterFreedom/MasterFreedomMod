/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.potion.PotionEffectType;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows (optionally smites) invisisible players", usage="/<command> (smite)")
/*    */ public class Command_invis
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     boolean smite = false;
/* 21 */     if (args.length >= 1) {
/* 23 */       if (args[0].equalsIgnoreCase("smite"))
/*    */       {
/* 25 */         TFM_Util.adminAction(sender.getName(), "Smiting all invisible players", true);
/* 26 */         smite = true;
/*    */       }
/*    */       else
/*    */       {
/* 30 */         return false;
/*    */       }
/*    */     }
/* 34 */     List<String> players = new ArrayList();
/* 35 */     int smites = 0;
/* 37 */     for (Player player : server.getOnlinePlayers()) {
/* 39 */       if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
/*    */       {
/* 41 */         players.add(player.getName());
/* 42 */         if ((smite) && (!TFM_AdminList.isSuperAdmin(player)))
/*    */         {
/* 44 */           player.setHealth(0.0D);
/* 45 */           smites++;
/*    */         }
/*    */       }
/*    */     }
/* 50 */     if (players.isEmpty())
/*    */     {
/* 52 */       playerMsg("There are no invisible players");
/* 53 */       return true;
/*    */     }
/* 56 */     if (smite) {
/* 58 */       playerMsg("Smitten " + smites + " players");
/*    */     } else {
/* 62 */       playerMsg("Invisble players (" + players.size() + "): " + StringUtils.join(players, ", "));
/*    */     }
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_invis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */