/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class TFM_ProtectedArea
/*     */ {
/*     */   public static final double MAX_RADIUS = 50.0D;
/*  23 */   private static final Map<String, SerializableProtectedRegion> PROTECTED_AREAS = new HashMap();
/*     */   
/*     */   private TFM_ProtectedArea()
/*     */   {
/*  27 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static boolean isInProtectedArea(Location modifyLocation)
/*     */   {
/*  32 */     boolean doSave = false;
/*  33 */     boolean inProtectedArea = false;
/*     */     
/*  35 */     Iterator<Map.Entry<String, SerializableProtectedRegion>> it = PROTECTED_AREAS.entrySet().iterator();
/*  37 */     while (it.hasNext())
/*     */     {
/*  39 */       SerializableProtectedRegion region = (SerializableProtectedRegion)((Map.Entry)it.next()).getValue();
/*     */       
/*  41 */       Location regionCenter = null;
/*     */       try
/*     */       {
/*  44 */         regionCenter = region.getLocation();
/*     */       }
/*     */       catch (TFM_ProtectedArea.SerializableProtectedRegion.CantFindWorldException ex)
/*     */       {
/*  48 */         it.remove();
/*  49 */         doSave = true;
/*     */       }
/*  50 */       continue;
/*  53 */       if (regionCenter != null) {
/*  55 */         if (modifyLocation.getWorld() == regionCenter.getWorld())
/*     */         {
/*  57 */           double regionRadius = region.getRadius();
/*  58 */           if (modifyLocation.distanceSquared(regionCenter) <= regionRadius * regionRadius)
/*     */           {
/*  60 */             inProtectedArea = true;
/*  61 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  67 */     if (doSave) {
/*  69 */       save();
/*     */     }
/*  72 */     return inProtectedArea;
/*     */   }
/*     */   
/*     */   public static boolean isInProtectedArea(Vector min, Vector max, String worldName)
/*     */   {
/*  77 */     boolean doSave = false;
/*  78 */     boolean inProtectedArea = false;
/*     */     
/*  80 */     Iterator<Map.Entry<String, SerializableProtectedRegion>> it = PROTECTED_AREAS.entrySet().iterator();
/*  82 */     while (it.hasNext())
/*     */     {
/*  84 */       SerializableProtectedRegion region = (SerializableProtectedRegion)((Map.Entry)it.next()).getValue();
/*     */       
/*  86 */       Location regionCenter = null;
/*     */       try
/*     */       {
/*  89 */         regionCenter = region.getLocation();
/*     */       }
/*     */       catch (TFM_ProtectedArea.SerializableProtectedRegion.CantFindWorldException ex)
/*     */       {
/*  93 */         it.remove();
/*  94 */         doSave = true;
/*     */       }
/*  95 */       continue;
/*  98 */       if (regionCenter != null) {
/* 100 */         if (worldName.equals(regionCenter.getWorld().getName())) {
/* 102 */           if (cubeIntersectsSphere(min, max, regionCenter.toVector(), region.getRadius()))
/*     */           {
/* 104 */             inProtectedArea = true;
/* 105 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 111 */     if (doSave) {
/* 113 */       save();
/*     */     }
/* 116 */     return inProtectedArea;
/*     */   }
/*     */   
/*     */   private static boolean cubeIntersectsSphere(Vector min, Vector max, Vector sphere, double radius)
/*     */   {
/* 121 */     double d = square(radius);
/* 123 */     if (sphere.getX() < min.getX()) {
/* 125 */       d -= square(sphere.getX() - min.getX());
/* 127 */     } else if (sphere.getX() > max.getX()) {
/* 129 */       d -= square(sphere.getX() - max.getX());
/*     */     }
/* 131 */     if (sphere.getY() < min.getY()) {
/* 133 */       d -= square(sphere.getY() - min.getY());
/* 135 */     } else if (sphere.getY() > max.getY()) {
/* 137 */       d -= square(sphere.getY() - max.getY());
/*     */     }
/* 139 */     if (sphere.getZ() < min.getZ()) {
/* 141 */       d -= square(sphere.getZ() - min.getZ());
/* 143 */     } else if (sphere.getZ() > max.getZ()) {
/* 145 */       d -= square(sphere.getZ() - max.getZ());
/*     */     }
/* 148 */     return d > 0.0D;
/*     */   }
/*     */   
/*     */   private static double square(double v)
/*     */   {
/* 153 */     return v * v;
/*     */   }
/*     */   
/*     */   public static void addProtectedArea(String label, Location location, double radius)
/*     */   {
/* 158 */     PROTECTED_AREAS.put(label.toLowerCase(), new SerializableProtectedRegion(location, radius));
/* 159 */     save();
/*     */   }
/*     */   
/*     */   public static void removeProtectedArea(String label)
/*     */   {
/* 164 */     PROTECTED_AREAS.remove(label.toLowerCase());
/* 165 */     save();
/*     */   }
/*     */   
/*     */   public static void clearProtectedAreas()
/*     */   {
/* 170 */     clearProtectedAreas(true);
/*     */   }
/*     */   
/*     */   public static void clearProtectedAreas(boolean createSpawnpointProtectedAreas)
/*     */   {
/* 175 */     PROTECTED_AREAS.clear();
/* 177 */     if (createSpawnpointProtectedAreas) {
/* 179 */       autoAddSpawnpoints();
/*     */     }
/* 182 */     save();
/*     */   }
/*     */   
/*     */   public static void cleanProtectedAreas()
/*     */   {
/* 187 */     boolean doSave = false;
/*     */     
/* 189 */     Iterator<Map.Entry<String, SerializableProtectedRegion>> it = PROTECTED_AREAS.entrySet().iterator();
/* 191 */     while (it.hasNext()) {
/*     */       try
/*     */       {
/* 195 */         ((SerializableProtectedRegion)((Map.Entry)it.next()).getValue()).getLocation();
/*     */       }
/*     */       catch (TFM_ProtectedArea.SerializableProtectedRegion.CantFindWorldException ex)
/*     */       {
/* 199 */         it.remove();
/* 200 */         doSave = true;
/*     */       }
/*     */     }
/* 204 */     if (doSave) {
/* 206 */       save();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Set<String> getProtectedAreaLabels()
/*     */   {
/* 212 */     return PROTECTED_AREAS.keySet();
/*     */   }
/*     */   
/*     */   public static void save()
/*     */   {
/*     */     try
/*     */     {
/* 219 */       FileOutputStream fos = new FileOutputStream(new File(TotalFreedomMod.plugin.getDataFolder(), "protectedareas.dat"));
/* 220 */       ObjectOutputStream oos = new ObjectOutputStream(fos);
/* 221 */       oos.writeObject(PROTECTED_AREAS);
/* 222 */       oos.close();
/* 223 */       fos.close();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 227 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/* 234 */     if (!TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) {
/* 236 */       return;
/*     */     }
/* 239 */     File input = new File(TotalFreedomMod.plugin.getDataFolder(), "protectedareas.dat");
/*     */     try
/*     */     {
/* 242 */       if (input.exists())
/*     */       {
/* 244 */         FileInputStream fis = new FileInputStream(input);
/* 245 */         ObjectInputStream ois = new ObjectInputStream(fis);
/* 246 */         PROTECTED_AREAS.clear();
/* 247 */         PROTECTED_AREAS.putAll((HashMap)ois.readObject());
/* 248 */         ois.close();
/* 249 */         fis.close();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 254 */       input.delete();
/* 255 */       TFM_Log.severe(ex);
/*     */     }
/* 258 */     cleanProtectedAreas();
/*     */   }
/*     */   
/*     */   public static void autoAddSpawnpoints()
/*     */   {
/* 263 */     if (!TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue()) {
/* 265 */       return;
/*     */     }
/* 268 */     if (TFM_ConfigEntry.PROTECTAREA_SPAWNPOINTS.getBoolean().booleanValue()) {
/* 270 */       for (World world : Bukkit.getWorlds()) {
/* 272 */         addProtectedArea("spawn_" + world.getName(), world.getSpawnLocation(), TFM_ConfigEntry.PROTECTAREA_RADIUS.getDouble().doubleValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SerializableProtectedRegion
/*     */     implements Serializable
/*     */   {
/*     */     private final double x;
/*     */     private final double y;
/*     */     private final double z;
/*     */     private final double radius;
/*     */     private final String worldName;
/*     */     private final UUID worldUUID;
/* 283 */     private transient Location location = null;
/*     */     
/*     */     public SerializableProtectedRegion(Location location, double radius)
/*     */     {
/* 287 */       x = location.getX();
/* 288 */       y = location.getY();
/* 289 */       z = location.getZ();
/* 290 */       this.radius = radius;
/* 291 */       worldName = location.getWorld().getName();
/* 292 */       worldUUID = location.getWorld().getUID();
/* 293 */       this.location = location;
/*     */     }
/*     */     
/*     */     public Location getLocation()
/*     */       throws TFM_ProtectedArea.SerializableProtectedRegion.CantFindWorldException
/*     */     {
/* 298 */       if (location == null)
/*     */       {
/* 300 */         World world = Bukkit.getWorld(worldUUID);
/* 302 */         if (world == null) {
/* 304 */           world = Bukkit.getWorld(worldName);
/*     */         }
/* 307 */         if (world == null) {
/* 309 */           throw new CantFindWorldException("Can't find world " + worldName + ", UUID: " + worldUUID.toString());
/*     */         }
/* 312 */         location = new Location(world, x, y, z);
/*     */       }
/* 314 */       return location;
/*     */     }
/*     */     
/*     */     public double getRadius()
/*     */     {
/* 319 */       return radius;
/*     */     }
/*     */     
/*     */     public static class CantFindWorldException
/*     */       extends Exception
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*     */       public CantFindWorldException(String string)
/*     */       {
/* 328 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_ProtectedArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */