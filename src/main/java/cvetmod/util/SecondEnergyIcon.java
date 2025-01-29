package cvetmod.util;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import cvetmod.patches.EnergyPatch;

public class SecondEnergyIcon extends AbstractCustomIcon {
    public static final String ID = "cvetmod:E2";
    private static SecondEnergyIcon singleton;

    public SecondEnergyIcon() {
        super(ID, EnergyPatch.secondOrb);
    }

    public static SecondEnergyIcon get()
    {
        if (singleton == null) {
            singleton = new SecondEnergyIcon();
        }
        return singleton;
    }
}
