package cvetmod.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.GrowthAction;
import cvetmod.cards.special.Originium;
import cvetmod.patches.CvetTags;

public class Growth extends AbstractCvetCard {
    public static final String ID = "cvetmod:Growth";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int DAMAGE = 8;
    public static final int MULTI = 2;
    public static final int UPG_MULTI = 1;
    public Growth() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MULTI;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered()) {
            addToBot(new GrowthAction(this, 2));
        }
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MULTI);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
