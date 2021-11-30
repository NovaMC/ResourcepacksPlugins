package de.themoep.resourcepacksplugin.core;

/*
 * ResourcepacksPlugins - core
 * Copyright (C) 2018 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Phoenix616 on 25.03.2015.
 */
public class ResourcePack {
    private String name;
    private String url;
    private byte[] hash = new byte[0];
    private int format;
    private int version;
    private boolean forced;
    private String prompt;
    private boolean restricted;
    private String permission;
    private ClientType type;

    private List<ResourcePack> variants = new ArrayList<>();

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     */
    public ResourcePack(String name, String url, String hash) {
        this(name, url, hash, 0);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta
     */
    public ResourcePack(String name, String url, String hash, int format) {
        this(name, url, hash, format, false);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, boolean restricted) {
        this(name, url, hash, 0, restricted);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, boolean restricted) {
        this(name, url, hash, format, restricted, "resourcepacksplugin.pack." + name.toLowerCase(Locale.ROOT));
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, boolean restricted, String permission) {
        this(name, url, hash, format, 0, restricted, permission);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     */
    public ResourcePack(String name, String url, String hash, int format, int version, boolean restricted, String permission) {
        this(name, url, hash, format, version, restricted, permission, ClientType.ORIGINAL);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(String name, String url, String hash, int format, int version, boolean restricted, String permission, ClientType type) {
        this(name, url, hash, format, version, false, "", restricted, permission, type);
    }

    /**
     * Object representation of a resourcepack set in the plugin's config file.
     * @param name The name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @param url The url where this resourcepack is located at and where the client will download it from
     * @param hash The hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @param format The version of this resourcepack as defined in the pack.mcmeta as pack_format
     * @param version The Minecraft version that this resourcepack is for
     * @param forced Whether the client should be told to force this pack
     * @param prompt The prompt to be shown to the client
     * @param permission A custom permission for this pack
     * @param restricted Whether or not this pack should only be send to players with the pluginname.pack.packname permission
     * @param type The type of the pack depending on the client which should receive it
     */
    public ResourcePack(String name, String url, String hash, int format, int version, boolean forced, String prompt, boolean restricted, String permission, ClientType type) {
        this.name = name;
        this.url = url;
        if (hash != null && !hash.isEmpty()) {
            setHash(hash);
        }
        this.format = format;
        this.version = version;
        this.forced = forced;
        this.prompt = prompt;
        this.restricted = restricted;
        this.permission = permission;
        this.type = type;
    }
    
    /**
     * Get the name of the resourcepack as set in the config. Serves as an uinque identifier. Correct case.
     * @return The name as a string in correct case
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the url where this resourcepack is located at and where the client will download it from
     * @return The url as a string
     */
    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the hash set for this resourcepack. Ideally this is the zip file's sha1 hash.
     * @return The 40 digit lowercase hash
     */
    public String getHash() {
        return BaseEncoding.base16().lowerCase().encode(hash);
    }

    void setHash(String hash) {
        setRawHash(BaseEncoding.base16().lowerCase().decode(hash.toLowerCase(Locale.ROOT)));
    }

    public byte[] getRawHash() {
        return hash;
    }

    public void setRawHash(byte[] hash) {
        if (hash.length > 0 && hash.length != 20) {
            throw new IllegalArgumentException("Hash needs to be either 0 or 20 bytes long!");
        }
        this.hash = hash;
    }

    /**
     * Get the pack format version
     * @return The pack version as an int
     */
    public int getFormat() {
        return format;
    }
    /**
     * Set the pack format version
     * @param format The pack version as an int
     * @return Whether or not the format changed
     */
    public boolean setFormat(int format) {
        if (this.format == format) {
            return false;
        }
        this.format = format;
        return true;
    }

    /**
     * Get the pack Minecraft protocol version
     * @return The Minecraft protocol version as an int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Set the pack Minecraft protocol version
     * @param version The Minecraft protocol version as an int
     */
    public boolean setVersion(int version) {
        if (this.version == version) {
            return false;
        }
        this.version = version;
        return true;
    }

    /**
     * Whether this pack should be forced on the client
     * @return Whether the pack is forced or not
     */
    public boolean isForced() {
        return forced;
    }

    /**
     * Whether this pack should be forced on the client
     * @param forced <code>true</code> if pack should be forced, <code>false</code> if not
     * @return Whether or not the forced status changed
     */
    public boolean setForced(boolean forced) {
        if (this.forced == forced) {
            return false;
        }
        this.forced = forced;
        return true;
    }

    /**
     * Get the prompt for this pack
     * @return The prompt as a string
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * The prompt to display for this pack
     * @param prompt The prompt as a string
     * @return Whether or not the prompt status changed
     */
    public boolean setPrompt(String prompt) {
        if (this.prompt.equals(prompt)) {
            return false;
        }
        this.prompt = prompt;
        return true;
    }

    /**
     * Whether or not this pack is restricted and a permission should be used
     * @return <code>true</code> if one needs the permission, <code>false</code> if not
     */
    public boolean isRestricted() {
        return restricted;
    }

    /**
     * Whether or not this pack is restricted and a permission should be used
     * @param restricted <code>true</code> if one needs the permission, <code>false</code> if not
     * @return Whether or not the restricted status changed
     */
    public boolean setRestricted(boolean restricted) {
        if (this.restricted == restricted) {
            return false;
        }
        this.restricted = restricted;
        return true;
    }

    /**
     * Get the permission to use this pack
     * @return The permission as a string
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission to use this pack
     * @param permission The permission as a string
     * @return Whether or not the permission changed
     */
    public boolean setPermission(String permission) {
        if (this.permission.equals(permission)) {
            return false;
        }
        this.permission = permission;
        return true;
    }

    /**
     * Get the client type that the resource pack is for
     * @return The type
     */
    public ClientType getType() {
        return type;
    }

    /**
     * Set the client type that the resource pack is for
     * @param type The type
     * @return
     */
    public boolean setType(ClientType type) {
        if (this.type == type) {
            return false;
        }
        this.type = type;
        return true;
    }

    /**
     * Get a list of different pack variants. Used to get, add and remove variants.
     * @return The list of pack variants
     */
    public List<ResourcePack> getVariants() {
        return variants;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (!(o instanceof ResourcePack)) {
            return false;
        } else {
            ResourcePack other = (ResourcePack)o;
            String this$name = this.getName();
            String other$name = other.getName();
            if (this$name == null) {
                if (other$name != null) {
                    return false;
                }
            } else if (!this$name.equals(other$name)) {
                return false;
            }
            
            String this$url = this.getUrl();
            String other$url = other.getUrl();
            if (this$url == null) {
                if (other$url != null) {
                    return false;
                }
            } else if (!this$url.equals(other$url)) {
                return false;
            }

            byte[] this$hash = this.getRawHash();
            byte[] other$hash = other.getRawHash();
            if (this$hash == null) {
                if (other$hash != null) {
                    return false;
                }
            } else if (!Arrays.equals(this$hash, other$hash)) {
                return false;
            }

            if (this.getType() != other.getType()) {
                return false;
            }

            return true;
        }
    }

    public String[] getReplacements() {
        return new String[] {
                "name", getName(),
                "url", getUrl(),
                "hash", getHash(),
                "format", String.valueOf(getFormat()),
                "version", String.valueOf(getVersion()),
                "restricted", String.valueOf(isRestricted()),
                "permission", getPermission(),
                "type", getType().humanName(),
                "variants", String.valueOf(getVariants().size())
        };
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("url", url.isEmpty() ? null : url);
        map.put("hash", hash.length == 0 ? null : getHash());
        if (name.equalsIgnoreCase(PackManager.EMPTY_IDENTIFIER)) {
            map.put("format", null);
            map.put("version", null);
            map.put("forced", null);
            map.put("prompt", null);
            map.put("restricted", null);
            map.put("permission", null);
        } else {
            map.put("format", format > 0 ? format : null);
            if (version > 0) {
                MinecraftVersion mcVersion = MinecraftVersion.getExactVersion(version);
                map.put("version", mcVersion != null ? mcVersion.toConfigString() : version);
            } else {
                map.put("version", null);
            }
            map.put("forced", forced);
            map.put("prompt", prompt);
            map.put("restricted", restricted);
            map.put("permission", permission);
            if (type == ClientType.ORIGINAL) {
                map.put("type", null);
            } else {
                map.put("type", type.name().toLowerCase(Locale.ROOT));
            }
        }
        if (variants.isEmpty()) {
            map.put("variants", null);
        } else {
            List<Map<String, Object>> variantsList = new ArrayList<>();
            for (ResourcePack packVariant : variants) {
                variantsList.add(packVariant.serialize());
            }
            map.put("variants", variantsList);
        }

        return map;
    }
}
