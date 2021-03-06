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
package org.spongepowered.common.data.manipulator.immutable.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Booleans;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.item.ImmutablePlaceableData;
import org.spongepowered.api.data.manipulator.mutable.item.PlaceableData;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.common.data.manipulator.immutable.common.collection.AbstractImmutableSingleSetData;
import org.spongepowered.common.data.manipulator.mutable.item.SpongePlaceableData;

import java.util.Set;

public class ImmutableSpongePlaceableData extends AbstractImmutableSingleSetData<BlockType, ImmutablePlaceableData, PlaceableData> implements ImmutablePlaceableData {

    public ImmutableSpongePlaceableData() {
        this(ImmutableSet.<BlockType>of());
    }

    public ImmutableSpongePlaceableData(Set<BlockType> placeable) {
        super(ImmutablePlaceableData.class, placeable, Keys.PLACEABLE_BLOCKS, SpongePlaceableData.class);
    }

    @Override
    public int compareTo(ImmutablePlaceableData o) {
        return Booleans.compare(o.placeable().containsAll(getValue()),
                getValue().containsAll(o.placeable().get()));
    }

    @Override
    public DataContainer toContainer() {
        Set<String> placeableIds = Sets.newHashSetWithExpectedSize(getValue().size());
        for (BlockType placeable : getValue()) {
            placeableIds.add(placeable.getId());
        }
        return new MemoryDataContainer().set(Keys.PLACEABLE_BLOCKS.getQuery(), placeableIds);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImmutableSetValue<BlockType> placeable() {
        return (ImmutableSetValue<BlockType>) getValueGetter();
    }
}
