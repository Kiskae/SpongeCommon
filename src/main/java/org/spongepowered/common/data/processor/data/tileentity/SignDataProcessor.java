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
package org.spongepowered.common.data.processor.data.tileentity;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.immutable.tileentity.ImmutableSpongeSignData;
import org.spongepowered.common.data.manipulator.mutable.tileentity.SpongeSignData;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.text.SpongeTexts;

import java.util.List;

@SuppressWarnings("deprecation")
public class SignDataProcessor implements DataProcessor<SignData, ImmutableSignData> {

    @Override
    public boolean supports(DataHolder dataHolder) {
        return dataHolder instanceof TileEntitySign || (dataHolder instanceof ItemStack && ((ItemStack) dataHolder).getItem().equals(Items.sign));
    }

    @Override
    public Optional<SignData> from(DataHolder dataHolder) {
        if (dataHolder instanceof TileEntitySign) {
            final SignData signData = new SpongeSignData();
            final IChatComponent[] rawLines = ((TileEntitySign) dataHolder).signText;
            final List<Text> signLines = Lists.newArrayListWithCapacity(4);
            for (int i = 0; i < rawLines.length; i++) {
                signLines.set(i, SpongeTexts.toText(rawLines[i]));
            }
            return Optional.of(signData.set(Keys.SIGN_LINES, signLines));
        } else if (dataHolder instanceof ItemStack) {
            if (!((ItemStack) dataHolder).hasTagCompound()) {
                return Optional.absent();
            } else {
                final NBTTagCompound mainCompound = ((ItemStack) dataHolder).getTagCompound();
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
                return Optional.<SignData>of(new SpongeSignData(texts));
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<SignData> fill(DataHolder dataHolder, SignData manipulator) {
        if (dataHolder instanceof TileEntitySign) {
            final IChatComponent[] rawLines = ((TileEntitySign) dataHolder).signText;
            final List<Text> signLines = Lists.newArrayListWithCapacity(4);
            for (int i = 0; i < rawLines.length; i++) {
                signLines.set(i, SpongeTexts.toText(rawLines[i]));
            }
            return Optional.of(manipulator.set(Keys.SIGN_LINES, signLines));
        } // todo itemstacks
        return Optional.absent();
    }

    @Override
    public Optional<SignData> fill(DataHolder dataHolder, SignData manipulator, MergeFunction overlap) {
        if (dataHolder instanceof TileEntitySign) {
            final SignData signData = new SpongeSignData();
            final IChatComponent[] rawLines = ((TileEntitySign) dataHolder).signText;
            final List<Text> signLines = Lists.newArrayListWithCapacity(4);
            for (int i = 0; i < rawLines.length; i++) {
                signLines.set(i, SpongeTexts.toText(rawLines[i]));
            }
            signData.set(Keys.SIGN_LINES, signLines);
            return Optional.of(overlap.merge(manipulator, signData));
        } // todo itemstacks
        return Optional.absent();
    }

    @Override
    public Optional<SignData> fill(DataContainer container, SignData signData) {
        return null;
    }

    @Override
    public DataTransactionResult set(DataHolder dataHolder, SignData manipulator) {
        if (dataHolder instanceof TileEntitySign) {
            final Optional<SignData> oldData = ((Sign) dataHolder).getData();
            if (oldData.isPresent()) {
                DataTransactionBuilder builder = DataTransactionBuilder.builder();
                builder.replace(oldData.get().getValues());
                final List<Text> texts = manipulator.get(Keys.SIGN_LINES).get();
                for (int i = 0; i < 4; i++) {
                    ((TileEntitySign) dataHolder).signText[i] = SpongeTexts.toComponent(texts.get(i));
                }
                ((TileEntitySign) dataHolder).markDirty();
                builder.success(manipulator.getValues()).result(DataTransactionResult.Type.SUCCESS);
                return builder.build();
            }
        } // todo itemstacks
        return DataTransactionBuilder.failResult(manipulator.getValues());
    }

    @Override
    public DataTransactionResult set(DataHolder dataHolder, SignData manipulator, MergeFunction function) {
        if (dataHolder instanceof TileEntitySign) {
            final Optional<SignData> oldData = ((Sign) dataHolder).getData();
            if (oldData.isPresent()) {
                DataTransactionBuilder builder = DataTransactionBuilder.builder();
                builder.replace(oldData.get().getValues());
                final List<Text> texts = manipulator.get(Keys.SIGN_LINES).get();
                for (int i = 0; i < 4; i++) {
                    ((TileEntitySign) dataHolder).signText[i] = SpongeTexts.toComponent(texts.get(i));
                }
                ((TileEntitySign) dataHolder).markDirty();
                builder.success(manipulator.getValues()).result(DataTransactionResult.Type.SUCCESS);
                return builder.build();
            }
        } // todo itemstacks
        return DataTransactionBuilder.failResult(manipulator.getValues());
    }

    @Override
    public Optional<ImmutableSignData> with(Key<? extends BaseValue<?>> key, Object value, ImmutableSignData immutable) {
        return null;
    }

    @Override
    public DataTransactionResult remove(DataHolder dataHolder) {
        return null;
    }

    @Override
    public SignData create() {
        return new SpongeSignData();
    }

    @Override
    public ImmutableSignData createImmutable() {
        return new ImmutableSpongeSignData(ImmutableList.of(Texts.of(), Texts.of(), Texts.of(), Texts.of()));
    }

    @Override
    public Optional<SignData> createFrom(DataHolder dataHolder) {
        if (dataHolder instanceof TileEntitySign) {
            return from(dataHolder);
        } else if (dataHolder instanceof ItemStack) {
            final ItemStack itemStack = ((ItemStack) dataHolder);
            if (!itemStack.getItem().equals(Items.sign)) {
                return Optional.absent();
            }
            if (itemStack.hasTagCompound()) {
                final NBTTagCompound mainCompound = ((ItemStack) dataHolder).getTagCompound();
                if (!mainCompound.hasKey(NbtDataUtil.BLOCK_ENTITY_TAG, NbtDataUtil.TAG_COMPOUND)) {
                    return Optional.of(create());
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
                return Optional.<SignData>of(new SpongeSignData(texts));
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<SignData> build(DataView container) throws InvalidDataException {

        return null;
    }
}
