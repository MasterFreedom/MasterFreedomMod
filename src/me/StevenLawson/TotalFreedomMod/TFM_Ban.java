/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.ChatColor;
/*     */ 
/*     */ public class TFM_Ban
/*     */ {
/*  13 */   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
/*  21 */   public static final Pattern IP_BAN_REGEX = Pattern.compile("^((?:(?:\\*|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.){3}(?:\\*|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))):([\\w\\s]+):([\\w]+):(\\d+):([\\s\\S]+)$");
/*  28 */   public static final Pattern UUID_BAN_REGEX = Pattern.compile("^([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}):([\\w\\s]+):([\\w]+):(\\d+):([\\s\\S]+)$");
/*     */   private final BanType type;
/*     */   private final boolean complete;
/*     */   private String subject;
/*     */   private String lastLoginName;
/*     */   private String by;
/*     */   private long expireUnix;
/*     */   private String reason;
/*     */   
/*     */   public TFM_Ban(String ip, String lastLoginName)
/*     */   {
/*  45 */     this(ip, lastLoginName, null, null, null);
/*     */   }
/*     */   
/*     */   public TFM_Ban(String ip, String lastLoginName, String sender, Date expire, String reason)
/*     */   {
/*  50 */     this(ip, lastLoginName, sender, expire, reason, BanType.IP);
/*     */   }
/*     */   
/*     */   public TFM_Ban(UUID uuid, String lastLoginName)
/*     */   {
/*  55 */     this(uuid, lastLoginName, null, null, null);
/*     */   }
/*     */   
/*     */   public TFM_Ban(UUID uuid, String lastLoginName, String sender, Date expire, String reason)
/*     */   {
/*  60 */     this(uuid.toString(), lastLoginName, sender, expire, reason, BanType.UUID);
/*     */   }
/*     */   
/*     */   private TFM_Ban(String subject, String lastLoginName, String sender, Date expire, String reason, BanType type)
/*     */   {
/*  65 */     this.type = type;
/*  66 */     this.subject = subject;
/*  67 */     this.lastLoginName = (lastLoginName == null ? "none" : lastLoginName);
/*  68 */     by = (sender == null ? "none" : sender);
/*  69 */     expireUnix = (expire == null ? 0L : TFM_Util.getUnixTime(expire));
/*  70 */     this.reason = (reason == null ? "none" : reason);
/*  71 */     complete = true;
/*     */   }
/*     */   
/*     */   public TFM_Ban(String banString, BanType type)
/*     */   {
/*  76 */     this.type = type;
/*     */     
/*  78 */     Matcher matcher = type == BanType.IP ? IP_BAN_REGEX.matcher(banString) : UUID_BAN_REGEX.matcher(banString);
/*  80 */     if (!matcher.find())
/*     */     {
/*  82 */       complete = false;
/*  83 */       return;
/*     */     }
/*  86 */     subject = matcher.group(1);
/*  87 */     lastLoginName = matcher.group(2);
/*  88 */     by = matcher.group(3);
/*  89 */     expireUnix = Long.valueOf(matcher.group(4)).longValue();
/*  90 */     reason = TFM_Util.colorize(matcher.group(5));
/*  91 */     complete = true;
/*     */   }
/*     */   
/*     */   public static enum BanType
/*     */   {
/*  96 */     IP,  UUID;
/*     */     
/*     */     private BanType() {}
/*     */   }
/*     */   
/*     */   public BanType getType()
/*     */   {
/* 102 */     return type;
/*     */   }
/*     */   
/*     */   public String getSubject()
/*     */   {
/* 107 */     return subject;
/*     */   }
/*     */   
/*     */   public String getLastLoginName()
/*     */   {
/* 112 */     return lastLoginName;
/*     */   }
/*     */   
/*     */   public String getBannedBy()
/*     */   {
/* 117 */     return by;
/*     */   }
/*     */   
/*     */   public long getExpireUnix()
/*     */   {
/* 122 */     return expireUnix;
/*     */   }
/*     */   
/*     */   public String getReason()
/*     */   {
/* 127 */     return reason;
/*     */   }
/*     */   
/*     */   public boolean isExpired()
/*     */   {
/* 132 */     return (expireUnix != 0L) && (expireUnix < TFM_Util.getUnixTime());
/*     */   }
/*     */   
/*     */   public boolean isComplete()
/*     */   {
/* 137 */     return complete;
/*     */   }
/*     */   
/*     */   public String getKickMessage()
/*     */   {
/* 142 */     StringBuilder message = new StringBuilder("You");
/*     */     
/* 144 */     message.append(type == BanType.IP ? "r IP address is" : " are").append(" temporarily banned from this server.");
/* 145 */     message.append("\nAppeal at ").append(ChatColor.GOLD).append(TFM_ConfigEntry.SERVER_BAN_URL.getString());
/* 147 */     if (!reason.equals("none")) {
/* 149 */       message.append("\nReason: ").append(reason);
/*     */     }
/* 152 */     if (getExpireUnix() != 0L) {
/* 154 */       message.append("\nYour ban will be removed on ").append(DATE_FORMAT.format(TFM_Util.getUnixDate(expireUnix)));
/*     */     }
/* 157 */     return message.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 164 */     return subject + ":" + lastLoginName + ":" + by + ":" + expireUnix + ":" + TFM_Util.decolorize(reason);
/*     */   }
/*     */   
/*     */   public boolean equals(Object object)
/*     */   {
/* 170 */     if (object == null) {
/* 172 */       return false;
/*     */     }
/* 175 */     if (!(object instanceof TFM_Ban)) {
/* 177 */       return false;
/*     */     }
/* 180 */     TFM_Ban ban = (TFM_Ban)object;
/* 182 */     if (toString().equals(ban.toString())) {
/* 184 */       return true;
/*     */     }
/* 187 */     if (getType() != ban.getType()) {
/* 189 */       return false;
/*     */     }
/* 192 */     if (!getSubject().equals(ban.getSubject())) {
/* 194 */       return false;
/*     */     }
/* 197 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 203 */     int prime = 37;
/* 204 */     int result = 1;
/* 205 */     result = 37 * result + getType().hashCode();
/* 206 */     result = 37 * result + getSubject().hashCode();
/* 207 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Ban.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */