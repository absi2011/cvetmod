package cvetmod.monsters;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class ArclightCommando extends AbstractCvetMonster {
    public static final String ID = "cvetmod:ArclightCommando";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String NAME = monsterStrings.NAME;

    public ArclightCommando(float x, float y) {
        super(NAME, ID, 40, 0, 0, 150.0F, 320.0F, null, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(43);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 10));
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, 5));
            damage.add(new DamageInfo(this, 9));
        } else {
            damage.add(new DamageInfo(this, 4));
            damage.add(new DamageInfo(this, 8));
        }
//        loadAnimation("cvetmod/images/monsters/enemy_1327_cbrokt/enemy_1327_cbrokt33.atlas", "cvetmod/images/monsters/enemy_1327_cbrokt/enemy_1327_cbrokt33.json", 1.5F);
//        state.setAnimation(0, "Idle_1", true);
        flipHorizontal = true;
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        setMove((byte)11, Intent.ATTACK, damage.get(1).base);
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            setMove((byte)2, Intent.ATTACK, damage.get(0).base);
        }
        else {
            setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
        }
    }
}
