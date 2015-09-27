/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Player;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager.TFM_UuidResolver;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE)
/*     */ @CommandParameters(description="Provides uuid tools", usage="/<command> <purge | recalculate>")
/*     */ public class Command_uuid
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  25 */     if (args.length != 1) {
/*  27 */       return false;
/*     */     }
/*  30 */     if ("purge".equals(args[0]))
/*     */     {
/*  32 */       playerMsg("Purged " + TFM_UuidManager.purge() + " cached UUIDs.");
/*  33 */       return true;
/*     */     }
/*  36 */     if ("recalculate".equals(args[0]))
/*     */     {
/*  38 */       playerMsg("Recalculating UUIDs...");
/*     */       
/*  41 */       Set<TFM_Player> players = TFM_PlayerList.getAllPlayers();
/*  42 */       List<String> names = new ArrayList();
/*  44 */       for (TFM_Player player : players) {
/*  46 */         names.add(player.getLastLoginName());
/*     */       }
/*  49 */       Map<String, UUID> playerUuids = new TFM_UuidManager.TFM_UuidResolver(names).call();
/*     */       
/*  51 */       int updated = 0;
/*  52 */       for (Iterator i$ = playerUuids.keySet().iterator(); i$.hasNext();)
/*     */       {
/*  52 */         name = (String)i$.next();
/*  54 */         for (TFM_Player player : players) {
/*  56 */           if ((player.getLastLoginName().equalsIgnoreCase(name)) && 
/*     */           
/*  61 */             (!player.getUniqueId().equals(playerUuids.get(name))))
/*     */           {
/*  66 */             TFM_PlayerList.setUniqueId(player, (UUID)playerUuids.get(name));
/*  67 */             TFM_UuidManager.rawSetUUID(name, (UUID)playerUuids.get(name));
/*  68 */             updated++;
/*     */           }
/*     */         }
/*     */       }
/*     */       String name;
/*  73 */       playerMsg("Recalculated " + updated + " player UUIDs");
/*  74 */       names.clear();
/*     */       
/*  77 */       Set<TFM_Admin> admins = TFM_AdminList.getAllAdmins();
/*  78 */       for (TFM_Admin admin : admins) {
/*  80 */         names.add(admin.getLastLoginName());
/*     */       }
/*  83 */       Map<String, UUID> adminUuids = new TFM_UuidManager.TFM_UuidResolver(names).call();
/*     */       
/*  85 */       updated = 0;
/*  86 */       for (Iterator i$ = adminUuids.keySet().iterator(); i$.hasNext();)
/*     */       {
/*  86 */         name = (String)i$.next();
/*  88 */         for (TFM_Admin admin : admins) {
/*  90 */           if ((admin.getLastLoginName().equalsIgnoreCase(name)) && 
/*     */           
/*  95 */             (!admin.getUniqueId().equals(adminUuids.get(name))))
/*     */           {
/* 100 */             TFM_AdminList.setUuid(admin, admin.getUniqueId(), (UUID)adminUuids.get(name));
/* 101 */             TFM_UuidManager.rawSetUUID(name, (UUID)adminUuids.get(name));
/* 102 */             updated++;
/*     */           }
/*     */         }
/*     */       }
/*     */       String name;
/* 107 */       playerMsg("Recalculated " + updated + " admin UUIDs");
/*     */       
/* 109 */       return true;
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_uuid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */