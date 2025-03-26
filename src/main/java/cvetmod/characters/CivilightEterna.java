package cvetmod.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.purple.TalkToTheHand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import cvetmod.cards.*;
import cvetmod.cards.special.Originium;
import cvetmod.patches.*;
import cvetmod.util.CostReserves;
import cvetmod.util.CvetEnergyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import static cvetmod.CvetMod.CvetPink;

public class CivilightEterna extends CustomPlayer {

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("cvetmod:CivilightEterna");
    public static final String NAME = characterStrings.NAMES[0];
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String IDLE = "cvetmod/images/char/idle.png";
    public static final String DIE = "cvetmod/images/char/die.png";
    public static final String SHOULDER = "cvetmod/images/char/shoulder.png";
    public static final String[] orbTextures = {
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
    public final HashMap<String, AbstractCharacterSpine> spines = new HashMap<>();

    public CivilightEterna(String name) {
        // 参数列表：角色名，角色类枚举，能量面板贴图路径列表，能量面板特效贴图路径，能量面板贴图旋转速度列表，能量面板，模型资源路径，动画资源路径
        super(name, CvetEnum.CVET_CLASS, orbTextures, "cvetmod/images/char/orb/vfx.png", new float[]{0, 0, 0, 0, 20}, null, null);

        dialogX = this.drawX;
        dialogY = this.drawY + 200.0F * Settings.scale;

        // 参数列表：静态贴图路径，越肩视角2贴图路径，越肩视角贴图路径，失败时贴图路径，角色选择界面信息，碰撞箱XY宽高，初始能量数
        initializeClass(IDLE, SHOULDER, SHOULDER, DIE, getLoadout(), 20.0F, -10.0F, 320.0F, 290.0F, new CvetEnergyManager(2, 2));

//        spines.put("M", new AbstractCharacterSpine(this, -170.0F * Settings.scale, "cvetmod/images/char/char_249_mlyss_1/char_249_mlyss.atlas", "cvetmod/images/char/char_249_mlyss_1/char_249_mlyss.json", 1.5F, "Skill_3_Idle", "Skill_3_loop"));
    }

    @Override
    public void playDeathAnimation() {
        for (AbstractCharacterSpine s : spines.values())
            s.state.setAnimation(0, "Die", false);
    }

    @Override
    public Color getSlashAttackColor() {
        return CvetPink;
    }

    @Override
    public Color getCardRenderColor() {
        return CvetPink;
    }

    @Override
    public Color getCardTrailColor() {
        return CvetPink;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.CVET_PINK;
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new CivilightEterna(name);
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new TacticalChant();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(CvetStrikeA.ID);
        ret.add(CvetStrikeT.ID);
        ret.add(CvetStrikeB.ID);
        ret.add(CvetStrikeB.ID);
        ret.add(CvetDefendA.ID);
        ret.add(CvetDefendT.ID);
        ret.add(CvetDefendB.ID);
        ret.add(CvetDefendB.ID);
        ret.add(TacticalChant.ID);
        ret.add(Analyse.ID);
        ret.add(Originium.ID);
        ret.add(Inhibitor.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
//        ret.add(TITStudentIdCard.ID);
//        UnlockTracker.markRelicAsSeen(TITStudentIdCard.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, TEXT[0],
                60, 60, 0, 99, 6, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (AbstractCharacterSpine s : spines.values()) s.dispose();
    }

    @Override
    public void render(SpriteBatch sb) {
        stance.render(sb);
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !isDead) {
            renderHealth(sb);
            if (!orbs.isEmpty()) {
                for (AbstractOrb o : orbs)
                    o.render(sb);
            }
        }

        if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
            sb.setColor(Color.WHITE);
            renderShoulderImg(sb);
        } else {
            for (AbstractCharacterSpine s : spines.values())
                s.render(sb);
            hb.render(sb);
            healthHb.render(sb);
        }
    }

    public void draw(AbstractCard c) {
        if (!(drawPile.group.contains(c))) {
            Logger.getLogger(cvetmod.characters.CivilightEterna.class.getName()).info("ERROR: card not in draw pile!");
        } else {
            c.current_x = CardGroup.DRAW_PILE_X;
            c.current_y = CardGroup.DRAW_PILE_Y;
            c.setAngle(0.0F, true);
            c.lighten(false);
            c.drawScale = 0.12F;
            c.targetDrawScale = 0.75F;
            c.triggerWhenDrawn();
            hand.addToHand(c);
            drawPile.group.remove(c);
            for (AbstractPower p : powers) p.onCardDraw(c);
            for (AbstractRelic r : relics) r.onCardDraw(c);
            hand.refreshHandLayout();
        }
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            // TODO:attack animation
        }
        c.calculateCardDamage(monster);
        if (c.cost == -1 && EnergyPanel.totalCount < energyOnUse && !c.ignoreEnergyOnUse) {
            c.energyOnUse = EnergyPanel.totalCount;
        }
        if (c instanceof AbstractCvetCard) {
            if (c.cost == -1) {
                c.energyOnUse = EnergyPanel.totalCount;
            }
            if (((AbstractCvetCard) c).secondCost == -1) {
                ((AbstractCvetCard) c).secondEnergyOnUse = CostReserves.reserveCount();
            }
        }
        if (c.isInAutoplay && (c.cost == -1 || (c instanceof AbstractCvetCard && ((AbstractCvetCard) c).secondCost == -1))) {
            c.freeToPlayOnce = true;
        }
        c.use(this, monster);
        AbstractDungeon.actionManager.addToBottom(new UseCardAction(c, monster));
        if (!c.dontTriggerOnUseCard) {
            hand.triggerOnOtherCardPlayed(c);
        }
        hand.removeCard(c);
        cardInUse = c;
        c.target_x = Settings.WIDTH / 2.0F;
        c.target_y = Settings.HEIGHT / 2.0F;

        if (!c.freeToPlay() && !c.isInAutoplay && (!hasPower("Corruption") || c.type != AbstractCard.CardType.SKILL)) {
            energy.use(c.costForTurn);
            if (c instanceof AbstractCvetCard) {
                CostReserves.useReserves(((AbstractCvetCard) c).getSecondCost(true));
            }
        }

        if (!this.hand.canUseAnyCard() && !this.endTurnQueued) {
            AbstractDungeon.overlayMenu.endTurnButton.isGlowing = true;
        }
    }
}