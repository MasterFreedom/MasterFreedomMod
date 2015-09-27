/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Validates if a given account is premium.", usage="/<command> <player>", aliases="prem")
/*    */ public class Command_premium
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 21 */     if (args.length != 1) {
/* 23 */       return false;
/*    */     }
/* 26 */     Player player = getPlayer(args[0]);
/*    */     String name;
/*    */     final String name;
/* 29 */     if (player != null) {
/* 31 */       name = player.getName();
/*    */     } else {
/* 35 */       name = args[0];
/*    */     }
/* 38 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 45 */           URL getUrl = new URL("https://minecraft.net/haspaid.jsp?user=" + name);
/* 46 */           URLConnection urlConnection = getUrl.openConnection();
/*    */           
/* 48 */           BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
/* 49 */           final String message = ChatColor.DARK_GREEN + "Yes";
/* 50 */           in.close();
/*    */           
/* 52 */           new BukkitRunnable()
/*    */           {
/*    */             public void run()
/*    */             {
/* 57 */               playerMsg("Player " + val$name + " is premium: " + message);
/*    */             }
/* 57 */           }.runTask(plugin);
/*    */         }
/*    */         catch (Exception ex)
/*    */         {
/* 64 */           TFM_Log.severe(ex);
/* 65 */           playerMsg("There was an error querying the mojang server.", ChatColor.RED);
/*    */         }
/*    */       }
/* 65 */     }.runTaskAsynchronously(plugin);
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_premium.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */