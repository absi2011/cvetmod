package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.ShatterAction;
import cvetmod.patches.CvetTags;
import cvetmod.powers.ShatterPower;

public class Shatter extends AbstractCvetCard {
    public static final String ID = "cvetmod:Shatter";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Shatter.png";
    public static final int COST = 2;
    public static final int SECOND_COST = 5;
    public static final int UPG_COST = 1;
    public Shatter() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        tags.add(CvetTags.IS_STRING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ShatterAction());
        addToBot(new ApplyPowerAction(p, p, new ShatterPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }
}
