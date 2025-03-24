package cvetmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.TerminateAction;
import cvetmod.cards.special.Originium;
import cvetmod.cards.special.TheRealityOfEnd;
import cvetmod.patches.CvetTags;

public class Terminate extends AbstractCvetCard {
    public static final String ID = "cvetmod:Terminate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Terminate.png";
    public static final int COST = 3;
    public static final int SECOND_COST = 0;
    public static final int DAMAGE_AMT = 3;
    public static final int UPG_AMT = 1;
    public Terminate() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        magicNumber = baseMagicNumber = DAMAGE_AMT;
        damage = baseDamage = 0;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
        cardsToPreview = new TheRealityOfEnd();
        isMultiDamage = true;
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            AbstractCard c = new TheRealityOfEnd();
            if (upgraded) {
                c.upgrade();
            }
            addToBot(new TerminateAction(this));
            addToBot(new MakeTempCardInHandAction(c));
            originiumAfterPlay = true;
        }
        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
    }

    private int groupCount(CardGroup group) {
        int cnt = 0;
        for (AbstractCard c: group.group) {
            if (!(c instanceof Originium)) {
                cnt ++;
            }
        }
        return cnt;
    }

    private int cardsCount() {
        AbstractPlayer p = AbstractDungeon.player;
        return groupCount(p.hand) + groupCount(p.discardPile) + groupCount(p.drawPile);
    }

    @Override
    public void applyPowers() {
        baseDamage = magicNumber * Originium.originiumPile.size();
        if (extraTriggered()) {
            baseDamage += magicNumber * cardsCount();
        }
        super.applyPowers();
        isDamageModified = (baseDamage != damage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = magicNumber * Originium.originiumPile.size();
        if (extraTriggered()) {
            baseDamage += magicNumber * cardsCount();
        }
        super.calculateCardDamage(mo);
        isDamageModified = (baseDamage != damage);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_AMT);
            rawDescription = UPGRADE_DESCRIPTION;
            cardsToPreview.upgrade();
            initializeDescription();
        }
    }
}
