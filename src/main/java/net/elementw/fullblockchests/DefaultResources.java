package net.elementw.fullblockchests;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

public class DefaultResources implements ResourcePack, ClientModInitializer {
    private static final LiteralText NAME = new LiteralText(ModConfig.MOD_NAME);
    private static final LiteralText DESCRIPTION = new LiteralText("Internal " + ModConfig.MOD_NAME + " resources");

    @Override
    public void onInitializeClient() {
        // Nostalgic Tweaks messes up our block models even if the chest tweaks are disabled.
        // Bypass this by creating a fake resource pack to load our resources after NT.
        if (!FabricLoader.getInstance().isModLoaded("nostalgic_tweaks")) {
            return;
        }
        ModConfig.LOGGER.info("Found Nostalgic Tweaks, creating fake resource pack to override its chest models");
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        ResourcePackProvider fbcProvider = (consumer, factory) -> {
            consumer.accept(factory.create(
                    ModConfig.MOD_NAME,
                    NAME,
                    true, // Force loading
                    () -> this,
                    new PackResourceMetadata(DESCRIPTION, 8),
                    ResourcePackProfile.InsertionPosition.TOP,
                    ResourcePackSource.PACK_SOURCE_BUILTIN));
        };
        try {
            manager.providers.add(fbcProvider);
        } catch (UnsupportedOperationException e) {
            ModConfig.LOGGER.info("Making ResourcePackManager.providers mutable");
            manager.providers = Sets.newHashSet(manager.providers);
            manager.providers.add(fbcProvider);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public InputStream openRoot(String fileName) throws IOException {
        if (fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        if (fileName.equals("pack.png")) {
            return getClass().getClassLoader().getResourceAsStream("assets/" + ModConfig.MOD_ID + "/icon.png");
        }
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) throw new FileNotFoundException(fileName);
        return is;
    }

    private URL url(ResourceType type, Identifier id) {
        return url(type.getDirectory()+"/"+id.getNamespace()+"/"+id.getPath());
    }

    private URL url(String path) {
        return getClass().getClassLoader().getResource(path);
    }

    @Override
    public InputStream open(ResourceType type, Identifier id) throws IOException {
        URL u = url(type, id);
        if (u == null) throw new FileNotFoundException(id.toString());
        return u.openStream();
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        return Collections.emptySet();
    }

    @Override
    public boolean contains(ResourceType type, Identifier id) {
        return url(type, id) != null;
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return ImmutableSet.of("minecraft");
    }

    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) {
        return null;
    }

    @Override
    public String getName() {
        return ModConfig.MOD_NAME;
    }

    @Override
    public void close() {}
}
