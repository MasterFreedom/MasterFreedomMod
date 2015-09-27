/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Shows nearby people sorted by distance.", usage="/<command> [range]")
/*     */ public class Command_radar
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  20 */     Location sender_pos = sender_p.getLocation();
/*     */     
/*  22 */     List<TFM_RadarData> radar_data = new ArrayList();
/*  24 */     for (Player player : sender_pos.getWorld().getPlayers()) {
/*  26 */       if (!player.equals(sender_p)) {
/*     */         try
/*     */         {
/*  30 */           radar_data.add(new TFM_RadarData(player, sender_pos.distance(player.getLocation()), player.getLocation()));
/*     */         }
/*     */         catch (IllegalArgumentException ex) {}
/*     */       }
/*     */     }
/*  38 */     if (radar_data.isEmpty())
/*     */     {
/*  40 */       playerMsg("You are the only player in this world. (" + ChatColor.GREEN + "Forever alone..." + ChatColor.YELLOW + ")", ChatColor.YELLOW);
/*  41 */       return true;
/*     */     }
/*  44 */     Collections.sort(radar_data, new TFM_RadarData());
/*     */     
/*  46 */     playerMsg("People nearby in " + sender_pos.getWorld().getName() + ":", ChatColor.YELLOW);
/*     */     
/*  48 */     int countmax = 5;
/*  49 */     if (args.length == 1) {
/*     */       try
/*     */       {
/*  53 */         countmax = Math.max(1, Math.min(64, Integer.parseInt(args[0])));
/*     */       }
/*     */       catch (NumberFormatException nfex) {}
/*     */     }
/*  60 */     for (Iterator i$ = radar_data.iterator(); i$.hasNext(); countmax <= 0)
/*     */     {
/*  60 */       TFM_RadarData i = (TFM_RadarData)i$.next();
/*     */       
/*  62 */       playerMsg(String.format("%s - %d", new Object[] { player.getName(), Long.valueOf(Math.round(distance)) }), ChatColor.YELLOW);
/*     */       
/*  66 */       countmax--;
/*     */     }
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   private class TFM_RadarData
/*     */     implements Comparator<TFM_RadarData>
/*     */   {
/*     */     public Player player;
/*     */     public double distance;
/*     */     public Location location;
/*     */     
/*     */     public TFM_RadarData(Player player, double distance, Location location)
/*     */     {
/*  83 */       this.player = player;
/*  84 */       this.distance = distance;
/*  85 */       this.location = location;
/*     */     }
/*     */     
/*     */     public TFM_RadarData() {}
/*     */     
/*     */     public int compare(TFM_RadarData t1, TFM_RadarData t2)
/*     */     {
/*  95 */       if (distance > distance) {
/*  97 */         return 1;
/*     */       }
/*  99 */       if (distance < distance) {
/* 101 */         return -1;
/*     */       }
/* 105 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_radar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */