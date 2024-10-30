package cvetmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import cvetmod.CvetMod;

public class BottledCalcium extends AbstractPotion {
    public static String ID = "cvetmod:BottledCalcium";
    public static PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static String NAME = potionStrings.NAME;
    public static String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static int CA_NUM = 5;
    public BottledCalcium() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.H, PotionEffect.NONE, Color.GRAY, null, Color.DARK_GRAY);
        labOutlineColor = CvetMod.CvetPink;
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        description = DESCRIPTIONS[0] + getPotency() + DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
    }

    @Override
    public int getPotency(int i) {
        return CA_NUM;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledCalcium();
    }
}
