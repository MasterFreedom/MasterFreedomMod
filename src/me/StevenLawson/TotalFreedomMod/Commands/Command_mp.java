/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Ambient;
/*    */ import org.bukkit.entity.Creature;
/*    */ import org.bukkit.entity.EnderDragon;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.Ghast;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.entity.Slime;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Purge all mobs in all worlds.", usage="/<command>")
/*    */ public class Command_mp
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 22 */     playerMsg("Purging all mobs...");
/* 23 */     playerMsg(purgeMobs() + " mobs removed.");
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   public static int purgeMobs()
/*    */   {
/* 29 */     int removed = 0;
/* 30 */     for (World world : Bukkit.getWorlds()) {
/* 32 */       for (Entity ent : world.getLivingEntities()) {
/* 34 */         if (((ent instanceof Creature)) || ((ent instanceof Ghast)) || ((ent instanceof Slime)) || ((ent instanceof EnderDragon)) || ((ent instanceof Ambient)))
/*    */         {
/* 36 */           ent.remove();
/* 37 */           removed++;
/*    */         }
/*    */       }
/*    */     }
/* 42 */     return removed;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_mp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */