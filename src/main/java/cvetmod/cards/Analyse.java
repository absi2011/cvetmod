package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.GetRandomCardFromOriginium;
import cvetmod.cards.special.Originium;

public class Analyse extends AbstractCvetCard {
    public static final String ID = "cvetmod:Analyse";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 2;
    public static final int UPG_SECOND_COST = 1;
    public static final int CARDS = 3;
    public Analyse() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        magicNumber = secondMagicNumber = CARDS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GetRandomCardFromOriginium(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseSecondCost(UPG_SECOND_COST);
            initializeDescription();
        }
    }
}
