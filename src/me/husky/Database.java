/*    */ package me.husky;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public abstract class Database
/*    */ {
/*    */   protected Plugin plugin;
/*    */   
/*    */   protected Database(Plugin plugin)
/*    */   {
/* 28 */     this.plugin = plugin;
/*    */   }
/*    */   
/*    */   public abstract Connection openConnection();
/*    */   
/*    */   public abstract boolean checkConnection();
/*    */   
/*    */   public abstract Connection getConnection();
/*    */   
/*    */   public abstract void closeConnection();
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\husky\Database.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */