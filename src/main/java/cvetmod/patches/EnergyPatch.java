package cvetmod.patches;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.characters.CivilightEterna;
import cvetmod.util.CostReserves;
import cvetmod.util.CvetEnergyManager;
import cvetmod.util.SecondCostEnergyOrb;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.logging.Logger;

import static com.megacrit.cardcrawl.core.CardCrawlGame.saveFile;

public class EnergyPatch {
    private static final Texture orbTexture = new Texture("cvetmod/images/ManaTheresa.png");
    public static final TextureAtlas.AtlasRegion secondOrb = new TextureAtlas.AtlasRegion(orbTexture, 0, 0, orbTexture.getWidth(), orbTexture.getHeight());

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCard _inst) {
            if (_inst instanceof AbstractCvetCard) {
                if ((EnergyPanel.getCurrentEnergy() >= _inst.costForTurn && CostReserves.reserveCount() >= ((AbstractCvetCard) _inst).getSecondCost()) ||
                        _inst.freeToPlay() || _inst.isInAutoplay) {
                    return SpireReturn.Return(true);
                }
                _inst.cantUseMessage = AbstractCard.TEXT[11];
                return SpireReturn.Return(false);
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class ShowTextForReserves {
        @SpirePostfixPatch
        public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
            if (AbstractDungeon.player instanceof CivilightEterna) {
                String toShow = CostReserves.reserveCount() + "/" + ((CvetEnergyManager) AbstractDungeon.player.energy).secondEnergy;
                AbstractDungeon.player.getEnergyNumFont().getData().setScale(SecondCostEnergyOrb.fontScale);
                FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), toShow, __instance.current_x + SecondCostEnergyOrb.X_OFFSET, __instance.current_y + SecondCostEnergyOrb.Y_OFFSET, Color.WHITE);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
    public static class EnergyManagerPatch {
        @SpirePostfixPatch
        public static void Postfix(CardCrawlGame __instance, AbstractPlayer p) {
            if (p instanceof CivilightEterna) {
                p.energy = new CvetEnergyManager(saveFile.red, saveFile.red);
            }
        }
    }


    @SpirePatch(clz = TipHelper.class, method = "renderBox")
    public static class RenderBoxPatch {
        private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(SpriteBatch sb, String word, float x, float y) {
            if (word.equals("[cvetmod:E2]")) {
                TipHelper.renderTipEnergy(sb, secondOrb, x + 22.0F * Settings.scale, y - 8.0F * Settings.scale);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(SecondCostEnergyOrb.TEXT[0]), x + 22.0F * Settings.scale * 2.5F, y + 12.0F * Settings.scale, Settings.GOLD_COLOR);
                FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, GameDictionary.TEXT[0], x + 22.0F * Settings.scale, y - 20.0F * Settings.scale, 280.0F * Settings.scale, 26.0F * Settings.scale, BASE_COLOR);
                return SpireReturn.Return();
            } else if (word.equals("[E]") && AbstractDungeon.player instanceof CivilightEterna) {
                TipHelper.renderTipEnergy(sb, AbstractDungeon.player.getOrb(), x + 22.0F * Settings.scale, y - 8.0F * Settings.scale);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(SecondCostEnergyOrb.TEXT[2]), x + 22.0F * Settings.scale * 2.5F, y + 12.0F * Settings.scale, Settings.GOLD_COLOR);
                FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, GameDictionary.TEXT[0], x + 22.0F * Settings.scale, y - 20.0F * Settings.scale, 280.0F * Settings.scale, 26.0F * Settings.scale, BASE_COLOR);
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(String.class, "equals");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }

    private static class DescriptionLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
            int[] lines = LineFinder.findAllInOrder(ctBehavior, new ArrayList<>(), methodCallMatcher);
            return new int[]{lines[lines.length - 1]};
        }
    }

    @SpirePatches({@SpirePatch(clz = AbstractCard.class, method = "renderDescription"), @SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")})
    public static class RenderDescriptionPatch {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
        @SpireInsertPatch(locator = DescriptionLocator.class, localvars = {"spacing", "i", "start_x", "draw_y", "font", "tmp", "gl"})
        public static void Insert(AbstractCard _inst, SpriteBatch sb, float spacing, int i, @ByRef float[] start_x, float draw_y, BitmapFont font, @ByRef String[] tmp, GlyphLayout gl) {
            if (tmp[0].equals("[cvetmod:E1]")) {
                gl.width = CARD_ENERGY_IMG_WIDTH * _inst.drawScale;
                _inst.renderSmallEnergy(sb, BaseMod.getCardEnergyOrbAtlasRegion(AbstractCardEnum.CVET_PINK), (start_x[0] - _inst.current_x) / Settings.scale / _inst.drawScale, (draw_y + i * 1.45F * -font.getCapHeight() - 6.0F - _inst.current_y + font.getCapHeight()) / Settings.scale / _inst.drawScale);
                start_x[0] += gl.width;
                if (!_inst.keywords.contains("[E]")) _inst.keywords.add("[E]");
                tmp[0] = "";
            }
        }
    }

    @SpirePatches({@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription"), @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")})
    public static class SingleRenderDescriptionPatch {
        private static final float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
        @SpireInsertPatch(locator = DescriptionLocator.class, localvars = {"spacing", "i", "start_x", "draw_y", "font", "tmp", "gl", "drawScale", "current_x", "current_y"})
        public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, float spacing, int i, @ByRef float[] start_x, float draw_y, BitmapFont font, @ByRef String[] tmp, GlyphLayout gl, float drawScale, float current_x, float current_y) {
            if (tmp[0].equals("[cvetmod:E1]")) {
                gl.width = CARD_ENERGY_IMG_WIDTH * drawScale;
                TextureAtlas.AtlasRegion region = BaseMod.getCardEnergyOrbAtlasRegion(AbstractCardEnum.CVET_PINK);
                float x = (start_x[0] - current_x) / Settings.scale / drawScale;
                float y = (draw_y + i * 1.53F * -font.getCapHeight() - 12.0F - current_y + font.getCapHeight()) / Settings.scale / drawScale;
                sb.setColor(Color.WHITE);
                sb.draw(region.getTexture(), current_x + x * Settings.scale * drawScale + region.offsetX * Settings.scale - 4.0F * Settings.scale, current_y + y * Settings.scale * drawScale - 50.0F * Settings.scale, 0.0F, 0.0F, region.packedWidth, region.packedHeight, drawScale * Settings.scale, drawScale * Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
                start_x[0] += gl.width;
                tmp[0] = "";
            }
        }
    }
}
