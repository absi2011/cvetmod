package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.patches.CvetTags;

public class Saving extends AbstractCvetCard {
    public static final String ID = "cvetmod:Saving";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Saving.png";
    public static final int COST = 2;
    public static final int SECOND_COST = 0;
    public static final int BLOCK_AMT = 7;
    public static final int DAMAGE_AMT = 7;
    public static final int UPG_AMT_B = 2;
    public static final int UPG_AMT_D = 2;
    public Saving() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE_AMT;
        block = baseBlock = BLOCK_AMT;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
        cardsToPreview = new Terminate();
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered()) {
            AbstractCard c = new Terminate();
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
            upgradeDamage(UPG_AMT_D);
            upgradeBlock(UPG_AMT_B);
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
