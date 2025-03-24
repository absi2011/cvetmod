package cvetmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.CrackAction;
import cvetmod.actions.SealAction;

public class Seal extends AbstractCvetCard {
    public static final String ID = "cvetmod:Seal";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int CARDS_SEAL = 1;
    public static final int UPG_CARDS_SEAL = 1;
    public Seal() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        exhaust = true;
        magicNumber = baseMagicNumber = CARDS_SEAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SealAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_CARDS_SEAL);
            initializeDescription();
        }
    }
}
