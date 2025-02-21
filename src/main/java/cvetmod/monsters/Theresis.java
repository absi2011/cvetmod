package cvetmod.monsters;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import cvetmod.powers.ChessPower;

import java.util.ArrayList;

public class Theresis extends AbstractCvetMonster {
    public static final String ID = "cvetmod:Theresis";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;
    public boolean isUpgrade = false;
    public final int blockGain;
    public final int strGain;

    public Theresis(float x, float y) {
        super(NAME, ID, 400, 0, 0, 150.0F, 320.0F, "cvetmod/images/monsters/armorlessassassin.png", x, y);
        type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            blockGain = 5;
            setHp(420);
        }
        else {
            blockGain = 4;
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            damage.add(new DamageInfo(this, 3));
            damage.add(new DamageInfo(this, 2));
            strGain = 2;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            damage.add(new DamageInfo(this, 2));
            damage.add(new DamageInfo(this, 1));
            strGain = 2;
        } else {
            damage.add(new DamageInfo(this, 2));
            damage.add(new DamageInfo(this, 1));
            strGain = 1;
        }
        /*
        loadAnimation("resources/cvetmod/images/monsters/enemy_1044_zomstr_2/enemy_1044_zomstr_233.atlas", "resources/cvetmod/images/monsters/enemy_1044_zomstr_2/enemy_1044_zomstr_233.json", 1.5F);
        flipHorizontal = true;
        stateData.setMix("Idle", "Die", 0.1F);
        state.setAnimation(0, "Idle", true);
        */
        isUpgrade = false;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new ChessPower(this)));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(0).output)));
            if (damage.get(0).output <= damage.get(0).base) {
                isUpgrade = true;
            }
        }
        else if (nextMove == 2) {
            addToBot(new GainBlockAction(this, this, blockGain));
        }
        else if (nextMove == 3) {
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(1).output)));
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(1).output)));
            if (damage.get(1).output <= damage.get(1).base) {
                isUpgrade = true;
            }
        }
        else if (nextMove == 4) {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, false)));
            }
            else {
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true)));
            }
        }
        else {
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strGain)));
        }
        getMove(0);
        isUpgrade = false;
    }

    @Override
    protected void getMove(int i) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        if (isUpgrade) {
            possibleMoves.add(5);
        }
        else {
            if (nextMove != 1) {
                possibleMoves.add(1);
            }
            if (nextMove != 2) {
                possibleMoves.add(2);
            }
            if (nextMove != 3) {
                possibleMoves.add(3);
            }
            if ((nextMove != 4) && (!AbstractDungeon.player.hasPower(WeakPower.POWER_ID))) {
                possibleMoves.add(4);
            }
            if (nextMove != 5) {
                possibleMoves.add(5);
            }
        }
        int move = possibleMoves.get(AbstractDungeon.aiRng.random(possibleMoves.size() - 1));
        if (move == 1) {
            setMove((byte)1, Intent.ATTACK, damage.get(0).base);
        }
        else if (move == 2) {
            setMove((byte)2, Intent.DEFEND);
        }
        else if (move == 3) {
            setMove((byte)3, Intent.ATTACK, damage.get(1).base, 2, true);
        }
        else if (move == 4) {
            setMove((byte)4, Intent.DEBUFF);
        }
        else if (move == 5) {
            setMove((byte)5, Intent.BUFF);
        }
    }
}
