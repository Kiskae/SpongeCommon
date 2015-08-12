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
package org.spongepowered.common.data.processor.value.tileentity;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.common.data.ValueProcessor;
import org.spongepowered.common.data.manipulator.mutable.tileentity.SpongeSignData;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.data.value.mutable.SpongeListValue;
import org.spongepowered.common.text.SpongeTexts;

import java.util.List;

public class SignLinesValueProcessor implements ValueProcessor<List<Text>, ListValue<Text>> {

    @Override
    public Key<? extends BaseValue<List<Text>>> getKey() {
        return Keys.SIGN_LINES;
    }

    @Override
    public Optional<List<Text>> getValueFromContainer(ValueContainer<?> container) {
        if (container instanceof TileEntitySign) {
            final IChatComponent[] rawLines = ((TileEntitySign) container).signText;
            final List<Text> signLines = Lists.newArrayListWithCapacity(4);
            for (int i = 0; i < rawLines.length; i++) {
                signLines.set(i, SpongeTexts.toText(rawLines[i]));
            }
            return Optional.of(signLines);
        } else if (container instanceof ItemStack) {
            if (!((ItemStack) container).hasTagCompound()) {
                return Optional.absent();
            } else {
                final NBTTagCompound mainCompound = ((ItemStack) container).getTagCompound();
                if (!mainCompound.hasKey(NbtDataUtil.BLOCK_ENTITY_TAG, NbtDataUtil.TAG_COMPOUND) || !mainCompound.getCompoundTag(NbtDataUtil.BLOCK_ENTITY_TAG).hasKey(NbtDataUtil.BLOCK_ENTITY_ID)) {
                    return Optional.absent();
                }
                final NBTTagCompound tileCompound = mainCompound.getCompoundTag(NbtDataUtil.BLOCK_ENTITY_TAG);
                final String id = tileCompound.getString(NbtDataUtil.BLOCK_ENTITY_ID);
                if (!id.equalsIgnoreCase(NbtDataUtil.SIGN)) {
                    return Optional.absent();
                }
                final List<Text> texts = Lists.newArrayListWithCapacity(4);
                texts.add(Texts.legacy().fromUnchecked(tileCompound.getString("Text1")));
                texts.add(Texts.legacy().fromUnchecked(tileCompound.getString("Text2")));
                texts.add(Texts.legacy().fromUnchecked(tileCompound.getString("Text3")));
                texts.add(Texts.legacy().fromUnchecked(tileCompound.getString("Text4")));
                return Optional.of(texts);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<ListValue<Text>> getApiValueFromContainer(ValueContainer<?> container) {
        final Optional<List<Text>> optional = getValueFromContainer(container);
        if (optional.isPresent()) {
            return Optional.<ListValue<Text>>of(new SpongeListValue<Text>(Keys.SIGN_LINES, optional.get()));
        }
        return Optional.absent();
    }

    @Override
    public boolean supports(ValueContainer<?> container) {
        return container instanceof TileEntitySign || (container instanceof ItemStack && ((ItemStack) container).getItem().equals(Items.sign));
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<List<Text>, List<Text>> function) {
        return null;
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<?> value) {
        return null;
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, List<Text> value) {
        return null;
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return null;
    }
}
