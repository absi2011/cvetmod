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
    public int turn;
    public final int maxTurn;

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
            damage.add(new DamageInfo(this, 30));
            maxTurn = 5;
            strGain = 2;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            damage.add(new DamageInfo(this, 2));
            damage.add(new DamageInfo(this, 1));
            damage.add(new DamageInfo(this, 24));
            maxTurn = 6;
            strGain = 2;
        } else {
            damage.add(new DamageInfo(this, 2));
            damage.add(new DamageInfo(this, 1));
            damage.add(new DamageInfo(this, 16));
            maxTurn = 6;
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
            if (damage.get(0).output < damage.get(0).base) {
                isUpgrade = true;
            }
        }
        else if (nextMove == 2) {
            addToBot(new GainBlockAction(this, this, blockGain));
        }
        else if (nextMove == 3) {
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(1).output)));
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(1).output)));
            if (damage.get(1).output < damage.get(1).base) {
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
        else if (nextMove == 5) {
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strGain)));
        }
        else if (nextMove == 7) {
            addToBot(new DamageAction(p, new DamageInfo(this, damage.get(2).output)));
            if (damage.get(2).output < damage.get(2).base) {
                isUpgrade = true;
            }
        }
        else if (nextMove == 6) {
            // 可以考虑来点对话，蓄力的时候说点废话很正常吧！
        }
        getMove(0);
        isUpgrade = false;
    }

    @Override
    protected void getMove(int i) {
        int move;
        if (isUpgrade) {
            move = 5;
        }
        else if (nextMove != 6) {
            ArrayList<Integer> possibleMoves = new ArrayList<>();
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
            if (nextMove != 7) {
                possibleMoves.add(6);
            }
            move = possibleMoves.get(AbstractDungeon.aiRng.random(possibleMoves.size() - 1));
            if (move == 6) {
                turn = maxTurn - 1;
            }
        }
        else {
            turn --;
            if (turn == 0) {
                move = 7;
            }
            else {
                move = 6;
            }
        }
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
        else if (move == 6) {
            setMove(MOVES[0] + (maxTurn - turn) + MOVES[1] + maxTurn, (byte)6, Intent.UNKNOWN);
        }
        else {
            setMove(MOVES[0] + maxTurn + MOVES[1] + maxTurn, (byte)7, Intent.ATTACK, damage.get(2).base);
        }
    }
}
