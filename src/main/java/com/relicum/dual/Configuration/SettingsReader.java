package com.relicum.dual.Configuration;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Name: SettingsReader.java Created: 19 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class SettingsReader<T> implements Callable<T> {

    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final Type type = new TypeToken<T>() {
    }.getType();
    private T src;
    private Path path;

    public SettingsReader(Path path) {
        this.path = path;


    }

    @Override
    public T call() throws Exception {
        System.out.println("Starting file read");
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(path, Charset.defaultCharset()))) {

            src = gson.fromJson(reader, type);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("file has been read");
        return src;
    }
}
