package cvetmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import cvetmod.CvetMod;
import cvetmod.cards.special.Originium;
import cvetmod.patches.AbstractCardEnum;
import cvetmod.patches.CvetTags;
import cvetmod.powers.ShatterPower;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCvetCard extends CustomCard {
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public int secondCost;
    public boolean upgradedSecondCost;
    public boolean isSecondCostModified;
    public int secondEnergyOnUse;
    public boolean originiumAfterPlay;
    public int secondCostReduce = 0;
    public int secondCostEqual = -1;

    public AbstractCvetCard(String id, String name, String img, int cost, int secondCost,
                            String rawDescription, AbstractCard.CardType type,
                            AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, AbstractCardEnum.CVET_PINK, rarity, target);
        this.secondCost = secondCost;
        upgradedSecondCost = false;
        isSecondCostModified = false;
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

    public void reduceSecondCost(int amount) {
        if ((this.secondCost == -1) || (this.secondCost == -2)) {
            return;
        }
        this.secondCost -= amount;
        if (secondCost < 0) {
            secondCost = 0;
        }
    }

    public boolean extraTriggered() {
        return false;
    }

    public boolean OriginiumArts() {
        if (AbstractDungeon.player.hasPower(ShatterPower.POWER_ID)) {
            return true;
        }
        for (AbstractCard c: AbstractDungeon.player.hand.group) {
            if (c instanceof Originium) {
                return true;
            }
        }
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
        t.upgradedDamage = s.upgradedDamage;
        t.upgradedBlock = s.upgradedBlock;
        t.upgradedMagicNumber = s.upgradedMagicNumber;
        t.upgradedSecondMagicNumber = s.upgradedSecondMagicNumber;
        t.cost = s.cost;
        t.costForTurn = s.costForTurn;
        t.upgradedCost = s.upgradedCost;
        t.isCostModified = s.isCostModified;
        t.isCostModifiedForTurn = s.isCostModifiedForTurn;
        t.secondCost = s.secondCost;
        t.upgradedSecondCost = s.upgradedSecondCost;
        t.isSecondCostModified = s.isSecondCostModified;
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
        if (s.cardsToPreview != null) {
            t.cardsToPreview = s.cardsToPreview.makeStatEquivalentCopy();
        }
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
        t.secondCostEqual = s.secondCostEqual;
        t.secondCostReduce = s.secondCostReduce;
        t.initializeDescription();
    }

    @Override
    public AbstractCvetCard makeStatEquivalentCopy() {
        AbstractCvetCard card = (AbstractCvetCard) this.makeCopy();
        copyStat(this, card);
        return card;
    }

    public int getSecondCost() {
        return getSecondCost(false);
    }

    public int getSecondCost(boolean isPlaying) {
        if ((secondCost == -1) || (secondCost == -2)) {
            return secondCost;
        }
        if (AbstractDungeon.getCurrMapNode() == null) {
            return secondCost;
        }
        int realCost = secondCost;
        if (secondCostEqual != -1) {
            realCost = secondCostEqual;
        }
        realCost -= secondCostReduce;
        if (hasTag(CvetTags.IS_STRING)) {
            realCost -= CvetMod.stringCount;
            if (isPlaying)
            {
                realCost ++;    // 抵消打出时本张牌计算的费用。
            }
        }
        if (realCost < 0) {
            realCost = 0;
        }
        isSecondCostModified = (realCost != secondCost);
        return realCost;
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

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondCost) {
            isSecondCostModified = true;
        }
    }

    public void upgradeBaseSecondCost(int newBaseCost) {
        secondCost = newBaseCost;
        upgradedSecondCost = true;
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);  // 这个函数就是cost变0
        setSecondCostThisTurn(0);
    }

    public void setSecondCostThisTurn(int amt) {
        secondCostReduce = 0;
        secondCostEqual = amt;
    }

    public void reduceSecondCostThisTurn(int amt) {
        secondCostReduce += amt;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c == this) {
            secondCostEqual = -1;
            secondCostReduce = 0;
        }
    }

    public void atTurnStart() {
        secondCostEqual = -1;
        secondCostReduce = 0;
    }
}