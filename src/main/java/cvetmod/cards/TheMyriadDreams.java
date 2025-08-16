package cvetmod.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.cards.special.Originium;
import cvetmod.patches.CvetTags;
import cvetmod.powers.WISPower;

public class TheMyriadDreams extends AbstractCvetCard {
    public static final String ID = "cvetmod:TheMyriadDreams";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 7;
    public static final int DAMAGE = 37;
    public static final int UPG_DAMAGE = 8;
    public TheMyriadDreams() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        damage = baseDamage = DAMAGE;
        isMultiDamage = true;
        selfRetain = true;
        tags.add(CvetTags.IS_STRING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public boolean extraTriggered() {
        return ((!Originium.originiumInHand()) && (!exhaust) && !(returnToHand) && !(shuffleBackIntoDrawPile));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            initializeDescription();
        }
    }
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatch {
        @SpireInsertPatch(rloc = 67)
        public static SpireReturn<?> Insert(UseCardAction _inst) {
            AbstractCard c = ReflectionHacks.getPrivate(_inst, UseCardAction.class, "targetCard");
            if (c instanceof TheMyriadDreams) {
                AbstractCard c2 = new TheMyriadDreams();
                if (c.upgraded) {
                    c2.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c2));
            }
            return SpireReturn.Continue();
        }
    }
}
