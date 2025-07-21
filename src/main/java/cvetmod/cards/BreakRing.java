package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import cvetmod.actions.DrawSpecificCardAction;
import cvetmod.cards.special.Originium;

public class BreakRing extends AbstractCvetCard {
    public static final String ID = "cvetmod:BreakRing";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/BreakRing.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int HP_LOSS = 2;
    public static final int STR_GAIN = 3;
    public static final int EXTRA_STR = 1;
    public BreakRing() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = HP_LOSS;
        secondMagicNumber = baseSecondMagicNumber = STR_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(p, new DamageInfo(p, magicNumber, DamageInfo.DamageType.HP_LOSS)));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, secondMagicNumber)));
        addToBot(new DrawSpecificCardAction(p.drawPile, Originium.ID));
        addToBot(new DrawSpecificCardAction(p.discardPile, Originium.ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(EXTRA_STR);
            initializeDescription();
        }
    }
}
