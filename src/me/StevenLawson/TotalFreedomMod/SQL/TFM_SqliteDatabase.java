/*     */ package me.StevenLawson.TotalFreedomMod.SQL;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import me.husky.Database;
/*     */ import me.husky.sqlite.SQLite;
/*     */ 
/*     */ public class TFM_SqliteDatabase
/*     */ {
/*     */   private final Database sql;
/*     */   private final String table;
/*     */   private final String fields;
/*     */   private final List<Statement> statements;
/*     */   
/*     */   public TFM_SqliteDatabase(String filename, String table, String fields)
/*     */   {
/*  23 */     sql = new SQLite(TotalFreedomMod.plugin, filename);
/*  24 */     this.table = table;
/*  25 */     this.fields = fields;
/*  26 */     statements = new ArrayList();
/*     */   }
/*     */   
/*     */   public Statement addPreparedStatement(String query)
/*     */   {
/*  31 */     if (sql.checkConnection()) {
/*  33 */       throw new IllegalStateException("Can not add prepared statements after connecting!");
/*     */     }
/*  36 */     Statement statement = new Statement(query, null);
/*  37 */     statements.add(statement);
/*  38 */     return statement;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Database db()
/*     */   {
/*  44 */     return sql;
/*     */   }
/*     */   
/*     */   public boolean connect()
/*     */   {
/*  49 */     if (sql.checkConnection()) {
/*  51 */       return true;
/*     */     }
/*  54 */     Connection con = sql.openConnection();
/*  55 */     if (con == null) {
/*  57 */       return false;
/*     */     }
/*  60 */     if (!TFM_SqlUtil.hasTable(con, table))
/*     */     {
/*  62 */       TFM_Log.info("Creating table: " + table);
/*  64 */       if (!TFM_SqlUtil.createTable(con, table, fields))
/*     */       {
/*  66 */         TFM_Log.severe("Could not create table: " + table);
/*  67 */         return false;
/*     */       }
/*     */     }
/*  72 */     for (Statement statement : statements) {
/*  74 */       if (!statement.prepare()) {
/*  76 */         return false;
/*     */       }
/*     */     }
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*  85 */     sql.closeConnection();
/*     */   }
/*     */   
/*     */   public int purge()
/*     */   {
/*  90 */     if (!connect()) {
/*  92 */       return 0;
/*     */     }
/*  95 */     TFM_Log.warning("Truncating table: " + table);
/*     */     
/*  97 */     int result = TFM_SqlUtil.updateQuery(sql.getConnection(), "DELETE FROM " + table + ";");
/*  99 */     if (result == -1) {
/* 101 */       TFM_Log.warning("Could not truncate table: " + table);
/*     */     }
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   public class Statement
/*     */   {
/*     */     private final String query;
/*     */     private PreparedStatement statement;
/*     */     
/*     */     private Statement(String query)
/*     */     {
/* 114 */       this.query = query;
/*     */     }
/*     */     
/*     */     private boolean prepare()
/*     */     {
/*     */       try
/*     */       {
/* 121 */         statement = sql.getConnection().prepareStatement(query);
/* 122 */         return true;
/*     */       }
/*     */       catch (SQLException ex)
/*     */       {
/* 126 */         TFM_Log.severe("Could not prepare statement: " + query);
/* 127 */         TFM_Log.severe(ex);
/*     */       }
/* 128 */       return false;
/*     */     }
/*     */     
/*     */     public void invalidate()
/*     */     {
/* 134 */       statement = null;
/* 135 */       statements.remove(this);
/*     */     }
/*     */     
/*     */     public PreparedStatement getStatement()
/*     */     {
/* 140 */       return statement;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\SQL\TFM_SqliteDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */