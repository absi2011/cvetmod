package cvetmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import cvetmod.cards.AbstractCvetCard;

public class CardRenderPatch {
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean ___darken, Color ___renderColor) {
            if (!___darken && !_inst.isLocked && _inst.isSeen && _inst instanceof AbstractCvetCard) {
//                sb.setColor(___renderColor);
//                sb.draw(CvetMod.specialImg.get(branch - 1),
//                        _inst.current_x - 256.0F, _inst.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F,
//                        _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle);
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCost")
    public static class SingleRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
            if (!___card.isLocked && ___card.isSeen && ___card instanceof AbstractCvetCard) {
//                sb.draw(CvetMod.specialImgLarge.get(branch - 1),
//                        (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F,
//                        512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale,
//                        0.0F, 0, 0, 1024, 1024, false, false);
            }
        }
    }
}