package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.cards.special.TheRealityOfEnd;
import cvetmod.patches.CvetTags;

public class Terminate extends AbstractCvetCard {
    public static final String ID = "cvetmod:Terminate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 3;
    public static final int SECOND_COST = 0;
    public static final int DAMAGE_AMT = 3;
    public static final int UPG_AMT = 1;
    public Terminate() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        damage = baseDamage = DAMAGE_AMT;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
        cardsToPreview = new TheRealityOfEnd();
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered()) {
            AbstractCard c = new TheRealityOfEnd();
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
            upgradeDamage(UPG_AMT);
            rawDescription = UPGRADE_DESCRIPTION;
            cardsToPreview.upgrade();
            initializeDescription();
        }
    }
}
