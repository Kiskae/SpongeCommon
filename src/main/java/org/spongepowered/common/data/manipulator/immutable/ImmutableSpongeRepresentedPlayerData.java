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
package org.spongepowered.common.data.manipulator.immutable;

import org.spongepowered.api.GameProfile;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.ImmutableRepresentedPlayerData;
import org.spongepowered.api.data.manipulator.mutable.RepresentedPlayerData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.common.data.manipulator.mutable.SpongeRepresentedPlayerData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;

public class ImmutableSpongeRepresentedPlayerData
        extends AbstractImmutableSingleData<GameProfile, ImmutableRepresentedPlayerData, RepresentedPlayerData>
        implements ImmutableRepresentedPlayerData {

    public ImmutableSpongeRepresentedPlayerData() {
        this(SpongeRepresentedPlayerData.NULL_PROFILE);
    }

    public ImmutableSpongeRepresentedPlayerData(GameProfile ownerId) {
        super(ImmutableRepresentedPlayerData.class, ownerId, Keys.REPRESENTED_PLAYER);
    }

    @Override
    public ImmutableValue<GameProfile> owner() {
        return new ImmutableSpongeValue<GameProfile>(this.usedKey, this.value);
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer container = new MemoryDataContainer();
        if (this.value.getUniqueId() != null) {
            container.set(this.usedKey.getQuery().then(DataQuery.of("Id")), this.value.getUniqueId().toString());
        }
        if (this.value.getName() != null) {
            container.set(this.usedKey.getQuery().then(DataQuery.of("Name")), this.value.getName());
        }
        return container;
    }

    @Override
    public int compareTo(ImmutableRepresentedPlayerData o) {
        return this.value.getUniqueId().compareTo(o.owner().get().getUniqueId());
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return this.owner();
    }

    @Override
    public RepresentedPlayerData asMutable() {
        return new SpongeRepresentedPlayerData(this.value);
    }

}
