/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.data.key;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeListKey;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

import com.google.common.collect.MapMaker;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.Career;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Axis;
import org.spongepowered.common.registry.RegistryHelper;

import java.awt.Color;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class KeyRegistry {

    private static final Map<String, Key<?>> keyMap = new MapMaker().concurrencyLevel(4).makeMap();

    public static void registerKeys() {
        keyMap.put("AXIS", makeSingleKey(Axis.class, Value.class, of("Axis")));
        keyMap.put("COLOR", makeSingleKey(Color.class, Value.class, of("Color")));
        keyMap.put("HEALTH", makeSingleKey(Double.class, MutableBoundedValue.class, of("Health")));
        keyMap.put("MAX_HEALTH", makeSingleKey(Double.class, MutableBoundedValue.class, of("MaxHealth")));
        keyMap.put("SHOWS_DISPLAY_NAME", makeSingleKey(Boolean.class, Value.class, of("ShowDisplayName")));
        keyMap.put("DISPLAY_NAME", makeSingleKey(Text.class, Value.class, of("DisplayName")));
        keyMap.put("CAREER", makeSingleKey(Career.class, Value.class, of("Career")));
        keyMap.put("SIGN_LINES", makeListKey(Text.class, of("SignLines")));
        RegistryHelper.mapFields(Keys.class, keyMap);
    }

}
