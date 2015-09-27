/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ import org.apache.commons.lang.math.DoubleRange;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="View ticks-per-second", usage="/<command>")
/*    */ public class Command_health
/*    */   extends TFM_Command
/*    */ {
/*    */   private static final int BYTES_PER_MB = 1048576;
/* 20 */   private static final DoubleRange TPS_RANGE = new DoubleRange(19.9D, 20.1D);
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 25 */     Runtime runtime = Runtime.getRuntime();
/* 26 */     long usedMem = runtime.totalMemory() - runtime.freeMemory();
/*    */     
/* 28 */     playerMsg("Reserved Memory: " + runtime.totalMemory() / 1048576.0D + "mb");
/* 29 */     playerMsg("Used Memory: " + new DecimalFormat("#").format(usedMem / 1048576.0D) + "mb (" + new DecimalFormat("#").format(usedMem / runtime.totalMemory() * 100.0D) + "%)");
/* 30 */     playerMsg("Max Memory: " + runtime.maxMemory() / 1048576.0D + "mb");
/* 31 */     playerMsg("Calculating ticks per second, please wait...");
/*    */     
/* 33 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 40 */           Command_health.TFM_TickMeter tickMeter = new Command_health.TFM_TickMeter(Command_health.this, plugin);
/* 41 */           tickMeter.startTicking();
/* 42 */           Thread.sleep(2500L);
/* 43 */           final double ticksPerSecond = tickMeter.stopTicking();
/*    */           
/* 45 */           new BukkitRunnable()
/*    */           {
/*    */             public void run()
/*    */             {
/* 50 */               playerMsg("Ticks per second: " + (Command_health.TPS_RANGE.containsDouble(ticksPerSecond) ? ChatColor.GREEN : ChatColor.RED) + ticksPerSecond);
/*    */             }
/* 50 */           }.runTask(plugin);
/*    */         }
/*    */         catch (Exception ex)
/*    */         {
/* 56 */           TFM_Log.severe(ex);
/*    */         }
/*    */       }
/* 56 */     }.runTaskAsynchronously(plugin);
/*    */     
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   private class TFM_TickMeter
/*    */   {
/* 66 */     private final AtomicInteger ticks = new AtomicInteger();
/*    */     private final TotalFreedomMod plugin;
/*    */     private long startTime;
/*    */     private BukkitTask task;
/*    */     
/*    */     public TFM_TickMeter(TotalFreedomMod plugin)
/*    */     {
/* 73 */       this.plugin = plugin;
/*    */     }
/*    */     
/*    */     public void startTicking()
/*    */     {
/* 78 */       startTime = System.currentTimeMillis();
/* 79 */       ticks.set(0);
/*    */       
/* 81 */       task = new BukkitRunnable()
/*    */       {
/*    */         public void run()
/*    */         {
/* 86 */           ticks.incrementAndGet();
/*    */         }
/* 86 */       }.runTaskTimer(plugin, 0L, 1L);
/*    */     }
/*    */     
/*    */     public double stopTicking()
/*    */     {
/* 93 */       task.cancel();
/* 94 */       long elapsed = System.currentTimeMillis() - startTime;
/* 95 */       int tickCount = ticks.get();
/*    */       
/* 97 */       return tickCount / (elapsed / 1000.0D);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_health.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */