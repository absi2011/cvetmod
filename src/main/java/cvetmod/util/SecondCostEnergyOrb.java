package cvetmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;

public class SecondCostEnergyOrb implements EnergyOrbInterface {
    public static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
    public static final float X_OFFSET = 10.0F * Settings.scale;
    public static final float Y_OFFSET = -120.0F * Settings.scale;
    public static final String[] orbTexturesAlt = {
            "cvetmod/images/char/orb/layer1.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
            "cvetmod/images/char/orb/layer2.png",
            "cvetmod/images/char/orb/layer1d.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
            "cvetmod/images/char/orb/EmptyLayer.png",
    };
    protected Texture baseLayer;
    protected Texture[] energyLayers;
    protected Texture[] noEnergyLayers;
    protected float[] layerSpeeds;
    protected float[] angles;
    private final Texture mask;
    private final FrameBuffer fbo;
    public SecondCostEnergyOrb() {
        int altIdx = 4;
        energyLayers = new Texture[altIdx];
        noEnergyLayers = new Texture[altIdx];
        for (int i = 0; i < altIdx; i++) {
            energyLayers[i] = ImageMaster.loadImage(orbTexturesAlt[i]);
            noEnergyLayers[i] = ImageMaster.loadImage(orbTexturesAlt[i + altIdx + 1]);
        }
        baseLayer = ImageMaster.loadImage(orbTexturesAlt[altIdx]);
        if (layerSpeeds == null) {
            layerSpeeds = new float[] { -20.0F, 20.0F, -40.0F, 40.0F };
        }
        angles = new float[this.layerSpeeds.length];
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
        this.mask = ImageMaster.loadImage("images/char/orb/mask.png");
    }

    @Override
    public void updateOrb(int energyCount) {
        if (fontScale != 0.8F) {
            fontScale = MathHelper.scaleLerpSnap(fontScale, 0.8F);
        }
        int d = angles.length;
        for (int i = 0; i < angles.length; i++) {
            if (energyCount == 0) {
                angles[i] = angles[i] - Gdx.graphics.getDeltaTime() * layerSpeeds[d - 1 - i] / 4.0F;
            } else {
                angles[i] = angles[i] - Gdx.graphics.getDeltaTime() * layerSpeeds[d - 1 - i];
            }
        }

        if (vfxTimer != 0.0F) {
            energyVfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - vfxTimer / 2.0F);
            energyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
            energyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - vfxTimer / 2.0F);
            vfxTimer -= Gdx.graphics.getDeltaTime();
            if (vfxTimer < 0.0F) {
                vfxTimer = 0.0F;
                energyVfxColor.a = 0.0F;
            }
        }
        hb.update();
    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
//        hb.move(current_x + X_OFFSET, current_y + Y_OFFSET);
//        sb.setColor(Color.WHITE);
//
//        if (AbstractDungeon.player instanceof CivilightEterna) {
//            if (CostReserves.reserveCount() > 0) {
//                for (int i = 0; i < this.energyLayers.length; i++) {
//                    sb.draw(this.energyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
//                }
//            } else {
//                for (int i = 0; i < this.noEnergyLayers.length; i++) {
//                    sb.draw(this.noEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
//                }
//            }
//            sb.draw(this.baseLayer, current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
//        }
//        sb.setColor(Color.WHITE);
//        sb.end();
//        this.fbo.begin();
//
//        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
//        Gdx.gl.glClear(16640);
//        Gdx.gl.glColorMask(true, true, true, true);
//        sb.begin();
        
        sb.setColor(Color.WHITE);
//        sb.setBlendFunction(770, 771);
        
        if (enabled) {
            for (int i = 0; i < this.energyLayers.length; i++) {
                sb.draw(this.energyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for (int i = 0; i < this.noEnergyLayers.length; i++) {
                sb.draw(this.noEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }

        hb.render(sb);
        if (hb.hovered && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(50.0F * Settings.scale, 380.0F * Settings.scale, TEXT[0], TEXT[1]);
        }
        
//        sb.setBlendFunction(0, 770);
//        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
////        sb.draw(this.mask, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 1.0F, 1.0F, 0.0F, 0, 0, 128, 128, false, false);
//        sb.setBlendFunction(770, 771);
//
//        sb.end();
//
//        this.fbo.end();
//        sb.begin();
//        TextureRegion drawTex = new TextureRegion(this.fbo.getColorBufferTexture());
//        drawTex.flip(false, true);
//        sb.draw(drawTex, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
//        sb.draw(this.baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class DoubleOrbField {
        public static SpireField<Boolean> isDoubleOrb = new SpireField<>(() -> Boolean.FALSE);
    }
    
    public static float vfxTimer = 0.0F;
    public static float fontScale = 0.8F;
    private static float energyVfxAngle = 0.0F;
    private static float energyVfxScale = Settings.scale;
    private static final Color energyVfxColor = Color.WHITE.cpy();

    private static final Hitbox hb = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("cvetmod:SecondCostEnergyOrb");
    public static final String[] TEXT = uiStrings.TEXT;

    @SpirePatch(clz = EnergyPanel.class, method = "renderVfx")
    public static class FlashSecondOrbPatch {
        @SpirePrefixPatch
        public static void flashSecondOrb(EnergyPanel __instance, SpriteBatch sb, Texture ___gainEnergyImg, Color ___energyVfxColor, float ___energyVfxScale, float ___energyVfxAngle) {
            if (DoubleOrbField.isDoubleOrb.get(AbstractDungeon.player) && SecondCostEnergyOrb.vfxTimer > 0.0F) {
                sb.setBlendFunction(770, 1);
                sb.setColor(SecondCostEnergyOrb.energyVfxColor);
                sb.draw(___gainEnergyImg, __instance.current_x + SecondCostEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + SecondCostEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, SecondCostEnergyOrb.energyVfxScale, SecondCostEnergyOrb.energyVfxScale, SecondCostEnergyOrb.energyVfxAngle - 50.0F, 0, 0, 256, 256, true, false);
                sb.draw(___gainEnergyImg, __instance.current_x + SecondCostEnergyOrb.X_OFFSET - 128.0F, __instance.current_y + SecondCostEnergyOrb.Y_OFFSET - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, SecondCostEnergyOrb.energyVfxScale, SecondCostEnergyOrb.energyVfxScale, -SecondCostEnergyOrb.energyVfxAngle, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }
        }
    }
}