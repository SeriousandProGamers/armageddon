package xyz.spgamers.forge.armageddon.util;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.common.util.Constants;

import java.util.function.Function;
import java.util.function.Supplier;

public final class EntityEnumDataHelper<ENUM extends Enum<ENUM>, ENTITY extends Entity>
{
	private final String dataTagName;
	private final Class<ENUM> enumClass;
	private final Class<ENTITY> entityClass;
	private final Function<ENUM, Integer> toIntFunc;
	private final Function<Integer, ENUM> fromIntFunc;
	private final Supplier<ENUM> defaultValueSupplier;
	private final DataParameter<Integer> dataKey;

	private int currentIndex = 0;
	private int previousIndex = 0;

	private EntityEnumDataHelper(Builder<ENUM, ENTITY> builder)
	{
		Preconditions.checkNotNull(builder.dataTagName);
		Preconditions.checkNotNull(builder.enumClass);
		Preconditions.checkNotNull(builder.entityClass);
		Preconditions.checkNotNull(builder.toIntFunc);
		Preconditions.checkNotNull(builder.fromIntFunc);
		Preconditions.checkNotNull(builder.defaultValueSupplier);

		dataTagName = builder.dataTagName;
		enumClass = builder.enumClass;
		entityClass = builder.entityClass;
		toIntFunc = builder.toIntFunc;
		fromIntFunc = builder.fromIntFunc;
		defaultValueSupplier = builder.defaultValueSupplier;

		dataKey = EntityDataManager.createKey(entityClass, DataSerializers.VARINT);
	}

	public DataParameter<Integer> getDataKey()
	{
		return dataKey;
	}

	public void registerDataKey(ENTITY entity)
	{
		entity.getDataManager().register(dataKey, 0);
	}

	// Current
	public int getCurrentIndex()
	{
		return currentIndex;
	}

	public ENUM getCurrentValue()
	{
		return fromIntFunc.apply(currentIndex);
	}

	// Previous
	public int getPreviousIndex()
	{
		return previousIndex;
	}

	public ENUM getPreviousValue()
	{
		return fromIntFunc.apply(previousIndex);
	}

	// Getter
	public int getIndex(ENTITY entity)
	{
		return entity.getDataManager().get(dataKey);
	}

	public ENUM getValue(ENTITY entity)
	{
		int index = getIndex(entity);
		return fromIntFunc.apply(index);
	}

	// Setter
	public void setIndex(ENTITY entity, int index)
	{
		previousIndex = currentIndex;
		currentIndex = index;
		entity.getDataManager().set(dataKey, index);
	}

	public void setValue(ENTITY entity, ENUM value)
	{
		int index = toIntFunc.apply(value);
		setIndex(entity, index);
	}

	// Serialize
	public CompoundNBT writeToEntityNBT(ENTITY entity, CompoundNBT entityTag)
	{
		int index = getIndex(entity);
		entityTag.putInt(dataTagName, index);
		return entityTag;
	}

	// Deserialize
	public void readFromEntityNBT(ENTITY entity, CompoundNBT entityTag)
	{
		int index = 0;

		if(entityTag.contains(dataTagName, Constants.NBT.TAG_INT))
			index = entityTag.getInt(dataTagName);

		setIndex(entity, index);
	}

	public static <ENUM extends Enum<ENUM>, ENTITY extends Entity> EntityEnumDataHelper<ENUM, ENTITY> create(String dataTagName, Class<ENUM> enumClass, Class<ENTITY> entityClass)
	{
		return builder(dataTagName, enumClass, entityClass).build();
	}

	public static <ENUM extends Enum<ENUM>, ENTITY extends Entity> Builder<ENUM, ENTITY> builder(String dataTagName, Class<ENUM> enumClass, Class<ENTITY> entityClass)
	{
		return new Builder<>(dataTagName, enumClass, entityClass);
	}

	public static <ENUM extends Enum<ENUM>> ENUM getEnumFromIndex(Class<ENUM> enumClass, int index)
	{
		ENUM[] values = enumClass.getEnumConstants();
		return values[index % (values.length - 1)];
	}

	public static <ENUM extends Enum<ENUM>> ENUM getEnumDefaultValue(Class<ENUM> enumClass)
	{
		return enumClass.getEnumConstants()[0];
	}

	public static final class Builder<ENUM extends Enum<ENUM>, ENTITY extends Entity>
	{
		private final Class<ENUM> enumClass;
		private final Class<ENTITY> entityClass;
		private final String dataTagName;

		private Function<ENUM, Integer> toIntFunc;
		private Function<Integer, ENUM> fromIntFunc;
		private Supplier<ENUM> defaultValueSupplier;

		private Builder(String dataTagName, Class<ENUM> enumClass, Class<ENTITY> entityClass)
		{
			this.dataTagName = dataTagName;
			this.enumClass = enumClass;
			this.entityClass = entityClass;

			setToIntFunc(Enum::ordinal);
			setFromIntFunc(index -> getEnumFromIndex(enumClass, index));
			setDefaultValueSupplier(() -> getEnumDefaultValue(enumClass));
		}

		public Builder<ENUM, ENTITY> setToIntFunc(Function<ENUM, Integer> toIntFunc)
		{
			this.toIntFunc = toIntFunc;
			return this;
		}

		public Builder<ENUM, ENTITY> setFromIntFunc(Function<Integer, ENUM> fromIntFunc)
		{
			this.fromIntFunc = fromIntFunc;
			return this;
		}

		public Builder<ENUM, ENTITY> setDefaultValueSupplier(Supplier<ENUM> defaultValueSupplier)
		{
			this.defaultValueSupplier = defaultValueSupplier;
			return this;
		}

		public EntityEnumDataHelper<ENUM, ENTITY> build()
		{
			return new EntityEnumDataHelper<>(this);
		}
	}
}
