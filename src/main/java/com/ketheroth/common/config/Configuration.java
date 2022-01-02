package com.ketheroth.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {

	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_PER_SLOT;

	static {
		ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

		LEVEL_PER_SLOT = BUILDER.comment("Obtain 1 slot every x levels (default : x=3)")
				.define("value", 3);

		CONFIG = BUILDER.build();
	}

}
