package ru.primer.mc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) s;
        FileConfiguration cfg = Main.instance.getConfig();
        if (s instanceof Player) {
            if (args.length <= 0 && !p.isOp()) {
                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                return true;
            } else if (args.length <= 0 && p.isOp()) {
                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                return true;
            }
            if (p.isOp()) {
                if (args.length > 0) {
                    if (args[0].equals("reload")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.reload-config")));
                        Main.instance.reloadConfig();
                        Main.instance.saveDefaultConfig();
                        return true;
                    }
                    if (args[0].equals("list")) {
                        if (cfg.getConfigurationSection("promocodes").getKeys(false).isEmpty()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-promocode-list")));
                        } else {
                            List<String> list = new ArrayList<>(cfg.getConfigurationSection("promocodes").getKeys(false));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.promocode-list")));
                            for (int i = 0; i < list.size(); i++) {
                                int amount = i + 1;
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + amount + ".&f " + list.get(i)));
                            }
                        }
                        return true;
                    }
                    if (args[0].equals("create")) {
                        if (args.length >= 4) {
                            if (cfg.contains("promocodes." + args[1])) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.already-created")));
                                return true;
                            } else {
                                try {
                                    int number = Integer.parseInt(args[2]);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 3; i < args.length; i++) sb.append(args[i]).append(' ');
                                    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                                    String command = sb.toString();
                                    Date date = new Date();
                                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                    int uses = Integer.parseInt(args[2]);
                                    int usesstart = Integer.parseInt(args[2]);
                                    List<String> list = new ArrayList<>();
                                    list.add(command);
                                    cfg.set("promocodes." + args[1] + ".uses", uses);
                                    cfg.set("promocodes." + args[1] + ".uses-start", usesstart);
                                    cfg.set("promocodes." + args[1] + ".commands", list);
                                    cfg.set("promocodes." + args[1] + ".date", formatForDateNow.format(date));
                                    Main.instance.saveConfig();
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-created")));
                                    return true;
                                } catch (NumberFormatException e) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-int")));
                                }
                            }
                        } else if (args.length < 4) {
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            } else if (p.isOp()) {
                                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                        }
                    }
                    if (args[0].equals("delete")) {
                        if (args.length >= 2) {
                            if (cfg.contains("promocodes." + args[1])) {
                                cfg.set("promocodes." + args[1], null);
                                Main.instance.saveConfig();
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-deleted")));
                                return true;
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                        } else if (args.length < 2) {
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            } else if (p.isOp()) {
                                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                            return true;
                        }
                    }
                    if (args[0].equals("addcmd")) {
                        if (args.length >= 3) {
                            if (cfg.contains("promocodes." + args[1])) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) sb.append(args[i]).append(' ');
                                if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                                String command = sb.toString();
                                List<String> list = cfg.getStringList("promocodes." + args[1] + ".commands");
                                list.add(command);
                                cfg.set("promocodes." + args[1] + ".commands", list);
                                Main.instance.saveConfig();
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.add-command")));
                                return true;
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                        } else if (args.length < 3) {
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            } else if (p.isOp()) {
                                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                        }
                    }
                    if (args[0].equals("delcmd")) {
                        if (args.length >= 3) {
                            if (cfg.contains("promocodes." + args[1])) {
                                int i = Integer.parseInt(args[2]);
                                int amount = i - 1;
                                List<String> list = cfg.getStringList("promocodes." + args[1] + ".commands");
                                if (list.get(amount) != null) {
                                    list.set(amount, null);
                                    cfg.set("promocodes." + args[1] + ".commands", list);
                                    Main.instance.saveConfig();
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.del-command")));
                                    return true;
                                } else {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-command")));
                                    return true;
                                }
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                        } else if (args.length < 3) {
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            } else if (p.isOp()) {
                                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                        }
                    }
                    if (args[0].equals("info")) {
                        if (args.length >= 2) {
                            if (cfg.contains("promocodes." + args[1])) {
                                String promocode = args[1];
                                int usesamountint = cfg.getInt("promocodes." + args[1] + ".uses");
                                int usesstartint = cfg.getInt("promocodes." + args[1] + ".uses-start");
                                String usesamount = String.valueOf(usesamountint);
                                String usesstart = String.valueOf(usesstartint);
                                String date = cfg.getString("promocodes." + args[1] + ".date");
                                cfg.getStringList("messages.promocode-info").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replaceAll("%promocode%", promocode).replaceAll("%uses-amount%", String.valueOf(usesamount)).replaceAll("%uses-start%", String.valueOf(usesstart)).replaceAll("%date%", date))));
                                List<String> list = new ArrayList<>(cfg.getStringList("promocodes." + args[1] + ".commands"));
                                for (int i = 0; i < list.size(); i++) {
                                    int amount = i + 1;
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + amount + ".&f " + list.get(i)));
                                }
                            } else if (args.length < 2) {
                                if (!p.isOp()) {
                                    cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                    return true;
                                } else if (p.isOp()) {
                                    cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                    return true;
                                }
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (cfg.contains("promocodes." + args[0])) {
                    int uses = cfg.getInt("promocodes." + args[0] + ".uses");
                    if (!cfg.getStringList("promocodes." + args[0] + ".players").contains(p.getName()) || cfg.getStringList("promocodes." + args[0] + ".players").isEmpty()) {
                        if (uses > 0) {
                            int usesminus = uses - 1;
                            cfg.set("promocodes." + args[0] + ".uses", usesminus);
                            List list = new ArrayList();
                            list.add(p.getName());
                            cfg.set("promocodes." + args[0] + ".players", list);
                            Main.instance.saveConfig();
                            List<String> listcmd = new ArrayList<>(cfg.getStringList("promocodes." + args[0] + ".commands"));
                            for (int i = 0; i < listcmd.size(); i++) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), listcmd.get(i).replaceAll("%player%", p.getName()));
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-used")));
                        }
                    } else if (uses <= 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-uses")));
                        return true;
                    } else if (cfg.getList("promocodes." + args[0] + ".players").contains(p.getName())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.already-used")));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                    return true;
                }
            }
        }
        return true;
    }
}