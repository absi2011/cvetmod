package cvetmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ScorePatch {
    public static boolean IS_MELT = false;
    public static final ScoreBonusStrings MELT = CardCrawlGame.languagePack.getScoreString("cvetmod:Melt");
    @SpirePatch(clz = GameOverScreen.class, method = "checkScoreBonus")
    public static class CheckScorePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = "points")
        public static void Insert(boolean victory, @ByRef int[] points) {
//            if (AbstractDungeon.player.hasRelic(Melt.ID)) {
//                IS_MELT = true;
//                points[0] -= 25;
//            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "resetScoreChecks")
    public static class ResetScorePatch {
        @SpirePostfixPatch
        public static void Postfix() {
            IS_MELT = false;
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(VictoryScreen _inst) {
            try {
                Field stats = GameOverScreen.class.getDeclaredField("stats");
                stats.setAccessible(true);
                if (IS_MELT) {
                    ((ArrayList<GameOverStat>) stats.get(_inst)).add(new GameOverStat(MELT.NAME, MELT.DESCRIPTIONS[0], Integer.toString(-25)));
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to set game over stats.", e);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(VictoryScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
    public static class DeathStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(DeathScreen _inst) {
            try {
                Field stats = GameOverScreen.class.getDeclaredField("stats");
                stats.setAccessible(true);
                if (IS_MELT) {
                    ((ArrayList<GameOverStat>) stats.get(_inst)).add(new GameOverStat(MELT.NAME, MELT.DESCRIPTIONS[0], Integer.toString(-25)));
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to set game over stats.", e);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DeathScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }
}
