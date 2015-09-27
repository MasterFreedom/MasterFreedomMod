/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockFace;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Set a landmine trap.", usage="/<command>")
/*    */ public class Command_landmine
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 23 */     if (!TFM_ConfigEntry.LANDMINES_ENABLED.getBoolean().booleanValue())
/*    */     {
/* 25 */       playerMsg("The landmine is currently disabled.", ChatColor.GREEN);
/* 26 */       return true;
/*    */     }
/* 29 */     if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())
/*    */     {
/* 31 */       playerMsg("Explosions are currently disabled.", ChatColor.GREEN);
/* 32 */       return true;
/*    */     }
/* 35 */     double radius = 2.0D;
/* 37 */     if (args.length >= 1)
/*    */     {
/* 39 */       if ("list".equalsIgnoreCase(args[0]))
/*    */       {
/* 41 */         Iterator<TFM_LandmineData> landmines = TFM_LandmineData.landmines.iterator();
/* 42 */         while (landmines.hasNext()) {
/* 44 */           playerMsg(((TFM_LandmineData)landmines.next()).toString());
/*    */         }
/* 46 */         return true;
/*    */       }
/*    */       try
/*    */       {
/* 51 */         radius = Math.max(2.0D, Math.min(6.0D, Double.parseDouble(args[0])));
/*    */       }
/*    */       catch (NumberFormatException ex) {}
/*    */     }
/* 58 */     Block landmine = sender_p.getLocation().getBlock().getRelative(BlockFace.DOWN);
/* 59 */     landmine.setType(Material.TNT);
/* 60 */     TFM_LandmineData.landmines.add(new TFM_LandmineData(landmine.getLocation(), sender_p, radius));
/*    */     
/* 62 */     playerMsg("Landmine planted - Radius = " + radius + " blocks.", ChatColor.GREEN);
/*    */     
/* 64 */     return true;
/*    */   }
/*    */   
/*    */   public static class TFM_LandmineData
/*    */   {
/* 69 */     public static final List<TFM_LandmineData> landmines = new ArrayList();
/*    */     public final Location location;
/*    */     public final Player player;
/*    */     public final double radius;
/*    */     
/*    */     public TFM_LandmineData(Location location, Player player, double radius)
/*    */     {
/* 76 */       this.location = location;
/* 77 */       this.player = player;
/* 78 */       this.radius = radius;
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* 84 */       return location.toString() + ", " + radius + ", " + player.getName();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_landmine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */