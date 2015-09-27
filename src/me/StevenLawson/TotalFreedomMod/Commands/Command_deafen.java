/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Random;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.BOTH, blockHostConsole=true)
/*    */ @CommandParameters(description="Make some noise.", usage="/<command>")
/*    */ public class Command_deafen
/*    */   extends TFM_Command
/*    */ {
/* 15 */   private static final Random random = new Random();
/*    */   public static final double STEPS = 10.0D;
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 21 */     for (final Player player : server.getOnlinePlayers()) {
/* 23 */       for (double percent = 0.0D; percent <= 1.0D; percent += 0.1D)
/*    */       {
/* 25 */         final float pitch = (float)(percent * 2.0D);
/*    */         
/* 27 */         new BukkitRunnable()
/*    */         {
/*    */           public void run()
/*    */           {
/* 32 */             player.playSound(Command_deafen.randomOffset(player.getLocation(), 5.0D), org.bukkit.Sound.values()[Command_deafen.random.nextInt(org.bukkit.Sound.values().length)], 100.0F, pitch);
/*    */           }
/* 32 */         }.runTaskLater(plugin, Math.round(20.0D * percent * 2.0D));
/*    */       }
/*    */     }
/* 38 */     return true;
/*    */   }
/*    */   
/*    */   private static Location randomOffset(Location a, double magnitude)
/*    */   {
/* 43 */     return a.clone().add(randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude, randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude, randomDoubleRange(-1.0D, 1.0D).doubleValue() * magnitude);
/*    */   }
/*    */   
/*    */   private static Double randomDoubleRange(double min, double max)
/*    */   {
/* 48 */     return Double.valueOf(min + random.nextDouble() * (max - min + 1.0D));
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_deafen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */