/*     */ package me.husky.sqlite;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import me.husky.Database;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class SQLite
/*     */   extends Database
/*     */ {
/*     */   private final String dbLocation;
/*     */   private Connection connection;
/*     */   
/*     */   public SQLite(Plugin plugin, String dbLocation)
/*     */   {
/*  32 */     super(plugin);
/*  33 */     this.dbLocation = dbLocation;
/*  34 */     connection = null;
/*     */   }
/*     */   
/*     */   public Connection openConnection()
/*     */   {
/*  40 */     File file = new File(dbLocation);
/*  41 */     if (!file.exists()) {
/*     */       try
/*     */       {
/*  45 */         file.createNewFile();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*  49 */         plugin.getLogger().log(Level.SEVERE, "Unable to create database!");
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*  54 */       Class.forName("org.sqlite.JDBC");
/*  55 */       connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().toPath().toString() + "/" + dbLocation);
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  59 */       plugin.getLogger().log(Level.SEVERE, "Could not connect to SQLite server! because: " + e.getMessage());
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  63 */       plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found!");
/*     */     }
/*  65 */     return connection;
/*     */   }
/*     */   
/*     */   public boolean checkConnection()
/*     */   {
/*     */     try
/*     */     {
/*  73 */       return (connection != null) && (!connection.isClosed());
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  77 */       e.printStackTrace();
/*     */     }
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   public Connection getConnection()
/*     */   {
/*  85 */     return connection;
/*     */   }
/*     */   
/*     */   public void closeConnection()
/*     */   {
/*  91 */     if (connection != null) {
/*     */       try
/*     */       {
/*  95 */         connection.close();
/*     */       }
/*     */       catch (SQLException e)
/*     */       {
/*  99 */         plugin.getLogger().log(Level.SEVERE, "Error closing the SQLite Connection!");
/* 100 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\husky\sqlite\SQLite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */