/*     */ package me.husky.mysql;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import me.husky.Database;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MySQL
/*     */   extends Database
/*     */ {
/*     */   private final String user;
/*     */   private final String database;
/*     */   private final String password;
/*     */   private final String port;
/*     */   private final String hostname;
/*     */   private Connection connection;
/*     */   
/*     */   public MySQL(Plugin plugin, String hostname, String port, String database, String username, String password)
/*     */   {
/*  41 */     super(plugin);
/*  42 */     this.hostname = hostname;
/*  43 */     this.port = port;
/*  44 */     this.database = database;
/*  45 */     user = username;
/*  46 */     this.password = password;
/*  47 */     connection = null;
/*     */   }
/*     */   
/*     */   public Connection openConnection()
/*     */   {
/*     */     try
/*     */     {
/*  55 */       Class.forName("com.mysql.jdbc.Driver");
/*  56 */       connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, user, password);
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  60 */       plugin.getLogger().log(Level.SEVERE, "Could not connect to MySQL server! because: " + e.getMessage());
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  64 */       plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found!");
/*     */     }
/*  66 */     return connection;
/*     */   }
/*     */   
/*     */   public boolean checkConnection()
/*     */   {
/*  72 */     return connection != null;
/*     */   }
/*     */   
/*     */   public Connection getConnection()
/*     */   {
/*  78 */     return connection;
/*     */   }
/*     */   
/*     */   public void closeConnection()
/*     */   {
/*  84 */     if (connection != null) {
/*     */       try
/*     */       {
/*  88 */         connection.close();
/*     */       }
/*     */       catch (SQLException e)
/*     */       {
/*  92 */         plugin.getLogger().log(Level.SEVERE, "Error closing the MySQL Connection!");
/*  93 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ResultSet querySQL(String query)
/*     */   {
/* 100 */     Connection c = null;
/* 102 */     if (checkConnection()) {
/* 104 */       c = getConnection();
/*     */     } else {
/* 108 */       c = openConnection();
/*     */     }
/* 111 */     Statement s = null;
/*     */     try
/*     */     {
/* 115 */       s = c.createStatement();
/*     */     }
/*     */     catch (SQLException e1)
/*     */     {
/* 119 */       e1.printStackTrace();
/*     */     }
/* 122 */     ResultSet ret = null;
/*     */     try
/*     */     {
/* 126 */       ret = s.executeQuery(query);
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 130 */       e.printStackTrace();
/*     */     }
/* 133 */     closeConnection();
/*     */     
/* 135 */     return ret;
/*     */   }
/*     */   
/*     */   public void updateSQL(String update)
/*     */   {
/* 141 */     Connection c = null;
/* 143 */     if (checkConnection()) {
/* 145 */       c = getConnection();
/*     */     } else {
/* 149 */       c = openConnection();
/*     */     }
/* 152 */     Statement s = null;
/*     */     try
/*     */     {
/* 156 */       s = c.createStatement();
/* 157 */       s.executeUpdate(update);
/*     */     }
/*     */     catch (SQLException e1)
/*     */     {
/* 161 */       e1.printStackTrace();
/*     */     }
/* 164 */     closeConnection();
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\husky\mysql\MySQL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */