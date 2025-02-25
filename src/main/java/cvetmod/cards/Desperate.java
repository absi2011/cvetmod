package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.patches.CvetTags;

public class Desperate extends AbstractCvetCard {
    public static final String ID = "cvetmod:Desperate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Desperate.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int BLOCK_AMT = 7;
    public static final int UPG_AMT = 2;
    public Desperate() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        cardsToPreview = new Saving();
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered()) {
            AbstractCard c = new Saving();
            if (upgraded) {
                c.upgrade();
            }
            addToBot(new MakeTempCardInHandAction(c));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_AMT);
            rawDescription = UPGRADE_DESCRIPTION;
            cardsToPreview.upgrade();
            initializeDescription();
        }
    }
}
