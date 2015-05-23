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
package org.spongepowered.common.data.manipulator;

import static org.spongepowered.api.data.DataQuery.of;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.entity.TradeOfferData;
import org.spongepowered.api.item.merchant.TradeOffer;

import java.util.List;

public class SpongeTradeOfferData extends AbstractListData<TradeOffer, TradeOfferData> implements TradeOfferData {

    public static final DataQuery OFFERS = of("Offers");

    public SpongeTradeOfferData() {
        super(TradeOfferData.class);
    }

    @Override
    public List<TradeOffer> getOffers() {
        return this.getAll();
    }

    @Override
    public TradeOfferData setOffers(List<TradeOffer> offers) {
        return this.set(offers);
    }

    @Override
    public TradeOfferData addOffer(TradeOffer offer) {
        return this.add(offer);
    }

    @Override
    public TradeOfferData copy() {
        return new SpongeTradeOfferData().set(this.elementList);
    }

    @Override
    public int compareTo(TradeOfferData o) {
        return o.getAll().hashCode() - this.getAll().hashCode();
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(OFFERS, this.elementList);
    }
}