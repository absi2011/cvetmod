package cvetmod.cards.special;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.cards.special.Originium;

public class TempInhibitor extends AbstractCvetCard {
    public static final String ID = "cvetmod:TempInhibitor";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 0;
    public TempInhibitor() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);
        color = CardColor.COLORLESS;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard c: p.hand.group) {
            if (c instanceof Originium) {
                addToBot(new DiscardSpecificCardAction(c));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
