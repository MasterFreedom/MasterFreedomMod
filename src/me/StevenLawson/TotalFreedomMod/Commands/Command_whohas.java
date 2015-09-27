/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="See who has a block and optionally smite.", usage="/<command> <item> [smite]", aliases="wh")
/*    */ public class Command_whohas
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 21 */     if (args.length < 1) {
/* 23 */       return false;
/*    */     }
/* 26 */     boolean doSmite = (args.length >= 2) && ("smite".equalsIgnoreCase(args[1]));
/*    */     
/* 28 */     String materialName = args[0];
/* 29 */     Material material = Material.matchMaterial(materialName);
/* 30 */     if (material == null) {
/*    */       try
/*    */       {
/* 34 */         material = TFM_DepreciationAggregator.getMaterial(Integer.parseInt(materialName));
/*    */       }
/*    */       catch (NumberFormatException ex) {}
/*    */     }
/* 41 */     if (material == null)
/*    */     {
/* 43 */       playerMsg("Invalid block: " + materialName, ChatColor.RED);
/* 44 */       return true;
/*    */     }
/* 47 */     List<String> players = new ArrayList();
/* 49 */     for (Player player : server.getOnlinePlayers()) {
/* 51 */       if (player.getInventory().contains(material))
/*    */       {
/* 53 */         players.add(player.getName());
/* 54 */         if ((doSmite) && (!TFM_AdminList.isSuperAdmin(player))) {
/* 56 */           Command_smite.smite(player);
/*    */         }
/*    */       }
/*    */     }
/* 61 */     if (players.isEmpty()) {
/* 63 */       playerMsg("There are no players with that item");
/*    */     } else {
/* 67 */       playerMsg("Players with item " + material.name() + ": " + StringUtils.join(players, ", "));
/*    */     }
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_whohas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */