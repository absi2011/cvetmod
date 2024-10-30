package cvetmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCvetCard extends CustomCard {
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;

    public AbstractCvetCard(String id, String name, String img, int cost, String rawDescription,
                             AbstractCard.CardType type, AbstractCard.CardColor color,
                             AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return new ArrayList<>();
    }

    public void upgradeSecondMagicNumber(int amount) {
        baseSecondMagicNumber += amount;
        secondMagicNumber = baseSecondMagicNumber;
        upgradedSecondMagicNumber = true;
    }

    public boolean extraTriggered() {
        return false;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (extraTriggered())
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        else
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    public static void addSpecificCardsToReward(AbstractCard card) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(card);
        addSpecificCardsToReward(cards);
    }

    public static void addSpecificCardsToReward(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (!card.canUpgrade()) continue;
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onPreviewObtainCard(card);
        }
        RewardItem item = new RewardItem();
        item.cards = cards;
        AbstractDungeon.getCurrRoom().addCardReward(item);
    }

    static public void copyStat(AbstractCvetCard s, AbstractCvetCard t) {
        t.name = s.name;
        t.target = s.target;
        t.upgraded = s.upgraded;
        t.timesUpgraded = s.timesUpgraded;
        t.baseDamage = s.baseDamage;
        t.baseBlock = s.baseBlock;
        t.baseMagicNumber = s.baseMagicNumber;
        t.baseSecondMagicNumber = s.baseSecondMagicNumber;
        t.damage = s.damage;
        t.block = s.block;
        t.magicNumber = s.magicNumber;
        t.secondMagicNumber = s.secondMagicNumber;
        t.cost = s.cost;
        t.costForTurn = s.costForTurn;
        t.isCostModified = s.isCostModified;
        t.isCostModifiedForTurn = s.isCostModifiedForTurn;
        t.inBottleLightning = s.inBottleLightning;
        t.inBottleFlame = s.inBottleFlame;
        t.inBottleTornado = s.inBottleTornado;
        t.isSeen = s.isSeen;
        t.isLocked = s.isLocked;
        t.misc = s.misc;
        t.freeToPlayOnce = s.freeToPlayOnce;
        t.exhaust = s.exhaust;
        t.isEthereal = s.isEthereal;
        t.retain = s.retain;
        t.selfRetain = s.selfRetain;
        t.isInnate = s.isInnate;
        t.returnToHand = s.returnToHand;
        t.shuffleBackIntoDrawPile = s.shuffleBackIntoDrawPile;
        t.cardsToPreview = s.cardsToPreview;
        t.rawDescription = s.rawDescription;
        t.baseDraw = s.baseDraw;
        t.isInAutoplay = s.isInAutoplay;
        t.type = s.type;
        t.tags.clear();
        t.tags.addAll(s.tags);
        if (!t.textureImg.equals(s.textureImg)) {
            t.textureImg = s.textureImg;
            t.loadCardImage(t.textureImg);
        }
        t.initializeDescription();
    }

    @Override
    public AbstractCvetCard makeStatEquivalentCopy() {
        AbstractCvetCard card = (AbstractCvetCard) this.makeCopy();
        copyStat(this, card);
        return card;
    }

    public static class SecondMagicNumber extends DynamicVariable {
        @Override
        public String key() {
            return "cvetmod:M2";
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractCvetCard) {
                return ((AbstractCvetCard) card).isSecondMagicNumberModified;
            } else {
                return false;
            }
        }

        @Override
        public void setIsModified(AbstractCard card, boolean v) {
            if (card instanceof AbstractCvetCard) {
                ((AbstractCvetCard) card).isSecondMagicNumberModified = v;
            }
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractCvetCard) {
                return ((AbstractCvetCard) card).secondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractCvetCard) {
                return ((AbstractCvetCard) card).baseSecondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractCvetCard) {
                return ((AbstractCvetCard) card).upgradedSecondMagicNumber;
            } else {
                return false;
            }
        }
    }
}