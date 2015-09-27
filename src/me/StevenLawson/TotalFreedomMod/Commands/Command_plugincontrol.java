/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Manage plugins", usage="/<command> <<enable | disable | reload> <pluginname>> | list>", aliases="plc")
/*     */ public class Command_plugincontrol
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  18 */     if ((args.length == 0) || (args.length > 2)) {
/*  20 */       return false;
/*     */     }
/*  23 */     PluginManager pm = server.getPluginManager();
/*  25 */     if (args.length == 1)
/*     */     {
/*  27 */       if (args[0].equalsIgnoreCase("list"))
/*     */       {
/*  29 */         for (Plugin serverPlugin : pm.getPlugins())
/*     */         {
/*  31 */           String version = serverPlugin.getDescription().getVersion();
/*  32 */           playerMsg(ChatColor.GRAY + "- " + (serverPlugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + serverPlugin.getName() + ChatColor.GOLD + ((version != null) && (!version.isEmpty()) ? " v" + version : "") + " by " + StringUtils.join(serverPlugin.getDescription().getAuthors(), ", "));
/*     */         }
/*  37 */         return true;
/*     */       }
/*  40 */       return false;
/*     */     }
/*  43 */     if ("enable".equals(args[0]))
/*     */     {
/*  45 */       Plugin target = getPlugin(args[1]);
/*  46 */       if (target == null)
/*     */       {
/*  48 */         playerMsg("Plugin not found!");
/*  49 */         return true;
/*     */       }
/*  52 */       if (target.isEnabled())
/*     */       {
/*  54 */         playerMsg("Plugin is already enabled.");
/*  55 */         return true;
/*     */       }
/*  58 */       pm.enablePlugin(target);
/*  60 */       if (!pm.isPluginEnabled(target))
/*     */       {
/*  62 */         playerMsg("Error enabling plugin " + target.getName());
/*  63 */         return true;
/*     */       }
/*  66 */       playerMsg(target.getName() + " is now enabled.");
/*  67 */       return true;
/*     */     }
/*  70 */     if ("disable".equals(args[0]))
/*     */     {
/*  72 */       Plugin target = getPlugin(args[1]);
/*  73 */       if (target == null)
/*     */       {
/*  75 */         playerMsg("Plugin not found!");
/*  76 */         return true;
/*     */       }
/*  79 */       if (!target.isEnabled())
/*     */       {
/*  81 */         playerMsg("Plugin is already disabled.");
/*  82 */         return true;
/*     */       }
/*  85 */       if (target.getName().equals(plugin.getName()))
/*     */       {
/*  87 */         playerMsg("You cannot disable " + plugin.getName());
/*  88 */         return true;
/*     */       }
/*  91 */       pm.disablePlugin(target);
/*  93 */       if (pm.isPluginEnabled(target))
/*     */       {
/*  95 */         playerMsg("Error disabling plugin " + target.getName());
/*  96 */         return true;
/*     */       }
/*  99 */       playerMsg(target.getName() + " is now disabled.");
/* 100 */       return true;
/*     */     }
/* 103 */     if ("reload".equals(args[0]))
/*     */     {
/* 105 */       Plugin target = getPlugin(args[1]);
/* 106 */       if (target == null)
/*     */       {
/* 108 */         playerMsg("Plugin not found!");
/* 109 */         return true;
/*     */       }
/* 112 */       if (target.getName().equals(plugin.getName()))
/*     */       {
/* 114 */         playerMsg("Use /tfm reload to reload instead.");
/* 115 */         return true;
/*     */       }
/* 118 */       pm.disablePlugin(target);
/* 119 */       pm.enablePlugin(target);
/* 120 */       playerMsg(target.getName() + " reloaded.");
/* 121 */       return true;
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   public Plugin getPlugin(String name)
/*     */   {
/* 129 */     for (Plugin serverPlugin : server.getPluginManager().getPlugins()) {
/* 131 */       if (serverPlugin.getName().equalsIgnoreCase(name)) {
/* 133 */         return serverPlugin;
/*     */       }
/*     */     }
/* 137 */     if (name.length() >= 3) {
/* 139 */       for (Plugin serverPlugin : server.getPluginManager().getPlugins()) {
/* 141 */         if (serverPlugin.getName().toLowerCase().contains(name.toLowerCase())) {
/* 143 */           return serverPlugin;
/*     */         }
/*     */       }
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_plugincontrol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */