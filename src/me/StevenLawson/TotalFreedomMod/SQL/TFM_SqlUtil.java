/*    */ package me.StevenLawson.TotalFreedomMod.SQL;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.DatabaseMetaData;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ 
/*    */ public class TFM_SqlUtil
/*    */ {
/*    */   public static boolean hasTable(Connection con, String table)
/*    */   {
/*    */     try
/*    */     {
/* 16 */       DatabaseMetaData dbm = con.getMetaData();
/* 17 */       ResultSet tables = dbm.getTables(null, null, table, null);
/* 18 */       return tables.next();
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 22 */       TFM_Log.severe(ex);
/*    */     }
/* 23 */     return false;
/*    */   }
/*    */   
/*    */   public static ResultSet executeQuery(Connection con, String query)
/*    */   {
/*    */     try
/*    */     {
/* 31 */       return con.createStatement().executeQuery(query);
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 35 */       TFM_Log.severe(ex);
/*    */     }
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public static int updateQuery(Connection con, String query)
/*    */   {
/*    */     try
/*    */     {
/* 44 */       return con.createStatement().executeUpdate(query);
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 48 */       TFM_Log.severe(ex);
/*    */     }
/* 49 */     return -1;
/*    */   }
/*    */   
/*    */   public static boolean createTable(Connection con, String name, String fields)
/*    */   {
/*    */     try
/*    */     {
/* 57 */       con.createStatement().execute("CREATE TABLE " + name + " (" + fields + ");");
/* 58 */       return true;
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 62 */       TFM_Log.severe(ex);
/*    */     }
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public static void close(ResultSet result)
/*    */   {
/* 69 */     if (result == null) {
/* 71 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 76 */       result.close();
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 80 */       TFM_Log.severe(ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean hasData(ResultSet result)
/*    */   {
/* 86 */     if (result == null) {
/* 88 */       return false;
/*    */     }
/*    */     try
/*    */     {
/* 93 */       return result.next();
/*    */     }
/*    */     catch (SQLException ex)
/*    */     {
/* 97 */       TFM_Log.severe(ex);
/*    */     }
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\SQL\TFM_SqlUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */