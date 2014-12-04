package com.relicum.dual.Configuration;


import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * SettingsWriter used to serialize {@link com.relicum.dual.Configuration.PlayerSettings} objects to json and write to disk.
 * <p>This is does not use the main thread to write to disk.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class SettingsWriter implements Runnable {

    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final Type type = new TypeToken<PlayerSettings>() {
    }.getType();
    private PlayerSettings src;
    private Path path;

    public SettingsWriter(PlayerSettings src, Path path) {
        this.src = src;
        this.path = path;
    }

    @Override
    public void run() {

        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(path, Charset.defaultCharset()))) {
            writer.setIndent("    ");
            gson.toJson(src, type, writer);
            writer.flush();
            writer.close();
            System.out.println("File " + path.toString() + " successfully wrote to disk");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
