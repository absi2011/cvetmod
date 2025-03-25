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

public class HalfDamagePower extends AbstractCvetPower {
    public static final String POWER_ID = "cvetmod:HalfDamagePower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public HalfDamagePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        //TODO: 找个机会创建个模板吧，别非要让我用cards的
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        amount --;
        updateDescription();
        if (amount == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            return damage * 0.5F;
        }
        return damage;
    }
}
