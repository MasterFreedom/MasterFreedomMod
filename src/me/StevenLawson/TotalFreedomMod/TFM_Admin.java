/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ 
/*     */ public class TFM_Admin
/*     */ {
/*     */   private final UUID uuid;
/*     */   private String lastLoginName;
/*     */   private final String loginMessage;
/*     */   private final boolean isSeniorAdmin;
/*     */   private final boolean isTelnetAdmin;
/*     */   private final List<String> consoleAliases;
/*     */   private final List<String> ips;
/*     */   private Date lastLogin;
/*     */   private boolean isActivated;
/*     */   
/*     */   public TFM_Admin(UUID uuid, String lastLoginName, Date lastLogin, String loginMessage, boolean isTelnetAdmin, boolean isSeniorAdmin, boolean isActivated)
/*     */   {
/*  28 */     this.uuid = uuid;
/*  29 */     this.lastLoginName = lastLoginName;
/*  30 */     ips = new ArrayList();
/*  31 */     this.lastLogin = lastLogin;
/*  32 */     this.loginMessage = loginMessage;
/*  33 */     this.isTelnetAdmin = isTelnetAdmin;
/*  34 */     this.isSeniorAdmin = isSeniorAdmin;
/*  35 */     consoleAliases = new ArrayList();
/*  36 */     this.isActivated = isActivated;
/*     */   }
/*     */   
/*     */   public TFM_Admin(UUID uuid, ConfigurationSection section)
/*     */   {
/*  41 */     this.uuid = uuid;
/*  42 */     lastLoginName = section.getString("last_login_name");
/*  43 */     ips = section.getStringList("ips");
/*  44 */     lastLogin = TFM_Util.stringToDate(section.getString("last_login", TFM_Util.dateToString(new Date(0L))));
/*  45 */     loginMessage = section.getString("custom_login_message", "");
/*  46 */     isSeniorAdmin = section.getBoolean("is_senior_admin", false);
/*  47 */     isTelnetAdmin = section.getBoolean("is_telnet_admin", false);
/*  48 */     consoleAliases = section.getStringList("console_aliases");
/*  49 */     isActivated = section.getBoolean("is_activated", true);
/*  51 */     for (Iterator<?> it = TFM_MainConfig.getList(TFM_ConfigEntry.NOADMIN_IPS).iterator(); it.hasNext();) {
/*  53 */       ips.remove((String)it.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  60 */     StringBuilder output = new StringBuilder();
/*     */     
/*  62 */     output.append("UUID: ").append(uuid.toString()).append("\n");
/*  63 */     output.append("- Last Login Name: ").append(lastLoginName).append("\n");
/*  64 */     output.append("- IPs: ").append(StringUtils.join(ips, ", ")).append("\n");
/*  65 */     output.append("- Last Login: ").append(TFM_Util.dateToString(lastLogin)).append("\n");
/*  66 */     output.append("- Custom Login Message: ").append(loginMessage).append("\n");
/*  67 */     output.append("- Is Senior Admin: ").append(isSeniorAdmin).append("\n");
/*  68 */     output.append("- Is Telnet Admin: ").append(isTelnetAdmin).append("\n");
/*  69 */     output.append("- Console Aliases: ").append(StringUtils.join(consoleAliases, ", ")).append("\n");
/*  70 */     output.append("- Is Activated: ").append(isActivated);
/*     */     
/*  72 */     return output.toString();
/*     */   }
/*     */   
/*     */   public UUID getUniqueId()
/*     */   {
/*  77 */     return uuid;
/*     */   }
/*     */   
/*     */   public void setLastLoginName(String lastLoginName)
/*     */   {
/*  82 */     this.lastLoginName = lastLoginName;
/*     */   }
/*     */   
/*     */   public String getLastLoginName()
/*     */   {
/*  87 */     return lastLoginName;
/*     */   }
/*     */   
/*     */   public List<String> getIps()
/*     */   {
/*  92 */     return Collections.unmodifiableList(ips);
/*     */   }
/*     */   
/*     */   public void addIp(String ip)
/*     */   {
/*  97 */     if (!ips.contains(ip)) {
/*  99 */       ips.add(ip);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addIps(List<String> ips)
/*     */   {
/* 105 */     for (String ip : ips) {
/* 107 */       addIp(ip);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeIp(String ip)
/*     */   {
/* 113 */     if (ips.contains(ip)) {
/* 115 */       ips.remove(ip);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearIPs()
/*     */   {
/* 121 */     ips.clear();
/*     */   }
/*     */   
/*     */   public Date getLastLogin()
/*     */   {
/* 126 */     return lastLogin;
/*     */   }
/*     */   
/*     */   public String getCustomLoginMessage()
/*     */   {
/* 131 */     return loginMessage;
/*     */   }
/*     */   
/*     */   public boolean isSeniorAdmin()
/*     */   {
/* 136 */     return isSeniorAdmin;
/*     */   }
/*     */   
/*     */   public boolean isTelnetAdmin()
/*     */   {
/* 141 */     return isTelnetAdmin;
/*     */   }
/*     */   
/*     */   public List<String> getConsoleAliases()
/*     */   {
/* 146 */     return Collections.unmodifiableList(consoleAliases);
/*     */   }
/*     */   
/*     */   public void setLastLogin(Date lastLogin)
/*     */   {
/* 151 */     this.lastLogin = lastLogin;
/*     */   }
/*     */   
/*     */   public boolean isActivated()
/*     */   {
/* 156 */     return isActivated;
/*     */   }
/*     */   
/*     */   public void setActivated(boolean isActivated)
/*     */   {
/* 161 */     this.isActivated = isActivated;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Admin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */