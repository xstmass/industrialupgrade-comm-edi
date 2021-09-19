
package com.denfop.damagesource;

import net.minecraft.util.DamageSource;

public class IUDamageSource extends DamageSource {
    public static final IUDamageSource current;
    public static final IUDamageSource radiation;

    public IUDamageSource(final String s) {
        super(s);
    }

    static {

        current = (IUDamageSource) new IUDamageSource(("current")).setDamageBypassesArmor();
        radiation = (IUDamageSource) new IUDamageSource(("radiation")).setDamageBypassesArmor().setFireDamage();

    }

}
