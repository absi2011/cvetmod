package cvetmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import cvetmod.cards.Inhibitor;
import cvetmod.cards.special.TempInhibitor;

public class ChimeraPower extends AbstractCvetPower {
    public static final String POWER_ID = "cvetmod:ChimeraPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static int turns;
    public ChimeraPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        turns = 1;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/powers/ChimeraPower_84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/powers/ChimeraPower_32.png"), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        turns = 1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + turns + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if ((usedCard instanceof Inhibitor) || (usedCard instanceof TempInhibitor)) {
            turns++;
            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn() {
        turns --;
        updateDescription();
        if (turns == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.HP_LOSS)));
        }
    }
}
