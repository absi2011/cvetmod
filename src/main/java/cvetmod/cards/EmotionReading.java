package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.patches.CvetTags;

public class EmotionReading extends AbstractCvetCard {
    public static final String ID = "cvetmod:EmotionReading";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/EmotionReading.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int BLOCK_GAIN = 8;
    public static final int CARDS_DRAW = 3;
    public static final int BLOCK_GAIN_UPG = 3;
    public static final int CARDS_DRAW_UPG = 1;
    public EmotionReading() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
        block = baseBlock = BLOCK_GAIN;
        magicNumber = baseMagicNumber = CARDS_DRAW;
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered()) {
            addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(BLOCK_GAIN_UPG);
            upgradeMagicNumber(CARDS_DRAW_UPG);
            initializeDescription();
        }
    }
}
