/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH, blockHostConsole=false)
/*     */ @CommandParameters(description="Remove all blocks of a certain type in the radius of certain players.", usage="/<command> <block> [radius (default=50)] [player]")
/*     */ public class Command_ro
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  23 */     if ((args.length < 1) || (args.length > 3)) {
/*  25 */       return false;
/*     */     }
/*  28 */     List<Material> materials = new ArrayList();
/*  30 */     for (String materialName : StringUtils.split(args[0], ","))
/*     */     {
/*  32 */       Material fromMaterial = Material.matchMaterial(materialName);
/*  33 */       if (fromMaterial == null) {
/*     */         try
/*     */         {
/*  37 */           fromMaterial = TFM_DepreciationAggregator.getMaterial(Integer.parseInt(materialName));
/*     */         }
/*     */         catch (NumberFormatException ex) {}
/*     */       }
/*  44 */       if (fromMaterial == null)
/*     */       {
/*  46 */         playerMsg("Invalid block: " + materialName, ChatColor.RED);
/*  47 */         return true;
/*     */       }
/*  50 */       materials.add(fromMaterial);
/*     */     }
/*  53 */     int radius = 20;
/*  54 */     if (args.length >= 2) {
/*     */       try
/*     */       {
/*  58 */         radius = Math.max(1, Math.min(50, Integer.parseInt(args[1])));
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/*  62 */         playerMsg("Invalid radius: " + args[1], ChatColor.RED);
/*  63 */         return true;
/*     */       }
/*     */     }
/*     */     Player targetPlayer;
/*  68 */     if (args.length == 3)
/*     */     {
/*  70 */       Player targetPlayer = getPlayer(args[2]);
/*  71 */       if (targetPlayer == null)
/*     */       {
/*  73 */         playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/*  74 */         return true;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  79 */       targetPlayer = null;
/*     */     }
/*  82 */     String names = StringUtils.join(materials, ", ");
/*     */     
/*  84 */     World adminWorld = null;
/*     */     try
/*     */     {
/*  87 */       adminWorld = TFM_AdminWorld.getInstance().getWorld();
/*     */     }
/*     */     catch (Exception ex) {}
/*  93 */     int affected = 0;
/*     */     Iterator i$;
/*     */     Player player;
/*  94 */     if (targetPlayer == null)
/*     */     {
/*  96 */       TFM_Util.adminAction(sender.getName(), "Removing all " + names + " within " + radius + " blocks of all players... Brace for lag!", false);
/*  98 */       for (i$ = server.getOnlinePlayers().iterator(); i$.hasNext();)
/*     */       {
/*  98 */         player = (Player)i$.next();
/* 100 */         if (player.getWorld() != adminWorld) {
/* 105 */           for (Material material : materials) {
/* 107 */             affected += TFM_Util.replaceBlocks(player.getLocation(), material, Material.AIR, radius);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 113 */     else if (targetPlayer.getWorld() != adminWorld)
/*     */     {
/* 115 */       for (Material material : materials)
/*     */       {
/* 117 */         TFM_Util.adminAction(sender.getName(), "Removing all " + names + " within " + radius + " blocks of " + targetPlayer.getName(), false);
/* 118 */         affected += TFM_Util.replaceBlocks(targetPlayer.getLocation(), material, Material.AIR, radius);
/*     */       }
/*     */     }
/* 123 */     TFM_Util.adminAction(sender.getName(), "Remove complete! " + affected + " blocks removed.", false);
/*     */     
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_ro.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */