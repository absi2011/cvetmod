package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Missing extends AbstractCvetCard {
    public static final String ID = "cvetmod:Missing";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Missing.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 1;
    public static final int BLOCK_GAIN = 6;
    public static final int BLOCK_GAIN_UPG = 2;
    public Missing() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_GAIN;
    }

    private int calcMagicNumber() {
        int cnt = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (card.uuid.equals(uuid)) {
                cnt ++;
            }
        }
        return cnt;
    }

    @Override
    public void applyPowers() {
        magicNumber = calcMagicNumber();
        isMagicNumberModified = true;
        super.applyPowers();
        rawDescription = DESCRIPTION;
        rawDescription = rawDescription + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < calcMagicNumber(); i++) {       // 会统计自身这次
            addToBot(new GainBlockAction(p, block));
        }
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        magicNumber = calcMagicNumber();
        isMagicNumberModified = true;
        super.calculateCardDamage(mo);
        rawDescription = DESCRIPTION;
        rawDescription = rawDescription + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(BLOCK_GAIN_UPG);
            initializeDescription();
        }
    }
}
