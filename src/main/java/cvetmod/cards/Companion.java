package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.CvetMod;

public class Companion extends AbstractCvetCard {
    public static final String ID = "cvetmod:Companion";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 0;
    public Companion() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        baseBlock = block = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        baseBlock = calcBlockAmt();
        super.applyPowers();
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseBlock = calcBlockAmt();
        super.calculateCardDamage(mo);
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    private int calcBlockAmt() {
        int cnt = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            cnt += c.costForTurn;
            if (c instanceof AbstractCvetCard) cnt += ((AbstractCvetCard) c).getSecondCost();
        }
        return cnt;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
