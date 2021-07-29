package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.spg.armageddon.shared.Armageddon;

import java.util.TreeMap;

public class RomanNumeralGenerator extends LanguageProvider
{
	private final TreeMap<Integer, String> map = new TreeMap<>() {{
		put(1000, "M");
		put(900, "CM");
		put(500, "D");
		put(400, "CD");
		put(100, "C");
		put(90, "XC");
		put(50, "L");
		put(40, "XL");
		put(10, "X");
		put(9, "IX");
		put(5, "V");
		put(4, "IV");
		put(1, "I");
	}};

	public RomanNumeralGenerator(DataGenerator generator)
	{
		super(generator, Armageddon.ID_FORGE, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		// start at 11, vanilla already has 1 -> 10
		for(var i = 11; i <= 100; i++)
		{
			add("enchantment.level." + i, toRoman(i));
		}
	}

	private String toRoman(int number)
	{
		if(number <= 0)
			return "";

		var roman = map.floorKey(number);

		if(number == roman)
			return map.get(number);

		return map.get(roman) + toRoman(number - roman);
	}

	@Override
	public String getName()
	{
		return "RomanNumeral-Generator";
	}
}
