package cvetmod.cards.special;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import cvetmod.actions.OriginiumAction;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.vfx.OriginiumEffect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

public class Originium extends CustomCard implements CustomSavable<String[]> {
    public static final String ID = "cvetmod:Originium";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Originium.png";
    public static final int COST = -2;
    public static CardGroup originiumPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public Originium() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onRemoveFromMasterDeck() {
        AbstractDungeon.effectsQueue.add(new OriginiumEffect(new Originium(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    public void triggerOnExhaust() {
        addToBot(new MakeTempCardInHandAction(makeCopy()));
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Originium();
    }
    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.selfRetain = selfRetain;
        card.isInnate = isInnate;
        card.rawDescription = rawDescription;
        card.initializeDescription();
        return card;
    }

    public String[] onSave() {
        String[] save = new String[3];
        save[0] = rawDescription;
        save[1] = "False";
        save[2] = "False";
        if (isInnate) {
            save[1] = "True";
        }
        if (selfRetain) {
            save[2] = "True";
        }
        return save;
    }

    public void onLoad(String[] info)
    {
        rawDescription = info[0];
        if ((info.length >= 2) && (info[1].equals("True"))) {
            isInnate = true;
        }
        if ((info.length >= 3) && (info[2].equals("True"))) {
            selfRetain = true;
        }
    }
    @Override
    public void upgrade() {
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatch {
        @SpireInsertPatch(rloc = 52)
        public static SpireReturn<?> Insert(UseCardAction _inst) {
            try {
                Field field = UseCardAction.class.getDeclaredField("targetCard");
                field.setAccessible(true);
                AbstractCard card = (AbstractCard)(field.get(_inst));
                if (originiumInHand() || ((card instanceof AbstractCvetCard) && ((AbstractCvetCard)card).originiumAfterPlay)) {
                    CardGroup hand = AbstractDungeon.player.hand;
                    if (AbstractDungeon.player.hoveredCard == card) {
                        AbstractDungeon.player.releaseCard();
                    }

                    AbstractDungeon.actionManager.removeFromQueue(card);
                    card.unhover();
                    card.untip();
                    card.stopGlowing();
                    addToOriginium(hand, card);
                    card.exhaustOnUseOnce = false;
                    card.dontTriggerOnUseCard = false;
                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                    Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
                    method.setAccessible(true);
                    method.invoke(_inst);
                    return SpireReturn.Return();
                }
                else {
                    return SpireReturn.Continue();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean originiumInHand() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Originium) {
                return true;
            }
        }
        return false;
    }

    public static void addToOriginium(CardGroup group, AbstractCard card) {
        group.removeCard(card);
        //TODO: 来个源石吃掉它的特效，souls特效不合适
        if (group.type == CardGroup.CardGroupType.HAND) {
            card.shrink();
        }
        AbstractDungeon.getCurrRoom().souls.discard(card,true);
        originiumPile.addToBottom(card);
    }
}
