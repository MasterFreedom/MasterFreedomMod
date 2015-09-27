/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Callable;
/*     */ import me.StevenLawson.TotalFreedomMod.SQL.TFM_SqlUtil;
/*     */ import me.StevenLawson.TotalFreedomMod.SQL.TFM_SqliteDatabase;
/*     */ import me.StevenLawson.TotalFreedomMod.SQL.TFM_SqliteDatabase.Statement;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.parser.JSONParser;
/*     */ 
/*     */ public class TFM_UuidManager
/*     */ {
/*     */   public static final String TABLE_NAME = "uuids";
/*     */   
/*     */   private TFM_UuidManager()
/*     */   {
/*  36 */     throw new AssertionError();
/*     */   }
/*     */   
/*  40 */   private static final TFM_SqliteDatabase SQL = new TFM_SqliteDatabase("uuids.db", "uuids", "username VARCHAR(20) NOT NULL PRIMARY KEY, uuid CHAR(36) NOT NULL");
/*  45 */   private static final TFM_SqliteDatabase.Statement FIND = SQL.addPreparedStatement("SELECT * FROM uuids WHERE lower(username) = ?;");
/*  46 */   private static final TFM_SqliteDatabase.Statement UPDATE = SQL.addPreparedStatement("REPLACE INTO uuids (username, uuid) VALUES (?, ?);");
/*     */   
/*     */   public static void load()
/*     */   {
/*  51 */     SQL.connect();
/*     */   }
/*     */   
/*     */   public static void close()
/*     */   {
/*  55 */     SQL.close();
/*     */   }
/*     */   
/*     */   public static int purge()
/*     */   {
/*  59 */     return SQL.purge();
/*     */   }
/*     */   
/*     */   public static UUID newPlayer(Player player, String ip)
/*     */   {
/*  63 */     TFM_Log.info("Obtaining UUID for new player: " + player.getName());
/*     */     
/*  65 */     String username = player.getName().toLowerCase();
/*     */     
/*  68 */     UUID dbUuid = find(username);
/*  69 */     if (dbUuid != null) {
/*  70 */       return dbUuid;
/*     */     }
/*  75 */     UUID uuid = TFM_UuidResolver.getUUIDOf(username);
/*  76 */     if (uuid == null) {
/*  78 */       uuid = generateSpoofUuid(username);
/*     */     }
/*  81 */     update(username, uuid);
/*  82 */     return uuid;
/*     */   }
/*     */   
/*     */   public static UUID getUniqueId(OfflinePlayer offlinePlayer)
/*     */   {
/*  87 */     if ((offlinePlayer.isOnline()) && (TFM_PlayerData.hasPlayerData(offlinePlayer.getPlayer()))) {
/*  88 */       return TFM_PlayerData.getPlayerData(offlinePlayer.getPlayer()).getUniqueId();
/*     */     }
/*  92 */     return getUniqueId(offlinePlayer.getName());
/*     */   }
/*     */   
/*     */   public static UUID getUniqueId(String username)
/*     */   {
/*  97 */     UUID dbUuid = find(username);
/*  98 */     if (dbUuid != null) {
/*  99 */       return dbUuid;
/*     */     }
/* 103 */     UUID apiUuid = TFM_UuidResolver.getUUIDOf(username);
/* 104 */     if (apiUuid != null) {
/* 105 */       return apiUuid;
/*     */     }
/* 109 */     return generateSpoofUuid(username);
/*     */   }
/*     */   
/*     */   public static void rawSetUUID(String name, UUID uuid)
/*     */   {
/* 113 */     if ((name == null) || (uuid == null) || (name.isEmpty()))
/*     */     {
/* 114 */       TFM_Log.warning("Not setting raw UUID: name and uuid may not be null!");
/* 115 */       return;
/*     */     }
/* 118 */     update(name.toLowerCase().trim(), uuid);
/*     */   }
/*     */   
/*     */   private static UUID find(String searchName)
/*     */   {
/* 122 */     if (!SQL.connect()) {
/* 123 */       return null;
/*     */     }
/*     */     ResultSet result;
/*     */     try
/*     */     {
/* 128 */       PreparedStatement statement = FIND.getStatement();
/* 129 */       statement.clearParameters();
/* 130 */       statement.setString(1, searchName.toLowerCase());
/* 131 */       result = statement.executeQuery();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 133 */       TFM_Log.severe("Could not execute find statement!");
/* 134 */       TFM_Log.severe(ex);
/* 135 */       return null;
/*     */     }
/* 138 */     if (!TFM_SqlUtil.hasData(result))
/*     */     {
/* 139 */       TFM_SqlUtil.close(result);
/* 140 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 144 */       String uuidString = result.getString("uuid");
/* 145 */       return UUID.fromString(uuidString);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */       UUID localUUID;
/* 147 */       TFM_Log.severe(ex);
/* 148 */       return null;
/*     */     }
/*     */     finally
/*     */     {
/* 150 */       TFM_SqlUtil.close(result);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean update(String username, UUID uuid)
/*     */   {
/* 155 */     if (!SQL.connect()) {
/* 156 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 160 */       PreparedStatement statement = UPDATE.getStatement();
/* 161 */       statement.clearParameters();
/* 162 */       statement.setString(1, username.toLowerCase());
/* 163 */       statement.setString(2, uuid.toString());
/* 164 */       statement.executeUpdate();
/* 165 */       return true;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 167 */       TFM_Log.severe("Could not execute update statement!");
/* 168 */       TFM_Log.severe(ex);
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   private static UUID generateSpoofUuid(String name)
/*     */   {
/* 174 */     name = name.toLowerCase();
/* 175 */     TFM_Log.info("Generating spoof UUID for " + name);
/*     */     try
/*     */     {
/* 178 */       MessageDigest digest = MessageDigest.getInstance("SHA1");
/* 179 */       byte[] result = digest.digest(name.getBytes());
/* 180 */       StringBuilder builder = new StringBuilder();
/* 181 */       for (int i = 0; i < result.length; i++) {
/* 182 */         builder.append(Integer.toString((result[i] & 0xFF) + 256, 16).substring(1));
/*     */       }
/* 185 */       return UUID.fromString("deadbeef-" + builder.substring(8, 12) + "-" + builder.substring(12, 16) + "-" + builder.substring(16, 20) + "-" + builder.substring(20, 32));
/*     */     }
/*     */     catch (NoSuchAlgorithmException ex)
/*     */     {
/* 192 */       TFM_Log.warning("Could not generate spoof UUID: SHA1 algorithm not found!");
/*     */     }
/* 195 */     return UUID.randomUUID();
/*     */   }
/*     */   
/*     */   public static class TFM_UuidResolver
/*     */     implements Callable<Map<String, UUID>>
/*     */   {
/*     */     private static final double PROFILES_PER_REQUEST = 100.0D;
/*     */     private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
/* 202 */     private final JSONParser jsonParser = new JSONParser();
/*     */     private final List<String> names;
/*     */     
/*     */     public TFM_UuidResolver(List<String> names)
/*     */     {
/* 206 */       this.names = ImmutableList.copyOf(names);
/*     */     }
/*     */     
/*     */     public Map<String, UUID> call()
/*     */     {
/* 211 */       Map<String, UUID> uuidMap = new HashMap();
/* 212 */       int requests = (int)Math.ceil(names.size() / 100.0D);
/* 213 */       for (int i = 0; i < requests; i++) {
/*     */         try
/*     */         {
/* 215 */           URL url = new URL("https://api.mojang.com/profiles/minecraft");
/* 216 */           HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/*     */           
/* 218 */           connection.setRequestMethod("POST");
/* 219 */           connection.setRequestProperty("Content-Type", "application/json");
/* 220 */           connection.setUseCaches(false);
/* 221 */           connection.setDoInput(true);
/* 222 */           connection.setDoOutput(true);
/*     */           
/* 224 */           String body = JSONArray.toJSONString(names.subList(i * 100, Math.min((i + 1) * 100, names.size())));
/*     */           
/* 226 */           OutputStream stream = connection.getOutputStream();
/* 227 */           stream.write(body.getBytes());
/* 228 */           stream.flush();
/* 229 */           stream.close();
/*     */           
/* 231 */           JSONArray array = (JSONArray)jsonParser.parse(new InputStreamReader(connection.getInputStream()));
/* 233 */           for (Object profile : array)
/*     */           {
/* 234 */             JSONObject jsonProfile = (JSONObject)profile;
/* 235 */             String id = (String)jsonProfile.get("id");
/* 236 */             String name = (String)jsonProfile.get("name");
/* 237 */             UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
/*     */             
/* 243 */             uuidMap.put(name, uuid);
/*     */           }
/* 246 */           if (i != requests - 1) {
/* 247 */             Thread.sleep(100L);
/*     */           }
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 250 */           TFM_Log.severe("Could not resolve UUID(s) of " + StringUtils.join(names.subList(i * 100, Math.min((i + 1) * 100, names.size())), ", "));
/*     */         }
/*     */       }
/* 255 */       return uuidMap;
/*     */     }
/*     */     
/*     */     public static UUID getUUIDOf(String name)
/*     */     {
/* 259 */       return (UUID)new TFM_UuidResolver(Arrays.asList(new String[] { name })).call().get(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_UuidManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */