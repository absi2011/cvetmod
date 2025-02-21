package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.HelloWorld;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.CvetMod;
import cvetmod.patches.CvetTags;

public class Wave extends AbstractCvetCard {
    public static final String ID = "cvetmod:Wave";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 1;
    public static final int BLOCK_AMT = 7;
    public static final int UPG_AMT_B = 4;
    public Wave() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
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
            AbstractCard c = CvetMod.getRandomCard(CvetTags.IS_ORIGINIUM_ARTS);
            addToBot(new MakeTempCardInHandAction(c));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_AMT_B);
            initializeDescription();
        }
    }
}
