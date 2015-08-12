package org.spongepowered.common.data.processor.value;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullType;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.ValueProcessor;
import org.spongepowered.common.data.processor.common.SkullUtils;
import org.spongepowered.common.data.type.SpongeSkullType;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class SkullValueProcessor implements ValueProcessor<SkullType, Value<SkullType>> {

    @Override
    public Key<? extends BaseValue<SkullType>> getKey() {
        return Keys.SKULL_TYPE;
    }

    @Override
    public Optional<SkullType> getValueFromContainer(ValueContainer<?> container) {
        if (container instanceof TileEntitySkull) {
            return Optional.of(SkullUtils.getSkullType((TileEntitySkull) container));
        } else if (SkullUtils.isValidItemStack(container)) {
            return Optional.of(SkullUtils.getSkullType((ItemStack) container));
        }
        return Optional.absent();
    }

    private SpongeValue<SkullType> getNewValue(SkullType skullType) {
        return new SpongeValue<SkullType>(this.getKey(), SkullTypes.SKELETON, skullType);
    }

    @Override
    public Optional<Value<SkullType>> getApiValueFromContainer(ValueContainer<?> container) {
        Optional<SkullType> optSkull = this.getValueFromContainer(container);
        if (optSkull.isPresent()) {
            return Optional.<Value<SkullType>>of(this.getNewValue(optSkull.get()));
        }
        return Optional.absent();
    }

    @Override
    public boolean supports(ValueContainer<?> container) {
        return SkullUtils.supportsObject(container);
    }

    @Override
    public DataTransactionResult transform(ValueContainer<?> container, Function<SkullType, SkullType> function) {
        if (this.supports(container)) {
            SkullType oldValue = this.getValueFromContainer(container).get();
            SkullType newValue = checkNotNull(function.apply(oldValue));
            return this.offerToStore(container, newValue);
        }
        return DataTransactionBuilder.failNoData();
    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, BaseValue<?> value) {
        BaseValue<SkullType> realValue = (BaseValue<SkullType>) value;
        ImmutableValue<SkullType> proposedValue = this.getNewValue(realValue.get()).asImmutable();
        ImmutableValue<SkullType> oldValue;

        DataTransactionBuilder builder = DataTransactionBuilder.builder();

        int skullType = ((SpongeSkullType) realValue.get()).getByteId();

        if (container instanceof TileEntitySkull) {
            oldValue = getApiValueFromContainer(container).get().asImmutable();
            SkullUtils.setSkullType((TileEntitySkull) container, skullType);
        } else if (SkullUtils.isValidItemStack(container)) {
            oldValue = getApiValueFromContainer(container).get().asImmutable();
            ((ItemStack) container).setItemDamage(skullType);
        } else {
            return DataTransactionBuilder.failResult(proposedValue);
        }

        return builder.success(proposedValue).replace(oldValue).result(DataTransactionResult.Type.SUCCESS).build();

    }

    @Override
    public DataTransactionResult offerToStore(ValueContainer<?> container, SkullType value) {
        return this.offerToStore(container, this.getNewValue(value));
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionBuilder.failNoData();
    }
}
