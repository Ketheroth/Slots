package com.ketheroth.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {

	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_PER_SLOT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> XP_ONLY;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PRESERVE_REWARD_ON_DEATH;

	static {
		ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

		LEVEL_PER_SLOT = BUILDER.comment("Obtain 1 slot every x levels (default : x=3)")
				.define("value", 3);
		XP_ONLY = BUILDER.comment("Determine if you gain slots only with levels (default: true)")
				.define("xp_only", true);
		PRESERVE_REWARD_ON_DEATH = BUILDER.comment("Determine if slots earned with items are preserved on death (default: true)")
				.define("preserve_reward_on_death", true);

		CONFIG = BUILDER.build();
	}

}
