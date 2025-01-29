package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAndDeckAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.StruggleAction;
import cvetmod.patches.CvetTags;

public class Struggle extends AbstractCvetCard {
    public static final String ID = "cvetmod:Struggle";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 0;
    public static final int SECOND_COST = 0;
    public static final int DAMAGE_AMT = 3;
    public static final int ADD_AMT = 2;
    public static final int UPG_AMT = 2;
    public Struggle() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE_AMT;
        magicNumber = baseMagicNumber = ADD_AMT;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        addToBot(new StruggleAction(this));
        if (extraTriggered()) {
            AbstractCard c = new Struggle();
            if (upgraded) c.upgrade();
            addToBot(new MakeTempCardInDiscardAndDeckAction(c));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_AMT);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
