/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ 
/*     */ public class TFM_TwitterHandler
/*     */ {
/*     */   private TFM_TwitterHandler()
/*     */   {
/*  15 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static String getTwitter(String player)
/*     */   {
/*  20 */     return request("action=gettwitter&player=" + player);
/*     */   }
/*     */   
/*     */   public static String setTwitter(String player, String twitter)
/*     */   {
/*  25 */     if (twitter.startsWith("@")) {
/*  27 */       twitter = twitter.replaceAll("@", "");
/*     */     }
/*  29 */     return request("action=settwitter&player=" + player + "&twitter=" + twitter);
/*     */   }
/*     */   
/*     */   public static String delTwitter(String player)
/*     */   {
/*  34 */     return request("action=deltwitter&player=" + player);
/*     */   }
/*     */   
/*     */   public static void delTwitterVerbose(String targetName, CommandSender sender)
/*     */   {
/*  39 */     String reply = delTwitter(targetName);
/*  40 */     if ("ok".equals(reply))
/*     */     {
/*  42 */       TFM_Util.adminAction(sender.getName(), "Removing " + targetName + " from TwitterBot", true);
/*     */     }
/*  44 */     else if ("disabled".equals(reply))
/*     */     {
/*  46 */       TFM_Util.playerMsg(sender, "Warning: Could not check if player has a twitter handle!");
/*  47 */       TFM_Util.playerMsg(sender, "TwitterBot has been temporarily disabled, please wait until it gets re-enabled", ChatColor.RED);
/*     */     }
/*  49 */     else if ("failed".equals(reply))
/*     */     {
/*  51 */       TFM_Util.playerMsg(sender, "Warning: Could not check if player has a twitter handle!");
/*  52 */       TFM_Util.playerMsg(sender, "There was a problem querying the database, please let a developer know.", ChatColor.RED);
/*     */     }
/*  54 */     else if ("false".equals(reply))
/*     */     {
/*  56 */       TFM_Util.playerMsg(sender, "Warning: Could not check if player has a twitter handle!");
/*  57 */       TFM_Util.playerMsg(sender, "There was a problem with the database, please let a developer know.", ChatColor.RED);
/*     */     }
/*  59 */     else if ("cannotauth".equals(reply))
/*     */     {
/*  61 */       TFM_Util.playerMsg(sender, "Warning: Could not check if player has a twitter handle!");
/*  62 */       TFM_Util.playerMsg(sender, "The database password is incorrect, please let a developer know.", ChatColor.RED);
/*     */     }
/*  64 */     else if ("notfound".equals(reply))
/*     */     {
/*  66 */       TFM_Util.playerMsg(sender, targetName + " did not have a twitter handle registered to their name.", ChatColor.GREEN);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String isEnabled()
/*     */   {
/*  72 */     return request("action=getstatus");
/*     */   }
/*     */   
/*     */   public static String setEnabled(String status)
/*     */   {
/*  77 */     return request("action=setstatus&status=" + status);
/*     */   }
/*     */   
/*     */   private static String request(String queryString)
/*     */   {
/*  82 */     String line = "failed";
/*     */     
/*  84 */     String twitterbotURL = TFM_ConfigEntry.TWITTERBOT_URL.getString();
/*  85 */     String twitterbotSecret = TFM_ConfigEntry.TWITTERBOT_SECRET.getString();
/*  87 */     if ((twitterbotURL != null) && (twitterbotSecret != null) && (!twitterbotURL.isEmpty()) && (!twitterbotSecret.isEmpty())) {
/*     */       try
/*     */       {
/*  91 */         URL getUrl = new URL(twitterbotURL + "?auth=" + twitterbotSecret + "&" + queryString);
/*  92 */         URLConnection urlConnection = getUrl.openConnection();
/*     */         
/*  94 */         BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
/*  95 */         line = in.readLine();
/*  96 */         in.close();
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 100 */         TFM_Log.severe(ex);
/*     */       }
/*     */     }
/* 104 */     return line;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_TwitterHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */