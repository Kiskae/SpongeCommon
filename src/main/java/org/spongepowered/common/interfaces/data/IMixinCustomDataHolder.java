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
package org.spongepowered.common.interfaces.data;

import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;

import java.util.List;
import java.util.Optional;

public interface IMixinCustomDataHolder {

    default DataTransactionResult offerCustom(DataManipulator<?, ?> manipulator) {
        return offerCustom(manipulator, MergeFunction.IGNORE_ALL);
    }

    DataTransactionResult offerCustom(DataManipulator<?, ?> manipulator, MergeFunction function);

    <T extends DataManipulator<?, ?>> Optional<T> getCustom(Class<T> customClass);

    DataTransactionResult removeCustom(Class<? extends DataManipulator<?, ?>> customClass);

    boolean hasManipulators();

    List<DataManipulator<?, ?>> getCustomManipulators();

    <E> DataTransactionResult offerCustom(Key<? extends BaseValue<E>> key, E value);

    DataTransactionResult removeCustom(Key<?> key);

}
