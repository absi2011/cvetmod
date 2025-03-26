package cvetmod.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.cards.special.Originium;
import cvetmod.patches.CvetTags;
import cvetmod.powers.HalfDamagePower;

public class YingXiaoFleetingNight extends AbstractCvetCard {
    public static final String ID = "cvetmod:YingXiaoFleetingNight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 2;
    public static final int SECOND_COST = 0;
    public static final int DAMAGE = 8;
    public static final int UPG_DAMAGE = 2;
    public static final int MISS_TURN = 2;
    public static final int UPG_MISS = 1;
    public YingXiaoFleetingNight() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = MISS_TURN;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(p, p, new HalfDamagePower(p, magicNumber)));
        }
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            upgradeMagicNumber(UPG_MISS);
            initializeDescription();
        }
    }

}
