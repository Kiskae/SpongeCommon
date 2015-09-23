package org.spongepowered.common.data.manipulator.immutable.entity;

import com.google.common.collect.ComparisonChain;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableHorseData;
import org.spongepowered.api.data.manipulator.mutable.entity.HorseData;
import org.spongepowered.api.data.type.HorseColor;
import org.spongepowered.api.data.type.HorseColors;
import org.spongepowered.api.data.type.HorseStyle;
import org.spongepowered.api.data.type.HorseStyles;
import org.spongepowered.api.data.type.HorseVariant;
import org.spongepowered.api.data.type.HorseVariants;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeHorseData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.util.GetterFunction;

public class ImmutableSpongeHorseData extends AbstractImmutableData<ImmutableHorseData, HorseData> implements ImmutableHorseData {

    private HorseColor horseColor;
    private HorseStyle horseStyle;
    private HorseVariant horseVariant;

    public ImmutableSpongeHorseData(HorseColor horseColor, HorseStyle horseStyle, HorseVariant horseVariant) {
        super(ImmutableHorseData.class);
        this.horseColor = horseColor;
        this.horseStyle = horseStyle;
        this.horseVariant = horseVariant;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(Keys.HORSE_COLOR, new GetterFunction<Object>() {

            @Override
            public Object get() {
                return getHorseColor();
            }
        });
        registerKeyValue(Keys.HORSE_COLOR, new GetterFunction<ImmutableValue<?>>() {

            @Override
            public ImmutableValue<?> get() {
                return color();
            }
        });

        registerFieldGetter(Keys.HORSE_STYLE, new GetterFunction<Object>() {

            @Override
            public Object get() {
                return getHorseStyle();
            }
        });
        registerKeyValue(Keys.HORSE_STYLE, new GetterFunction<ImmutableValue<?>>() {

            @Override
            public ImmutableValue<?> get() {
                return style();
            }
        });

        registerFieldGetter(Keys.HORSE_VARIANT, new GetterFunction<Object>() {

            @Override
            public Object get() {
                return getHorseVariant();
            }
        });
        registerKeyValue(Keys.HORSE_VARIANT, new GetterFunction<ImmutableValue<?>>() {

            @Override
            public ImmutableValue<?> get() {
                return variant();
            }
        });
    }

    @Override
    public ImmutableValue<HorseColor> color() {
        return ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, Keys.HORSE_COLOR, HorseColors.BLACK, this.horseColor);
    }

    @Override
    public ImmutableValue<HorseStyle> style() {
        return ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, Keys.HORSE_STYLE, HorseStyles.NONE, this.horseStyle);
    }

    @Override
    public ImmutableValue<HorseVariant> variant() {
        return ImmutableDataCachingUtil.getValue(ImmutableSpongeValue.class, Keys.HORSE_VARIANT, HorseVariants.HORSE, this.horseVariant);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
                .set(Keys.HORSE_COLOR.getQuery(), this.horseColor)
                .set(Keys.HORSE_STYLE.getQuery(), this.horseStyle)
                .set(Keys.HORSE_VARIANT.getQuery(), this.horseVariant);
    }

    @Override
    public int compareTo(ImmutableHorseData other) {
        return ComparisonChain.start()
                .compare(this.horseColor.getId(), other.color().get().getId())
                .compare(this.horseStyle.getId(), other.style().get().getId())
                .compare(this.horseVariant.getId(), other.variant().get().getId())
                .result();
    }

    @Override
    public ImmutableHorseData copy() {
        return this;
    }

    @Override
    public HorseData asMutable() {
        return new SpongeHorseData(this.horseColor, this.horseStyle, this.horseVariant);
    }

    private HorseColor getHorseColor() {
        return horseColor;
    }

    private HorseStyle getHorseStyle() {
        return horseStyle;
    }

    private HorseVariant getHorseVariant() {
        return horseVariant;
    }
}