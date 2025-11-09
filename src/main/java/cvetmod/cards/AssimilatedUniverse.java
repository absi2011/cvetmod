package cvetmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.AssimilatedUniverseAction;
import cvetmod.actions.SealAction;

public class AssimilatedUniverse extends AbstractCvetCard {
    public static final String ID = "cvetmod:AssimilatedUniverse";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Seal.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 2;
    public static final int UPG_SECOND_COST = 1;
    public AssimilatedUniverse() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AssimilatedUniverseAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeSecondMagicNumber(UPG_SECOND_COST);
            upgradeName();
            initializeDescription();
        }
    }
}
