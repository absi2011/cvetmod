package cvetmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.colorless.Chrysalis;
import com.megacrit.cardcrawl.cards.colorless.Metamorphosis;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.MummifiedHand;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.relics.WristBlade;
import cvetmod.cards.AbstractCvetCard;
import javassist.CtBehavior;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SecondCostPatch {
    // 这里Patch的是各种 耗能变为1/0的内容
    // EnlightenmentAction : 耗能变为1
    @SpirePatch(clz = EnlightenmentAction.class, method = "update")
    public static class EnlightenmentActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void Insert(EnlightenmentAction _inst, boolean ___forCombat, AbstractCard c) {
            if (c instanceof AbstractCvetCard) {
                if (((AbstractCvetCard) c).getSecondCost() > 1) {
                    ((AbstractCvetCard) c).setSecondCostThisTurn(1);
                }
                if (___forCombat && ((AbstractCvetCard) c).secondCost > 1) {
                    ((AbstractCvetCard) c).upgradeBaseSecondCost(1);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    // ForethoughtAction : 判断耗能>0
    @SpirePatch(clz = ForethoughtAction.class, method = "update")
    public static class ForethoughtActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void Insert(ForethoughtAction _inst, AbstractCard c) {
            if (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).getSecondCost() > 0) {
                c.freeToPlayOnce = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToBottomOfDeck");
                return LineFinder.findAllInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    // MadnessAction : 判断耗能>0，耗能变为0
    @SpirePatch(clz = MadnessAction.class, method = "update")
    public static class MadnessActionUpdatePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c", "betterPossible", "possible"})
        public static void Insert(MadnessAction _inst, AbstractCard c, @ByRef boolean[] betterPossible, @ByRef boolean[] possible) {
            if (c instanceof AbstractCvetCard) {
                if (((AbstractCvetCard) c).getSecondCost() > 0) {
                    possible[0] = true;
                } else if (((AbstractCvetCard) c).secondCost > 0) {
                    possible[0] = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = MadnessAction.class, method = "findAndModifyCard")
    public static class MadnessActionModifyPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(MadnessAction _inst, boolean better) {
            AbstractCard c;
            if (better) {
                do {
                    c = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                } while (c.costForTurn == 0 && (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).getSecondCost() == 0));
            } else {
                do {
                    c = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                } while (c.cost == 0 && (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).secondCost == 0));
            }
            c.cost = 0;
            c.costForTurn = 0;
            if (c instanceof AbstractCvetCard) {
                ((AbstractCvetCard) c).secondCost = 0;
                ((AbstractCvetCard) c).setSecondCostThisTurn(0);
            }
            return SpireReturn.Return();
        }
    }

    // RandomizeHandCostAction : 随机耗能
    @SpirePatch(clz = RandomizeHandCostAction.class, method = "update")
    public static class RandomizeHandCostActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card"})
        public static void Insert(RandomizeHandCostAction _inst, AbstractCard card) {
            if (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).secondCost >= 0) {
                int newCost = AbstractDungeon.cardRandomRng.random(3);
                if (((AbstractCvetCard) card).secondCost != newCost) {
                    ((AbstractCvetCard) card).secondCost = newCost;
                    ((AbstractCvetCard) card).setSecondCostThisTurn(newCost);
                    ((AbstractCvetCard) card).isSecondCostModified = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cost");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    // SetupAction : 判断耗能>0
    @SpirePatch(clz = SetupAction.class, method = "update")
    public static class SetupActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void Insert(SetupAction _inst, AbstractCard c) {
            if (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).getSecondCost() > 0) {
                c.freeToPlayOnce = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDeck");
                return LineFinder.findAllInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    // Chrysalis : 判断耗能>0，耗能变为0
    @SpirePatch(clz = Chrysalis.class, method = "use")
    public static class ChrysalisPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card"})
        public static void Insert(Chrysalis _inst, AbstractPlayer p, AbstractMonster m, AbstractCard card) {
            if (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).secondCost > 0) {
                ((AbstractCvetCard) card).secondCost = 0;
                ((AbstractCvetCard) card).setSecondCostThisTurn(0);
                ((AbstractCvetCard) card).isSecondCostModified = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cost");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    // Metamorphosis : 判断耗能>0，耗能变为0
    @SpirePatch(clz = Metamorphosis.class, method = "use")
    public static class MetamorphosisPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card"})
        public static void Insert(Metamorphosis _inst, AbstractPlayer p, AbstractMonster m, AbstractCard card) {
            if (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).secondCost > 0) {
                ((AbstractCvetCard) card).secondCost = 0;
                ((AbstractCvetCard) card).setSecondCostThisTurn(0);
                ((AbstractCvetCard) card).isSecondCostModified = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cost");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    // ConfusionPower : 随机耗能
    @SpirePatch(clz = ConfusionPower.class, method = "onCardDraw")
    public static class ConfusionPowerPatch {
        @SpirePostfixPatch
        public static void Postfix(ConfusionPower _inst, AbstractCard card) {
            if (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).secondCost >= 0) {
                int newCost = AbstractDungeon.cardRandomRng.random(3);
                if (((AbstractCvetCard) card).secondCost != newCost) {
                    ((AbstractCvetCard) card).secondCost = newCost;
                    ((AbstractCvetCard) card).setSecondCostThisTurn(newCost);
                    ((AbstractCvetCard) card).isSecondCostModified = true;
                }
            }
        }
    }

    // Enchiridion : 耗能变为0
    @SpirePatch(clz = Enchiridion.class, method = "atPreBattle")
    public static class EnchiridionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void Insert(Enchiridion _inst, AbstractCard c) {
            if (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).secondCost != -1) {
                ((AbstractCvetCard) c).setSecondCostThisTurn(0);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cost");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    // MummifiedHand : 判断耗能>0
    @SpirePatch(clz = MummifiedHand.class, method = "onUseCard")
    public static class MummifiedHandPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c", "groupCopy"})
        public static void Insert(MummifiedHand _inst, AbstractCard card, UseCardAction action, AbstractCard c, ArrayList<AbstractCard> groupCopy) {
            if (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).getSecondCost() > 0) {
                groupCopy.add(c);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Logger.class, "info");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    // Necronomicon : 费用>=2，建议判断为任意费用>=2
    @SpirePatch(clz = Necronomicon.class, method = "onUseCard")
    public static class NecronomiconPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(Necronomicon _inst, AbstractCard card, UseCardAction action, @ByRef boolean[] ___activated, @ByRef boolean[] ___pulse) {
            if (card.type == AbstractCard.CardType.ATTACK &&
                    (((card.costForTurn >= 2 || (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).getSecondCost() >= 2)) && !card.freeToPlayOnce) ||
                            (card.cost == -1 && card.energyOnUse >= 2) ||
                            (card instanceof AbstractCvetCard && ((AbstractCvetCard) card).secondCost == -1 && ((AbstractCvetCard) card).secondEnergyOnUse >= 2)) &&
                    ___activated[0]) {
                ___activated[0] = false;
                _inst.flash();
                AbstractMonster m = null;
                if (action.target != null) {
                    m = (AbstractMonster)action.target;
                }

                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, _inst));
                AbstractCard tmp = card.makeSameInstanceOf();
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                tmp.applyPowers();
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                ___pulse[0] = false;
            }
            return SpireReturn.Return();
        }
    }

    // WristBlade : 理论见不到，也很好重写
    @SpirePatch(clz = WristBlade.class, method = "atDamageModify")
    public static class WristBladePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(WristBlade _inst, float damage, AbstractCard c) {
            if (c.costForTurn != 0 && (!c.freeToPlayOnce || c.cost == -1)) return SpireReturn.Return(damage);
            if (!(c instanceof AbstractCvetCard)) return SpireReturn.Return(damage + 4.0F);
            if (((AbstractCvetCard) c).getSecondCost() != 0 && (!c.freeToPlayOnce || ((AbstractCvetCard) c).secondCost == -1)) return SpireReturn.Return(damage);
            return SpireReturn.Return(damage + 4.0F);
        }
    }
}
