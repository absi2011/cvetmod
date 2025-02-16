package cvetmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import cvetmod.util.OriginiumPanel;
import cvetmod.util.OriginiumPileViewScreen;
import javassist.CtBehavior;

public class OriginiumPilePatch {
    @SpirePatch(clz = OverlayMenu.class, method = SpirePatch.CLASS)
    public static class OverlayMenuFields {
        public static SpireField<OriginiumPanel> originiumPanel = new SpireField<>(OriginiumPanel::new);
    }

    @SpirePatch(clz = OverlayMenu.class, method = "update")
    public static class UpdateOverlayMenuPatch {
        @SpirePrefixPatch
        public static void Prefix(OverlayMenu _inst) {
            OverlayMenuFields.originiumPanel.get(_inst).updatePositions();
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class ShowCombatPanelsPatch {
        @SpirePrefixPatch
        public static void Prefix(OverlayMenu _inst) {
            OverlayMenuFields.originiumPanel.get(_inst).show();
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class HideCombatPanelsPatch {
        @SpirePrefixPatch
        public static void Prefix(OverlayMenu _inst) {
            OverlayMenuFields.originiumPanel.get(_inst).hide();
        }
    }

    private static class RenderLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(OverlayMenu.class, "exhaustPanel");
            return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class RenderOverlayMenuPatch {
        @SpireInsertPatch(locator = RenderLocator.class)
        public static void Insert(OverlayMenu _inst, SpriteBatch sb) {
            OverlayMenuFields.originiumPanel.get(_inst).render(sb);
        }
    }

    @SpirePatch(clz = CardRewardScreen.class, method = "render")
    public static class RenderCardRewardScreenPatch {
        @SpireInsertPatch(locator = RenderLocator.class)
        public static void Insert(CardRewardScreen _inst, SpriteBatch sb) {
            OverlayMenuFields.originiumPanel.get(AbstractDungeon.overlayMenu).render(sb);
        }
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "render")
    public static class RenderHandCardSelectScreenPatch {
        @SpireInsertPatch(locator = RenderLocator.class)
        public static void Insert(HandCardSelectScreen _inst, SpriteBatch sb) {
            OverlayMenuFields.originiumPanel.get(AbstractDungeon.overlayMenu).render(sb);
        }
    }

    private static class SwitchScreenLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
            return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class AbstractDungeonUpdatePatch {
        @SpireInsertPatch(locator = SwitchScreenLocator.class)
        public static void Insert(AbstractDungeon _inst) {
            if (AbstractDungeon.screen == OriginiumPileViewScreen.ORIGINIUM_VIEW) {
                OriginiumPanel.originiumPileViewScreen.update();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class AbstractDungeonRenderPatch {
        @SpireInsertPatch(locator = SwitchScreenLocator.class)
        public static void Insert(AbstractDungeon _inst, SpriteBatch sb) {
            if (AbstractDungeon.screen == OriginiumPileViewScreen.ORIGINIUM_VIEW) {
                OriginiumPanel.originiumPileViewScreen.render(sb);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "openPreviousScreen")
    public static class OpenPreviousScreenPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractDungeon.CurrentScreen s) {
            if (s == OriginiumPileViewScreen.ORIGINIUM_VIEW) {
                OriginiumPanel.originiumPileViewScreen.reopen();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class CloseCurrentScreenPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            if (AbstractDungeon.screen == OriginiumPileViewScreen.ORIGINIUM_VIEW) {
                AbstractDungeon.overlayMenu.cancelButton.hide();

                // AbstractDungeon.genericScreenOverlayReset
                if (AbstractDungeon.previousScreen == null) {
                    if (AbstractDungeon.player.isDead) {
                        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
                    } else {
                        AbstractDungeon.isScreenUp = false;
                        AbstractDungeon.overlayMenu.hideBlackScreen();
                    }
                }

                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
                    AbstractDungeon.overlayMenu.showCombatPanels();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                int[] pos = LineFinder.findAllInOrder(ctBehavior, fieldAccessMatcher);
                return new int[]{pos[1]};
            }
        }
    }
}
