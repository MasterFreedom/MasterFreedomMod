/*     */ package me.StevenLawson.TotalFreedomMod.World;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.World.Environment;
/*     */ import org.bukkit.WorldCreator;
/*     */ import org.bukkit.WorldType;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public final class TFM_AdminWorld
/*     */   extends TFM_CustomWorld
/*     */ {
/*     */   private static final long CACHE_CLEAR_FREQUENCY = 30000L;
/*     */   private static final long TP_COOLDOWN_TIME = 500L;
/*  33 */   private static final String GENERATION_PARAMETERS = TFM_ConfigEntry.FLATLANDS_GENERATE_PARAMS.getString();
/*     */   private static final String WORLD_NAME = "adminworld";
/*  36 */   private final Map<Player, Long> teleportCooldown = new HashMap();
/*  37 */   private final Map<CommandSender, Boolean> accessCache = new HashMap();
/*  39 */   private Long cacheLastCleared = null;
/*  40 */   private Map<Player, Player> guestList = new HashMap();
/*  41 */   private WeatherMode weatherMode = WeatherMode.OFF;
/*  42 */   private TimeOfDay timeOfDay = TimeOfDay.INHERIT;
/*     */   
/*     */   public void sendToWorld(Player player)
/*     */   {
/*  51 */     if (!canAccessWorld(player)) {
/*  53 */       return;
/*     */     }
/*  56 */     super.sendToWorld(player);
/*     */   }
/*     */   
/*     */   protected World generateWorld()
/*     */   {
/*  62 */     WorldCreator worldCreator = new WorldCreator("adminworld");
/*  63 */     worldCreator.generateStructures(false);
/*  64 */     worldCreator.type(WorldType.NORMAL);
/*  65 */     worldCreator.environment(World.Environment.NORMAL);
/*  66 */     worldCreator.generator(new CleanroomChunkGenerator(GENERATION_PARAMETERS));
/*     */     
/*  68 */     World world = Bukkit.getServer().createWorld(worldCreator);
/*     */     
/*  70 */     world.setSpawnFlags(false, false);
/*  71 */     world.setSpawnLocation(0, 50, 0);
/*     */     
/*  73 */     Block welcomeSignBlock = world.getBlockAt(0, 50, 0);
/*  74 */     welcomeSignBlock.setType(Material.SIGN_POST);
/*  75 */     org.bukkit.block.Sign welcomeSign = (org.bukkit.block.Sign)welcomeSignBlock.getState();
/*     */     
/*  77 */     org.bukkit.material.Sign signData = (org.bukkit.material.Sign)welcomeSign.getData();
/*  78 */     signData.setFacingDirection(BlockFace.NORTH);
/*     */     
/*  80 */     welcomeSign.setLine(0, ChatColor.GREEN + "AdminWorld");
/*  81 */     welcomeSign.setLine(1, ChatColor.DARK_GRAY + "---");
/*  82 */     welcomeSign.setLine(2, ChatColor.YELLOW + "Spawn Point");
/*  83 */     welcomeSign.setLine(3, ChatColor.DARK_GRAY + "---");
/*  84 */     welcomeSign.update();
/*     */     
/*  86 */     TFM_GameRuleHandler.commitGameRules();
/*     */     
/*  88 */     return world;
/*     */   }
/*     */   
/*     */   public boolean addGuest(Player guest, Player supervisor)
/*     */   {
/*  93 */     if ((guest == supervisor) || (TFM_AdminList.isSuperAdmin(guest))) {
/*  95 */       return false;
/*     */     }
/*  98 */     if (TFM_AdminList.isSuperAdmin(supervisor))
/*     */     {
/* 100 */       guestList.put(guest, supervisor);
/* 101 */       wipeAccessCache();
/* 102 */       return true;
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public Player removeGuest(Player guest)
/*     */   {
/* 110 */     Player player = (Player)guestList.remove(guest);
/* 111 */     wipeAccessCache();
/* 112 */     return player;
/*     */   }
/*     */   
/*     */   public Player removeGuest(String partialName)
/*     */   {
/* 117 */     partialName = partialName.toLowerCase();
/* 118 */     Iterator<Player> it = guestList.keySet().iterator();
/* 120 */     while (it.hasNext())
/*     */     {
/* 122 */       Player player = (Player)it.next();
/* 123 */       if (player.getName().toLowerCase().contains(partialName))
/*     */       {
/* 125 */         removeGuest(player);
/* 126 */         return player;
/*     */       }
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   public String guestListToString()
/*     */   {
/* 135 */     List<String> output = new ArrayList();
/* 136 */     Iterator<Map.Entry<Player, Player>> it = guestList.entrySet().iterator();
/* 137 */     while (it.hasNext())
/*     */     {
/* 139 */       Map.Entry<Player, Player> entry = (Map.Entry)it.next();
/* 140 */       Player player = (Player)entry.getKey();
/* 141 */       Player supervisor = (Player)entry.getValue();
/* 142 */       output.add(player.getName() + " (Supervisor: " + supervisor.getName() + ")");
/*     */     }
/* 144 */     return StringUtils.join(output, ", ");
/*     */   }
/*     */   
/*     */   public void purgeGuestList()
/*     */   {
/* 149 */     guestList.clear();
/* 150 */     wipeAccessCache();
/*     */   }
/*     */   
/*     */   public boolean validateMovement(PlayerMoveEvent event)
/*     */   {
/*     */     World world;
/*     */     try
/*     */     {
/* 158 */       world = getWorld();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 162 */       return true;
/*     */     }
/* 165 */     if ((world != null) && (event.getTo().getWorld() == world))
/*     */     {
/* 167 */       final Player player = event.getPlayer();
/* 168 */       if (!canAccessWorld(player))
/*     */       {
/* 170 */         Long lastTP = (Long)teleportCooldown.get(player);
/* 171 */         long currentTimeMillis = System.currentTimeMillis();
/* 172 */         if ((lastTP == null) || (lastTP.longValue() + 500L <= currentTimeMillis))
/*     */         {
/* 174 */           teleportCooldown.put(player, Long.valueOf(currentTimeMillis));
/* 175 */           TFM_Log.info(player.getName() + " attempted to access the AdminWorld.");
/* 176 */           new BukkitRunnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 181 */               player.teleport(((World)Bukkit.getWorlds().get(0)).getSpawnLocation());
/*     */             }
/* 181 */           }.runTaskLater(TotalFreedomMod.plugin, 1L);
/*     */         }
/* 185 */         event.setCancelled(true);
/* 186 */         return false;
/*     */       }
/*     */     }
/* 190 */     return true;
/*     */   }
/*     */   
/*     */   public void wipeAccessCache()
/*     */   {
/* 195 */     cacheLastCleared = Long.valueOf(System.currentTimeMillis());
/* 196 */     accessCache.clear();
/*     */   }
/*     */   
/*     */   public boolean canAccessWorld(Player player)
/*     */   {
/* 201 */     long currentTimeMillis = System.currentTimeMillis();
/* 202 */     if ((cacheLastCleared == null) || (cacheLastCleared.longValue() + 30000L <= currentTimeMillis))
/*     */     {
/* 204 */       cacheLastCleared = Long.valueOf(currentTimeMillis);
/* 205 */       accessCache.clear();
/*     */     }
/* 208 */     Boolean cached = (Boolean)accessCache.get(player);
/* 209 */     if (cached == null)
/*     */     {
/* 211 */       boolean canAccess = TFM_AdminList.isSuperAdmin(player);
/* 212 */       if (!canAccess)
/*     */       {
/* 214 */         Player supervisor = (Player)guestList.get(player);
/* 215 */         canAccess = (supervisor != null) && (supervisor.isOnline()) && (TFM_AdminList.isSuperAdmin(supervisor));
/* 216 */         if (!canAccess) {
/* 218 */           guestList.remove(player);
/*     */         }
/*     */       }
/* 221 */       cached = Boolean.valueOf(canAccess);
/* 222 */       accessCache.put(player, cached);
/*     */     }
/* 224 */     return cached.booleanValue();
/*     */   }
/*     */   
/*     */   public static enum WeatherMode
/*     */   {
/* 229 */     OFF("off"),  RAIN("rain"),  STORM("storm,thunderstorm");
/*     */     
/*     */     private final List<String> aliases;
/*     */     
/*     */     private WeatherMode(String aliases)
/*     */     {
/* 237 */       this.aliases = Arrays.asList(StringUtils.split(aliases, ","));
/*     */     }
/*     */     
/*     */     private void setWorldToWeather(World world)
/*     */     {
/* 242 */       world.setStorm((this == RAIN) || (this == STORM));
/* 243 */       world.setWeatherDuration((this == RAIN) || (this == STORM) ? 6000 : 0);
/*     */       
/* 245 */       world.setThundering(this == STORM);
/* 246 */       world.setThunderDuration(this == STORM ? 6000 : 0);
/*     */     }
/*     */     
/*     */     public static WeatherMode getByAlias(String needle)
/*     */     {
/* 251 */       needle = needle.toLowerCase();
/* 252 */       for (WeatherMode mode : values()) {
/* 254 */         if (aliases.contains(needle)) {
/* 256 */           return mode;
/*     */         }
/*     */       }
/* 259 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum TimeOfDay
/*     */   {
/* 265 */     INHERIT,  SUNRISE("sunrise,morning", 0),  NOON("noon,midday,day", 6000),  SUNSET("sunset,evening", 12000),  MIDNIGHT("midnight,night", 18000);
/*     */     
/*     */     private final int timeTicks;
/*     */     private final List<String> aliases;
/*     */     
/*     */     private TimeOfDay()
/*     */     {
/* 276 */       timeTicks = 0;
/* 277 */       aliases = null;
/*     */     }
/*     */     
/*     */     private TimeOfDay(String aliases, int timeTicks)
/*     */     {
/* 282 */       this.timeTicks = timeTicks;
/* 283 */       this.aliases = Arrays.asList(StringUtils.split(aliases, ","));
/*     */     }
/*     */     
/*     */     public int getTimeTicks()
/*     */     {
/* 288 */       return timeTicks;
/*     */     }
/*     */     
/*     */     public void setWorldToTime(World world)
/*     */     {
/* 293 */       long time = world.getTime();
/* 294 */       time -= time % 24000L;
/* 295 */       world.setTime(time + 24000L + getTimeTicks());
/*     */     }
/*     */     
/*     */     public static TimeOfDay getByAlias(String needle)
/*     */     {
/* 300 */       needle = needle.toLowerCase();
/* 301 */       for (TimeOfDay time : values()) {
/* 303 */         if ((aliases != null) && (aliases.contains(needle))) {
/* 305 */           return time;
/*     */         }
/*     */       }
/* 308 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public WeatherMode getWeatherMode()
/*     */   {
/* 314 */     return weatherMode;
/*     */   }
/*     */   
/*     */   public void setWeatherMode(WeatherMode weatherMode)
/*     */   {
/* 319 */     this.weatherMode = weatherMode;
/*     */     try
/*     */     {
/* 323 */       weatherMode.setWorldToWeather(getWorld());
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */   public TimeOfDay getTimeOfDay()
/*     */   {
/* 332 */     return timeOfDay;
/*     */   }
/*     */   
/*     */   public void setTimeOfDay(TimeOfDay timeOfDay)
/*     */   {
/* 337 */     this.timeOfDay = timeOfDay;
/*     */     try
/*     */     {
/* 341 */       timeOfDay.setWorldToTime(getWorld());
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */   public static TFM_AdminWorld getInstance()
/*     */   {
/* 350 */     return TFM_AdminWorldHolder.INSTANCE;
/*     */   }
/*     */   
/*     */   private static class TFM_AdminWorldHolder
/*     */   {
/* 355 */     private static final TFM_AdminWorld INSTANCE = new TFM_AdminWorld(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\World\TFM_AdminWorld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */